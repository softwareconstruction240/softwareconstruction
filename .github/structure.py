from dataclasses import dataclass
import os
from typing import List


@dataclass
class FilePath:
    dirpath: str
    """Path from root to parent directory of file"""
    filename: str
    """Name of the file"""

    @property
    def full_path(self) -> str:
        """Full path from root to the file"""
        return os.path.join(self.dirpath, self.filename)

    @property
    def parent(self) -> str:
        """Immediate parent directory"""
        return os.path.basename(self.dirpath)

    def relative_from(self, root: str):
        return os.path.relpath(self.full_path, root)


@dataclass(init=False)
class MarkdownFile(FilePath):
    body: List[str]
    """All content of the file as separate lines"""

    def __init__(self, dirpath: str, filename: str, body: List[str]):
        super().__init__(dirpath, filename)
        self.body = body
