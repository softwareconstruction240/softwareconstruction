

LINK_BASE_CASES = [r':\/\/', r'^Home#?', r'^tel:.*', r'^mailto:.*', r'^#']
"""List of regex strings that will leave a markdown link unchanged
when transformed for wiki usage."""

EMBED_EXTS = {'png', 'gif', 'jpg', 'jpeg', 'svg', 'webp', 'uml', 'mp4', 'mov', 'webm'}
"""File extensions that are used inside Markdown files to embed."""
