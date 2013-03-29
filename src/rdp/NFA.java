package rdp;

import java.util.HashSet;
import java.util.Stack;

public class NFA
{
    private State startState;
    private State acceptState;
    
    public NFA(State ss, State as)
    {
        this.startState = ss;
        this.acceptState = as;
    }
    
    public NFA(State one)
    {
        State newState = new State("start" + one.getName(), false);
        newState.addTransition(new Transition(Transition.EPSILON, one));
        one.setAccept(false);
        State newAccept = new State("accept" + one.getName(), true);
        one.addTransition(new Transition(Transition.EPSILON, newAccept));
        this.startState = newState;
        this.acceptState = newAccept;
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
        State newStart = new State(one.getStartState().getName() + "+" + two.getStartState().getName(),
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
        State newStart = new State(one.getStartState().getName() + "|" + two.getStartState().getName(),
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
        State newStart = new State(one.getStartState().getName() + "*", false);
        State newAccept = new State(one.getAcceptState().getName() + "*", true);
        newStart.addTransition(new Transition(Transition.EPSILON, one.getStartState()));
        one.getStartState().addTransition(new Transition(Transition.EPSILON, newAccept));
        one.getAcceptState().addTransition(new Transition(Transition.EPSILON, newAccept));
        one.getAcceptState().addTransition(new Transition(Transition.EPSILON, one.getStartState()));
        one.getAcceptState().setAccept(false);
        NFA newNFA = new NFA(newStart, newAccept);
        return newNFA;
    }
    
    public void prettyPrint()
    {
        HashSet<State> visited = new HashSet<State>();
//        Queue<State> openList = new LinkedList<State>();
        Stack<State> openList = new Stack<State>();
        openList.add(startState);
        while(!openList.isEmpty())
        {
//            State curr = openList.poll();
            State curr = openList.pop();
            visited.add(curr);
            System.out.println(curr.getName());
            if(curr.getTransitions().size() == 0)
            {
                System.out.println("\tACCEPT");
                continue;
            }
            for(Transition t : curr.getTransitions())
            {
                String transCharStr = "" + t.getTransitionChar();
                if(t.getTransitionChar() == Transition.EPSILON)
                    transCharStr = "EPSILON";
                System.out.println("\t" + transCharStr + " -> " + t.getDestState().getName());
                if(!visited.contains(t.getDestState()))
                    openList.add(t.getDestState());
            }
        }
        
    }
    
    
    
    
}
