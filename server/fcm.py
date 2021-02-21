import logging
import traceback

from dataclasses import asdict
from pyfcm import FCMNotification
from secret import Secret


class FcmClient:
  def __init__(self):
    api_key = Secret('fcm-api-key').get()
    self.service = FCMNotification(api_key=api_key, env='app_engine')

  def notify(self, post):
    title = post.title
    url = post.url
    region_codes = post.region_codes()

    if not region_codes:
      # Can't send anything to topics if we don't know what regions to notify.
      logging.info(f'No regions found for {post}')
      return False

    topics = ['any']
    for i in range(len(region_codes)):
      topic = '-'.join(region_codes[0:i+1])
      topics.append(topic)

    topic_condition = ' || '.join(f"'{topic}' in topics" for topic in topics)

    logging.info(f'Sending notification to topics: {topics}')
    logging.info(f'Post: {post}')

    message = asdict(post)

    try:
      self.service.notify_topic_subscribers(condition=topic_condition, data_message=message)
      return True
    except:
      stacktrace = traceback.format_exc()
      logging.error("%s", stacktrace)
      return False
