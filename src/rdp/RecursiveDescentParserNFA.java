package rdp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import rdp.tests.NFATest;
public class RecursiveDescentParserNFA{
    
    //Flag to turn on debug printing
    private static int debug = 0;
    //Flag to print out the results of everything
    private static int returnValueDebug = 0;
    //Counter used for naming NFA states
    private static Integer counter;
    //List used as a queue to store the inputted regex to be validated/creating the NFA for
    private static ArrayList<Character> list;
    //Keeps track of the index for the list so it knows where the head is pointed to
    private static int index;
    //Hashset used for all the characters that need escaped RE_CHARS
    private static HashSet<Character> RESpecials;
    private static Character[] RE_Special = {' ', '\\', '*', '+', '?', '|', '[', ']', '(', ')', '.', '\'', '\"', '$'};
    //2) escaped characters: \ (backslash space), \\, \*, \+, \?, \|, \[, \], \(, \), \., \' and \"
    //Hashset used for all the characters that need CLS_CHAR
    private static HashSet<Character> CLSSpecials;
    private static Character[] CLS_Special = {'\\', '^', '-', '[', ']'};
    
//    private static String[] charClassTestCases = {"[0-9]"};//, "[^0]IN$DIGIT", "[a-zA-Z]", "[^a-z]IN$CHAR", "[^A-Z]IN$CHAR"};
    private static String[] charClassTestCases = {"[a-zA-Z]"};
    private static HashMap<String, NFA> definedClasses;
    
