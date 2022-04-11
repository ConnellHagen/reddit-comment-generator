# About
Using comment data from Reddit's r/place community 2022 and markov chains, this program will generate a faux-reddit comment.

![image](https://user-images.githubusercontent.com/72321241/162654872-e3f77fdf-7e36-4483-8fef-ddec58854c82.png)

# Usage
In order to use this, first you must have a set of comments to be used as sample data. The Reddit r/place file is found in the releases section of this page.
To utilise this class in your code you must create a `CommentExtractor` object passing the comment data file as a parameter. Using the `generateComment` method on this object will return a `String` with a faux-comment for you to use as you please.
