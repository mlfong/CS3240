package sg.tests;

import java.util.HashMap;
import java.util.HashSet;

import sg.Util;
import sg.fa.DFA;
import sg.fa.NFA;
import sg.rdp.RecursiveDescentParserNFA;

public class RDPTest {
    
    public static void main(String[] args){
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
        test9();
        test10();
        test11();
        test12();
        test13();
        test14();
        
        
    }
    
    public static void test1(){
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        String regex ="[0-9]";
        System.out.println("*****Testing: " + regex);
        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test[] =
        { "0-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "1425345",
                "asfdasdf", "!" };
        boolean answers[] =
        { false, true, true, true, true, true, true, true, true, true, true,
                false, false, false };
        NFATest.testAll(dfaResult, test, answers);
        System.out.println();
    }
    
    public static void test2(){
        // Testing [^0]IN$DIGIT
        String regex ="[^0]IN$DIGIT";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");
        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test1[] =
        { "0-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "1425345",
                "asfdasdf", "!" };
        boolean answers1[] =
        { false, false, true, true, true, true, true, true, true, true, true,
                false, false, false };
        NFATest.testAll(dfaResult, test1, answers1);
        System.out.println();
    }
    
    
    public static void test3(){
        String regex ="[a-zA-Z]";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test2[] =
        { "a", "aA", "B", "Z", "D", "e", "!", "1", "543ds", "Afd324" };
        boolean answers2[] =
        { true, false, true, true, true, true, false, false, false, false };
        NFATest.testAll(dfaResult, test2, answers2);
        System.out.println();
    }
    
    public static void test4(){
        String regex ="[^a-z]IN$CHAR";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[a-zA-Z]", "$CHAR");
        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test3[] =
        { "a", "z", "A", "Z", "1", "asd", "!" };
        boolean answers3[] =
        { false, false, true, true, false, false, false };
        NFATest.testAll(dfaResult, test3, answers3);
        System.out.println();
    }
    
    public static void test5(){
        String regex ="[^A-Z]IN$CHAR";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[a-zA-Z]", "$CHAR");
        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test4[] =
        { "a", "z", "A", "Z", "1", "asd", "!" };
        boolean answers4[] =
        { true, true, false, false, false, false, false };
        NFATest.testAll(dfaResult, test4, answers4);
        System.out.println();
    }
    
    public static void test6(){
        // $LOWER($LOWER|$DIGIT)*
        String regex ="$LOWER($LOWER|$DIGIT)*";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");
        initDefinedEntry(exampleDefinedClass, "[a-zA-Z]", "$CHAR");
        initDefinedEntry(exampleDefinedClass, "[^A-Z]IN$CHAR", "$LOWER");

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test5[] =
        { "a", "assdfasdf", "a123134", "a2", "A443543", "Basdafd", "!" };
        boolean answers5[] =
        { true, true, true, true, false, false, false };
        NFATest.testAll(dfaResult, test5, answers5);
        System.out.println();
    }
    
    public static void test7(){
        // $INT ($DIGIT)+
        // String regex ="($DIGIT)+";
        String regex ="($DIGIT)($DIGIT)*";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test6[] =
        { "1", "1111", "2222", "435235", "a", "!" };
        boolean answers6[] =
        { true, true, true, true, false, false };
        NFATest.testAll(dfaResult, test6, answers6);
        System.out.println();
    }
    
    public static void test8(){
        // $FLOAT ($DIGIT)+ \. ($DIGIT)+
        // String regex ="($DIGIT)+\\.($DIGIT)+";
        String regex ="($DIGIT)($DIGIT)*\\.($DIGIT)($DIGIT)*";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test7[] =
        { "3333.33333", "1.1", "2130.112", "asfd4343.43534", "asdfaew",
                "!.432534", ".3452354", "435345.", ".0", "..345345345",
                "0000.000", "1111.111", "2222.222", "3333.333", "4444.444",
                "5555.555", "6666.666", "7777.777", "8888.888", "9999.999",
                "33.12", "12.33" };
        boolean answers7[] =
        { true, true, true, false, false, false, false, false, false, false,
                true, true, true, true, true, true, true, true, true, true,
                true, true };
        NFATest.testAll(dfaResult, test7, answers7);
        // dfaResult.prettyPrint();
        System.out.println();
    }
    
