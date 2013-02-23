/******
 * @author mlfong
 * @version 1.0
 *****/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

@SuppressWarnings("all")

public class Util
{
    public static void prettyPrint(ArrayList al)
    {
        System.out.print("[");
        for(int i = 0 ; i < al.size(); i++)
        {
            System.out.print(al.get(i));
            if(i != al.size()-1)
                System.out.print(", ");
        }
        System.out.println("]");
    }
    
    public static void prettyPrint(HashSet hs)
    {
        System.out.print("[");
        Iterator iter = hs.iterator();
        while(iter.hasNext())
        {
            System.out.print(iter.next() + ", ");
        }
        System.out.println("]");
    }
    
    public static void reallyPrettyPrint(HashSet hs)
    {
        ArrayList al = new ArrayList();
        for(Object o : hs)
            al.add(o);
        Collections.sort(al);
        prettyPrint(al);
    }
    
    public static int count(String s, char c)
    {
        int cc = 0;
        for(int i = 0 ; i < s.length(); i++)
            if(s.charAt(i)==c)
                cc++;
        return cc;
    }
}
