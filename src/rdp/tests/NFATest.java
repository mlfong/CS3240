package rdp.tests;

import rdp.DFA;
import rdp.NFA;
import rdp.State;
import rdp.Transition;

public class NFATest
{
    public static void main(String[] args)
    {
//        test100();
         test04();
        // test03();
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
//         NFA LETTER = NFA.makeRangedNFA((char)32, (char)(126));
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
            System.out.println("Test " + (i + 1) + ": " + testStrings[i]
                    + " -> " + s);
        }
    }
}
