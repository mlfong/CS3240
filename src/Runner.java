/******
 * @author mlfong
 * @version 1.0
 *****/

import java.util.HashMap;
import java.util.HashSet;


public class Runner
{
    public static void main(String[] args) throws BadDefinitionException
    {
        HashMap<String,HashSet<Character>> tokenClasses = 
                LexReader.parseInput("input/mlfong_sample_input.txt");
        for(String s : tokenClasses.keySet())
        {
            System.out.println(s);
            System.out.print("\t");
            Util.reallyPrettyPrint(tokenClasses.get(s));
        }

    }

}
