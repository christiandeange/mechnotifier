import re

from dataclasses import dataclass
from google.cloud import datastore


COUNTRY_CODE_REGEX = re.compile('^\\[(\\w{2})(?:-(\\w{2}))?\\].*$', re.M)

@dataclass(frozen=True)
class Post:
  post_id: str
  title: str
  flair: str
  url: str
  created_utc: int

  def region_codes(self):
    country_matcher = COUNTRY_CODE_REGEX.match(self.title)
    if not country_matcher:
      return None

    # Remove any match results that are None.
    return list(filter(lambda x: x, country_matcher.groups()))

  @staticmethod
  def from_submission(submission):
    return Post(
      post_id=submission.id,
      title=submission.title,
      flair=submission.link_flair_text,
      url=submission.url,
      created_utc=int(submission.created_utc)
    )
