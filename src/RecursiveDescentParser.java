import java.util.ArrayList;
import java.util.HashSet;
public class RecursiveDescentParser{

    private static int debug = 1;
    private static ArrayList<Character> list;
    private static int index;
    private static int copyIndex;
    private static HashSet<Character> RESpecials;
    //MAKE SURE TO ADD \$dddd
    private static Character[] RE_Special = {' ', '\\', '*', '+', '?', '|', '[', ']', '(', ')', '.', '\'', '\"', '$'};
    //2) escaped characters: \ (backslash space), \\, \*, \+, \?, \|, \[, \], \(, \), \., \' and \"
    private static HashSet<Character> CLSSpecials;
    private static Character[] CLS_Special = {'\\', '^', '-', '[', ']'};
    //1) http://en.wikipedia.org/wiki/ASCII#ASCII_printable_characters) other than \, ^, -, [ and ]
    
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
        String line = "($DIGIT)+";
        System.out.println("The value was: "+validateRegex(line, exampleDefinedClass));
        

    }
    //This is the function that you need to call!
    public static boolean validateRegex(String regex, ArrayList<String> definedClass){
        debugPrint("In validateRegex()");
        init(regex, definedClass);
        return reGex();
    }
    private static void init(String regex, ArrayList<String> definedClass){
        debugPrint("In init()");
        list = new ArrayList<Character>();
        index = 0;
        copyIndex = 0;
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
    private static boolean reGex(){
        if(top() == null)
            return false;
        debugPrint("In reGex");
        return rexp();
    }
    
    //<rexp> -> <rexp1> <rexp’>
    private static boolean rexp(){
        debugPrint("In rexp()");
        if(top() == null)
            return false;
        boolean rexp1 = rexp1();
        boolean rexpPrime = rexpPrime();

        return rexp1 || rexpPrime;
    }
    //<rexp’> -> UNION <rexp1> <rexp’>  | epsilon
    private static boolean rexpPrime(){
        debugPrint("In rexp`()");
        if(top() == null)
            return false;
        boolean rexp1 = rexp1();
        if(!rexp1)
            return false;
        boolean rexpPrime = rexpPrime();

        //Can always return true;
        return rexp1 || rexpPrime || false;
    }

    //<rexp1> -> <rexp2> <rexp1’>
    private static boolean rexp1(){
        debugPrint("In rexp1()");
        if(top() == null)
            return false;
        boolean rexp2 = rexp2();
        boolean rexp1Prime = rexp1Prime();

        return rexp2 && rexp1Prime;
    }

    //<rexp1’> -> <rexp2> <rexp1’>  | epsilon
    private static boolean rexp1Prime(){
        debugPrint("In rexp1Prime()");
        if(top() == null)
            return false;
        boolean rexp2 = rexp2();
        if(rexp2 == false)
            return true;//epsilon
        boolean rexp1Prime = rexp1Prime();

        //Can always return true;
        return (rexp2 && rexp1Prime) || false;
    }

    //<rexp2> -> (<rexp>) <rexp2-tail>  | RE_CHAR <rexp2-tail> | <rexp3>
    private static boolean rexp2(){
        debugPrint("In rexp2()");
        if(top() == null)
            return false;
        
        //Part 1
        //(<rexp>) <rexp2-tail> 
        boolean part1 = false;
        if(top() == '('){
            consume();
            boolean rexp = rexp();
            if(top() == null)
                return false;
            if(rexp && top() == ')'){
                consume();
                boolean rexp2Tail = rexp2Tail();
                if(rexp2Tail){
                    part1 = true;
                }
            }
        }
        if(part1 == true)
            return true;

        //Part 2
        //RE_CHAR <rexp2-tail>
        boolean part2 = false;
        boolean RE_CHAR = RE_CHAR();
        boolean rexp2Tail = false;
        if(RE_CHAR)
            rexp2Tail = rexp2Tail();
        part2 = RE_CHAR && rexp2Tail;
        if(part2 == true)
            return true;

        //Part3
        //<rexp3>
        boolean part3 = rexp3();

        return part1 || part2 || part3;
    }

    //<rexp2-tail> -> * | + |  epsilon
    private static boolean rexp2Tail(){
        debugPrint("In rexp2Tail()");
        if(top() == null)
            return false;
        boolean result = false;
        if(top() == '*' || top() == '+'){
            consume();
            result = true;
        }
        //Can return true
        return result || false;
    }
    
    //<rexp3> -> <char-class>  |  epsilon
    private static boolean rexp3(){
        debugPrint("In rexp3()");
        if(top() == null)
            return false;
        boolean charClass = charClass();
        return charClass || false;
    }

    //<char-class> ->  .  |  [ <char-class1>  | <defined-class>
    private static boolean charClass(){
        debugPrint("In charClass()");
        if(top() == null)
            return false;
        //Part1 
        //.
        boolean part1 = false;
        if(top() == '.'){
            consume();
            part1 = true;
            return true;
        }


        //Part2
        //[ <char-class1>

        boolean part2 = false;
        if(!part1 && top() == '['){
            boolean charClass1 = charClass1();
            if(charClass1){
                consume();
                part2 = true;
                return true;
            }
        }

        //Part3
        //<defined-class>
        boolean part3 = definedClass();

        return part1 || part2 || part3;
    }
    
    //<char-class1> ->  <char-set-list> | <exclude-set>
    private static boolean charClass1(){
        debugPrint("In charClass1()");
        if(top() == null)
            return false;
        //Part 1
        //<char-set-list>
        boolean charSetList = charSetList();
        
        if(charSetList)
            return true;
        
        //Part2
        //<exclude-set>
        boolean excludeSet = excludeSet();

        return charSetList || excludeSet;
    }
    
    //<char-set-list> ->  <char-set> <char-set-list> |  ]
    private static boolean charSetList(){
        debugPrint("In charSetList()");
        debugPrint("Value of top() is "+top());
        if(top() == null){
//          System.out.println("Top is null");
            return false;
        }
        boolean charSet = charSet();
//        debugPrint("After charSet()");
        boolean charSetList = charSetList();
//      debugPrint("after charSetList");
        boolean part1 = charSet && charSetList();

        if(part1)
            return true;
        
        boolean part2 = false;
        if(top() == null)
            return false;
        
        if(top() == ']'){
            consume();
            part2 = true;
        }

        return part1 || part2;
    }
    
    //<char-set> -> CLS_CHAR <char-set-tail> 
    private static boolean charSet(){
        debugPrint("In charSet()");
        if(top() == null)
            return false;
        boolean CLS_CHAR = CLS_CHAR();
        if(!CLS_CHAR)
            return false;
        boolean charSetTail = charSetTail();

        return CLS_CHAR && charSetTail;
    }
    
    //<char-set-tail> -> –CLS_CHAR | epsilon
    private static boolean charSetTail(){
        debugPrint("In charSetTail()");
        if(top() == null)
            return false;
        boolean CLS_CHAR = false;
        if(top() == '-'){
            consume();
            CLS_CHAR = CLS_CHAR();
    
        }
        return CLS_CHAR || false;
    }

    //<exclude-set> -> ^<char-set>] IN <exclude-set-tail> 
    private static boolean excludeSet(){
        debugPrint("In excludeSet()");
        if(top() == null)
            return false;
        if(top() == '^'){
            consume();
            boolean charSet = charSet();
            if(top() == null)
                return false;
            if(charSet && top() == ']'){
                consume();
                if(top() == 'I'){
                    consume();
                    if(top() == 'N'){
                        consume();
                        return excludeSetTail();
                    }
                }
            }
        }
        return false;
    }
    
    // <exclude-set-tail> -> [<char-set>]  | <defined-class>
    private static boolean excludeSetTail(){
        debugPrint("In excludeSetTail()");
        if(top() == null)
            return false;
        boolean part1 = false;
        if(top() == '['){
            consume();
            boolean charSet = charSet();
            if(top() == null)
                return false;
            if(charSet && top() == ']'){
                consume();
                part1 = true;
                return true;
            }
        }
        boolean part2 = definedClass();

        return part1 || part2;
    }

    private static boolean CLS_CHAR(){
        debugPrint("In CLS_CHAR()");
        if(top() == null)
            return false;
        boolean escaped = checkEscaped();
        debugPrint("escaped value is "+escaped);
        boolean specialChar = false;
        if(escaped){
            specialChar = CLSSpecialChars();
            if(specialChar){
                return true;
            }
        }
        if(top() == null)
            return false;
        debugPrint("Value of CLSSpecialChars is "+CLSSpecialChars());
        if(CLSSpecialChars())
            return false;
        
        consume();
        return true;
    }

    private static boolean RE_CHAR(){
        debugPrint("In RE_CHAR()");
        if(top() == null)
            return false;
        boolean escaped = checkEscaped();
        debugPrint("escaped value is "+escaped);
        boolean specialChar = false;
        if(escaped){
            specialChar = RESpecialChars();
            if(specialChar){
                return true;
            }
        }
        debugPrint("Value of RESpecialChars is "+RESpecialChars());
        if(RESpecialChars())
            return false;
        
        consume();
        return true;
    }
    
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
    private static boolean RESpecialChars(){
        if(top() == null)
            return false;
        debugPrint("In RESpecialChars()");
        return RESpecials.contains(top());
    }
    private static boolean CLSSpecialChars(){
        if(top() == null)
            return false;
        debugPrint("In CLSSpecialChars()");
        return CLSSpecials.contains(top());
    }
    
    
    private static boolean definedClass(){
        if(top() == null)
            return false;
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
                for(int x = 0; x < defined.length(); x++){
                    consume();
                }
                return true;
            }
        }
        
        return false;
    }


    /*
        Bookkeeping stuff
    */

    private static Character top(){
//      debugPrint("In top()");
        if(index < list.size())
            return list.get(index);
//        return new Character('☃');]
        return null;
    }
    private static Character consume(){
        if(index < list.size()){
            debugPrint("*****Consumed: "+list.get(index));
//          System.out.println("Consumed: "+list.get(index));
            index++;
            return list.get(index - 1);
        }
        return null;
    }

    private static Character get(int offset){
//      debugPrint("In get()");
        if(index + offset <= list.size()){
            return list.get(index + offset);
        }
        return null;
    }
    private static void initList(String regex){
        for(int x = 0; x < regex.length(); x++){
            list.add(regex.charAt(x));
        }
    }
    private static void createCopy(){
        copyIndex = index;
    }

    private static void revertList(){
        index = copyIndex;
    }
    
    private static void debugPrint(String statement){
        if(debug == 1)
            System.out.println(statement);
    }
}