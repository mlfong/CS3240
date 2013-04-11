package sg;

/*****
 * Driver
 * 
 * the main driver class that calls the scanner generator
 * 
 * @author mlfong
 * @version 1.0
 */

import java.io.IOException;

import sg.fa.DFA;
import sg.tw.TableWalker;

public class Driver
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            usage();
            System.exit(0);
        }
        String specfile = args[0];
        String inputfile = args[1];
        ScannerGenerator sg = null;
        try
        {
            sg = ScannerGenerator.init(specfile);
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
        DFA dfa = sg.getDFA();
        TableWalker reader = new TableWalker(inputfile);
        reader.tableWalk(dfa);
        reader.printUserTokens();
    }

    public static void usage()
    {
        System.out.println("Usage:");
        System.out.println("java Driver SPEC_FILE INPUT_FILE");
    }
}
