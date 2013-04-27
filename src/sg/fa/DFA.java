package sg.fa;

/****
 * DFA
 * Represents a deterministic finite automata
 * 
 * @author mlfong
 * @version 1.0
 */

import java.util.ArrayList;
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
     * getStartState getter for start state
     * 
     * @return
     */
    public DFAState getStartState()
    {
        return this.startState;
    }

    /****
     * getDFATable getter for DFA table
     * 
     * @return
     */
    public HashMap<DFAState, HashMap<Character, DFAState>> getDFATable()
    {
        return this.dfaTable;
    }

    /******
     * specialValidate validates a string to the regex of the DFA, and prints
     * out the accept token returns a boolean for accept or not, and the accept
     * token
     * 
     * @param string
     * @return
     */
    public Object[] specialValidate(String string)
    {
        return specialValidate(string.toCharArray());
    }

    /******
     * specialValidate validates a string to the regex of the DFA, and prints
     * out the accept token returns a boolean for accept or not, and the accept
     * token
     * 
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
        if (!accept)
        {
            o[0] = accept;
            o[1] = null;
        }
        else
        {
            accept = curr.isAccept();
            String thetoken = "";
            for (State ss : curr.getInnerStates())
            {
                if (ss.isTrueAccept())
                {
                    thetoken = ss.getAcceptToken();
                    break;
                }
            }
            assert (thetoken.length() > 0);
            o[0] = accept;
            o[1] = thetoken;
        }
        return o;
    }

    /****
     * validate Returns true or false by trying to validate the string to the
     * regex the DFA represents
     * 
     * @param string
     * @return
     */
    public boolean validate(String string)
    {
        return this.validate(string.toCharArray());
    }

    /****
     * validate Returns true or false by trying to validate the string to the
     * regex the DFA represents
     * 
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
     * epsilonClose Performs epsilon closure on a (NFA) state, returns a DFA
     * State
     * 
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
            if (!curr.getHMTransitions().keySet().contains(Transition.EPSILON))
                continue;
            ArrayList<Transition> epsilonTransitions = curr.getHMTransitions()
                    .get(Transition.EPSILON);
            for (Transition t : epsilonTransitions)
                openList.add(t.getDestState());
        }
        DFAState dfaState = new DFAState(visited);
        return dfaState;
    }

    /****
     * convertNFA Factory method to create DFA, converts a NFA into a DFA
     * 
     * @param nfa
     * @return
     */
    public static DFA convertNFA(NFA nfa)
    {
        Queue<DFAState> openList = new LinkedList<DFAState>();
        HashSet<DFAState> visited = new HashSet<DFAState>();
        DFA dfa = new DFA();
        HashMap<DFAState, HashMap<Character, DFAState>> table = new HashMap<DFAState, HashMap<Character, DFAState>>();
        dfa.dfaTable = table;
        DFAState newStartState = DFA.epsilonClose(nfa.getStartState());
        dfa.startState = newStartState;
        openList.add(newStartState);
        while (!openList.isEmpty())
        {
            DFAState curr = openList.poll();
            if (visited.contains(curr))
                continue;
            visited.add(curr);
            // get the column headers
            HashSet<Character> ts = new HashSet<Character>();
            for (State innerState : curr.getInnerStates())
                ts.addAll(innerState.getHMTransitions().keySet());

            HashMap<Character, DFAState> aColumn = new HashMap<Character, DFAState>();
            table.put(curr, aColumn);
            for (Character tc : ts)
            {
                DFAState element = new DFAState();
                for (State innerState : curr.getInnerStates())
                {
                    if(innerState.getHMTransitions().containsKey(tc) == false)
                        continue;
                    for ( Transition possible : innerState.getHMTransitions().get(tc)){
                        State destination = possible.getDestState();
                        element.getInnerStates().addAll(
                                DFA.epsilonClose(destination)
                                        .getInnerStates());
                    }
                }
                for (DFAState dfas : table.keySet())
                    if (dfas.equals(element))
                        element = dfas;
                if (element.getInnerStates().contains(nfa.getAcceptState()))
                    element.setAccept(true);
                aColumn.put(tc, element);

            }
            if (aColumn.size() > 0)
                table.put(curr, aColumn);
            for (Character columnHeader : aColumn.keySet())
                openList.add(aColumn.get(columnHeader));
        }
        for (DFAState key1 : table.keySet())
        {
            HashMap<Character, DFAState> column = table.get(key1);
            for (Character c : column.keySet())
            {
                DFAState element = column.get(c);
                for (DFAState key2 : table.keySet())
                {
                    if (element.equals(key2) && element != key2)
                    {
                        boolean ac = element.isAccept();
                        column.remove(element);
                        column.put(c, key2);
                        key2.setAccept(ac);
                    }
                }
            }
        }

        return dfa;
    }

    /*****
     * prettyPrint prints the DFA table
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
