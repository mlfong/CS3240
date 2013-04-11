package sg.rdp;

import java.util.Stack;
import java.util.HashMap;

public class InitRegex
{
    private static final char PLUS = '+';
    private static final char LEFT_PARENS = '(';
    private static final char RIGHT_PARENS = ')';
    private static final char BACKSLASH = '\\';
    private static final char DOLLARSIGN = '$';
    private static final char SPACE = ' ';
    private static final int PARENS = 1;
    private static final int SLASH = -1;
    private static final int REGULARCHAR = 0;
    private static final String STAR = "*";
    // Testcases for adding parens, and changing + to *
    private static final String testCases[] =
    { "a+", "aaa+", "ab+", "abbb+", "(a)+", "(ab)+", "\\+", "\\t+", "\\)",
            "(ab(cd))+", "(ab(cd)*)+", "a+b+c+", "(ab(cd)+)+", "abc", "a*b*c*",
            "(a+)+", "(a)", "a(|b)+", "\\ +" };
    private static final String testResults[] =
    { "(aa*)", "(aaaa*)", "(abb*)", "(abbbb*)", "((a)(a)*)", "((ab)(ab)*)",
            "(\\+)", "(\\t\\t*)", "(\\))", "((ab(cd))(ab(cd))*)",
            "((ab(cd)*)(ab(cd)*)*)", "(aa*bb*cc*)",
            "((ab(cd)(cd)*)(ab(cd)(cd)*)*)", "(abc)", "(a*b*c*)",
            "((aa*)(aa*)*)", "(a)", "(a(|b)(|b)*)", "(\\ \\ *)" };

    // Testcases for removing spaces + mapping character classes
    private static final String moreTestCases[] =
    { "$letter" };

    private static final String moreTestResults[] =
    { "$letter"

    };

    public static void main(String[] args)
    {
        // runTest("(ab(cd)+)+", "(ab(cd)(cd)*)(ab(cd)(cd)*)*");
        runAllTests();
        // testSpacesAndMapping();
        // System.out.println(removeSpaces("the quick	brown fox jumped over the river"));
    }

    /*
     * This is the method that other people should use to run this code
     */
    public static String initializeRegex(String regex)
    {
        return appendParens(convertPlus(regex));
    }

    // Runs a single tests
    public static void runTest(String input, String expected)
    {
        String converted = initializeRegex(input);
        System.out.println(converted);
        if (!converted.equals(expected))
        {
            System.out.println("\tERROR: Expected " + expected + " from "
                    + input);
        }
    }

    // Runs all the testcases
    public static void runAllTests()
    {
        if (testCases.length != testResults.length)
        {
            System.out
                    .println("Error: testCases and testResults lengths do not match");
        }
        else
        {
            for (int i = 0; i < testCases.length; i++)
            {
                String test = testCases[i];
                String result = testResults[i];
                String converted = initializeRegex(test);
                System.out.println(converted);
                if (!result.equals(converted))
                {
                    System.out.println("\tERROR: Expected " + result + " from "
                            + test);
                }
            }
        }
    }

    public static void testSpacesAndMapping()
    {
        if (moreTestCases.length != moreTestResults.length)
        {
            System.out
                    .println("Error: testCases and testResults lengths do not match");
        }
        else
        {
            for (int i = 0; i < moreTestCases.length; i++)
            {
                String test = moreTestCases[i];
                String result = moreTestResults[i];
                String converted = testMapping(test);
                System.out.println(converted);
                if (!result.equals(converted))
                {
                    System.out.println("\tERROR: Expected " + result + " from "
                            + test);
                }
            }
        }
    }

    /*
     * Private implementation code
     */

    private static String appendParens(String regex)
    {
        // Check to make sure there are not already parens around the entire
        // regex
        if (regex.charAt(0) == LEFT_PARENS
                && regex.charAt(regex.length() - 1) == RIGHT_PARENS)
            return regex;

        return "(" + regex + ")";
    }

    private static String convertPlus(String regex)
    {
        StringBuilder newString = new StringBuilder();
        for (int regexIndex = 0; regexIndex < regex.length(); regexIndex++)
        {
            // Check for + AND not \+
            if (regex.charAt(regexIndex) == PLUS && regexIndex > 0
                    && regex.charAt(regexIndex - 1) != BACKSLASH)
            {
                int previous = checkPrevious(regex, regexIndex);
                if (previous == PARENS)
                {
                    // System.out.println("PARENS!");
                    newString.append(getParensSubstring(newString.toString()));
                }
                else if (previous == SLASH)
                {
                    // System.out.println("SLASH");
                    newString.append(BACKSLASH);
                    newString.append(regex.charAt(regexIndex - 1));
                    newString.append(STAR);
                }
                else
                {
                    // System.out.println("RegularChar");
                    newString.append(regex.charAt(regexIndex - 1));
                    newString.append(STAR);
                }
            }
            else
            {
                newString.append(regex.charAt(regexIndex));
            }
        }
        return newString.toString();
    }

