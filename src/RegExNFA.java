import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class RegExNFA
{

    private DGraph graph;
    private int vertNum;
    private String[] regex;
    private HashMap<String, HashSet<Character>> charClasses;

    public RegExNFA(String[] regex)
    {
        this.regex = regex;
        this.vertNum = regex.length;
        this.graph = new DGraph(vertNum + 1);
        Stack<Integer> stack = new Stack<Integer>();
        for (int index = 0; index < this.vertNum; index++)
        {
            int lastPoint = index;
            if (regex[index].contains("(") || regex[index].contains("|"))
            {
                stack.push(index);
            }
            else if (regex[index].contains(")"))
            {
                int or = stack.pop();
                if (regex[or].contains("|"))
                {
                    lastPoint = stack.pop();
                    this.graph.addEdge(lastPoint, or + 1);
                    this.graph.addEdge(or, index);
                }
                else if (regex[or].contains("("))
                {
                    lastPoint = or;
                }
                else
                    assert false;
            }

            // Lookahead;
            if (index < vertNum - 1 && regex[index + 1].contains("*"))
            {
                this.graph.addEdge(lastPoint, index + 1);
                this.graph.addEdge(index + 1, lastPoint);
            }
            if (regex[index].contains("(") || regex[index].contains("*")
                    || regex[index].contains(")"))
            {
                this.graph.addEdge(index, index + 1);
            }
        }
    }
    
    public RegExNFA(String[] regex, HashMap<String, HashSet<Character>> map){
    	this(regex);
    	this.charClasses = map;
    }

    public boolean check(String[] input)
    {
        FAWalk dfs = new FAWalk(this.graph, 0);
        ArrayList<Integer> checked = new ArrayList<Integer>();
        for (int i = 0; i < this.graph.getNumVertices(); i++)
            if (dfs.marked(i))
            {
                checked.add(i);
            }

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < input.length; i++)
        {
            ArrayList<Integer> match = new ArrayList<Integer>();
            for (int vertex : checked)
            {
                if (vertex == this.vertNum)
                {
                    continue;
                }
                if ((regex[vertex].equals(input[i]) || regex[vertex].contains(".")))
                {
                    match.add(vertex + 1);
                }
            }
            dfs = new FAWalk(this.graph, match);
            checked = new ArrayList<Integer>();
            for (int v = 0; v < this.graph.getNumVertices(); v++)
                if (dfs.marked(v))
                {
                    checked.add(v);
                }

            // optimization if no states reachable
            if (checked.size() == 0)
            {
                return false;
            }
        }

        // check for accept state
        for (int vertex : checked)
            if (vertex == this.vertNum)
                return true;
        return false;
    }
    
    public boolean check2(String[] input)
    {
        FAWalk dfs = new FAWalk(this.graph, 0);
        ArrayList<Integer> checked = new ArrayList<Integer>();
        for (int i = 0; i < this.graph.getNumVertices(); i++)
            if (dfs.marked(i))
            {
                checked.add(i);
            }

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < input.length; i++)
        {
            ArrayList<Integer> match = new ArrayList<Integer>();
            for (int vertex : checked)
            {
                if (vertex == this.vertNum)
                {
                    continue;
                }
                /*
                if(this.charClasses==null)
                	System.err.println("this char class is null");
                if(this.charClasses.get(regex[vertex]) == null)
                	System.err.println("bad");
                */
                if(this.charClasses.get(regex[vertex]) != null && input[i]!= null){
	                if ((this.charClasses.get(regex[vertex]).contains((input[i].charAt(0))) || regex[vertex].contains(".")))
	                {
	                    match.add(vertex + 1);
	                }
                }
                else{
                	
                	//System.out.println(regex[vertex] + "is Null");
                }
            }
            dfs = new FAWalk(this.graph, match);
            checked = new ArrayList<Integer>();
            for (int v = 0; v < this.graph.getNumVertices(); v++)
                if (dfs.marked(v))
                {
                    checked.add(v);
                }

            // optimization if no states reachable
            if (checked.size() == 0)
            {
                return false;
            }
        }

        // check for accept state
        for (int vertex : checked)
            if (vertex == this.vertNum)
                return true;
        return false;
    }
    
    public String[] getRegex()
    {
        return regex;
    }
    

    public static void main(String[] args)
    {
    	System.out.println("Hey baby");
        String[] regex =
        { "(","=",")" };
        String[] testString =
        { "="};

        RegExNFA test = new RegExNFA(regex);
        System.out.println(test.check(testString));

    }

}
