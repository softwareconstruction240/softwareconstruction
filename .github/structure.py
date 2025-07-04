from dataclasses import dataclass
import os
from typing import List

@dataclass
class MarkdownFile:
  parent: str
  filename: str
  body: List[str]


@dataclass(init=False)
class FilePath:
  full_path: str
  """Relative full path from root to the file"""
  parent: str
  """Immediate parent directory"""
  filename: str
  """Name of the file"""

  def __init__(self, from_root: str, filename: str):
    self.filename = filename
    self.parent = os.path.basename(from_root)
    self.full_path = os.path.join(from_root, filename)
