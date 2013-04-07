package rdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ScannerGenerator2
{
    private static boolean sg2debug = false;
    
    private HashMap<String, NFA> classesNFA;
    private HashMap<String, NFA> tokensNFA;
    
    private ScannerGenerator2()
    {
        this.classesNFA = new HashMap<String, NFA>();
        this.tokensNFA = new HashMap<String, NFA>();
    }
    public HashMap<String, NFA> getClassesNFA()
    {
        return this.classesNFA;
    }
    public HashMap<String, NFA> getTokensNFA()
    {
        return this.tokensNFA;
    }
    
//    public static DFA scanSpec(String filename) throws IOException
    public static ScannerGenerator2 makeScannerGenerator(String filename) throws IOException
    {
        File f = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String s;
        DFA classesDFA = null, tokensDFA = null;
        boolean inTokens = false;
        ScannerGenerator2 sg = new ScannerGenerator2();
        HashMap<String, NFA> classesNFA = sg.classesNFA;
        HashMap<String, NFA> tokensNFA = sg.tokensNFA;
        while( (s = br.readLine()) != null)
        {
            if(s.length() < 2 && !inTokens)
            {
                inTokens = true;
                continue;
            }
            int divider = s.indexOf(" ");
            String classname = s.substring(0, divider);
            String theregex = s.substring(divider+1);
            // remove +'s -> ()()*
//            NFA nfa = RecursiveDescentParser.rdp(theregex);
            if(inTokens) // token generation
            {
//                tokensNFA.put(classname, nfa);
            }
            else // class generation
            {
//                classesNFA.put(classname, nfa);
            }
        }
        br.close();
        return sg;
    }
}
