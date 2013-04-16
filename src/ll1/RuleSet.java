package ll1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class RuleSet
{
    private HashMap<String, Rule> rules;
    
    public RuleSet(){
        this.rules = new HashMap<String, Rule>();
    }
    
    public void addRule(Rule r){
        if(this.rules.keySet().contains(r.getLHS())){
            this.rules.get(r.getLHS()).merge(r);
        } else {
            this.rules.put(r.getLHS(), r);
        }
    }
    
    public void init(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        String s ;
        while(null != (s=br.readLine())){
            Rule r = new Rule();
            r.init(s);
            this.addRule(r);
        }
        br.close();
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String s : rules.keySet()){
            sb.append(this.rules.get(s).toString() + "\n");
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws Exception {
        RuleSet rs = new RuleSet();
        rs.init("part2txt/grammar.txt");
        System.out.println(rs);
    }
}
