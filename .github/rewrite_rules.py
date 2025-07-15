"""
This module provides a common place to change the specific information of the rewrite script
to allow its reuse in other repositories as desired.
"""

import re
import structure

LINK_BASE_CASES = [r':\/\/', r'^Home#?', r'^tel:.*', r'^mailto:.*', r'^#']
"""List of regex strings that will leave a markdown link unchanged
when transformed for wiki usage."""

EMBED_EXTS = {'png', 'gif', 'jpg', 'jpeg', 'svg', 'webp', 'uml', 'mp4', 'mov', 'webm'}
"""File extensions that are used inside Markdown files to embed."""

def wiki_page_title(title: str | None, body: list[str], file_path: structure.FilePath) -> str:
    # pylint: disable=W0613
    """
    Takes in the extracted H1 header if found, the file's remaining text, and the file path info.
    Returns the new title for the Markdown file, which will be slugified and used to name the page
    when used in the wiki. Shouldn't include the '.md' extension.
    
    The title H1 header is only found if it's the first non-empty line in the file.
    """
    if title:
        phase_dir = re.search(r'(\d+)', file_path.parent)
        if file_path.filename == 'getting-started.md' and phase_dir:
            return f"{title}---Phase-{phase_dir.group(1)}"
        return title
    return file_path.filename
