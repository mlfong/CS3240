package sg.fa;

/*****
 * DFAState
 * 
 * represents a DFA state, that is, a state that has many NFA states
 * 
 * @author mlfong
 */

import java.util.HashSet;

public class DFAState
{
    private HashSet<State> states;
    private String id;
    private boolean accept;

    public DFAState()
    {
        states = new HashSet<State>();
        id = "";
        accept = false;
    }

    public DFAState(DFAState other)
    {
        this.states = new HashSet<State>();
        for(State s : other.states)
            this.states.add(new State(s));
        this.id = other.id;
        this.accept = other.accept;
    }
    
    public DFAState(HashSet<State> states)
    {
        this.states = states;
        id = "";
        accept = false;
    }

    public HashSet<State> getInnerStates()
    {
        return this.states;
    }

    public void addInnerState(State s)
    {
        this.states.add(s);
    }

    public void setID(String s)
    {
        this.id = s;
    }

    public String getID()
    {
        return this.id;
    }

    public boolean isAccept()
    {
        return this.accept;
    }

    public void setAccept(boolean b)
    {
        this.accept = b;
    }

    /****
     * Merges two DFAStates into one
     * @param one
     * @param two
     * @return
     */
    public static DFAState merge(DFAState one, DFAState two)
    {
        HashSet<State> join = one.states;
        for (State s : two.states)
            join.add(s);
        return new DFAState(join);
    }

    /****
     * join
     * Adds all of the other DFAState's inner NFA states to its own states
     * @param other
     */
    public void join(DFAState other)
    {
        for (State s : other.states)
            this.states.add(s);
    }

    public boolean equals(Object other)
    {
        if (other == this)
            return true;
        if (!(other instanceof DFAState))
            return false;
        DFAState dfas = (DFAState) other;
        if (dfas.states.size() != this.states.size())
            return false;
        return this.states.containsAll(dfas.states)
                && dfas.states.containsAll(this.states);
        // return this.states.equals(dfas.states);
    }

    public int hashCode()
    {
        StringBuilder sb = new StringBuilder();
        for (State s : states)
            sb.append(s.getName());
        return sb.toString().hashCode();
    }

    public void dumpInnerStates()
    {
        System.out.print("{");
        for (State s : states)
            System.out.print(s + " ");
        System.out.println("}");

    }
}
