package sg;

/*****
 * Driver
 * 
 * the main driver class that calls the scanner generator
 * 
 * @author mlfong
 * @version 1.0
 */

import ll1.LL1Walker;

public class Driver
{
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
        	System.out.println(args.length);
            usage();
            System.exit(0);
        }
        String specfile = args[0];
        String grammarfile = args[1];
        String inputfile = args[2];
        
//        String specfile = "part2txt/token_spec.txt";
//        String inputfile = "part2txt/script.txt";
//        String grammarfile = "part2txt/grammar.txt";
//        specfile = "testcase1/spec.txt";
//        inputfile = "testcase1/script.txt";
//        grammarfile = "testcase1/grammar.txt";
        LL1Walker walker = new LL1Walker(specfile, inputfile, grammarfile);
        System.out.println("Does the script pass the LL1Parsing: "+walker.mandoLL1());
    }

    
    
    /*public static void main(String[] args)
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
    }*/
    
    
    public static void usage()
    {
        System.out.println("Usage:");
        System.out.println("java Driver SPEC_FILE GRAMMAR_FILE INPUT_FILE OR");
        System.out.println("ant Driver -DS=\"path to spec file\" -DG=\"path to grammar file\" -DI=\"path to script file\"");
    }
}
