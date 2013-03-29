package rdp;

public class NFA
{
    private State startState;
    private State acceptState;
    
    public NFA(State ss, State as)
    {
        this.startState = ss;
        this.acceptState = as;
    }
    
    public State getStartState()
    {
        return this.startState;
    }
    public State getAcceptState()
    {
        return this.acceptState;
    }
    
    public void setStartState(State ss)
    {
        this.startState = ss;
    }
    public void setAcceptState(State as)
    {
        this.acceptState = as;
    }
    
    public static NFA concatenate(NFA one, NFA two)
    {
        State newStart = new State(one.getStartState().getName() + two.getStartState().getName(),
                false);
        newStart.addTransition(new Transition(Transition.EPSILON, one.getStartState()));
        one.getAcceptState().setAccept(false);
        one.getAcceptState().addTransition(new Transition(Transition.EPSILON, two.getStartState()));
        State newAccept = new State(one.getAcceptState().getName() + two.getAcceptState().getName(), true);
        two.getAcceptState().setAccept(false);
        two.getAcceptState().addTransition(new Transition(Transition.EPSILON, newAccept));
        NFA newNFA = new NFA(newStart, newAccept);
        return newNFA;
    }
    
    public static NFA union(NFA one, NFA two)
    {
        State newStart = new State(one.getStartState().getName() + two.getStartState().getName(),
                false);
        
        newStart.addTransition(new Transition(Transition.EPSILON, one.getStartState()));
        newStart.addTransition(new Transition(Transition.EPSILON, two.getStartState()));
        one.getAcceptState().setAccept(false);
        two.getAcceptState().setAccept(false);
        
        State newAccept = new State(one.getAcceptState().getName() + two.getAcceptState().getName(), true);
        one.getAcceptState().addTransition(new Transition(Transition.EPSILON, newAccept));
        two.getAcceptState().addTransition(new Transition(Transition.EPSILON, newAccept));
        
        NFA newNFA = new NFA(newStart, newAccept);
        return newNFA;
    }

    public static NFA star(NFA one)
    {
        State newStart = new State(one.getStartState().getName() + "star", false);
        State newAccept = new State(one.getAcceptState().getName() + "star", true);
        newStart.addTransition(new Transition(Transition.EPSILON, one.getStartState()));
        one.getStartState().addTransition(new Transition(Transition.EPSILON, newAccept));
        one.getAcceptState().addTransition(new Transition(Transition.EPSILON, newAccept));
        one.getAcceptState().addTransition(new Transition(Transition.EPSILON, one.getStartState()));
        one.getAcceptState().setAccept(false);
        NFA newNFA = new NFA(newStart, newAccept);
        return newNFA;
    }
    
    
    
    
    
    
}
