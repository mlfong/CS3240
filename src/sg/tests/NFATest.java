package sg.tests;

import java.util.HashSet;

import sg.Util;
import sg.fa.DFA;
import sg.fa.NFA;
import sg.fa.State;
import sg.fa.Transition;

public class NFATest
{
    public static void main(String[] args)
    {
        // test100();
        // test04();
        // test03();
        // test101();
        test104();
        // testTransitionPull();
    }

    public static void testTransitionPull()
    {
        NFA nfa1 = NFA.makeRangedNFA('A', 'Z');
        HashSet<Character> hs = NFA.oneLayerTransitions(nfa1);
        Util.prettyPrint(hs);
        Util.reallyPrettyPrint(hs);
    }

    // THIS ONE IS GREAT
    public static void test104()
    {
        NFA nfa1 = NFA.makeRangedNFA('0', '1');
        nfa1.prettyPrint();
        System.out.println("------------");
        NFA nfa2 = NFA.makeRangedNFA('0', '1');
        NFA nfa3 = NFA.star(nfa2);
        NFA nfa4 = NFA.concatenate(nfa1, nfa3);
        nfa4.setAcceptToken("$INT");
        // System.out.println("------------");
        // nfa4.prettyPrint();
        // System.out.println("------------");

        // lower (lower|digit)*
        NFA nfa5 = NFA.makeRangedNFA('a', 'b');
        NFA nfa6 = NFA.makeRangedNFA('0', '1');
        NFA nfa7 = NFA.union(nfa5, nfa6);
        NFA nfa8 = NFA.star(nfa7);
        NFA nfa9 = NFA.makeRangedNFA('a', 'b');
        NFA nfa10 = NFA.concatenate(nfa9, nfa8);
        nfa10.setAcceptToken("$IDENTIFIER");
        // DFA dfa2 = DFA.convertNFA(nfa4);
        // String[] testStrings2 =
        // { "0", "1", "01", "" , "askdjadsk", "00000"};
        // boolean[] answers2 =
        // {
        // true, true, true, false, false, true
        // };
        // NFATest.advancedTest(dfa2, testStrings2, answers2);
        //
        // $FLOAT ($DIGIT)+ \. ($DIGIT)+
        NFA nfa90 = NFA.makeRangedNFA('0', '1');
        NFA nfa91 = NFA.makeRangedNFA('0', '1');
        nfa91 = NFA.star(nfa91);
        NFA nfa92 = NFA.makeRangedNFA('0', '1');
        NFA nfa93 = NFA.makeRangedNFA('0', '1');
        nfa93 = NFA.star(nfa93);
        NFA dot = NFA.makeCharNFA('.');
        NFA n94 = NFA.concatenate(nfa90, nfa91);
        NFA n95 = NFA.concatenate(nfa92, nfa93);
        NFA n96 = NFA.concatenate(n94, dot);
        NFA n97 = NFA.concatenate(n96, n95);
        n97.setAcceptToken("$FLOAT");

        NFA nfa11 = NFA.union(nfa4, nfa10);
        NFA nfa122 = NFA.union(nfa11, n97);
        // NFA nfa122 = NFA.union(nfa4, n97);
        // DFA dfa = DFA.convertNFA(nfa11);
        // nfa122.prettyPrint();
        DFA dfa = DFA.convertNFA(nfa122);
        String[] testStrings =
        { "0", "1", "01", "a10", "bbbbb", "101asdad", "a8aa8a88s8das8a8",
                "0.0", ".34234", "1.0", "1.", ".111" };
        boolean[] answers =
        { true, true, true, true, true, false, false, true, false, true, false,
                false };
        NFATest.advancedTest(dfa, testStrings, answers);
        // dfa.prettyPrint();

    }

    public static void test101()
    {
        // 0-9 OR A-Z
        NFA nfa1 = NFA.makeRangedNFA('0', '9');
        nfa1.setAcceptToken("$DIGIT");
        NFA nfa2 = NFA.makeRangedNFA('A', 'Z');
        nfa2.setAcceptToken("$UPPER");

        NFA nfa3 = NFA.union(nfa1, nfa2);
        DFA dfa = DFA.convertNFA(nfa3);
        String[] testStrings =
        { "A", "B", "AA", "", "0", "1", "9", "b" };
        boolean[] answers =
        { true, true, false, false, true, true, true, false };
        NFATest.advancedTest(dfa, testStrings, answers);
    }

