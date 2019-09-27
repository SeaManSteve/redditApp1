
# A very simple Flask Hello World app for you to get started with...
import praw
from flask import Flask
import math



app = Flask(__name__)

@app.route('/')
def hello_world():
    postTotal = ""

    reddit = praw.Reddit(client_id ='fmZutAqe7Rfwaw',
                     client_secret ='dUIDaZ_JmehAD9FxzLJaGXAEuXU',
                     username='TheStealthGuy',
                     password='Triforce223',
                     user_agent='InvestTest')

    subreddit = reddit.subreddit('MemeEconomy')

    hot_python = subreddit.new(limit = 10)

    for submission in hot_python:
        if not submission.stickied:
            post = submission.upvote_ratio * 100
            postTotal += submission.url + "," + str(submission.ups) + "," + submission.id + "," + str(post) + "\n"
    return postTotal
@app.route('/pullpost/<id>')
def uh(id):
    reddit = praw.Reddit(client_id ='fmZutAqe7Rfwaw',
                     client_secret ='dUIDaZ_JmehAD9FxzLJaGXAEuXU',
                     username='TheStealthGuy',
                     password='Triforce223',
                     user_agent='InvestTest')

    subreddit = reddit.subreddit('MemeEconomy')
    hot_check = subreddit.new()
    for submission in hot_check:
        if (submission.id == id):
            return str(submission.ups)
            break
@app.route('/genid')
def createNewId():
    reddit = praw.Reddit(client_id ='fmZutAqe7Rfwaw',
                     client_secret ='dUIDaZ_JmehAD9FxzLJaGXAEuXU',
                     username='TheStealthGuy',
                     password='Triforce223',
                     user_agent='InvestTest')

    subreddit = reddit.subreddit('MemeEconomy')

    hot_python = subreddit.new(limit = 1)

    for submission in hot_python:
        if not submission.stickied:
            return submission.id









