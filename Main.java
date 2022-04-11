import java.io.File;

public class Main
{
    public static void main(String args[])
    {
        CommentExtractor commentExtractor = new CommentExtractor(new File("the-reddit-place-dataset-comments.csv"));
        
        for(int i = 0; i < 10; i++)
        {
            System.out.println(commentExtractor.generateComment());
            System.out.println("\n\n");
        }
    }
}