    public static void test100()
    {
        NFA nfaA = NFA.makeRangedNFA('0', '9');
        DFA dfaA = DFA.convertNFA(nfaA);
        String[] testStrings =
        { "a", "b", "aa", "", "0", "1", "9" };
        boolean[] answers =
        { false, false, false, false, true, true, true };
        NFATest.testAll(dfaA, testStrings, answers);
    }

    public static void test02()
    {
        // a
        NFA nfaA = NFA.makeCharNFA('a');
        DFA dfaA = DFA.convertNFA(nfaA);
        String[] testStrings =
        { "a", "b", "aa", "" };
        boolean[] answers =
        { true, false, false, false };
        NFATest.testAll(dfaA, testStrings, answers);
    }

    public static void test03()
    {
        // (a|b)*
        NFA nfaA = NFA.makeCharNFA('a');
        NFA nfaB = NFA.makeCharNFA('b');
        NFA aUb = NFA.union(nfaA, nfaB);
        NFA aorbstar = NFA.star(aUb);

        DFA dfaAll = DFA.convertNFA(aorbstar);
        String[] testStrings =
        { "a", "b", "abba", "c", "" };
        boolean[] answers =
        { true, true, true, false, true };
        NFATest.testAll(dfaAll, testStrings, answers);
    }

    public static void test04()
    {
        // intLETTER*=DIGIT*
        NFA DIGIT = NFA.makeRangedNFA('0', '9');
        NFA digitStar = NFA.star(DIGIT);
        NFA LETTER = NFA.makeRangedNFA('A', 'Z');
        // NFA LETTER = NFA.makeRangedNFA((char)32, (char)(126));
        NFA letterStar = NFA.star(LETTER);
        NFA eq = NFA.makeCharNFA('=');
        NFA i = NFA.makeCharNFA('i');
        NFA n = NFA.makeCharNFA('n');
        NFA t = NFA.makeCharNFA('t');
        NFA myInt = NFA.concatenate(NFA.concatenate(i, n), t);
        NFA all = NFA.concatenate(NFA.concatenate(myInt, letterStar),
                NFA.concatenate(eq, digitStar));

        DFA dfaAll = DFA.convertNFA(all);
        // dfaAll.prettyPrint();
        String[] testStrings =
        { "intAWWWYEAH=65", "intBITCHESANDHOES=7876320487643987", "abba", "c",
                "", "intB=1", "intq=1" };
        boolean[] answers =
        { true, true, false, false, false, true, false };
        NFATest.testAll(dfaAll, testStrings, answers);
    }

    public static void test01()
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
        String[] testStrings =
        { "", "ac", "bc", "acac", "bcbc", "acbc", "acacacacacacacbc", "d",
                "cacacac", "a" };
        boolean[] answers =
        { true, true, true, true, true, true, true, false, false, false };
        for (int i = 0; i < testStrings.length; i++)
        {
            boolean b = dfa.validate(testStrings[i]);
            String s = b == answers[i] ? "Pass" : "Fail";
            System.out.println("Test " + (i + 1) + ": " + testStrings[i]
                    + " -> " + s);
        }

    }

    public static void testAll(DFA dfa, String[] testStrings, boolean[] answers)
    {
        for (int i = 0; i < testStrings.length; i++)
        {
            boolean b = dfa.validate(testStrings[i]);
            String s = b == answers[i] ? "Pass" : "Fail";
            if (s == "Pass")
                System.out.println("Test " + (i + 1) + ": " + testStrings[i]
                        + " -> " + s);
            else
                System.err.println("Test " + (i + 1) + ": " + testStrings[i]
                        + " -> " + s);
        }
    }

    public static void advancedTest(DFA dfa, String[] testStrings,
            boolean[] answers)
    {
        boolean bbbb = true;
        for (int i = 0; i < testStrings.length; i++)
        {
            Object[] o = dfa.specialValidate(testStrings[i]);
            Boolean bb = (Boolean) o[0];
            boolean b = bb.booleanValue();
            String s = b == answers[i] ? "Pass" : "Fail";
            System.out.println("Test " + (i + 1) + ": " + testStrings[i]
                    + " -> " + s);
            bbbb &= (b == answers[i]);

            if (b) // so it is supposed to accept
            {
                String thetoken = (String) o[1];
                System.out.println("\tAccept, token is: " + thetoken);
            }
            else
            {
                System.out.println("\tDoes not accept");
            }
        }
        if (!bbbb)
            System.out.println("failed");
        else
            System.out.println("All pass");
    }
}
