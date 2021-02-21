import google.cloud.logging
import praw

from fcm import FcmClient
from flask import Flask, make_response, request
from post import Post
from secret import Secret
from store import fetch_last, set_last


app = Flask(__name__)

# Send all logging at level INFO or higher to gcloud.
client = google.cloud.logging.Client()
client.get_default_handler()
client.setup_logging()

def _text_response(text, code=200):
  response = make_response(text, code)
  response.mimetype = "text/plain"
  return response

def get_posts_since(reddit, last_post):
  all_posts = []

  submissions = reddit.subreddit('mechmarket').new(limit=100)
  for post in map(lambda x: Post.from_submission(x), submissions):
    if post.created_utc > last_post.created_utc:
      all_posts.append(post)
    else:
      break

  return all_posts

@app.route('/check')
def check():
  has_cron_header = request.headers.get('X-Appengine-Cron') == 'true'

  reddit = praw.Reddit(
    client_id=Secret('reddit-client-id').get(),
    client_secret=Secret('reddit-client-secret').get(),
    user_agent="mk-notifier by /u/guy_from_canada"
  )

  last_post = fetch_last(Post)
  if not last_post:
    most_recent_submission = next(reddit.subreddit('mechmarket').new(limit=1))
    last_post = Post.from_submission(most_recent_submission)
    set_last(last_post)
    return _text_response(str(last_post))

  all_new_posts = get_posts_since(reddit, last_post)

  if not all_new_posts:
    return _text_response(str(last_post))

  newest_post = all_new_posts[0]

  # Send notifications in reverse order, so notifications are sent for the oldest posts first.
  all_new_posts.reverse()
  for post in all_new_posts:
    FcmClient().notify(post)

  set_last(newest_post)

  return _text_response(str(newest_post))

@app.route('/')
def root():
  return _text_response(str(fetch_last(Post)))


if __name__ == '__main__':
  # This is used when running locally only. When deploying to Google App
  # Engine, a webserver process such as Gunicorn will serve the app. This
  # can be configured by adding an `entrypoint` to app.yaml.
  # Flask's development server will automatically serve static files in
  # the "static" directory. See:
  # http://flask.pocoo.org/docs/1.0/quickstart/#static-files. Once deployed,
  # App Engine itself will serve those files as configured in app.yaml.
  app.run(host='127.0.0.1', port=8080, debug=True)
