package sg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import sg.fa.DFA;
import sg.fa.NFA;
import sg.rdp.InitRegex;
import sg.rdp.RecursiveDescentParserNFA;

public class ScannerGenerator
{
    private HashMap<String, NFA> classesNFA;
    private HashMap<String, NFA> tokensNFA;
    private DFA bigDFA;

    private ScannerGenerator()
    {
        this.classesNFA = new HashMap<String, NFA>();
        this.tokensNFA = new HashMap<String, NFA>();
        this.bigDFA = null;
    }

    public HashMap<String, NFA> getClassesNFA()
    {
        return this.classesNFA;
    }

    public HashMap<String, NFA> getTokensNFA()
    {
        return this.tokensNFA;
    }

    public DFA getDFA()
    {
        if (this.bigDFA != null)
            return this.bigDFA;
        ArrayList<String> keys = new ArrayList<String>(this.getTokensNFA()
                .keySet());
        NFA bigNFA = NFA.union(this.getTokensNFA().get(keys.get(0)), this
                .getTokensNFA().get(keys.get(1)));
        for (int i = 2; i < keys.size(); i++)
            bigNFA = NFA.union(bigNFA, this.getTokensNFA().get(keys.get(i)));
        this.bigDFA = DFA.convertNFA(bigNFA);
        return this.bigDFA;
    }

    public static ScannerGenerator makeScannerGenerator(String filename)
            throws IOException
    {
        File f = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String s;
        boolean inTokens = false;
        ScannerGenerator sg = new ScannerGenerator();
        HashMap<String, NFA> classesNFA = sg.classesNFA;
        HashMap<String, NFA> tokensNFA = sg.tokensNFA;
        while ((s = br.readLine()) != null)
        {
            if (s.length() < 2 && !inTokens)
            {
                inTokens = true;
                continue;
            }
            int divider = s.indexOf(" ");
            String classname = s.substring(0, divider);
            String theregex = s.substring(divider + 1);
            String sanitized = InitRegex.initializeRegex(theregex);
            // remove +'s -> ()()*
            sanitized = sanitized.substring(1, sanitized.length() - 1);
            sanitized = sanitized.replaceAll(" ", "");
            // System.out.println("We are on: " + classname + " which is: "
            // + sanitized);

            NFA nfa = RecursiveDescentParserNFA.validateRegex(sanitized,
                    classesNFA);
            if (nfa == null)
            {
                System.out.println("uh oh failed on: ");
                System.out.println("\tname: " + classname);
                System.out.println("\tregex: " + sanitized);
            }
            if (inTokens) // token generation
            {
                tokensNFA.put(classname, nfa);
                nfa.setAcceptToken(classname);
            }
            else
            // class generation
            {
                classesNFA.put(classname, nfa);
            }
        }
        br.close();
        return sg;
    }

}
