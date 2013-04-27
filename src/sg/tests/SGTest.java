package sg.tests;

import java.io.IOException;

import sg.ScannerGenerator;
import sg.fa.DFA;
import sg.tw.TableWalker;

public class SGTest
{
    public static void main(String[] args) throws IOException
    {
//        ScannerGenerator sg2 = ScannerGenerator.init("sample/SampleSpec");
        int num = 6;
        ScannerGenerator sg2 = ScannerGenerator.init("proj1_testcases/"+num+"/spec");
        DFA dfa = sg2.getDFA();
        TableWalker reader = new TableWalker("proj1_testcases/"+num+"/input");
        reader.tableWalk(dfa, false);
        reader.printUserTokens();
    }

    public static void test01(DFA dfa)
    {
        // String[] testStrings =
        // { "0", "1", "01", "a10", "bbbbb", "101asdad", "a8aa8a88s8das8a8",
        // "0.0", ".34234", "1.0", "1.", ".111", "=", "==", "+", "++",
        // "*", "**", "-", "--", "PRINT", "aPRINT", "PRINTa", "aPrINT" };
        // boolean[] answers =
        // { true, true, true, true, true, false, true, true, false, true,
        // false,
        // false, true, false, true, false, true, false, true, false,
        // true, false, false, false };
        // NFATest.advancedTest(dfa, testStrings, answers);
    }
}
