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
    private HashSet<Character> ESCAPED;
    
    
    public RegExNFA(String[] regex)
    {
    	this.ESCAPED = new HashSet<Character>();
    	this.ESCAPED.add('\\'); 
    	this.ESCAPED.add('-'); 
    	this.ESCAPED.add('['); 
    	this.ESCAPED.add(']');
        // extra
    	this.ESCAPED.add(' '); 
    	this.ESCAPED.add('*'); 
    	this.ESCAPED.add('+'); 
    	this.ESCAPED.add('|'); 
    	this.ESCAPED.add('(');
    	this.ESCAPED.add(')'); 
    	this.ESCAPED.add('.');
    	this.ESCAPED.add('\''); 
    	this.ESCAPED.add('\"');
        this.regex = regex;
        this.vertNum = regex.length;
        this.graph = new DGraph(vertNum + 1);
        this.graph.nameStates(regex);
        this.graph.setGoal(vertNum);
        Stack<Integer> stack = new Stack<Integer>();
        for (int index = 0; index < this.vertNum; index++)
        {
            int lastPoint = index;
            if (this.regex[index].contains("(") || this.regex[index].contains("|"))
            {
                stack.push(index);
            }
            else if (this.regex[index].contains(")"))
            {
                int or = stack.pop();
                if (this.regex[or].contains("|"))
                {
                    lastPoint = stack.pop();
                    this.graph.addEdge(lastPoint, or + 1,this.regex[or+1]);
                    this.graph.addEdge(or, index, this.regex[index]);
                }
                else if (this.regex[or].contains("("))
                {
                    lastPoint = or;
                }
                else
                    assert false;
            }

            if (index < vertNum - 1 && this.regex[index + 1].contains("*"))
            {
                this.graph.addEdge(lastPoint, index + 1, this.regex[index+1]);
                this.graph.addEdge(index + 1, lastPoint, this.regex[lastPoint]);
            }
            if (this.regex[index].contains("(") || this.regex[index].contains("*")
                    || this.regex[index].contains(")"))
            {
            	if(index+1<this.regex.length){
            		this.graph.addEdge(index, index + 1, this.regex[index+1]);
            	}
            	else{
            		this.graph.addEdge(index, index + 1, "Epsilon");
            	}
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

        for (int i = 0; i < input.length; i++)
        {
            ArrayList<Integer> match = new ArrayList<Integer>();
            for (int vertex : checked)
            {
                if (vertex == this.vertNum)
                {
                    continue;
                }
                if(ESCAPED.contains(input[i].charAt(0))){
                	if((regex[vertex].equals("\\"+input[i])) || regex[vertex].contains(".")){
                    	match.add(vertex + 1);
                    }
                }else{
                	if ((regex[vertex].equals(input[i]) || regex[vertex].contains("."))){
                		match.add(vertex + 1);
                	}
                }
 
            }
            dfs = new FAWalk(this.graph, match);
            checked = new ArrayList<Integer>();
            for (int v = 0; v < this.graph.getNumVertices(); v++)
                if (dfs.marked(v))
                {
                    checked.add(v);
                }

        }

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
            	if(this.charClasses.get(regex[vertex]) != null && input[i]!= null && input[i].length() <2){
	                if ((this.charClasses.get(regex[vertex]).contains((input[i].charAt(0))) || regex[vertex].contains(".")))
	                {
	                    match.add(vertex + 1);
	                }
                }
                else  if(ESCAPED.contains(input[i].charAt(0))){
                	if(regex[vertex].equals("\\"+input[i]) || regex[vertex].contains(".")){
                		match.add(vertex + 1);
                	}
                }
                else if((regex[vertex].equals(input[i]) || regex[vertex].contains("."))){
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

        }

        for (int vertex : checked)
            if (vertex == this.vertNum)
                return true;
        return false;
    }
    
    public String[] getRegex()
    {
        return regex;
    }
    
    public void printNFA(){
    	System.out.println("All Verticies");
    	for(RegExNode n: this.graph.getVertList()){
    		System.out.print(n.getStateID());
    		if(n.isTerminal()){
    			System.out.print(" is GOAL");
    		}
    		System.out.println();
    	}
    	System.out.println("------------------------");
    	System.out.println("All Edges");
    	for(RegExEdge e: this.graph.getEdgeList()){
    		System.out.println(e);
    	}
    	System.out.println();
    }
    

    public static void main(String[] args)
    {
    	System.out.println("Hey baby");
        String[] regex =
        { "(","a",")","*" };
        String[] testString =
        { "a"};

        RegExNFA test = new RegExNFA(regex);
        System.out.println(test.check(testString));

    }
    
    
    
}
