/******
 * @author mlfong
 * @version 1.0
 *****/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ScannerGenerator
{
    
    private static final HashSet<Character> ESCAPE_CHARS = new HashSet<Character>(){
        private static final long serialVersionUID = 4633395235151976767L;
    {
        this.add('^'); this.add('\\'); this.add('-'); this.add('['); this.add(']');
    } };
    
    private static final HashSet<Character> ALPHABET = new HashSet<Character>(){
        private static final long serialVersionUID = -2139943170358709421L;
    {for(int i = 32 ; i < 127; i++) this.add((char)i);}}; 
    
    public static HashMap<String, HashSet<Character>> parseInput(String filename)
            throws BadDefinitionException
    {
        File f = new File(filename);
        HashMap<String, HashSet<Character>> characterClasses = 
                new HashMap<String, HashSet<Character>>();
        boolean charClassFlag = false, tokenDefFlag = false;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String s;
            while ((s = br.readLine()) != null)
            {
                if (s.length() < 2) continue;
                if (s.charAt(0) == '%')
                {
                    if (!charClassFlag)
                        charClassFlag = true;
                    else if (!tokenDefFlag)
                        tokenDefFlag = true;
                    continue;
                }
                if (tokenDefFlag)
                {
                    // TODO: token def
                }
                else if (charClassFlag)
                {
                    // replace tabs except inside the brackets
                    int leftBracket = s.indexOf("["), rightBracket = s.lastIndexOf("]");
                    if(leftBracket < 0 || rightBracket < 0 || leftBracket > rightBracket)
                    {
                        br.close();
                        throw new BadDefinitionException();
                    }
                    String bPart = s.substring(0, leftBracket).replaceAll("\t", " ");
                    String ePart = s.substring(rightBracket+1).replaceAll("\t", " ");
                    s = bPart + s.substring(leftBracket, rightBracket+1) + ePart;
                    
                    HashSet<Character> hsc = new HashSet<Character>();
                    int white = s.indexOf(" ");
                    String charClass = s.substring(0, white);
                    String contents = s.substring(leftBracket + 1, rightBracket);
                    if(contents.length() < 1)
                    {
                        br.close();
                        throw new BadDefinitionException();
                    }
                    boolean notFlag = contents.charAt(0) == '^';
                    if(notFlag)
                        contents = contents.substring(1);
                    for(int i = 0; i < contents.length(); i++)
                    {
                        if(contents.charAt(i) == '\\')
                        {
                            if(i+1 >= contents.length())
                            {
                                br.close();
                                throw new BadDefinitionException();
                            }
                            if(ESCAPE_CHARS.contains(contents.charAt(i+1)))
                                hsc.add(contents.charAt(i+1));
                            else
                            {
                                br.close();
                                throw new BadDefinitionException();
                            }
                            i++;
                        }
                        else
                        {
                            if(ESCAPE_CHARS.contains(contents.charAt(i)))
                            {
                                if(contents.charAt(i) == '-')
                                {
                                    if(i+1 >= contents.length())
                                    {
                                        br.close();
                                        throw new BadDefinitionException();
                                    }
                                    char rangeStr = contents.charAt(i - 1);
                                    char rangeEnd = contents.charAt(i + 1);
                                    if(ESCAPE_CHARS.contains(rangeEnd))
                                    {
                                        if(i+2 >= contents.length())
                                        {
                                            br.close();
                                            throw new BadDefinitionException();
                                        }
                                        rangeEnd = contents.charAt(i+2);
                                    }
                                    if(rangeEnd < rangeStr) // It is an error if a range is empty (e.g., [1-0])
                                    {
                                        br.close();
                                        throw new BadDefinitionException();
                                    }
                                    for (int j = rangeStr; j < rangeEnd + 1; j++)
                                        hsc.add(new Character((char) j));
                                    i = i+2;
                                }
                                else
                                {
                                    br.close();
                                    throw new BadDefinitionException();
                                }
                            }
                            else
                                hsc.add(contents.charAt(i));
                        }
                    }
                    if(notFlag)
                    {
                        // disjoint set
                        int INindex = s.lastIndexOf("IN");
                        int rightBracketIndex = s.lastIndexOf("]");
                        HashSet<Character> takeAway ;
                        if(INindex > rightBracketIndex)
                        {
                            int dollarIndex = s.indexOf("$", 1);
                            String classKey = s.substring(dollarIndex).trim();
                            takeAway = characterClasses.get(classKey);
                        }
                        else
                            takeAway = ALPHABET;
                        HashSet<Character> nhsc = new HashSet<Character>();
                        for(Character c : takeAway)
                            if(!hsc.contains(c))
                                nhsc.add(c);
                        hsc = nhsc;
                    }
                    characterClasses.put(charClass, hsc);
                }
                else
                    System.err.println("Should not be here");
            }
            br.close();
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return characterClasses;
    }

}
