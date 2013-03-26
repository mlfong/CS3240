import java.util.HashSet;

public class RecursiveDescentParser
{
    public static HashSet<String> RE_CHAR = new HashSet<String>()
    {
        {
            for(int i = 32; i < 126; i++)
            {
                String s = "" + ((char)i);
                RE_CHAR.add(s);
            }
            RE_CHAR.remove("\\"); RE_CHAR.add("\\\\");
            RE_CHAR.remove("*"); RE_CHAR.add("\\*");
            RE_CHAR.remove("+"); RE_CHAR.add("\\+");
            RE_CHAR.remove("?"); RE_CHAR.add("\\?");
            RE_CHAR.remove("|"); RE_CHAR.add("\\|");
            RE_CHAR.remove("["); RE_CHAR.add("\\[");
            RE_CHAR.remove("]"); RE_CHAR.add("\\]");
            RE_CHAR.remove("("); RE_CHAR.add("\\(");
            RE_CHAR.remove(")"); RE_CHAR.add("\\)");
            RE_CHAR.remove("."); RE_CHAR.add("\\.");
            RE_CHAR.remove("'"); RE_CHAR.add("\\'");
            RE_CHAR.remove("\""); RE_CHAR.add("\\\"");
            RE_CHAR.remove(" "); RE_CHAR.add("\\ ");
        }
    };
    
    public static HashSet<String> CLS_CHAR = new HashSet<String>()
            {
                {
                    for(int i = 32; i < 126; i++)
                    {
                        String s = "" + ((char)i);
                        CLS_CHAR.add(s);
                    }
                    CLS_CHAR.remove("\\"); CLS_CHAR.add("\\\\");
                    CLS_CHAR.remove("^"); CLS_CHAR.add("\\^");
                    CLS_CHAR.remove("-"); CLS_CHAR.add("\\-");
                    CLS_CHAR.remove("["); CLS_CHAR.add("\\[");
                    CLS_CHAR.remove("]"); CLS_CHAR.add("\\]");
                }
            };
    
    

    public static Character peekToken(String s, Integer i) throws Exception
    {
        return s.charAt(i);
    }

    public static Character matchToken(String s, Integer i, String want) throws Exception
    {
        String ss = "" + s.charAt(i) + s.charAt(i+1);
        if(!want.equals(ss))
            throw new BadDefinitionException();
        i+=2;
        return s.charAt(i+1);
        
    }
    
    public static Character matchToken(String s, Integer i, Character want)
            throws Exception
    {
        Character c = s.charAt(i.intValue());
        if (want != c)
            throw new BadDefinitionException();
        i++;
        return c;
    }

    public static Object[] recursiveDescentParse(String s) throws Exception
    {
        Object[] o = new Object[5];
        regEx(s, o, 0);
        return o;
    }

    public static Object[] regEx(String s, Object[] o, Integer i)
            throws Exception
    {
        rexp(s, o, i);
        return o;
    }

    public static Object[] rexp(String s, Object[] o, Integer i)
            throws Exception
    {
        rexp1(s, o, i);
        rexpPrime(s, o, i);
        return o;
    }

    public static Object[] regxPrime(String s, Object[] o, Integer i)
            throws Exception
    {
        rexp1(s, o, i);
        rexpPrime(s, o, i);
        // | \epsilon
        return o;
    }

    public static Object[] rexp1(String s, Object[] o, Integer i)
            throws Exception
    {
        rexp2(s, o, i);
        rexp1Prime(s, o, i);
        return o;
    }

    public static Object[] rexp1Prime(String s, Object[] o, Integer i)
    {
        
    }
    
    public static Object[] rexp2(String s, Object[] o, Integer i)
            throws Exception
    {
        Character c = peekToken(s, i);
        String s1 = "" + c;
        if (c == '(')
        {
            matchToken(s, i, '(');
            rexp(s, o, i);
            matchToken(s, i, ')');
            rexp2Tail(s, o, i);
        }
        else if(c == '\\')
        {
            String ss = "" + c + peekToken(s,i+1);
            matchToken(s, i, ss);
            rexp2Tail(s, o, i);
        }
        else if(RE_CHAR.contains(s1))
        {
            matchToken(s, i, s1);
            rexp2Tail(s,o,i);
        }
        else
        {
            rexp3(s,o,i);
        }
        return o;
    }

    public static Object[] rexp2Tail(String s, Object[] o, Integer i)
            throws Exception
    {
        Character c = s.charAt(i);
        if (c == '*' || c == '+')
        {
            i++;
        }
        return o;
    }

}
