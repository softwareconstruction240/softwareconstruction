#!/usr/bin/env python3
import os
import re
import sys
from typing import List
from structure import MarkdownFile, FilePath

EMBED_EXTS = {'png', 'jpg', 'jpeg', 'webp', 'gif', 'uml'}
CODE_EXTS = {'txt', 'iml', 'java', 'sh', 'xml'}

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

def phase_prefix(path: str) -> str:
    """
    Find the parent directory's number to determine the chess
    phase it belongs to, where applicable.
    """
    parent = os.path.basename(os.path.dirname(path))
    m = re.search(r'(\d+)', parent)
    return m.group(1) if m else ''

def main(root: str, code_base: str):
    mapping: dict[str, MarkdownFile] = {}
    for file_path in find_files_with_exts(root, "md", root):
        title, body = extract_title_and_body(file_path.full_path)
        filename = file_path.filename
        if title:
            phase = phase_prefix(file_path.full_path)
            if filename == 'getting-started.md' and phase:
                filename = f"{title}-Phase-{phase}.md"
            else:
                filename = f"{title}.md"

        mapping[file_path.full_path] = MarkdownFile(file_path.dirpath, filename, body)

    md_tuple_map: dict[tuple[str, str], str] = {}
    md_name_map: dict[str, str] = {}
    for old_path, info in mapping.items():
        old_filename = os.path.basename(old_path)
        md_tuple_map[(info.parent, old_filename)] = info.filename
        if old_filename not in md_name_map or md_name_map[old_filename] == info.filename:
            md_name_map[old_filename] = info.filename

    embed_tuple_map: dict[tuple[str, str], str] = {}
    embed_name_map: dict[str, str] = {}
    for file_path in find_files_with_exts(root, *EMBED_EXTS):
        rel = file_path.relative_from(root)
        embed_tuple_map[(file_path.parent, file_path.filename)] = rel
        if file_path.filename not in embed_name_map or embed_name_map[file_path.filename] == rel:
            embed_name_map[file_path.filename] = rel

    link_re = re.compile(r'\[([^\]]+)\]\(([^)]+)\)')

    for old_path, info in mapping.items():
        # pylint: disable=W0640
        def rewrite_line(line: str) -> str:
            def repl(m: re.Match[str]) -> str:
                text, target = m.groups()

                if '://' in target:
                    return m.group(0)

                path_part, sep, anchor = target.partition('#')
                dirname = os.path.basename(os.path.dirname(path_part))
                basename = os.path.basename(path_part)
                ext = get_ext(basename)

                new_link: str | None = None

                if ext in CODE_EXTS or 'example-code' in path_part.split('/'):
                    abs_path = os.path.normpath(os.path.join(info.dirpath, path_part))
                    rel_to_root = os.path.relpath(abs_path, root)

                    if code_base.startswith(('http://', 'https://')):
                        new_link = code_base.rstrip('/') + '/' + rel_to_root
                    else:
                        new_link = os.path.normpath(os.path.join(code_base, rel_to_root))

                elif ext in EMBED_EXTS:
                    new_link = (embed_tuple_map.get((dirname, basename))
                           or embed_tuple_map.get((info.parent, basename))
                           or embed_name_map.get(basename))

                elif ext == 'md':
                    new_base = (md_tuple_map.get((dirname, basename))
                                or md_tuple_map.get((info.parent, basename))
                                or md_name_map.get(basename))
                    if new_base:
                        new_link = re.sub(r'\.md$', '', new_base, flags=re.IGNORECASE)

                return (f'[{text}]({new_link}{("#"+anchor) if sep else ""})'
                        if new_link else m.group())

            return link_re.sub(repl, line)
        # pylint: enable=W0640

        new_body = [rewrite_line(ln) for ln in info.body]

        similar_name = old_path.casefold() == info.full_path.casefold()
        name_changed = old_path != info.full_path

        if name_changed and similar_name:
            os.remove(old_path)

        os.makedirs(info.dirpath, exist_ok=True)
        with open(info.full_path, 'w', encoding='utf-8') as f:
            f.writelines(new_body)

        if name_changed and not similar_name:
            os.remove(old_path)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Usage: rewrite.py <root-markdown-dir> <code-base-path-or-URL>")
        sys.exit(1)
    main(sys.argv[1], sys.argv[2])
