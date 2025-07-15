#!/usr/bin/env python3
import os
import re
import sys
from structure import ContentFilePath, FilePath, TupleNameMap, slugify
from rewrite_rules import LINK_BASE_CASES, EMBED_EXTS, EDIT_FILE_EXTS, wiki_page_title


def get_ext(path: str):
    """Get the file extension, if present"""
    return path.lower().rsplit('.', 1)[-1] if '.' in path else ''

def find_files_with_exts(root: str, *exts: str):
    """Yield FilePaths for files of the given extensions."""
    for path_from_root, _, files in os.walk(root):
        for f in files:
            if get_ext(f) in exts:
                yield FilePath(path_from_root, f)

def extract_title_and_body(path: str) -> tuple[str | None, list[str]]:
    """
    Returns (title, body_lines) as extracted from the H1 header and the rest of the file.
    - title is None if first non-empty line is not an H1 (# ).
    - body_lines is the remaining lines after dropping the title line
      and any immediately following blank lines.
    """
    with open(path, encoding='utf-8') as f:
        lines = f.readlines()

    line_number, first_line = next((i, ln) for (i, ln) in enumerate(lines) if ln.strip())
    if not first_line.lstrip().startswith('# '):
        return None, lines
    title = first_line.strip()[2:]

    j = line_number + 1
    while j < len(lines) and lines[j].strip() == '':
        j += 1
    return title, lines[j:]

def main(root: str, code_base: str):
    mapping: dict[str, ContentFilePath] = {}
    for file_path in find_files_with_exts(root, *EDIT_FILE_EXTS):
        title, body = extract_title_and_body(file_path.full_path)
        filename = slugify(wiki_page_title(title, body, file_path))

        mapping[file_path.full_path] = ContentFilePath(file_path.dirpath, filename, body)

    edited_map = TupleNameMap(
        (info.parent, os.path.basename(old_path), info.filename)
        for (old_path, info) in mapping.items())

    embed_map = TupleNameMap(
        (file_path.parent, file_path.filename, file_path.relative_from(root))
        for file_path in find_files_with_exts(root, *EMBED_EXTS))

    # Groups: 1) Links inside <>, 2) All other links
    link_re = re.compile(r'\[(?:[^\]]+)\]\((?:<([^>]+)>|((?:[^()\\]|\\[()])+))\)')

    base_re = re.compile('|'.join(LINK_BASE_CASES))

    clean_ext_pattern = r'\.(' + '|'.join(EDIT_FILE_EXTS) + r')$'

    def extract_new_link(info: ContentFilePath, path_part: str) -> str:
        dirname = os.path.basename(os.path.dirname(path_part))
        basename = os.path.basename(path_part)

        match get_ext(basename):
            case embed if embed in EMBED_EXTS:
                return embed_map.get(dirname, info.parent, basename)
            case edit if edit in EDIT_FILE_EXTS:
                new_base = edited_map.get(dirname, info.parent, basename)
                return re.sub(clean_ext_pattern, '', new_base, flags=re.IGNORECASE)
            case _:
                abs_path = os.path.normpath(os.path.join(info.dirpath, path_part))
                rel_to_root = os.path.relpath(abs_path, root)

                if code_base.startswith(('http://', 'https://')):
                    return code_base.rstrip('/') + '/' + rel_to_root
                return os.path.normpath(os.path.join(code_base, rel_to_root))

    def rewrite_line(line: str, info: ContentFilePath) -> str:
        def repl(m: re.Match[str]) -> str:
            target = m.group(1) or m.group(2)

            if base_re.search(target):
                return m.group(0)

            path_part = target.partition('#')[0]
            new_link = extract_new_link(info, path_part)

            return re.sub(re.escape(path_part), new_link, m.group(0), 1)

        return link_re.sub(repl, line)

    for old_path, info in mapping.items():
        new_body = [rewrite_line(ln, info) for ln in info.body]

        if old_path != info.full_path:
            os.remove(old_path)

        with open(info.full_path, 'w', encoding='utf-8') as f:
            f.writelines(new_body)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Usage: rewrite.py <root-markdown-dir> <code-base-path-or-URL>")
        sys.exit(1)
    main(sys.argv[1], sys.argv[2])
