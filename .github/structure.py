from dataclasses import dataclass
from typing import List

@dataclass
class MarkdownFile:
  filename: str
  body: List[str]