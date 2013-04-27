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
//    public static void main(String[] args)
//    {
//        if (args.length != 3)
//        {
//            usage();
//            System.exit(0);
//        }
//        String specfile = args[0];
//        String inputfile = args[1];
//        String grammarfile = args[2];
//        LL1Walker walker = new LL1Walker(specfile, inputfile, grammarfile);
//        System.out.println("Result of LL1Parsing is: "+walker.doLL1());
//    }

    
    
    // /home/victor/CS3240/CS3240/part2txt/token_spec.txt /home/victor/CS3240/CS3240/part2txt/script.txt /home/victor/CS3240/CS3240/part2txt/grammar.txt
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
        reader.tableWalk(dfa, true);
        //reader.printUserTokens();
    }
    
    
    public static void usage()
    {
        System.out.println("Usage:");
        System.out.println("java Driver SPEC_FILE INPUT_FILE GRAMMAR_FILE OR");
        System.out.println("ant Driver -Dspec=\"path to spec file\" -Dinput=\"path to input file\"");
    }
}
