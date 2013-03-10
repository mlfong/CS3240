import java.util.HashMap;
import java.util.HashSet;

/******
 * @author mlfong
 * @version 1.0
 *****/

public class TokenUtil
{
    public static int SIMP_STR = 0;
    public static int SIMP_MAP = 0;

    private static final HashSet<Character> REGEX_CHARS = new HashSet<Character>()
    {   private static final long serialVersionUID = 3983065818321009688L;
    { this.add('('); this.add(')'); this.add('+'); this.add('*'); this.add('|'); }};
    
    private static final HashSet<Character> POS_CHARS = new HashSet<Character>()
    {   private static final long serialVersionUID = 2690642328791035372L;
    { this.add('|'); this.add('+'); this.add('*'); }};
    
    public static boolean validateRegex(String re,
            HashMap<String, HashSet<Character>> characterClasses)
    {
        if(re == null || re.length() < 1) return false;
        re = re.replaceAll(" ", "");
        boolean inDollar = false;
        StringBuilder sb = new StringBuilder();
        int numLeftParen = 0, numClasses = 0;
        for (int i = 0; i < re.length(); i++)
        {
            char c = re.charAt(i);
            if (inDollar)
            {
                if (REGEX_CHARS.contains(c))
                {
                    if (!characterClasses.containsKey(sb.toString()))
                        return false;
                    inDollar = false;
                    if (c == '(')
                        numLeftParen++;
                    else if (c == ')')
                    {
                        numLeftParen--;
                        if (numLeftParen < 0)
                            return false;
                    }
                    else if(POS_CHARS.contains(c))
                    {
                        if(i-1 < 0) return false;
                        if(POS_CHARS.contains(re.charAt(i-1)))
                            return false;
                    }
                    sb.delete(0, sb.length());
                    numClasses++;
                }
                else
                {
                    if(c == '$')
                    {
                        if (!characterClasses.containsKey(sb.toString()))
                            return false;
                        sb.delete(0, sb.length());
                    }
                    sb.append(c);
                }
            }
            else
            {
                if (c == '$')
                {
                    inDollar = true;
                    sb.append(c);
                }
                else if(c == '(')
                    numLeftParen++;
                else if(c==')')
                {
                    numLeftParen--;
                    if(numLeftParen < 0) return false;
                }
                else if(POS_CHARS.contains(c))
                {
                    if(i-1 < 0) return false;
                    if(POS_CHARS.contains(re.charAt(i-1)))
                        return false;
                }
                else
                    return false;
            }
        }
        if(sb.length() > 0)
        {
            if (!characterClasses.containsKey(sb.toString()))
                return false;
            numClasses++;
        }
        if(numClasses == 0) return false;
        if(numLeftParen != 0) return false;
        return true;
    }
    
    public static boolean validateIdentifier(String s, 
            HashMap<String, HashSet<Character>> characterClasses)
    {
        System.out.println("s: " + s);
        int name_re_divider = s.indexOf(" ");
        if(name_re_divider < 0)
            return false;
        String name = s.substring(0, name_re_divider);
        System.out.println("name: " + name);
        if(name_re_divider >= s.length())
            return false;
        String rest = s.substring(name_re_divider+1);
        System.out.println("rest: " + rest);
        return false;
    }
}