    // Checks to see what the character before the + is
    // To determine if it's a ), \ or a regular char
    // index currently points to the +
    private static int checkPrevious(String regex, int index)
    {
        // Shouldn't happen, but just double check
        if (index == 0)
        {
            return 0;
        }

        if (regex.charAt(index - 1) == RIGHT_PARENS)
        {
            // Checks for the case of \)
            if (index > 1)
            {
                if (regex.charAt(index - 2) == BACKSLASH)
                {
                    return SLASH;
                }
            }
            return PARENS;
        }
        else if (index > 1 && regex.charAt(index - 2) == BACKSLASH)
        {
            return SLASH;
        }
        else
        {
            return REGULARCHAR;
        }
    }

    // If the char previous to + was a ), get the entire grouping within that
    // parens
    // With the * appended
    private static String getParensSubstring(String regex)
    {
        // Uses a stack to keep track of parens
        // Going from right to left
        // Pushes when it hits a right parens,
        // Pops when it hits a left paren
        // Ends when stack is empty
        Stack<Character> parens = new Stack<Character>();
        int beginIndex = regex.length() - 1;

        // System.out.println("The whole regex is: "+regex);
        // System.out.println("The current index is: "+tempIndex);
        while (beginIndex >= 0)
        {
            char lastChar = regex.charAt(beginIndex);
            // System.out.println(lastChar);
            if (lastChar == LEFT_PARENS)
            {
                // System.out.println("LEFT_PARENS");
                parens.pop();
            }
            else if (lastChar == RIGHT_PARENS)
            {
                parens.push(RIGHT_PARENS);
                // System.out.println("RIGHT_PARENS");
            }

            if (parens.empty())
                break;

            beginIndex--;
        }
        return regex.substring(beginIndex) + STAR;
    }

    private static String removeSpaces(String regex)
    {
        StringBuilder newRegex = new StringBuilder();

        for (int x = 0; x < regex.length(); x++)
        {
            char character = regex.charAt(x);
            // handle spaces
            if (character != SPACE && character != '\t')
            {
                // System.out.println(character);
                newRegex.append(character);
            }

            // handle escaping spaces
            else if (character == SPACE && x > 0
                    && regex.charAt(x - 1) == BACKSLASH)
            {
                newRegex.append(character);
            }
        }
        return newRegex.toString();

    }

    // Creates the map from characterclass to a char
    private static HashMap<String, Character> createMap(String regex)
    {
        HashMap<String, Character> charClassMapping = new HashMap<String, Character>();

        // Used to map the character class into
        // a value
        char mappedValue = 'a';
        int index = 0;
        while (index < regex.length())
        {
            if (regex.charAt(index) == DOLLARSIGN)
            {
                StringBuilder charClass = new StringBuilder();
                charClass.append(DOLLARSIGN);
                index++;
                while (index < regex.length() && isChar(regex.charAt(index)))
                {
                    charClass.append(regex.charAt(index));
                    index++;
                }
                if (charClassMapping.get(charClass.toString()) == null)
                {
                    charClassMapping.put(charClass.toString(), mappedValue);
                    mappedValue++;
                }
            }
            else
            {
                index++;
            }
        }

        return charClassMapping;
    }

    // Creates the mapped string
    private static String createMappedRegex(String regex,
            HashMap<String, Character> map)
    {
        StringBuilder newRegex = new StringBuilder();
        int index = 0;
        while (index < regex.length())
        {
            if (regex.charAt(index) == DOLLARSIGN)
            {
                StringBuilder charClass = new StringBuilder();
                charClass.append(DOLLARSIGN);
                index++;
                while (index < regex.length() && isChar(regex.charAt(index)))
                {
                    charClass.append(regex.charAt(index));
                    index++;
                }
                newRegex.append(map.get(charClass.toString()));

            }
            else
            {
                newRegex.append(regex.charAt(index));
                index++;
            }
        }
        return regex;
    }

    // Creates the final string using the mapping
    private static String remapClasses(String regex,
            HashMap<String, Character> map)
    {
        StringBuilder newRegex = new StringBuilder();
        HashMap<Character, String> reversedMap = reverseMap(map);
        int index = 0;
        while (index < regex.length())
        {
            if (isChar(regex.charAt(index)))
            {
                newRegex.append(reversedMap.get(regex.charAt(index)));
            }
            else
            {
                newRegex.append(regex.charAt(index));
            }
            index++;
        }
        return newRegex.toString();

    }

    private static HashMap<Character, String> reverseMap(
            HashMap<String, Character> map)
    {
        HashMap<Character, String> reversedMap = new HashMap<Character, String>();
        for (String key : map.keySet())
        {
            reversedMap.put(map.get(key), key);
        }
        return reversedMap;
    }

    // Checks to see if the inputted char is a
    // a-z or A-Z character
    private static boolean isChar(char character)
    {
        if ((character >= 'a' && character <= 'z')
                || (character >= 'A' && character <= 'Z'))
        {
            return true;
        }
        return false;
    }

    private static String testMapping(String regex)
    {
        String noSpaces = removeSpaces(regex);
        System.out.println("\tTEST: noSpaces is " + noSpaces);
        HashMap<String, Character> map = createMap(noSpaces);
        for (String s : map.keySet())
        {
            System.out.println("\tTEST: Pairing is (" + s + ", " + map.get(s)
                    + ")");
        }
        String mappedRegex = createMappedRegex(noSpaces, map);
        System.out.println("\tTEST: mappedRegex is " + mappedRegex);
        String finalRegex = remapClasses(mappedRegex, map);
        System.out.println("\tTEST: finalRegex is " + finalRegex);
        return finalRegex;
    }

}