    public static void test9(){
         // $ASSIGN =
        String regex ="=";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        ;

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test8[] =
        { "=", "123", "asdfasdf", "!3543", "453345.423543", "+" };
        boolean answers8[] =
        { true, false, false, false, false, false };
        NFATest.testAll(dfaResult, test8, answers8);
        System.out.println();
    }
    
    public static void test10(){
         // $PLUS \+
        String regex ="\\+";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        ;

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test9[] =
        { "+", "123", "asdfasdf", "!3543", "453345.423543", "-" };
        boolean answers9[] =
        { true, false, false, false, false, false };
        NFATest.testAll(dfaResult, test9, answers9);
        System.out.println();
        
    }
    
    public static void test11(){
        // $MINUS -
        String regex ="-";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        ;

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test10[] =
        { "-", "123", "asdfasdf", "!3543", "453345.423543", "+" };
        boolean answers10[] =
        { true, false, false, false, false, false };
        NFATest.testAll(dfaResult, test10, answers10);
        System.out.println();
    }
    
    public static void test12(){
        // $MULTIPLY \*
        String regex ="\\*";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        ;

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test11[] =
        { "*", "123", "asdfasdf", "!3543", "453345.423543", "+" };
        boolean answers11[] =
        { true, false, false, false, false, false };
        NFATest.testAll(dfaResult, test11, answers11);
        System.out.println();
    }
    
    public static void test13(){
        // $PRINT PRINT
        String regex ="PRINT";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        ;

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test12[] =
        { "PRINT", "123", "asdfasdf", "!3543", "453345.423543", "+" };
        boolean answers12[] =
        { true, false, false, false, false, false };
        NFATest.testAll(dfaResult, test12, answers12);
        System.out.println();
    }
    
    public static void test14(){
        // $FLOAT ($DIGIT)+ \. ($DIGIT)+
        // String regex ="($DIGIT)+\\.($DIGIT)+";
        String regex ="($DIGIT)($DIGIT)*\\.";
        System.out.println("*****Testing: " + regex);
        HashMap<String, NFA> exampleDefinedClass =initDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");

        NFA result = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
        // result.prettyPrint();
        DFA dfaResult =DFA.convertNFA(result);
        String test13[] =
        { "1234.", "1.", "!@#$", "3333.", "33.", "3333333333." };
        boolean answers13[] =
        { true, true, false, true, true, true };
        NFATest.testAll(dfaResult, test13, answers13);
        // dfaResult.prettyPrint();
        System.out.println();
    }
    private static HashMap<String, NFA> initDefinedClasses()
    {
        HashMap<String, NFA> exampleDefinedClass =new HashMap<String, NFA>();
        exampleDefinedClass.put("$DIGIT", null);
        exampleDefinedClass.put("$NON-ZERO", null);
        exampleDefinedClass.put("$CHAR", null);
        exampleDefinedClass.put("$UPPER", null);
        exampleDefinedClass.put("$LOWER", null);
        exampleDefinedClass.put("$IDENTIFIER", null);
        exampleDefinedClass.put("$INT", null);
        exampleDefinedClass.put("$FLOAT", null);
        exampleDefinedClass.put("$ASSIGN", null);
        exampleDefinedClass.put("$PLUS", null);
        exampleDefinedClass.put("$MINUS", null);
        exampleDefinedClass.put("$MULTIPLY", null);
        exampleDefinedClass.put("$PRINT", null);

        return exampleDefinedClass;
    }
    
    private static void initDefinedEntry(
            HashMap<String, NFA> exampleDefinedClass, String regex, String key)
    {
        NFA entry = RecursiveDescentParserNFA.validateRegex(regex, exampleDefinedClass);
        exampleDefinedClass.put(key, entry);
    }
}
