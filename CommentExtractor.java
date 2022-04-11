import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CommentExtractor
{
    HashMap<String, ArrayList<String>> wordProbs;

    public CommentExtractor(File commentFile)
    {
        wordProbs = new HashMap<String, ArrayList<String>>();
        extract(commentFile);
    }

    private void extract(File commentFile)
    {
        try
        {
            Scanner commentSeperator = new Scanner(commentFile, "UTF-8");
            commentSeperator.useDelimiter("comment,");

            while(commentSeperator.hasNextLine())
            {
                String commentData = commentSeperator.nextLine();
                // there may be multiple entries that make up the comment if it 
                // contains commas in the original
                ArrayList<String> partsOfCommentData = new ArrayList<String>();
                java.util.Collections.addAll(partsOfCommentData, commentData.split(","));

                boolean commentNotComplete = true;
                while(commentNotComplete)
                {
                    try
                    {
                        // the final entry in a line of data is a double
                        // ensures the entire comment is captures if there are commas or new lines within
                        Double.parseDouble(partsOfCommentData.get(partsOfCommentData.size() - 1));
                        commentNotComplete = false;
                    }
                    catch(NumberFormatException e)
                    {
                        commentData = commentSeperator.nextLine();
                        java.util.Collections.addAll(partsOfCommentData, commentData.split(","));   
                    }
                }
                
                boolean buildingComment = false;
                String comment = "";
                for(int i = 0; i < partsOfCommentData.size() - 2; i++)
                {
                    if(buildingComment)
                    {
                        if(!comment.equals(""))
                        {
                            comment += " ";
                        }
                        comment += partsOfCommentData.get(i);
                    }

                    // the url is the final part before the comment starts
                    else if(partsOfCommentData.get(i).contains("old.reddit.com"))
                    {
                        buildingComment = true;
                    }
                }

                //filter out comments that are [removed] or contain links
                if( comment.contains("[") ||
                    comment.contains(".com") || comment.contains(".org") ||
                    comment.contains(".net") || comment.contains(".tv") ||
                    comment.contains(".us") || comment.contains(".uk") ||
                    comment.contains(".edu") || comment.contains(".gov") ||
                    comment.contains(".info") || comment.contains(".gg")) 
                    continue;

                ArrayList<String> wordsInComment = new ArrayList<String>();
                java.util.Collections.addAll(wordsInComment, comment.split("([^a-zA-Z'-])+"));
                 
                // getting rid of weird characters like emojis
                Iterator<String> wordsInComment_ITERATOR = wordsInComment.iterator();
                while(wordsInComment_ITERATOR.hasNext())
                {
                    String word = wordsInComment_ITERATOR.next();
                    if(word.contains("?"))
                    {
                        wordsInComment_ITERATOR.remove();
                    }
                }

                // making all words lowercase for uniformity
                for(int i = 0; i < wordsInComment.size(); i++)
                {
                    wordsInComment.set(i, wordsInComment.get(i).toLowerCase());
                }

                // adding in words to the map with the first word as the key and the following as the value
                for(int i = 0; i < wordsInComment.size(); i++)
                {
                    if(i == wordsInComment.size() - 1)
                    {
                        if(!wordProbs.containsKey(wordsInComment.get(i)))
                        {
                            wordProbs.put(wordsInComment.get(i), new ArrayList<String>());
                        }

                        wordProbs.get(wordsInComment.get(i)).add(null);
                    }
                    else
                    {
                        if(!wordProbs.containsKey(wordsInComment.get(i)))
                        {
                            wordProbs.put(wordsInComment.get(i), new ArrayList<String>());
                        }

                        wordProbs.get(wordsInComment.get(i)).add(wordsInComment.get(i + 1));
                    }
                }

            }

            commentSeperator.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }

    public String generateComment()
    {
        String comment = "";

        // randomly choosing the first word
        int index = (int)(Math.random() * wordProbs.keySet().size());
        Iterator<String> keySetIterator = wordProbs.keySet().iterator();
        while(index > 0)
        {
            keySetIterator.next();
            index--;
        }
        String firstWord = keySetIterator.next();
        comment += firstWord;

        boolean isCommentFinished = false;
        String previousWord = firstWord;
        while(!isCommentFinished)
        {
            ArrayList<String> nextWords = wordProbs.get(previousWord);
            String nextWord = nextWords.get((int)(Math.random() * nextWords.size()));
            if(nextWord == null)
            {
                comment += ".";
                isCommentFinished = true;
            }
            else
            {
                comment = comment + " " + nextWord;
            }
    
            previousWord = nextWord;
        }

        return comment;
    }

}