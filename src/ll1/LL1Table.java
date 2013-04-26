package ll1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import sg.Util;

public class LL1Table
{
    private HashMap<String, HashMap<String, LL1Entry>> table;
    private RuleSet ruleset;
    
    private LL1Table(){
        table = new HashMap<String, HashMap<String, LL1Entry>>();
    }
    
    public RuleSet getRuleSet(){
        return this.ruleset;
    }
    
    public HashMap<String, HashMap<String, LL1Entry>> getLL1Table(){
        return this.table;
    }
    
    public static LL1Table makeLL1Table(String filename) throws Exception{
        LL1Table ll1table = new LL1Table();
        
        // make first and follow sets
        RuleSet rs = new RuleSet();
        rs.init(filename);
//        System.out.println(rs);
        rs.generateFirstSets();
        rs.generateFollowSets();
        ll1table.ruleset = rs;
        
        Set<String> namesOfNonterminals = rs.getAllNonterminals();
        Set<String> namesOfTerminals = rs.getAllTerminals();

//        /*
        System.out.println("terminals");
        Util.reallyPrettyPrint(namesOfNonterminals);
        System.out.println("nonterminals");
        Util.reallyPrettyPrint(namesOfTerminals);
//*/
        // create rows
        for(String nonTerminal : namesOfNonterminals){
            ll1table.table.put(nonTerminal, new HashMap<String, LL1Entry>());
        }
        // create columns
        for(String rulename : rs.getRules().keySet()){
            Rule therule = rs.getRules().get(rulename);
            for(int i = 0 ; i < therule.getRHS().size(); i++){
                ArrayList<String> onepossibleRHS = therule.getRHS().get(i);
                String firstTokenOnRHS = onepossibleRHS.get(0);
                if(firstTokenOnRHS.equals("<epsilon>")){
                    ArrayList<String> followSet = rs.getRules().get(rulename).getFollowSet();
                    for(String item : followSet){
                        ll1table.table.get(therule.getLHS()).put(item, new LL1Entry(therule, i));
                    }
                }
                else if(!Rule.isRule(firstTokenOnRHS)){
                    // terminal, so FI==FO==firstTokenOnRHS
                    ll1table.table.get(therule.getLHS()).put(firstTokenOnRHS, new LL1Entry(therule,i));
                } else {
                    // nonterminal aka a rule
                    ArrayList<String> firstSet = new ArrayList<String>();
                    if(firstTokenOnRHS.equals("<epsilon>"))
                        firstSet.add("<epsilon>");
                    else
                        firstSet = rs.getRules().get(firstTokenOnRHS).getFirstSet();
                    for(String item : firstSet){
                        if(!item.equals("<epsilon>")){
                            ll1table.table.get(therule.getLHS()).put(item, new LL1Entry(therule, i));
                        }
                    }
                    if(firstSet.contains("<epsilon>")){
                        ArrayList<String> followSet = rs.getRules().get(firstTokenOnRHS).getFollowSet();
                        for(String item : followSet){
                            ll1table.table.get(therule.getLHS()).put(item, new LL1Entry(therule, i));
                        }
                        ll1table.table.get(therule.getLHS()).put("$", new LL1Entry(therule, i));
                    }
                }
            }
        }
        
        return ll1table;
    }
    
    public void dump(){
        System.out.println("DUMPING");
        for(String rh : this.table.keySet()){
            System.out.println(rh + ":");
            HashMap<String, LL1Entry> column = this.table.get(rh);
            for(String ch : column.keySet()){
                System.out.println("\t" + ch + ": ") ;
                System.out.print("\t\t");
                Util.prettyPrint(column.get(ch).getCorrectRHS());
                System.out.println();
                
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        LL1Table ll1table = LL1Table.makeLL1Table("part2txt/Lecture9Grammar.txt");
//        LL1Table ll1table = LL1Table.makeLL1Table("part2txt/Hw5Grammar.txt");
        ll1table.dump();
    }
}
