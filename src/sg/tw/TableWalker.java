package sg.tw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import sg.Util;
import sg.fa.DFA;


/**
 * 
 * @author JGleason
 * @version 1.0
 * 
 * Walks a dfa and stores the results of the desired input
 */
public class TableWalker
{
    private File file;
    private ArrayList<InputToken> userTokens;

    public TableWalker(String filename)
    {
        this.file = new File(filename);
        this.userTokens = new ArrayList<InputToken>();
    }

    public TableWalker()
    {
        this("");
    }

    /***
     * walks the dfa given a set input file
     * @param dfa
     */
    public void tableWalk(DFA dfa)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(this.file));
            String currentLine;
            while ((currentLine = br.readLine()) != null)
            {
                int index = 0;
                boolean matchFound = false, failFound = false;
                ArrayList<String> testStrings = new ArrayList<String>();
                String lastMatch = null;
                String lastInputTokeName = "";
                currentLine = sanitize(currentLine);
                while (index < currentLine.length())
                {
                    char current = currentLine.charAt(index);
                    testStrings.add(Character.toString(current));
                    lastInputTokeName += current;

                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(Character.toString(current));

                    failFound = false;

                    Object[] answers = dfa
                            .specialValidate(makeString(testStrings));
                    boolean match = ((Boolean) answers[0]).booleanValue();
                    if (match)
                    {
                        failFound = true;
                    }

                    if (match)
                    {
                        matchFound = true;
                        lastMatch = (String) answers[1];
                        index++;
                        if (index == currentLine.length())
                        {
                            this.userTokens.add(new InputToken(
                                    lastInputTokeName, lastMatch));
                            lastInputTokeName = "";
                            testStrings.clear();
                            matchFound = false;
                        }
                    }
                    else
                    {
                        if (matchFound)
                        {
                            lastInputTokeName = lastInputTokeName.substring(0,
                                    lastInputTokeName.length() - 1);
                            this.userTokens.add(new InputToken(
                                    lastInputTokeName, lastMatch));
                            lastInputTokeName = "";
                            testStrings.clear();
                            matchFound = false;
                        }
                        else
                        {
                        	String failedme = lastInputTokeName;
                            if (failFound == false)
                            {
                                index++;
                            }
                            else
                            {
                                lastInputTokeName = "";
                                testStrings.clear();
                            }
                            if (index == currentLine.length())
                            {
                            	System.out.println("The line: " +failedme );
                            	System.out.println("Has matched nothing");
                                System.exit(0);
                            }
                            failedme = "";
                        }
                    }
                }
            }
            br.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Removes all the spaces from a string
     * @param input String you want to removes spaces from
     * @return String with no spaces
     */
    public String sanitize(String input)
    {
        return Util.removeSpaces(input);
    }
    
    /**
     * Makes a string from an arraylist of strings
     * @param stringList
     * @return Single string
     */
    public String makeString(ArrayList<String> stringList)
    {
        StringBuilder sb = new StringBuilder();
        for (String s : stringList)
            sb.append(s);
        return sb.toString();
    }
    
    public ArrayList<InputToken> getUserTokens()
    {
        return userTokens;
    }

    public void printUserTokens()
    {
        for (InputToken t : this.userTokens)
        {
            System.out.println(t.toString().substring(1));
        }
    }

    public void printArray(String[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            System.out.print(array[i]);
        }
        System.out.println();
    }
}
