package rgx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyRegex
{
    public static void main(String[] args){
//        Pattern p = Pattern.compile("([0-9]|b)*b");
//        Pattern p = Pattern.compile("[a-zA-Z]");
        Pattern p = Pattern.compile("[abcdefghqowelsj]");
//        Matcher m = p.matcher("aaaaab");
//        Matcher m = p.matcher("0bbbbb");
        Matcher m = p.matcher("Z");
        boolean b = m.matches();
        System.out.println(b);
    }
}
