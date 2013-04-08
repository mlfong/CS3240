package rdp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
public class RecursiveDescentParserNFA{
    
    //Flag to turn on debug printing
    private static int debug = 0;
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
    
    private static String[] charClassTestCases = {"[0-9]", "[^0] IN $DIGIT", "[a-zA-Z]", "[^a-z] IN $CHAR", "[^A-Z] IN $CHAR"};
    
    private static ArrayList<String> definedClasses;
    public static void main(String[] args){
        //Initialize the definedclasses
        ArrayList<String> exampleDefinedClass = new ArrayList<String>();
        exampleDefinedClass.add("$DIGIT");
        exampleDefinedClass.add("$NON-ZERO");
        exampleDefinedClass.add("$CHAR");
        exampleDefinedClass.add("$UPPER");
        exampleDefinedClass.add("$LOWER");
        exampleDefinedClass.add("$IDENTIFIER");
        exampleDefinedClass.add("$INT");
        exampleDefinedClass.add("$FLOAT");
        exampleDefinedClass.add("$ASSIGN");
        exampleDefinedClass.add("$PLUS");
        exampleDefinedClass.add("$MINUS");
        exampleDefinedClass.add("$MULTIPLY");
        exampleDefinedClass.add("$PRINT");
        
        for(String line: charClassTestCases){
        //Creates the nfa
	        NFA result = validateRegex(line, exampleDefinedClass);
	        if(result == null)
	            System.out.println("FAILED: "+line+" was null...");
	        else
	            System.out.println("PASSED: "+line);
        }
        

    }
    //This is the function that you need to call!
    public static NFA validateRegex(String regex, ArrayList<String> definedClass){
        debugPrint("In validateRegex()");
        init(regex, definedClass);
        return reGex();
    }
    private static void init(String regex, ArrayList<String> definedClass){
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
       
        definedClasses = new ArrayList<String>(definedClass);
        
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
        if(top() == null)
        	return createEpsilonNFA();
        NFA rexp1 = rexp1();
        
        if(rexp1 == null)
            return null;
        
        NFA rexpPrime = rexpPrime();
        if(rexpPrime == null)
            return null;

        return NFA.union(rexp1, rexpPrime);
    }
    //<rexp’> -> UNION <rexp1> <rexp’>  | epsilon
    private static NFA rexpPrime(){
        debugPrint("In rexp`()");
        if(top() == null)
        	return createEpsilonNFA();
        NFA rexp1 = rexp1();
        if(rexp1 == null)
            return createEpsilonNFA();
        NFA rexpPrime = rexpPrime();
        if(rexpPrime == null)
            return createEpsilonNFA();

        //Can always return true;
        return NFA.union(rexp1, rexpPrime);
    }

    //<rexp1> -> <rexp2> <rexp1’>
    private static NFA rexp1(){
        debugPrint("In rexp1()");
        if(top() == null)
        	return createEpsilonNFA();
        NFA rexp2 = rexp2();
        if(rexp2 == null)
            return null;
        NFA rexp1Prime = rexp1Prime();
        if(rexp1Prime == null)
            return null;

        return NFA.concatenate(rexp2, rexp1Prime);
    }

    //<rexp1’> -> <rexp2> <rexp1’>  | epsilon
    private static NFA rexp1Prime(){
        debugPrint("In rexp1Prime()");
        if(top() == null)
        	return createEpsilonNFA();
        NFA rexp2 = rexp2();
        if(rexp2 == null)
            return createEpsilonNFA();
        
        NFA rexp1Prime = rexp1Prime();
        if(rexp1Prime == null)
            return createEpsilonNFA();

        return NFA.concatenate(rexp2, rexp1Prime);
    }
    
    
    //<rexp2> -> (<rexp>) <rexp2-tail>  | RE_CHAR <rexp2-tail> | <rexp3>
    private static NFA rexp2(){
        debugPrint("In rexp2()");
        if(top() == null)
        	return createEpsilonNFA();
        
        //Part 1
        //(<rexp>) <rexp2-tail> 
        if(top() == '('){
            consume();
            NFA leftParens = createLiteralNFA('(');
            
            NFA rexp = rexp();
            if(top() == null)
            	return createEpsilonNFA();
            if(rexp != null && top() == ')'){
                consume();
                NFA rightParens = createLiteralNFA(')');
                NFA rexp2Tail = rexp2Tail();
                if(rexp2Tail != null){
                    NFA result = NFA.concatenate(leftParens, rexp);
                    result = NFA.concatenate(result, rightParens);
                    return result;
                }
            }
        }
        //Part 2
        //RE_CHAR <rexp2-tail>
        NFA RE_CHAR = RE_CHAR();
        NFA rexp2Tail = null;
        if(RE_CHAR != null)
            rexp2Tail = rexp2Tail();
        if(rexp2Tail != null)
            return NFA.concatenate(RE_CHAR, rexp2Tail);

        //Part3
        //<rexp3>
        return rexp3();
    }

