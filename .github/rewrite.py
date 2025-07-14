#!/usr/bin/env python3
import os
import re
import sys
from typing import List
from structure import MarkdownFile, FilePath, TupleNameMap

EMBED_EXTS = {'png', 'jpg', 'jpeg', 'webp', 'gif', 'uml'}

def slugify(name: str) -> str:
    """Remove filesystem-unfriendly chars"""
    name = name.strip()
    name = re.sub(r'[\\/:"*?<>|]+', '', name)
    name = re.sub(r'\s+', '-', name)
    return name

def get_ext(path: str):
    """Get the file extension, if present"""
    return path.lower().rsplit('.', 1)[-1] if '.' in path else ''

def find_files_with_exts(root: str, *exts: str):
    """Yield FilePaths for files of the given extensions."""
    for path_from_root, _, files in os.walk(root):
        for f in files:
            if get_ext(f) in exts:
                yield FilePath(path_from_root, f)

def extract_title_and_body(path: str) -> tuple[str | None, List[str]]:
    """
    Returns (title, body_lines), where title is slugified.
    - title is None if first non-empty line is not an H1 (# ).
    - body_lines is the remaining lines after dropping the title line
      and any immediately following blank lines.
    """
    with open(path, encoding='utf-8') as f:
        lines = f.readlines()

    line_number, first_line = next((i, ln) for (i, ln) in enumerate(lines) if ln.strip())
    if not first_line.lstrip().startswith('# '):
        return None, lines
    title = slugify(first_line.strip()[2:])

    j = line_number + 1
    while j < len(lines) and lines[j].strip() == '':
        j += 1
    return title, lines[j:]

def main(root: str, code_base: str):
    mapping: dict[str, MarkdownFile] = {}
    for file_path in find_files_with_exts(root, "md"):
        title, body = extract_title_and_body(file_path.full_path)
        filename = file_path.filename
        if title:
            phase_dir = re.search(r'(\d+)', file_path.parent)
            if filename == 'getting-started.md' and phase_dir:
                title = f"{title}---Phase-{phase_dir.group(1)}"
            filename = f"{title}.md"

        mapping[file_path.full_path] = MarkdownFile(file_path.dirpath, filename, body)

    markdown_map = TupleNameMap(
        (info.parent, os.path.basename(old_path), info.filename)
        for (old_path, info) in mapping.items())

    embed_map = TupleNameMap(
        (file_path.parent, file_path.filename, file_path.relative_from(root))
        for file_path in find_files_with_exts(root, *EMBED_EXTS))

    # Groups: 1) Link text, 2) Links inside <>, 3) All other links
    link_re = re.compile(r'\[(?:[^\]]+)\]\((?:<([^>]+)>|((?:[^()\\]|\\[()])+))\)')

    base_cases = [r':\/\/', r'^Home$', r'^tel:.*', r'^mailto:.*']
    base_re = re.compile('|'.join(base_cases))

    def rewrite_line(line: str, info: MarkdownFile) -> str:
        def repl(m: re.Match[str]) -> str:
            target = m.group(1) or m.group(2)

            if base_re.search(target):
                return m.group(0)

            path_part, sep, _ = target.partition('#')
            dirname = os.path.basename(os.path.dirname(path_part))
            basename = os.path.basename(path_part)
            ext = get_ext(basename)

            new_link: str | None = None

            if ext in EMBED_EXTS:
                new_link = embed_map.get(dirname, info.parent, basename)

            elif ext == 'md':
                new_base = markdown_map.get(dirname, info.parent, basename)
                if new_base:
                    new_link = re.sub(r'\.md$', '', new_base, flags=re.IGNORECASE)

            elif ext != '' or sep == '':
                print('Code:')
                print(ext != '')
                print(sep == '')
                abs_path = os.path.normpath(os.path.join(info.dirpath, path_part))
                rel_to_root = os.path.relpath(abs_path, root)

                if code_base.startswith(('http://', 'https://')):
                    new_link = code_base.rstrip('/') + '/' + rel_to_root
                else:
                    new_link = os.path.normpath(os.path.join(code_base, rel_to_root))

            if not new_link:
                return m.group(0)

            return re.sub(path_part, new_link, m.group(0))

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
