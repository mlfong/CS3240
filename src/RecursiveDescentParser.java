import java.util.ArrayList;
import java.util.HashSet;
public class RecursiveDescentParser{


    private static ArrayList<Character> list;
    private static int index;
    private static int copyIndex;
    private static HashSet<Character> RESpecials;
    private static Character[] RE_Special = {' ', '\\', '*', '+', '?', '|', '[', ']', '(', ')', '.', '\'', '\"'};
    //2) escaped characters: \ (backslash space), \\, \*, \+, \?, \|, \[, \], \(, \), \., \' and \"
    private static HashSet<Character> CLSSpecials;
    private static Character[] CLS_Special = {'\\', '^', '-', '[', ']'};
    //1) http://en.wikipedia.org/wiki/ASCII#ASCII_printable_characters) other than \, ^, -, [ and ]
    
    private static ArrayList<String> definedClasses;
    public static void main(String[] args){

    }
    //This is the function that you need to call!
    public static boolean validateRegex(String regex, ArrayList<String> definedClass){
        init(regex, definedClass);
        return reGex();
    }
    private static void init(String regex, ArrayList<String> definedClass){
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

    private static boolean reGex(){
        return rexp();
    }
    private static boolean rexp(){
        boolean rexp1 = rexp1();
        boolean rexpPrime = rexpPrime();

        return rexp1 || rexpPrime;
    }
    private static boolean rexpPrime(){
        boolean rexp1 = rexp1();
        boolean rexpPrime = rexpPrime();

        //Can always return true;
        return rexp1 || rexpPrime || true;
    }

    private static boolean rexp1(){
        boolean rexp2 = rexp2();
        boolean rexp1Prime = rexp1Prime();

        return rexp2 && rexp1Prime;
    }

    private static boolean rexp1Prime(){
        boolean rexp2 = rexp2();
        boolean rexp1Prime = rexp1Prime();

        //Can always return true;
        return (rexp2 && rexp1Prime) || true;
    }

    private static boolean rexp2(){
        //Part 1
        //(<rexp>) <rexp2-tail> 
        boolean part1 = false;
        if(top() == '('){
            boolean rexp = rexp();
            if(rexp && top() == ')'){
                boolean rexp2Tail = rexp2Tail();
                if(rexp2Tail){
                    part1 = true;
                }
            }
        }

        //Part 2
        //RE_CHAR <rexp2-tail>
        boolean part2 = false;
        boolean RE_CHAR = RE_CHAR();
        boolean rexp2Tail = rexp2Tail();
        part2 = RE_CHAR && rexp2Tail;

        //Part3
        //<rexp3>
        boolean part3 = rexp3();

        return part1 || part2 || part3;
    }

    private static boolean rexp2Tail(){
        boolean result = false;
        if(top() == '*' || top() == '+'){
            consume();
            result = true;
        }
        return result || true;
    }

    private static boolean rexp3(){
        boolean charClass = charClass();
        return charClass || true;
    }

    private static boolean charClass(){
        //Part1 
        //.
        boolean part1 = false;
        if(top() == '.'){
            consume();
            part1 = true;
        }


        //Part2
        //[ <char-class1>

        boolean part2 = false;
        if(!part1 && top() == '['){
            boolean charClass1 = charClass1();
            if(charClass1){
                consume();
                part2 = true;
            }
        }

        //Part3
        //<defined-class>
        boolean part3 = definedClass();

        return part1 || part2 || part3;
    }

    private static boolean charClass1(){
        //Part 1
        //<char-set-list>
        boolean charSetList = charSetList();

        //Part2
        //<exclude-set>
        boolean excludeSet = excludeSet();

        return charSetList || excludeSet;
    }
    private static boolean charSetList(){
        boolean charSet = charSet();
        boolean charSetList = charSetList();
        boolean part1 = charSet && charSetList();

        boolean part2 = false;
        if(top() == ']'){
            consume();
            part2 = true;
        }

        return part1 || part2;
    }

    private static boolean charSet(){
        boolean CLS_CHAR = CLS_CHAR();
        boolean charSetTail = charSetTail();

        return CLS_CHAR && charSetTail;
    }

    private static boolean charSetTail(){
        boolean CLS_CHAR = CLS_CHAR();

        return CLS_CHAR || true;
    }

    private static boolean excludeSet(){
        if(top() == '^'){
            consume();
            boolean charSet = charSet();
            if(charSet && top() == ']'){
                if(top() == 'I'){
                    consume();
                    if(top() == 'N'){
                        return excludeSet();
                    }
                }
            }
        }
        return false;
    }

    private static boolean excludeSetTail(){
        boolean part1 = false;
        if(top() == '['){
            boolean charSet = charSet();
            if(charSet && top() == ']'){
                part1 = true;
            }
        }
        boolean part2 = definedClass();

        return part1 || part2;
    }

    private static boolean CLS_CHAR(){
        boolean escaped = checkEscaped();
        boolean specialChar = false;
        if(!escaped){
            specialChar = RESpecialChars();
            if(specialChar){
                return false;
            }
        }
        consume();
        
        return true;
    }

    private static boolean RE_CHAR(){
        boolean escaped = checkEscaped();
        boolean specialChar = false;
        if(!escaped){
            specialChar = CLSSpecialChars();
            if(specialChar){
                return false;
            }
        }
        consume();
        
        return true;
    }
    
    private static boolean checkEscaped(){
        if(top() == '\\'){
                consume();
            return true;
        }
        return false;
    }
    private static boolean RESpecialChars(){
        return RESpecials.contains(top());
    }
    private static boolean CLSSpecialChars(){
        return CLSSpecials.contains(top());
    }
    
    
    private static boolean definedClass(){
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
        if(index < list.size())
            return list.get(index);
        return null;
    }
    private static Character consume(){
        if(index < list.size()){
            index++;
            return list.get(index - 1);
        }
        return null;
    }

    private static Character get(int offset){
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
}