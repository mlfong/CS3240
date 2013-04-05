package rdp;

public class NFATest
{
    public static void main(String[] args)
    {
        // ((a|b)c)*
        System.out.println("NFA 1: ");
        State s1pre = new State("s1p", false);
        State s1mid = new State("s1m", false);
        State s1fin = new State("s1f", true);
        s1pre.addTransition(new Transition('a', s1mid));
        s1mid.addTransition(new Transition(Transition.EPSILON, s1fin));
        NFA nfa1 = new NFA(s1pre, s1fin);
        
        State s2pre = new State("s2p", false);
        State s2mid = new State("s2m", false);
        State s2fin = new State("s2f", true);
        s2pre.addTransition(new Transition('b', s2mid));
        s2mid.addTransition(new Transition(Transition.EPSILON, s2fin));
        NFA nfa2 = new NFA(s2pre, s2fin);
        
        NFA nfa3 = NFA.union(nfa1, nfa2);
        
        State s3pre = new State("s3p", false);
        State s3mid = new State("s3m", false);
        State s3fin = new State("s3f", true);
        s3pre.addTransition(new Transition('c', s3mid));
        s3mid.addTransition(new Transition(Transition.EPSILON, s3fin));
        NFA nfa4 = new NFA(s3pre, s3fin);
        NFA nfa5 = NFA.concatenate(nfa3, nfa4);
        NFA nfa6 = NFA.star(nfa5);
        nfa6.prettyPrint();
        
        System.out.println("\nTurn it into a DFA");
        DFA dfa = DFA.convertNFA(nfa6);
        dfa.prettyPrint();
        
    }

}
