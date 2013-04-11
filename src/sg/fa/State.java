package sg.fa;
/****
 * State
 * Represents a NFA State
 * 
 * @author mlfong
 */

import java.util.ArrayList;
import java.util.HashMap;

public class State
{
    private String name;
    private boolean acceptState;
    private ArrayList<Transition> transitions;
    private HashMap<Character, ArrayList<Transition>> hmTransitions;
    private String acceptToken;
    private boolean trueAccept;

    public State(String name, boolean acceptState)
    {
        this.name = name;
        this.acceptState = acceptState;
        this.transitions = new ArrayList<Transition>();
        this.hmTransitions = new HashMap<Character, ArrayList<Transition>>();
        this.acceptToken = "";
        this.trueAccept = false;
    }

    public State(State other)
    {
        this.name = new String(other.name);
        this.acceptState = other.acceptState;
        this.transitions = new ArrayList<Transition>();
        for(Transition t : other.transitions)
            this.transitions.add(new Transition(t));
        this.acceptToken = new String(other.acceptToken);
        this.trueAccept = other.trueAccept;
    }
    
    public void setAcceptToken(String token)
    {
        this.acceptToken = token;
        trueAccept = true;
    }
    public String getAcceptToken()
    {
        return this.acceptToken;
    }
    public boolean isTrueAccept()
    {
        return this.trueAccept;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public void setAccept(boolean accept)
    {
        this.acceptState = accept;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isAcceptState()
    {
        return this.acceptState;
    }

    public ArrayList<Transition> getTransitions()
    {
        return this.transitions;
    }

    public HashMap<Character, ArrayList<Transition>> getHMTransitions()
    {
        return this.hmTransitions;
    }
    
    public void addTransition(Transition t)
    {
        if(this.hmTransitions.containsKey(t.getTransitionChar())){
            this.hmTransitions.get(t.getTransitionChar()).add(t);
        } else {
            this.hmTransitions.put(t.getTransitionChar(), new ArrayList<Transition>());
            this.hmTransitions.get(t.getTransitionChar()).add(t);
        }
        this.transitions.add(t);
    }

//    public boolean equals(Object other)
//    {
//        return this == other;
//        if (other == this)
//            return true;
//        if (!(other instanceof State))
//            return false;
//        State o = (State) other;
//        return this.name.equals(o.name) || this.name == o.name;
//    }

    public int hashCode()
    {
        return this.name.hashCode();
    }
}
