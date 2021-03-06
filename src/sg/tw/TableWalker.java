package sg.tw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import ll1.Rule;

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
    
    public ArrayList<InputToken> getTokens()
    {
    	return userTokens;
    }

    /***
     * walks the dfa given a set input file
     * @param dfa
     */
    public void tableWalk(DFA dfa, boolean printTokens)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(this.file));
            String currentLine;
            while ((currentLine = br.readLine()) != null)
            {
                int index = 0, lastIndex = 0, erased = 0;
                boolean matchFound = false;
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

                    Object[] answers = dfa
                            .specialValidate(makeString(testStrings));
//                    String ttt = makeString(testStrings);
//                    if(Rule.regexToToken.keySet().contains(ttt))
//                    {
//                        String wantTokenName = Rule.regexToToken.get(ttt);
//                        this.userTokens.add(new InputToken(ttt, wantTokenName));
//                        lastInputTokeName = "";
//                        testStrings.clear();
//                        matchFound = false;
//                        lastMatch = null;
//                        lastInputTokeName = "";
//                        currentLine = currentLine.substring(index+1);
//                        index = 0;
//                        lastIndex =0;
//                        erased = 0;
//                        continue;
//                    }
                    boolean match = ((Boolean) answers[0]).booleanValue();
                    
                    if (match)
                    {
                        matchFound = true;
                        lastMatch = (String) answers[1];
                        lastIndex = index;
                        index++;
                        if (index == currentLine.length())
                        {
                            this.userTokens.add(new InputToken(
                                    lastInputTokeName, lastMatch));
                            if(printTokens)
                            	System.out.println(this.userTokens.get(userTokens.size() - 1).toString().substring(1));
                            
                            lastInputTokeName = "";
                            testStrings.clear();
                            matchFound = false;
                        }
                    }
                    else
                    {
                        if (matchFound)
                        {
                        	index++;
                        	if(index == currentLine.length()) {
	                            lastInputTokeName = lastInputTokeName.substring(0,
	                                    lastIndex - erased + 1);
	                            erased += lastInputTokeName.length();
	                            this.userTokens.add(new InputToken(
	                                    lastInputTokeName, lastMatch));
	                            if(printTokens)
	                            	System.out.println(this.userTokens.get(userTokens.size() - 1).toString().substring(1));
	                            
	                            lastInputTokeName = "";
	                            testStrings.clear();
	                            matchFound = false;
	                            index = lastIndex + 1;
                        	}
                        }
                        else
                        {
                        	String failedme = lastInputTokeName;
                        	index++;
                        	if (index == currentLine.length() && printTokens)
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
