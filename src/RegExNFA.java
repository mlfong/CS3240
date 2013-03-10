import java.util.ArrayList;
import java.util.Stack;

public class RegExNFA
{

    private DGraph g;
    private int vertNum;
    private String[] regex;

    public RegExNFA(String[] regex)
    {
        this.regex = regex;
        this.vertNum = regex.length;
        this.g = new DGraph(vertNum + 1);
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < this.vertNum; i++)
        {
            int lp = i;
            if (regex[i].contains("(") || regex[i].contains("|"))
            {
                stack.push(i);
            }
            else if (regex[i].contains(")"))
            {
                int or = stack.pop();
                if (regex[or].contains("|"))
                {
                    lp = stack.pop();
                    this.g.addEdge(lp, or + 1);
                    this.g.addEdge(or, i);
                }
                else if (regex[or].contains("("))
                {
                    lp = or;
                }
                else
                    assert false;
            }

            // Lookahead;
            if (i < vertNum - 1 && regex[i + 1].contains("*"))
            {
                this.g.addEdge(lp, i + 1);
                this.g.addEdge(i + 1, lp);
            }
            if (regex[i].contains("(") || regex[i].contains("*")
                    || regex[i].contains(")"))
            {
                this.g.addEdge(i, i + 1);
            }
        }
    }

    public boolean check(String[] input)
    {
        FAWalk dfs = new FAWalk(this.g, 0);
        ArrayList<Integer> pc = new ArrayList<Integer>();
        for (int i = 0; i < this.g.getNumVertices(); i++)
            if (dfs.marked(i))
            {
                pc.add(i);
            }

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < input.length; i++)
        {
            ArrayList<Integer> match = new ArrayList<Integer>();
            for (int v : pc)
            {
                if (v == this.vertNum)
                {
                    continue;
                }
                if ((regex[v].equals(input[i]) || regex[v].contains(".")))
                {
                    match.add(v + 1);
                }
            }
            dfs = new FAWalk(this.g, match);
            pc = new ArrayList<Integer>();
            for (int v = 0; v < this.g.getNumVertices(); v++)
                if (dfs.marked(v))
                {
                    pc.add(v);
                }

            // optimization if no states reachable
            if (pc.size() == 0)
            {
                return false;
            }
        }

        // check for accept state
        for (int v : pc)
            if (v == this.vertNum)
                return true;
        return false;
    }

    public static void main(String[] args)
    {
        String[] regex =
        { "(", "A", "*", "B", "|", "A", "C", ")", "D" };
        String[] testString =
        { "A", "A", };

        RegExNFA test = new RegExNFA(regex);

    }

}
