package sg.tw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import sg.Util;
import sg.fa.DFA;

public class TableWalker
{
    private File file;
    private ArrayList<MyToken> tokens;
    private ArrayList<InputToken> userTokens;

    public TableWalker(String filename, ArrayList<MyToken> tokens)
    {
        this.file = new File(filename);
        this.tokens = tokens;
        this.userTokens = new ArrayList<InputToken>();
    }

    public TableWalker()
    {
        this("", null);
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
                                System.exit(0);
                            }
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

    public String sanitize(String input)
    {
        return Util.removeSpaces(input);
    }

    public MyToken firstMatch(String[] checkString)
    {
        for (MyToken token : tokens)
        {
            if (token.check2(checkString))
            {
                return token;
            }
        }
        return null;
    }

    public boolean hasMatch(String[] checkString)
    {
        for (MyToken token : tokens)
        {
            if (token.check2(checkString))
            {
                return true;
            }
        }
        return false;
    }

    public String makeString(ArrayList<String> stringList)
    {
        StringBuilder sb = new StringBuilder();
        for (String s : stringList)
            sb.append(s);
        return sb.toString();
    }

    public String[] makeArray(ArrayList<String> stringList)
    {
        String[] toReturn = new String[stringList.size()];
        for (int i = 0; i < toReturn.length; i++)
        {
            toReturn[i] = stringList.get(i);
        }
        return toReturn;
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
