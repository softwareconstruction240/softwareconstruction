from dataclasses import dataclass
from typing import List

@dataclass
class MarkdownFile:
  parent: str
  filename: str
  body: List[str]