    //<rexp2-tail> -> * | + |  epsilon
    private static NFA rexp2Tail(){
        debugPrint("In rexp2Tail()");
        if(top() == null)
        	return createEpsilonNFA();
        NFA result = null;
        if(top() == '*'){
            consume();
            result = createLiteralNFA('*');
        }
        else if(top() == '+'){
            consume();
            result = createLiteralNFA('+');
        }

        return result;
    }
    
    //<rexp3> -> <char-class>  |  epsilon
    private static NFA rexp3(){
        debugPrint("In rexp3()");
        if(top() == null)
        	return createEpsilonNFA();
        NFA charClass = charClass();
        if(charClass != null)
            return charClass;
        
        return createEpsilonNFA();
    }

    //<char-class> ->  .  |  [ <char-class1>  | <defined-class>
    private static NFA charClass(){
        debugPrint("In charClass()");
        if(top() == null)
        	return createEpsilonNFA();
        //Part1 
        //.
        if(top() == '.'){
            consume();
            return createLiteralNFA('.');
        }

        //Part2
        //[ <char-class1>
        if(top() == '['){
            consume();
            NFA leftBracket = createLiteralNFA('[');
            NFA charClass1 = charClass1();
            if(charClass1 != null){
                return NFA.concatenate(leftBracket, charClass1);
            }
        }

        //Part3
        //<defined-class>
        return definedClass();
    }
    
    //<char-class1> ->  <char-set-list> | <exclude-set>
    private static NFA charClass1(){
        debugPrint("In charClass1()");
        if(top() == null)
        	return createEpsilonNFA();
        //Part 1
        //<char-set-list>
        NFA charSetList = charSetList();
        
        if(charSetList != null)
            return charSetList;
        
        //Part2
        //<exclude-set>
        return excludeSet();
    }
    
    //<char-set-list> ->  <char-set> <char-set-list> |  ]
    private static NFA charSetList(){
        debugPrint("In charSetList()");
        if(top() == null){
        	return createEpsilonNFA();
        }
        NFA charSet = charSet();
        if(charSet != null){
            NFA charSetList = charSetList();
            if(charSetList != null)
                return NFA.concatenate(charSet, charSetList);
        }
        
        if(top() == null)
        	return createEpsilonNFA();
        
        if(top() == ']'){
            consume();
            return createLiteralNFA(']');
        }

        return null;
    }
    
    //<char-set> -> CLS_CHAR <char-set-tail> 
    private static NFA charSet(){
        debugPrint("In charSet()");
        if(top() == null)
        	return createEpsilonNFA();
        NFA CLS_CHAR = CLS_CHAR();
        if(CLS_CHAR == null)
            return null;
        NFA charSetTail = charSetTail();
        
        if(charSetTail != null)
            return NFA.concatenate(CLS_CHAR, charSetTail);
        
        return null;
    }
    
    //<char-set-tail> -> –CLS_CHAR | epsilon
    private static NFA charSetTail(){
        debugPrint("In charSetTail()");
        if(top() == null)
        	return createEpsilonNFA();
        if(top() == '-'){
            consume();
            NFA dash = createLiteralNFA('-');
            
            NFA CLS_CHAR = CLS_CHAR();
            if(CLS_CHAR != null)
                return NFA.concatenate(dash, CLS_CHAR);
    
        }
        
        return createEpsilonNFA();
    }

