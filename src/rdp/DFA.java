package rdp;
/****
 * DFA
 * Represents a deterministic finite automata
 * 
 * @author mlfong
 * @version 1.0
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DFA
{
    private HashMap<DFAState, HashMap<Character, DFAState>> dfaTable;
    private DFAState startState;

    /****
     * private constructor
     */
    private DFA()
    {
        dfaTable = new HashMap<DFAState, HashMap<Character, DFAState>>();
        startState = null;
    }

    /****
     * getStartState
     * getter for start state
     * @return
     */
    public DFAState getStartState()
    {
        return this.startState;
    }

    /****
     * getDFATable
     * getter for DFA table
     * @return
     */
    public HashMap<DFAState, HashMap<Character, DFAState>> getDFATable()
    {
        return this.dfaTable;
    }

    /******
     * specialValidate
     * validates a string to the regex of the DFA, and prints out the accept token
     * returns a boolean for accept or not, and the accept token
     * @param string
     * @return
     */
    public Object[] specialValidate(String string)
    {
        return specialValidate(string.toCharArray());
    }
    
    /******
     * specialValidate
     * validates a string to the regex of the DFA, and prints out the accept token
     * returns a boolean for accept or not, and the accept token
     * @param string
     * @return
     */
    public Object[] specialValidate(char[] string)
    {
        Object[] o = new Object[2];
        Boolean accept = true;
        
        DFAState curr = this.startState;
        for (int i = 0; i < string.length; i++)
        {
            Character c = string[i];
            HashMap<Character, DFAState> column = this.dfaTable.get(curr);
            if (column.keySet().contains(c))
                curr = column.get(c);
            else
            {
                accept = false;
                break;
            }
        }
        if(!accept)
        {
            o[0] = accept;
            o[1] = null;
        }
        else
        {
            accept = curr.isAccept();
            String thetoken = "";
            for(State ss : curr.getInnerStates())
            {
                if(ss.isTrueAccept())
                {
                    thetoken = ss.getAcceptToken();
                    break;
                }
            }
            assert(thetoken.length() > 0);
            o[0] = accept;
            o[1] = thetoken;
        }
        return o;
    }
    
    /****
     * validate
     * Returns true or false by trying to validate the string to the regex the DFA represents
     * @param string
     * @return
     */
    public boolean validate(String string)
    {
        return this.validate(string.toCharArray());
    }

    /****
     * validate
     * Returns true or false by trying to validate the string to the regex the DFA represents
     * @param string
     * @return
     */
    public boolean validate(char[] string)
    {
        DFAState curr = this.startState;
        for (int i = 0; i < string.length; i++)
        {
            Character c = string[i];
            HashMap<Character, DFAState> column = this.dfaTable.get(curr);
            if (column.keySet().contains(c))
                curr = column.get(c);
            else
                return false;
        }
        return curr.isAccept();
    }
    
    /****
     * epsilonClose
     * Performs epsilon closure on a (NFA) state, returns a DFA State
     * @param s
     * @return
     */
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

    /****
     * convertNFA
     * Factory method to create DFA, converts a NFA into a DFA 
     * @param nfa
     * @return
     */
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
        boolean found = false;
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
                                found = true;
                                break;
                            }
                        }
                        if (found)
                            break;
                    }
                    if (found)
                        break;
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
            // if (hm.size() > 0)
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

    /*****
     * prettyPrint
     * prints the DFA table
     */
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
