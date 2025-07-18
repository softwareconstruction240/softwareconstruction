"""
This module provides a common place to change the specific information of the rewrite script
to allow its reuse in other repositories as desired.
"""
import re
from structure import FilePath, slugify     # pylint: disable=W0611

LINK_BASE_CASES = [r':\/\/', r'^Home#?', r'^tel:.*', r'^mailto:.*', r'^#']
"""List of regex strings that will leave a markdown link unchanged
when transformed for wiki usage."""

EMBED_EXTS = {'png', 'gif', 'jpg', 'jpeg', 'svg', 'webp', 'uml', 'mp4', 'mov', 'webm'}
"""File extensions that are used inside Markdown files to embed."""

EDIT_FILE_EXTS = {'md'}
"""File extensions that will be modified by the script."""

def wiki_page_title(title: str | None, body: list[str], file_path: FilePath) -> str:
    # pylint: disable=W0613
    """
    Takes in the extracted file title if found, the file's remaining text, and the file path info.

    Returns the new title for the editing file, which will be slugified and used to name the page
    when used in the wiki, including the file extension.
    
    The logic to find the title is defined in extract_title_and_body.
    """
    if title:
        phase_dir = re.search(r'(\d+)', file_path.parent)
        if file_path.filename == 'getting-started.md' and phase_dir:
            return f"{title} ‐ Phase {phase_dir.group(1)}.md"
        return title + '.md'
    return file_path.filename

def extract_title_and_body(path: str) -> tuple[str | None, list[str]]:
    """
    Takes in the full path to the file being accessed.

    Returns (title, body_lines) as extracted from the file, and the rest of the file.
    - title can be None if file's name must remain unchanged or doesn't match pattern.
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
