import java.util.HashMap;
import java.util.HashSet;

public class MyToken
{
    private String name;
    private String[] re;
    private RegExNFA nfa;
    private boolean nfaCreated;

    public MyToken(String name)
    {
        this.name = name;
        this.nfaCreated = false;
    }

    public void makeNFA(String[] regex, HashMap<String, HashSet<Character>> map)
    {
        this.nfa = new RegExNFA(regex, map);
        this.nfaCreated = true;
        this.re = regex;
    }

    public boolean check(String[] input)
    {
        if (this.nfaCreated)
        {
            return this.nfa.check(input);
        }
        else
        {
            System.out.println("NFA HAS NOT BEEN GENERATED YET");
            return false;
        }
    }
    
    public boolean check2(String[] input)
    {
        if (this.nfaCreated)
        {
            return this.nfa.check2(input);
        }
        else
        {
            System.out.println("NFA HAS NOT BEEN GENERATED YET");
            return false;
        }
    }

    public RegExNFA getNFA()
    {
        return this.nfa;
    }

    public String getName()
    {
        return this.name;
    }

    public String toString()
    {
        return name;
    }
    
    public String prettyPrint(){
    	String toReturn = "";
    	for(int i = 0; i < this.re.length ; i++){
    		toReturn+=this.re[i];
    	}
    	return name + " " + toReturn;
    }

}
