import java.util.HashMap;
import java.util.HashSet;

public class MyToken
{
    private String name;
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

}
