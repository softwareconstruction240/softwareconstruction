#!/usr/bin/env python3
import os
import re
import sys
from typing import List
import uuid
from structure import MarkdownFile, FilePath

EMBED_EXTS = {'png', 'jpg', 'jpeg', 'webp', 'gif', 'uml'}
CODE_EXTS = {'txt', 'iml', 'java', 'sh', 'xml'}

def slugify(name: str) -> str:
    """Remove filesystem-unfriendly chars"""
    name = name.strip()
    name = re.sub(r'[\\/:"*?<>|]+', '', name)
    name = re.sub(r'\s+', '-', name)
    return name

def find_files_with_exts(root: str, *exts: str):
    """Yield FilePaths for files of the given extensions."""
    for path_from_root, _, files in os.walk(root):
        for f in files:
            ext = f.lower().rsplit('.', 1)[-1]
            if '.' in f and ext in exts:
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
    # Drop the title line + any following blank lines
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
    for file_path in find_files_with_exts(root, ".md", root):
        title, body = extract_title_and_body(file_path.full_path)
        filename = file_path.filename
        if title:
            phase = phase_prefix(file_path.full_path)
            if filename == 'getting-started.md' and phase:
                filename = f"{title}-Phase-{phase}.md"
            else:
                filename = f"{title}.md"

        mapping[file_path.full_path] = MarkdownFile(file_path.dirpath, filename, body)

    # tuple: (parent_dir, old_filename) -> new_filename
    # name: old_filename -> new_fallname fallback
    md_tuple_map = {}
    md_name_map = {}
    for old_path, info in mapping.items():
        old_filename = os.path.basename(old_path)
        md_tuple_map[(info.parent, old_filename)] = info.filename
        # only set fallback if unique or consistent
        if old_filename not in md_name_map or md_name_map[old_filename] == info.filename:
            md_name_map[old_filename] = info.filename

    # (parent_dir, basename) -> rel_path
    # basename -> rel_path fallback
    embed_tuple_map = {}
    embed_name_map = {}
    for file_path in find_files_with_exts(root, *EMBED_EXTS):
        rel = file_path.relative_from(root)
        embed_tuple_map[(file_path.parent, file_path.filename)] = rel
        if file_path.filename not in embed_name_map or embed_name_map[file_path.filename] == rel:
            embed_name_map[file_path.filename] = rel

    link_re = re.compile(r'\[([^\]]+)\]\(([^)]+)\)')

    for old_path, info in mapping.items():
        old_dir = info.dirpath
        cur_parent = info.parent
        new_name = info.filename
        body = info.body

        # pylint: disable=W0640
        def rewrite_line(line: str) -> str:
            def repl(m: re.Match[str]) -> str:
                text, target = m.groups()
                # skip external URLs
                if '://' in target:
                    return m.group(0)

                path_part, sep, anchor = target.partition('#')
                dirname = os.path.basename(os.path.dirname(path_part))
                basename = os.path.basename(path_part)
                ext = basename.rsplit('.', 1)[-1].lower() if '.' in basename else ''

                # Case A: code/example-code links → full link via code_base (which may be URL)
                segments = path_part.replace('\\', '/').split('/')
                if ext in CODE_EXTS or 'example-code' in segments:
                    # compute relative path under root
                    abs_path = os.path.normpath(os.path.join(old_dir, path_part))
                    rel_to_root = os.path.relpath(abs_path, root).replace(os.sep, '/')
                    # build full link
                    if code_base.startswith(('http://', 'https://')):
                        full_link = code_base.rstrip('/') + '/' + rel_to_root
                    else:
                        full_link = os.path.normpath(os.path.join(code_base, rel_to_root)).replace(os.sep, '/')
                    return f'[{text}]({full_link}{("#"+anchor) if sep else ""})'

                # Case B: embed links → relative path from root
                if ext in EMBED_EXTS:
                    rel = (embed_tuple_map.get((dirname, basename))
                           or embed_tuple_map.get((cur_parent, basename))
                           or embed_name_map.get(basename))
                    if rel:
                        rel = rel.replace('\\', '/')
                        return f'[{text}]({rel}{("#"+anchor) if sep else ""})'

                # Case C: markdown links → strip .md, no path
                if ext == 'md':
                    new_base = (md_tuple_map.get((dirname, basename))
                                or md_tuple_map.get((cur_parent, basename))
                                or md_name_map.get(basename))
                    if new_base:
                        clean = re.sub(r'\.md$', '', new_base, flags=re.IGNORECASE)
                        return f'[{text}]({clean}{("#"+anchor) if sep else ""})'

                return m.group(0)

            return link_re.sub(repl, line)
        # pylint: enable=W0640

        # Rewrite links in the body
        new_body = [rewrite_line(ln) for ln in body]

        # Detect case-only rename: same lowercased path but different case
        new_path = os.path.join(old_dir, new_name)
        same_lower = old_path.lower() == new_path.lower()
        really_changed = old_path != new_path and not same_lower

        # If case-only, move via a temp name to force the change on
        # case-insensitive filesystems
        tmp_path = None
        if same_lower and old_path != new_path:
            tmp_path = old_path + f".tmp-{uuid.uuid4().hex}"
            os.rename(old_path, tmp_path)

        # Write out the new file
        os.makedirs(old_dir, exist_ok=True)
        with open(new_path, 'w', encoding='utf-8') as f:
            f.writelines(new_body)

        # Clean up: remove old if really changed, or remove the temp
        if really_changed:
            os.remove(old_path)
        elif tmp_path:
            os.remove(tmp_path)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Usage: rewrite.py <root-markdown-dir> <code-base-path-or-URL>")
        sys.exit(1)
    main(sys.argv[1], sys.argv[2])
