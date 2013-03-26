/******
 * @author mlfong
 * @version 1.0
 *****/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Runner
{
    public static void characterClassTest() throws BadDefinitionException
    {
        Object[] specifications = ScannerGenerator.parseInput(
        // "input/mlfong_sample_input.txt"
                "SampleSpec");
        HashMap<String, HashSet<Character>> characterClasses = (HashMap<String, HashSet<Character>>) specifications[0];
        ArrayList<MyToken> myTokens = (ArrayList<MyToken>) specifications[1];
        System.out.println("---------------");
        System.out.println("Character Classes:");
        System.out.println("---------------");
        for (String s : characterClasses.keySet())
        {
            System.out.println(s);
            System.out.print("\t");
            Util.reallyPrettyPrint(characterClasses.get(s));
        }
        System.out.println("---------------");
        System.out.println("Token definitions:");
        System.out.println("---------------");
        for(MyToken mt : myTokens)
        {
            String[] regex = mt.getNFA().getRegex();
            System.out.println(mt.getName());
            System.out.print("\t");
            Util.prettyPrint(regex);
        }
        
        /*
        InputReader ir = new InputReader("SampleInput", myTokens);
        ir.doStuff();
        
        System.out.println("Baby please work");
        ir.printUserTokens();
        */
       
        String[] test = {"="};
        for(MyToken t : myTokens){
        	System.out.println(t.prettyPrint());
        	if(t.check2(test)){
        		System.out.println("Found");
        		System.out.println(t);
        	}
        }
       
    }

    public static void regexValidatorTest()
    {
        HashMap<String, HashSet<Character>> tokenClasses = new HashMap<String, HashSet<Character>>();
        tokenClasses.put("$LETTER", null);
        tokenClasses.put("$DIGIT", null);
        tokenClasses.put("$SMALLCASE", null);
        tokenClasses.put("$UPPERCASE", null);
        String[] res =
        {
                "$SMALLCASE ($LETTER | $DIGIT)*",
                "$UPPERCASE ($LETTER | $DIGIT)*",
                "",
                "$UPPERCASE ($LETTER | $DIGIT | $NOTACLASS)*",
                "$SMALLCASE ($LETTER | $DIGIT)*$DIGIT",
                "$SMALLCASE ($LETTER | $DIGIT)*|",
                "$DIGIT$DIGIT$DIGIT$DIGIT$DIGIT$DIGIT$DIGIT    $DIGIT",
                "$DIGIT$DIGIT$DIGIT$LETTER$UPPERCASE",
                "((()))",
                "(()))",
                "$UPPERCASE ($LETTER | $DIG|IT)*",
                "$UPPERCASE ($LETTER | $DIGIT |+ $UPPERCASE)*",
                "$UPPERCASE ($LETTER | $DIGIT | $UPPERCASE)*",
                "+",
                "|",
                "(",
                ")",
                "*",
                "($UPPERCASE | $UPPERCASE) $UPPERCASE* $UPPERCASE+",
                "$UPPERCASE+",
                "$UPPERCASE*",
                "$UPPERCASE",
                "qwertyyuuidlfsdfgklsjdfg",
                "[$UPPERCASE|$UPPERCASE]",
                ";",
                "'",
                "             ",
                "$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE$UPPERCASE",
                null, "$DIGIT $DIG" };
        boolean[] correct_vals =
        { true, true, false, false, true, false, true, true, false, false,
                false, false, true, false, false, false, false, false, true,
                true, true, true, false, false, false, false, false, true,
                false, false };
        if (res.length != correct_vals.length)
        {
            System.err.println("You forgot a truth value!");
            System.exit(-1);
        }
        for (int i = 0; i < res.length; i++)
        {
            System.out
                    .println("Test "
                            + (i + 1)
                            + ": "
                            + (TokenUtil.validateRegex(res[i], tokenClasses) == correct_vals[i] ? "pass"
                                    : "fail"));
        }

    }

    public static void main(String[] args) throws BadDefinitionException
    {
        // regexValidatorTest();
        characterClassTest();
    }

}
