package rdp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DFA
{
    private HashMap<DFAState, HashMap<Character, DFAState>> dfaTable;
    private DFAState startState;

    private DFA()
    {
        dfaTable = new HashMap<DFAState, HashMap<Character, DFAState>>();
        startState = null;
    }

    public DFAState getStartState()
    {
        return this.startState;
    }

    public HashMap<DFAState, HashMap<Character, DFAState>> getDFATable()
    {
        return this.dfaTable;
    }

    public boolean validate(String string)
    {
        // TODO
        return false;
    }

    public boolean validate(Character[] string)
    {
        // TODO
        return false;
    }

    private static DFAState epsilonClose(State s)
    {
        Stack<State> openList = new Stack<State>();
        HashSet<State> visited = new HashSet<State>();
        openList.add(s);
        while (!openList.isEmpty())
        {
            State curr = openList.pop();
            if (visited.contains(curr))
                continue;
            visited.add(curr);
            for (Transition t : curr.getTransitions())
                if (t.getTransitionChar() == Transition.EPSILON)
                    openList.add(t.getDestState());
        }
        DFAState dfaState = new DFAState(visited);
        return dfaState;
    }

    public static DFA convertNFA(NFA nfa)
    {
        Queue<DFAState> openList = new LinkedList<DFAState>(); // new
                                                               // Stack<DFAState>();
        HashSet<DFAState> visited = new HashSet<DFAState>();
        DFA dfa = new DFA();
        HashMap<DFAState, HashMap<Character, DFAState>> table = new HashMap<DFAState, HashMap<Character, DFAState>>();
        dfa.dfaTable = table;
        DFAState newStartState = DFA.epsilonClose(nfa.getStartState());
        dfa.startState = newStartState;
        openList.add(newStartState);
        int counter = 0;
        newStartState.setID("s" + counter++);
        while (!openList.isEmpty())
        {
            DFAState curr = openList.poll();
            if (visited.contains(curr))
            {
                // for orphaned states
                for (DFAState dfas : table.keySet())
                {
                    if (dfas.equals(curr))
                    {
                        DFAState want = dfas;
                        for (DFAState k : table.keySet())
                        {
                            HashMap<Character, DFAState> hm2 = table.get(k);
                            for (Character c : hm2.keySet())
                            {
                                hm2.remove(c);
                                hm2.put(c, want);
                                break;
                            }
                        }
                    }
                }
                continue;
            }
            visited.add(curr);
            HashMap<Character, DFAState> hm = new HashMap<Character, DFAState>();
            
            // get ALL transition characters possible at this row
            HashSet<Character> transitionChars = new HashSet<Character>();
            for (State s : curr.getInnerStates())
                for (Transition t : s.getTransitions())
                    if (t.getTransitionChar() != Transition.EPSILON)
                        transitionChars.add(t.getTransitionChar());

            for (Character c : transitionChars)
            {
                HashSet<State> hs = new HashSet<State>();
                for (State s : curr.getInnerStates())
                {
                    for (Transition t : s.getTransitions())
                    {
                        if (t.getTransitionChar() == c)
                        {
                            DFAState close = DFA.epsilonClose(t.getDestState());
                            hs.addAll(close.getInnerStates());
                        }
                    }
                }
                DFAState wantToAdd = new DFAState(hs);
                for (DFAState rowHeader : table.keySet())
                {
                    if (rowHeader.equals(wantToAdd))
                    {
                        wantToAdd = rowHeader;
                        break;
                    }
                }
                if (wantToAdd.getInnerStates().contains(nfa.getAcceptState()))
                    wantToAdd.setAccept(true);

                if (hs.size() > 0)
                {
                    hm.put(c, wantToAdd);
                }

            }

            for (Character c : hm.keySet())
                openList.add(hm.get(c));
            if (hm.size() > 0)
                table.put(curr, hm);
        }// end while

        counter = 100;
        for (DFAState key : dfa.dfaTable.keySet())
        {
            key.setID("s" + counter++);
            if (key.getInnerStates().contains(nfa.getAcceptState()))
                key.setAccept(true);
        }

        return dfa;
    }

    public void prettyPrint()
    {
        System.out.println("Start state: " + this.startState.getID());
        for (DFAState key1 : this.dfaTable.keySet())
        {
            System.out.print("key1: " + key1.getID() + "\n\t");
            System.out.println(key1.getID() + " accept: " + key1.isAccept());
            HashMap<Character, DFAState> columns = this.dfaTable.get(key1);
            for (Character key2 : columns.keySet())
            {
                System.out.println("\t" + key2 + " -> "
                        + columns.get(key2).getID());
            }
        }
    }
}