    private static int rangeCount;
    public static void main(String[] args){
        //Testing [0-9]
        HashMap<String, NFA> exampleDefinedClass = createDefinedClasses();
        String regex = "[0-9]";
        System.out.println("*****Testing: "+regex);
        NFA result = validateRegex(regex, exampleDefinedClass);
        HashSet<Character> resultChar = NFA.oneLayerTransitions(result);
//        result.prettyPrint();
        DFA dfaResult = DFA.convertNFA(result);
        String test[] = {"0-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "1425345", "asfdasdf", "!"};
        boolean answers[] = {false, true, true, true, true, true, true, true, true, true, true, false, false, false};
        NFATest.testAll(dfaResult, test, answers);
        System.out.println();
        
        //Testing [^0]IN$DIGIT
        regex = "[^0]IN$DIGIT";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test1[] = {"0-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "1425345", "asfdasdf", "!"};
        boolean answers1[] = {false, false, true, true, true, true, true, true, true, true, true, false, false, false};
        NFATest.testAll(dfaResult, test1, answers1);
        System.out.println();
        
        
        regex = "[a-zA-Z]";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test2[] = {"a", "aA", "B", "Z", "D", "e", "!", "1", "543ds", "Afd324"};
        boolean answers2[] = {true, false, true, true, true, true, false, false, false, false};
        NFATest.testAll(dfaResult, test2, answers2);
        System.out.println();
        
        
        regex = "[^a-z]IN$CHAR";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[a-zA-Z]", "$CHAR");
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test3[] = {"a", "z", "A", "Z", "1", "asd", "!"};
        boolean answers3[] = {false, false, true, true, false, false, false};
        NFATest.testAll(dfaResult, test3, answers3);
        System.out.println();
        
        
        regex = "[^A-Z]IN$CHAR";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[a-zA-Z]", "$CHAR");
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test4[] = {"a", "z", "A", "Z", "1", "asd", "!"};
        boolean answers4[] = {true, true, false, false, false, false, false};
        NFATest.testAll(dfaResult, test4, answers4);
        System.out.println();
        
        
        debug = 0;
        returnValueDebug = 0;
        //$LOWER($LOWER|$DIGIT)*
        regex = "$LOWER($LOWER|$DIGIT)*";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");
        initDefinedEntry(exampleDefinedClass, "[a-zA-Z]", "$CHAR");
        initDefinedEntry(exampleDefinedClass, "[^A-Z]IN$CHAR", "$LOWER");
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test5[] = {"a", "assdfasdf", "a123134", "a2", "A443543", "Basdafd", "!"};
        boolean answers5[] = {true, true, true, true, false, false, false};
        NFATest.testAll(dfaResult, test5, answers5);
        System.out.println();
        debug = 0;
        returnValueDebug = 0;
        
        //$INT ($DIGIT)+
        regex = "($DIGIT)+";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test6[] = {"1", "1111", "2222", "435235", "a", "!"};
        boolean answers6[] = {true, true, true, true, false, false};
        NFATest.testAll(dfaResult, test6, answers6);
        System.out.println();
        
        debug = 1;
        //$FLOAT ($DIGIT)+ \. ($DIGIT)+
//        regex = "($DIGIT)+\\.($DIGIT)+";
        regex = "($DIGIT)($DIGIT)*\\.($DIGIT)($DIGIT)*";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();
        initDefinedEntry(exampleDefinedClass, "[0-9]", "$DIGIT");
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test7[] = {"1245.13423", "1.1", "0.234324", "asfd4343.43534", "asdfaew", "!.432534", ".3452354"};
        boolean answers7[] = {true, true, true, false, false, false, false};
        NFATest.testAll(dfaResult, test7, answers7);
        System.out.println();
        
        debug = 0;
        //$ASSIGN =
        regex = "=";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();;
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test8[] = {"=", "123", "asdfasdf", "!3543", "453345.423543", "+"};
        boolean answers8[] = {true, false, false, false, false, false};
        NFATest.testAll(dfaResult, test8, answers8);
        System.out.println();
        debug = 0;
        
        
        debug = 0;
        //$PLUS \+
        regex = "\\+";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();;
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test9[] = {"+", "123", "asdfasdf", "!3543", "453345.423543", "-"};
        boolean answers9[] = {true, false, false, false, false, false};
        NFATest.testAll(dfaResult, test9, answers9);
        System.out.println();
        debug = 0;
        
        
        debug = 0;
        //$MINUS -
        regex = "-";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();;
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test10[] = {"-", "123", "asdfasdf", "!3543", "453345.423543", "+"};
        boolean answers10[] = {true, false, false, false, false, false};
        NFATest.testAll(dfaResult, test10, answers10);
        System.out.println();
        debug = 0;
        
        
        debug = 0;
        //$MULTIPLY \*
        regex = "\\*";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();;
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test11[] = {"*", "123", "asdfasdf", "!3543", "453345.423543", "+"};
        boolean answers11[] = {true, false, false, false, false, false};
        NFATest.testAll(dfaResult, test11, answers11);
        System.out.println();
        debug = 0;
        
        debug = 0;
        //$PRINT PRINT
        regex = "PRINT";
        System.out.println("*****Testing: "+regex);
        exampleDefinedClass = createDefinedClasses();;
        
        result = validateRegex(regex, exampleDefinedClass);
        resultChar = NFA.oneLayerTransitions(result);
        Util.reallyPrettyPrint(resultChar);
//        result.prettyPrint();
        dfaResult = DFA.convertNFA(result);
        String test12[] = {"PRINT", "123", "asdfasdf", "!3543", "453345.423543", "+"};
        boolean answers12[] = {true, false, false, false, false, false};
        NFATest.testAll(dfaResult, test12, answers12);
        System.out.println();
        debug = 0;
        
    }
    
    private static HashMap<String, NFA> createDefinedClasses(){
        HashMap<String, NFA> exampleDefinedClass = new HashMap<String, NFA>();
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
    
    private static void initDefinedEntry(HashMap<String, NFA> exampleDefinedClass, String regex, String key){
        int debugFlag = debug;
        int resultsFlag = returnValueDebug;
        debug = 0;
        returnValueDebug = 0;
        NFA entry = validateRegex(regex, exampleDefinedClass);
        exampleDefinedClass.put(key, entry);
        debug = debugFlag;
        returnValueDebug = resultsFlag;
        
        
    }
    
    //This is the function that you need to call!
    public static NFA validateRegex(String regex, HashMap<String, NFA> definedClass){
        debugPrint("In validateRegex()");
        init(regex, definedClass);
        return reGex();
    }
    private static void init(String regex, HashMap<String, NFA> definedClass){
        debugPrint("In init()");
        list = new ArrayList<Character>();
        counter = new Integer(0);
        index = 0;
        initList(regex);
        RESpecials = new HashSet<Character>();
        for(Character character: RE_Special){
           RESpecials.add(character);
        }
        CLSSpecials = new HashSet<Character>();
        for(Character character: CLS_Special){
           CLSSpecials.add(character);
        }
       
        definedClasses = new HashMap<String, NFA>();
        definedClasses.putAll(definedClass);
        
        rangeCount = 0;
        
    }

    /*
      Recursive descent parser begins here
    */

    //<reg-ex> ->  <rexp> 
    private static NFA reGex(){
        debugPrint("In reGex");
        return rexp();
    }
    
    //<rexp> -> <rexp1> <rexp’>
    private static NFA rexp(){
        debugPrint("In rexp()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        NFA rexp1 = rexp1();
        
        if(rexp1 == null){
            resultsPrint("Null");
            return null;
        }
        
        NFA rexpPrime = rexpPrime();
        if(rexpPrime == null){
            resultsPrint("Null");
            return null;
        }
        resultsPrint("Concatenate of rexp1 and rexp`");
        return NFA.union(rexp1, rexpPrime);
    }
    //<rexp’> -> UNION <rexp1> <rexp’>  | epsilon
    private static NFA rexpPrime(){
        debugPrint("In rexp`()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        if(top() == '|'){
            consume();
        }
        else{
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA(); 
        }
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        NFA rexp1 = rexp1();
        if(rexp1 == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        NFA rexpPrime = rexpPrime();
        if(rexpPrime == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        resultsPrint("Concatenate rexp1 and rexp`");
        //Can always return true;
        return NFA.union(rexp1, rexpPrime);
    }

    //<rexp1> -> <rexp2> <rexp1’>
    private static NFA rexp1(){
        debugPrint("In rexp1()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        NFA rexp2 = rexp2();
        if(rexp2 == null){
            resultsPrint("NULL");
            return null;
        }
        NFA rexp1Prime = rexp1Prime();
        if(rexp1Prime == null){
            resultsPrint("NULL");
            return null;
        }
        
        resultsPrint("Concatenate rexp2 and rexp1`");
        return NFA.concatenate(rexp2, rexp1Prime);
    }

    //<rexp1’> -> <rexp2> <rexp1’>  | epsilon
    private static NFA rexp1Prime(){
        debugPrint("In rexp1Prime()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        NFA rexp2 = rexp2();
        if(rexp2 == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        
        NFA rexp1Prime = rexp1Prime();
        if(rexp1Prime == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        
        resultsPrint("Conatenate rexp2 and rexp1`");
        return NFA.concatenate(rexp2, rexp1Prime);
    }
    
    
    //<rexp2> -> (<rexp>) <rexp2-tail>  | RE_CHAR <rexp2-tail> | <rexp3>
    private static NFA rexp2(){
        debugPrint("In rexp2()");
        if(top() == null){
            resultsPrint("null");
//            return createEpsilonNFA();
            return null;
        }
        
//        System.out.println("PART1");
        //Part 1
        //(<rexp>) <rexp2-tail> 
        if(top() == '('){
            consume();

            NFA rexp = rexp();
            if(top() == null){
                resultsPrint("null");
                return null;
            }
            if(rexp != null && top() == ')'){
                consume();
                Character rexp2Tail = rexp2Tail();
                if(rexp2Tail != null){
//                    NFA result = NFA.concatenate(leftParens, rexp);
//                    result = NFA.concatenate(result, rightParens);
//                    resultsPrint("Concatenate of rexp and rexp2-tail");
                    
                    return NFA.star(rexp);
                }
            }
        }
        
//        System.out.println("PART2");
        //Part 2
        //RE_CHAR <rexp2-tail>
        NFA RE_CHAR = RE_CHAR();
        Character rexp2Tail = null;
        if(RE_CHAR != null)
            rexp2Tail = rexp2Tail();
        if(rexp2Tail != null){
//            resultsPrint("RE_CHAR and rexp2Tail: "+rexp2Tail);
            
//            return NFA.concatenate(RE_CHAR, rexp2Tail);
//          System.out.println("rexp2Tail is: "+rexp2Tail);
            if(rexp2Tail != '*')
                return NFA.star(RE_CHAR);
            else if(rexp2Tail == Transition.EPSILON)
                return RE_CHAR;
        }

//        System.out.println("PART3");
        
        //Part3
        //<rexp3>
        return rexp3();
    }

    //<rexp2-tail> -> * | + |  epsilon
    private static Character rexp2Tail(){
        debugPrint("In rexp2Tail()");
        if(top() == null){
            resultsPrint("null");
            return Transition.EPSILON;
        }
        Character result = Transition.EPSILON;;
        if(top() == '*'){
            consume();
            result = '*';
            resultsPrint("*");
        }
        else if(top() == '+'){
            consume();
            result = '+';
            resultsPrint("+");
        }
        
        
        return result;
    }
    
    //<rexp3> -> <char-class>  |  epsilon
    private static NFA rexp3(){
        debugPrint("In rexp3()");
        if(top() == null){
            resultsPrint("null");
//            return createEpsilonNFA();
            return null;
        }
        NFA charClass = charClass();
        if(charClass != null){
            resultsPrint("charClass");
            return charClass;
        }
        resultsPrint("null");
       // return createEpsilonNFA();
        return null;
    }

    //<char-class> ->  .  |  [ <char-class1>  | <defined-class>
    private static NFA charClass(){
        debugPrint("In charClass()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        //Part1 
        //.
        if(top() == '.'){
            consume();
            resultsPrint(".");
            return createLiteralNFA('.');
        }

        //Part2
        //[ <char-class1>
        if(top() == '['){
            consume();
            //NFA leftBracket = createLiteralNFA('[');
            NFA charClass1 = charClass1();
            if(charClass1 != null){
                //return NFA.concatenate(leftBracket, charClass1);
                resultsPrint("CharClass1");
                return charClass1;
            }
        }

        //Part3
        //<defined-class>
        resultsPrint("DefinedClass");
        return definedClass();
    }
    
    //<char-class1> ->  <char-set-list> | <exclude-set>
    private static NFA charClass1(){
        debugPrint("In charClass1()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        //Part 1
        //<char-set-list>
        NFA charSetList = charSetList();
        
        if(charSetList != null){
            resultsPrint("charSetLIst");
            HashSet<Character> charSetListChar = NFA.oneLayerTransitions(charSetList);
            /*System.out.println("****Printing charSetList");
            Util.reallyPrettyPrint(charSetListChar);*/
            return charSetList;
        }
        
        resultsPrint("ExcludeSet");
        //Part2
        //<exclude-set>
        return excludeSet();
    }
    
    //<char-set-list> ->  <char-set> <char-set-list> |  ]
    private static NFA charSetList(){
        debugPrint("In charSetList()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        NFA charSet = charSet();
        if(charSet != null){
            NFA charSetList = charSetList();
            if(charSetList != null){
                resultsPrint("Concatenate charSet and charSetList");
                return NFA.union(charSet, charSetList);
            }
        }
        
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        
        if(top() == ']'){
            consume();
            //return createLiteralNFA(']');
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        
        resultsPrint("NULL");
        return null;
    }
    
    //<char-set> -> CLS_CHAR <char-set-tail> 
    private static NFA charSet(){
        debugPrint("In charSet()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        Character CLS_CHAR = CLS_CHAR();
        if(CLS_CHAR == null){
            resultsPrint("NULL");
            return null;
        }
        Character charSetTail = charSetTail();
//        System.out.println("charsettail is "+charSetTail);
        if(charSetTail != null){
            //return NFA.concatenate(CLS_CHAR, charSetTail);
            if((int) charSetTail != (int)Transition.EPSILON){
                resultsPrint("Created RangedNFA");
//                System.out.println("Value of CLS_CHAR is: "+CLS_CHAR);
//                System.out.println("Value of charSetTail is: "+charSetTail);
                
                
                
                return NFA.makeRangedNFA(CLS_CHAR, charSetTail);
            }
            else{
                resultsPrint("Did not create RangedNFA");
                return NFA.makeCharNFA(CLS_CHAR);
            }
        }
        resultsPrint("NULL");
        return null;
    }
    
    //<char-set-tail> -> –CLS_CHAR | epsilon
    private static Character charSetTail(){
        debugPrint("In charSetTail()");
        if(top() == null){
            resultsPrint("Epsilon");
            return Transition.EPSILON;
        }
        if(top() == '-'){
            consume();
            //NFA dash = createLiteralNFA('-');
            
            Character CLS_CHAR = CLS_CHAR();
//            System.out.println("CLS_CHar is "+CLS_CHAR);
            if(CLS_CHAR != null){
                //return NFA.concatenate(dash, CLS_CHAR);
                resultsPrint("CLS_CHAR: "+CLS_CHAR);
                return CLS_CHAR;
            }
    
        }
        resultsPrint("Epsilon");
        return Transition.EPSILON;
    }

    //<exclude-set> -> ^<char-set>] IN <exclude-set-tail> 
    private static NFA excludeSet(){
        debugPrint("In excludeSet()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        if(top() == '^'){
            consume();
            NFA charSet = charSet();
            if(top() == null){
                resultsPrint("Epsilon NFA");
                return createEpsilonNFA();
            }
            if(charSet != null && top() == ']'){
                consume();
                if(top() == 'I'){
                    consume();
                    if(top() == 'N'){
                        consume();
                        NFA excludeSetTail = excludeSetTail();

                        if(excludeSetTail != null){     
                            HashSet<Character> charSetChars = NFA.oneLayerTransitions(charSet);
//                          System.out.println("****Printing charSetChars");
//                          Util.reallyPrettyPrint(charSetChars);
                            HashSet<Character> excludeSetTailChars = NFA.oneLayerTransitions(excludeSetTail);
//                            System.out.println("\n");
//                            System.out.println("****Printing excludeSetTailChars");
//                          Util.reallyPrettyPrint(excludeSetTailChars);
                            HashSet<Character> disjointSet = NFA.disjointSet(excludeSetTailChars, charSetChars);
//                            System.out.println("****Printing disjointSet");
//                          Util.reallyPrettyPrint(disjointSet);
                            NFA result = NFA.makeRangedNFA(disjointSet);
                            resultsPrint("Disjoint set stuff");
                            return result;
                        }
                    }
                }
            }
        }
        resultsPrint("NULL");
        return null;
    }
    
    // <exclude-set-tail> -> [<char-set>]  | <defined-class>
    private static NFA excludeSetTail(){
        debugPrint("In excludeSetTail()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        if(top() == '['){
            consume();
            NFA leftBracket = createLiteralNFA('[');
            NFA charSet = charSet();
            if(top() == null){
                resultsPrint("Epsilon NFA");
                return createEpsilonNFA();
            }
            if(charSet != null && top() == ']'){
                consume();
                NFA rightBracket = createLiteralNFA(']');
                
                NFA result = NFA.concatenate(leftBracket, charSet);
                result = NFA.concatenate(result, rightBracket);
                resultsPrint("charSet");
                return result;
            }
        }
        
        resultsPrint("DefinedClass");
        return definedClass();
    }

    private static Character CLS_CHAR(){
        debugPrint("In CLS_CHAR()");
        if(top() == null){
            resultsPrint("Epsilon");
            //return createEpsilonNFA();
            return Transition.EPSILON;
        }
        
        //Checks to see if the next character is escaped
        boolean escaped = checkEscaped();
        debugPrint("escaped value is "+escaped);
        if(escaped){
            NFA backslash = createLiteralNFA('\\');
            boolean specialChar = false;
            //If there's a '\\', check to see if the escaped character is one that's
            //accepted by CLS_CHAR
            specialChar = CLSSpecialChars();
            if(specialChar){
                NFA special = createLiteralNFA(top());
                Character value = consume();
                //return NFA.concatenate(backslash, special);
                resultsPrint("Escaped CLS_CHAR: "+value);
                return value;
            }
        }
        
        if(top() == null){
            resultsPrint("Epsilon");
            return Transition.EPSILON;
        }
        debugPrint("Value of CLSSpecialChars is "+CLSSpecialChars());
        
        //Check to see if the next character SHOULD be escaped but it's not. If so, it fails
        if(CLSSpecialChars()){
            resultsPrint("NULL");
            return null;
        }
        
        //NFA literal = createLiteralNFA(top());
        Character value = consume();
        resultsPrint("CLS_CHAR: "+value);
        return value;
    }

    private static NFA RE_CHAR(){
        debugPrint("In RE_CHAR()");
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        //Checks to see if the next character is escaped
        boolean escaped = checkEscaped();
        debugPrint("escaped value is "+escaped);
        if(escaped){
            //NFA backslash = createLiteralNFA('\\');
            boolean specialChar = false;
            //If there's a '\\', check to see if the escaped character is one that's
            //accepted by RE_CHAR
            specialChar = RESpecialChars();
            if(specialChar){
                NFA special = createLiteralNFA(top());
                consume();
                //return NFA.concatenate(backslash, special);
                return special;
            }
        }
        debugPrint("Value of RESpecialChars is "+RESpecialChars());
        
        //Check to see if the next character SHOULD be escaped but it's not. If so, it fails
        if(RESpecialChars()){
            resultsPrint("NULL");
            return null;
        }
        
        NFA literal = createLiteralNFA(top());
        consume();
        resultsPrint("Returned literal");
        return literal;
    }
    
    
    private static NFA definedClass(){
        if(top() == null){
            resultsPrint("Epsilon NFA");
            return createEpsilonNFA();
        }
        debugPrint("In definedClass()");
        for(String defined: definedClasses.keySet()){
            boolean found = true;
            for(int index = 0; index < defined.length(); index++){
                if(get(index).equals(defined.charAt(index)) == false){
                    found = false;
                    break;
                }
            }
            if(found){
                resultsPrint("DefinedClass: "+defined);
                NFA definedNFA = definedClasses.get(defined);
                resultsPrint("DefinedClass's nfa is not null is: " +(definedNFA != null));
                for(int index = 0; index < defined.length(); index++){
                    consume();
                }
//                definedNFA.prettyPrint();
                
                //Original working
//                definedClasses.put(defined, new NFA(definedNFA));
//                return definedNFA;
                
                HashSet<Character> copySet = NFA.oneLayerTransitions(definedNFA);
                return NFA.makeRangedNFA(copySet);
                
//                return new NFA(definedNFA);
            }
        }
        resultsPrint("null");
        return null;
    }
    
    //Checks to see if the next character is the escape character '\\'
    //If so, it consumes it and returns true
    //If not, it does nothing
    private static boolean checkEscaped(){
        debugPrint("In checkEscaped()");
        if(top() == null)
            return false;
        if(top() == '\\'){
                consume();
            return true;
        }
        return false;
    }
    
    //Checks to see if the head of the list is a specialcharacter for RE that should be escaped or not
    private static boolean RESpecialChars(){
        if(top() == null)
            return false;
        debugPrint("In RESpecialChars()");
        return RESpecials.contains(top());
    }
    
    //Checks to see if the head of the list is a specialcharacter for CLS that should be escaped or not
    private static boolean CLSSpecialChars(){
        if(top() == null)
            return false;
        debugPrint("In CLSSpecialChars()");
        return CLSSpecials.contains(top());
    }


    /*
        Bookkeeping stuff
    */
    
    //Returns the value of at the head of the list, same as peek()
    private static Character top(){
//      debugPrint("In top()");
        if(index < list.size())
            return list.get(index);
//        return new Character('☃');]
        return null;
    }
    
    //Increases the index to "head" of the array list and returns the old value
    private static Character consume(){
        if(index < list.size()){
            debugPrint("*****Consumed: "+list.get(index));
//          System.out.println("Consumed: "+list.get(index));
            index++;
            return list.get(index - 1);
        }
        return null;
    }
    
    //Gets the character at a given offset from the begining of the list
    private static Character get(int offset){
//      debugPrint("In get()");
        if(index + offset <= list.size()){
            return list.get(index + offset);
        }
        return null;
    }
    
    //Initialize the list into the arraylist
    private static void initList(String regex){
        for(int x = 0; x < regex.length(); x++){
            list.add(regex.charAt(x));
        }
    }
    
    //Used for debug printing
    private static void debugPrint(String statement){
        if(debug == 1)
            System.out.println(statement);
    }
    
    private static void resultsPrint(String value){
        if(returnValueDebug == 1)
            System.out.println("Returning "+value);
    }
    
    //Creates an NFA with an epsilon
    private static NFA createEpsilonNFA(){
        return NFA.makeCharNFA(Transition.EPSILON);
    }
    
    //Creates an NFA for a given literal
    private static NFA createLiteralNFA(char literal){
        return NFA.makeCharNFA(literal);
    }
}