from dataclasses import dataclass
import os
from typing import List, Iterator


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


@dataclass
class MarkdownFile(FilePath):
    body: List[str]
    """All content of the file as separate lines"""


@dataclass(init=False)
class TupleNameMap:
    _tuple_map: dict[tuple[str, str], str]
    _name_map: dict[str, str]

    def __init__(self, mapper: Iterator[tuple[str, str, str]]):
        self._tuple_map = {}
        self._name_map = {}
        for parent, name, value in mapper:
            print(value)
            print(parent)
            print(name)
            self._tuple_map[(parent, name)] = value
            self._name_map[name] = value

    def get(self, dirpath: str, parent: str, name: str) -> str | None:
        return (self._tuple_map.get(dirpath, name) or
                self._tuple_map.get(parent, name) or
                self._name_map.get(name))