    //<exclude-set> -> ^<char-set>] IN <exclude-set-tail> 
    private static NFA excludeSet(){
        debugPrint("In excludeSet()");
        if(top() == null)
        	return createEpsilonNFA();
        if(top() == '^'){
            consume();
            NFA carrot = createLiteralNFA('^');
            NFA charSet = charSet();
            if(top() == null)
            	return createEpsilonNFA();
            if(charSet != null && top() == ']'){
                consume();
                NFA rightBracket = createLiteralNFA(']');
                if(top() == 'I'){
                    consume();
                    NFA I = createLiteralNFA('I');
                    if(top() == 'N'){
                        consume();
                        NFA N = createLiteralNFA('N');
                        NFA excludeSetTail = excludeSetTail();
                        
                        if(excludeSetTail != null){
                            NFA result = NFA.concatenate(carrot, charSet);
                            result = NFA.concatenate(result, rightBracket);
                            result = NFA.concatenate(result, I);
                            result = NFA.concatenate(result, N);
                            result = NFA.concatenate(result, excludeSetTail);
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    // <exclude-set-tail> -> [<char-set>]  | <defined-class>
    private static NFA excludeSetTail(){
        debugPrint("In excludeSetTail()");
        if(top() == null)
        	return createEpsilonNFA();
        if(top() == '['){
            consume();
            NFA leftBracket = createLiteralNFA('[');
            NFA charSet = charSet();
            if(top() == null)
            	return createEpsilonNFA();
            if(charSet != null && top() == ']'){
                consume();
                NFA rightBracket = createLiteralNFA(']');
                
                NFA result = NFA.concatenate(leftBracket, charSet);
                result = NFA.concatenate(result, rightBracket);
                return result;
            }
        }
        return definedClass();
    }

    private static NFA CLS_CHAR(){
        debugPrint("In CLS_CHAR()");
        if(top() == null)
        	return createEpsilonNFA();
        
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
                consume();
                return NFA.concatenate(backslash, special);
            }
        }
        
        if(top() == null)
        	return createEpsilonNFA();
        debugPrint("Value of CLSSpecialChars is "+CLSSpecialChars());
        
        //Check to see if the next character SHOULD be escaped but it's not. If so, it fails
        if(CLSSpecialChars())
            return null;
        
        NFA literal = createLiteralNFA(top());
        consume();
        return literal;
    }

    private static NFA RE_CHAR(){
        debugPrint("In RE_CHAR()");
        if(top() == null)
        	return createEpsilonNFA();
        //Checks to see if the next character is escaped
        boolean escaped = checkEscaped();
        debugPrint("escaped value is "+escaped);
        if(escaped){
            NFA backslash = createLiteralNFA('\\');
            boolean specialChar = false;
            //If there's a '\\', check to see if the escaped character is one that's
            //accepted by RE_CHAR
            specialChar = RESpecialChars();
            if(specialChar){
                NFA special = createLiteralNFA(top());
                consume();
                return NFA.concatenate(backslash, special);
            }
        }
        debugPrint("Value of RESpecialChars is "+RESpecialChars());
        
        //Check to see if the next character SHOULD be escaped but it's not. If so, it fails
        if(RESpecialChars())
            return null;
        
        NFA literal = createLiteralNFA(top());
        consume();
        return literal;
    }
    
    
    private static NFA definedClass(){
        if(top() == null)
        	return createEpsilonNFA();
        debugPrint("In definedClass()");
        for(String defined: definedClasses){
            boolean found = true;
            for(int index = 0; index < defined.length(); index++){
                if(get(index).equals(defined.charAt(index)) == false){
                    found = false;
                    break;
                }
            }
            if(found){
                LinkedList<NFA> list = new LinkedList<NFA>();
                for(int x = 0; x < defined.length(); x++){
                    NFA literal = createLiteralNFA(top());
                    consume();
                    list.add(literal);
                }
                NFA result = list.getFirst();
                list.remove();
                while(list.isEmpty()){
                    NFA next = list.getFirst();
                    result = NFA.concatenate(result, next);
                    list.remove();
                }
                return result;
            }
        }
        
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
    
    //Creates an NFA with an epsilon
    private static NFA createEpsilonNFA(){
        
        return createLiteralNFA(Transition.EPSILON);
    }
    
    //Creates an NFA for a given literal
    private static NFA createLiteralNFA(char literal){
        State s1pre = new State(counter.toString(), false);
        counter++;
        State s1mid = new State(counter.toString(), false);
        counter++;
        State s1fin = new State(counter.toString(), true);
        counter++;
        s1pre.addTransition(new Transition(literal, s1mid));
        s1mid.addTransition(new Transition(Transition.EPSILON, s1fin));
        NFA nfa1 = new NFA(s1pre, s1fin);
        return nfa1;
    }
}