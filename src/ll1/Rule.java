package ll1;

import java.util.ArrayList;

public class Rule
{
    private String lhs ;
    private ArrayList<ArrayList<String>> rhs ;
    
    public Rule(){
        this.lhs = "";
        this.rhs = new ArrayList<ArrayList<String>>();
    }
    
    public String getLHS(){
        return this.lhs;
    }
    public ArrayList<ArrayList<String>> getRHS(){
        return this.rhs;
    }
    
    public void init(String line){
        int coloncolonequals = line.indexOf("::=");
        if(coloncolonequals < 0){
            System.err.println("The line: " + line + " failed.");
            System.exit(0);
        }
        int rhsIndex = 0;
        String lhs = line.substring(0, coloncolonequals).trim();
        String rhs = line.substring(coloncolonequals+3);
        StringBuilder sb = new StringBuilder();
        this.rhs.add(new ArrayList<String>());
        for(Character c : rhs.toCharArray()){
            if(c == ' ' || (c == '>') && (sb.length() > 0 && sb.charAt(0)=='<')){
                if(c == '>')
                    sb.append(c);
                if(sb.toString().length() > 0){
                    this.rhs.get(rhsIndex).add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else if(c == '|'){
                this.rhs.add(new ArrayList<String>());
                rhsIndex += 1;
                sb.delete(0, sb.length());
            } else {
                sb.append(c);
            }
        }
        if(sb.toString().length() > 0)
            this.rhs.get(rhsIndex).add(sb.toString());
        this.lhs = lhs;
    }
    
    public void merge(Rule other){
        assert(this.lhs.equals(other.lhs));
        for(ArrayList<String> als : other.rhs)
            this.rhs.add(als);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.lhs + " ::=\n");
        for(ArrayList<String> onerhs : this.rhs){
            sb.append("\t[");
            for(int i = 0;  i < onerhs.size(); i++){
                sb.append(onerhs.get(i));
                if(i != onerhs.size()-1)
                    sb.append(", ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
    
    public static boolean isRule(String token){
        return token.charAt(0) == '<' && token.charAt(token.length()-1) == '>';
    }
    
   
    
    
}
