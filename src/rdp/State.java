package rdp;

import java.util.ArrayList;

public class State
{
    private String name;
    private boolean acceptState;
    private ArrayList<Transition> transitions;
    
    public State(String name, boolean acceptState)
    {
        this.name = name;
        this.acceptState = acceptState;
        transitions = new ArrayList<Transition>();
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
    
    public void addTransition(Transition t)
    {
        this.transitions.add(t);
    }
    
}
