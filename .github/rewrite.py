#!/usr/bin/env python3
import os
import re
import sys
from typing import List
import uuid
from structure import MarkdownFile

EMBED_EXTS = {'png', 'jpg', 'jpeg', 'webp', 'gif', 'uml'}
CODE_EXTS = {'txt', 'iml', 'java', 'sh', 'xml'}

def slugify(name: str) -> str:
    """Remove filesystem-unfriendly chars"""
    name = name.strip()
    name = re.sub(r'[\\/:"*?<>|]+', '', name)
    name = re.sub(r'\s+', '-', name)
    return name

def find_file_with_exts(root: str, *exts: str):
    """Yield (full_path, parent_directory, file_name) for files of the given extensions."""
    for dirpath, _, files in os.walk(root):
        parent = os.path.basename(dirpath)
        for f in files:
            ext = f.lower().rsplit('.', 1)[-1]
            if ext in exts:
                yield os.path.join(dirpath, f), parent, f

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
    """Find the parent directory's number to determine the chess phase it belongs to, where applicable."""
    parent = os.path.basename(os.path.dirname(path))
    m = re.search(r'(\d+)', parent)
    return m.group(1) if m else ''

def main(root: str, code_base: str):
    # 1) Build mapping: full_path -> {new_base, body}
    mapping: dict[str, MarkdownFile] = {}
    for full_path, parent_dir, filename in find_file_with_exts(root, ".md"):
        title, body = extract_title_and_body(full_path)
        if title:
            phase = phase_prefix(full_path)
            if filename == 'getting-started.md' and phase:
                filename = f"{title}-Phase-{phase}.md"
            else:
                filename = f"{title}.md"

        mapping[full_path] = MarkdownFile(parent_dir, filename, body)

    # 2) Build lookup tables for markdown links
    # (parent_dir, basename) -> new_base, and basename -> new_base fallback
    md_tuple_map = {}
    md_name_map = {}
    for old_path, info in mapping.items():
        base = os.path.basename(old_path)
        parent = os.path.basename(os.path.dirname(old_path))
        md_tuple_map[(parent, base)] = info.filename
        # only set fallback if unique or consistent
        if base not in md_name_map or md_name_map[base] == info.filename:
            md_name_map[base] = info.filename
    
    # (parent_dir, basename) -> rel_path, and basename -> rel_path fallback
    embed_tuple_map = {}
    embed_name_map = {}
    for full, parent, base in find_file_with_exts(root, *EMBED_EXTS):
        rel = os.path.relpath(full, root)
        embed_tuple_map[(parent, base)] = rel
        if base not in embed_name_map or embed_name_map[base] == rel:
            embed_name_map[base] = rel

    link_re = re.compile(r'\[([^\]]+)\]\(([^)]+)\)')

    # 3) Rewrite and write files
    for old_path, info in mapping.items():
        old_dir = os.path.dirname(old_path)
        cur_parent = os.path.basename(old_dir)
        new_name = info.filename
        body = info.body

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
