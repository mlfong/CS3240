package rdp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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

    public NFA(NFA other)
    {
        this.startState = new State(other.startState);
        Queue<State> openList = new LinkedList<State>();
        HashSet<State> visited = new HashSet<State>();
        openList.add(this.startState);
        State temp = null;
        while(!openList.isEmpty())
        {
            State curr = openList.poll();
            if(curr.isAcceptState())
            {
                temp = curr;
                break;
            }
            if(visited.contains(curr))
                continue;
            visited.add(curr);
            for(Transition t : curr.getTransitions())
                openList.add(t.getDestState());
        }
        this.acceptState = temp;
//        this.acceptState = new State(other.acceptState);
    }
    
    public void setAcceptToken(String token)
    {
        this.acceptState.setAcceptToken(token);
    }

    public static NFA makeRangedNFA(HashSet<Character> hs)
    {
        ArrayList<NFA> nfas = new ArrayList<NFA>();
        // make individual
        for(Character c : hs)
        {
            nfas.add(NFA.makeCharNFA(c));
        }
        // union them all
        Character start = 'S', end = 'E';
        int rand1 = (int)(Math.random()*10000);
        int rand2 = (int)(Math.random()*10000);
        State startMe = new State("range" + start + "-" + end + "s"+rand1, false);
        State endMe = new State("range" + start + "-" + end + "f"+rand2, true);
        for(NFA nn : nfas)
        {
            startMe.addTransition(new Transition(Transition.EPSILON, nn.getStartState()));
            nn.getAcceptState().setAccept(false);
            nn.getAcceptState().addTransition(new Transition(Transition.EPSILON, endMe));
        }
        NFA all = new NFA(startMe, endMe);
        return all;
    }
    
    public static HashSet<Character> disjointSet(HashSet<Character> superset, HashSet<Character> removeset)
    {
        HashSet<Character> newSet = new HashSet<Character>();
        for(Character c : superset)
            if(!removeset.contains(c))
                newSet.add(c);
        return newSet;
    }

    public static HashSet<Character> oneLayerTransitions(NFA nfa)
    {
        HashSet<Character> hs = new HashSet<Character>();
        Queue<State> openList = new LinkedList<State>();
        HashSet<State> visited = new HashSet<State>();
        openList.add(nfa.getStartState());
        while(!openList.isEmpty())
        {
            State curr = openList.poll();
            if(visited.contains(curr))
                continue;
            visited.add(curr);
            for(Transition t : curr.getTransitions())
            {
                if(t.getTransitionChar() != Transition.EPSILON)
                    hs.add(t.getTransitionChar());
                openList.add(t.getDestState());
            }
        }
        return hs;
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

    public static NFA makeRangedNFA(int start, int end)
    {
        return makeRangedNFA((char)(start), (char)(end));
    }
    
    public static NFA makeRangedNFA(char start, char end)
    {
        return makeRangedNFA(new Character(start), new Character(end));
    }

    public static NFA makeRangedNFA(Character start, Character end)
    {
        assert(start.charValue() >= 32 && start.charValue() <= 126);
        assert(end.charValue() >= 32 && end.charValue() <= 126);
        assert(start.charValue() <= end.charValue());
        ArrayList<NFA> nfas = new ArrayList<NFA>();
        // make individual
        for(char c = start.charValue(); c <= end.charValue(); c++)
        {
            nfas.add(NFA.makeCharNFA(c));
        }
        // union them all
        State startMe = new State("range" + start + "-" + end + "s", false);
        State endMe = new State("range" + start + "-" + end + "f", true);
        for(NFA nn : nfas)
        {
            startMe.addTransition(new Transition(Transition.EPSILON, nn.getStartState()));
            nn.getAcceptState().setAccept(false);
            nn.getAcceptState().addTransition(new Transition(Transition.EPSILON, endMe));
        }
        NFA all = new NFA(startMe, endMe);
        return all;
    }

    public static NFA makeCharNFA(char c)
    {
        return makeCharNFA(new Character(c));
    }

    public static NFA makeCharNFA(Character c)
    {
        State s1pre = new State("" + c + (int) (Math.random() * 1000) + "p",
                false);
        State s1mid = new State("" + c + (int) (Math.random() * 1000) + "m",
                false);
        State s1fin = new State("" + c + (int) (Math.random() * 1000) + "f",
                true);
        s1pre.addTransition(new Transition(c, s1mid));
        s1mid.addTransition(new Transition(Transition.EPSILON, s1fin));
        return new NFA(s1pre, s1fin);
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
        State newStart = new State(one.getStartState().getName() + "+"
                + two.getStartState().getName(), false);
        newStart.addTransition(new Transition(Transition.EPSILON, one
                .getStartState()));
        one.getAcceptState().setAccept(false);
        one.getAcceptState().addTransition(
                new Transition(Transition.EPSILON, two.getStartState()));
        State newAccept = new State(one.getAcceptState().getName()
                + two.getAcceptState().getName(), true);
        two.getAcceptState().setAccept(false);
        two.getAcceptState().addTransition(
                new Transition(Transition.EPSILON, newAccept));
        NFA newNFA = new NFA(newStart, newAccept);
        return newNFA;
    }

    public static NFA union(NFA one, NFA two)
    {
        State newStart = new State(one.getStartState().getName() + "|"
                + two.getStartState().getName(), false);

        newStart.addTransition(new Transition(Transition.EPSILON, one
                .getStartState()));
        newStart.addTransition(new Transition(Transition.EPSILON, two
                .getStartState()));
        one.getAcceptState().setAccept(false);
        two.getAcceptState().setAccept(false);

        State newAccept = new State(one.getAcceptState().getName()
                + two.getAcceptState().getName(), true);
        one.getAcceptState().addTransition(
                new Transition(Transition.EPSILON, newAccept));
        two.getAcceptState().addTransition(
                new Transition(Transition.EPSILON, newAccept));

        NFA newNFA = new NFA(newStart, newAccept);
        return newNFA;
    }

    public static NFA star(NFA one)
    {
        State newStart = new State(one.getStartState().getName() + "*", false);
        State newAccept = new State(one.getAcceptState().getName() + "*", true);
        newStart.addTransition(new Transition(Transition.EPSILON, one
                .getStartState()));
        one.getStartState().addTransition(
                new Transition(Transition.EPSILON, newAccept));
        one.getAcceptState().addTransition(
                new Transition(Transition.EPSILON, newAccept));
        one.getAcceptState().addTransition(
                new Transition(Transition.EPSILON, one.getStartState()));
        one.getAcceptState().setAccept(false);
        NFA newNFA = new NFA(newStart, newAccept);
        return newNFA;
    }

    public void prettyPrint()
    {
        HashSet<State> visited = new HashSet<State>();
        // Queue<State> openList = new LinkedList<State>();
        Stack<State> openList = new Stack<State>();
        openList.add(startState);
        while (!openList.isEmpty())
        {
            // State curr = openList.poll();
            State curr = openList.pop();
            visited.add(curr);
            System.out.println(curr.getName());
            if (curr.getTransitions().size() == 0)
            {
                System.out.println("\tACCEPT");
                continue;
            }
            for (Transition t : curr.getTransitions())
            {
                String transCharStr = "" + t.getTransitionChar();
                if (t.getTransitionChar() == Transition.EPSILON)
                    transCharStr = "EPSILON";
                System.out.println("\t" + transCharStr + " -> "
                        + t.getDestState().getName());
                if (!visited.contains(t.getDestState()))
                    openList.add(t.getDestState());
            }
        }

    }

}
