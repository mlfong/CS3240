package ll1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class RuleSet
{
    private HashMap<String, Rule> rules;
    private Rule startRule;
    
    public RuleSet(){
        this.rules = new HashMap<String, Rule>();
        this.startRule = null;
    }
    
    public Rule getStartRule(){
    	return this.startRule;
    }
    public void setStartRule(Rule r){
    	this.startRule = r;
    }
    
    public void addRule(Rule r){
        if(this.rules.keySet().contains(r.getLHS())){
            this.rules.get(r.getLHS()).merge(r);
        } else {
            this.rules.put(r.getLHS(), r);
        }
    }
    
    public HashMap<String, Rule> getRules(){
        return this.rules;
    }
    public Set<String> getAllNonterminals(){
        return this.rules.keySet();
    }
    public Set<String> getAllTerminals(){
        Set<String> set = new HashSet<String>();
        for(String ruleName : this.rules.keySet()){
            Rule therule = this.rules.get(ruleName);
            for(int i = 0; i < therule.getRHS().size(); i++){
                ArrayList<String> oneRHS = therule.getRHS().get(i);
                for(String oneToken : oneRHS){
                    if(!Rule.isRule(oneToken))
                        set.add(oneToken);
                }
            }
        }
        return set;
    }
    
    public void init(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        String s ;
        boolean first = true;
        int i = 0;
        while(null != (s=br.readLine())){
            Rule r = new Rule();
            if(i == 0){
            	r.setStart(true);
            	this.startRule = r;
            }
            i++;
            r.init(s);
            if(first){
            	r.setStart(true);
            	first = false;
            }
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
        rs.init("part2txt/Lecture9Grammar.txt");
        System.out.println(rs);
      
        rs.generateFirstSets();
        System.out.println("First sets");
        rs.printFirstSets();
        System.out.println();
        rs.generateFollowSets();
        System.out.println("Follow sets");
        rs.printFollowSets();
    }
    
    public void generateFirstSets(){
    	
    	for (String rl: rules.keySet()){
    		Rule current = rules.get(rl);
    		int k = 0;
    		while(k < current.getRHS().size()){
    			ArrayList<String> ruleK = current.getRHS().get(k);
    			int c = 0;
    			while(c < ruleK.size()){
    				ArrayList<String> firsts = first(ruleK.get(c));
    				if(!firsts.contains("<epsilon>")){
    					current.addToFirst(firsts);
    					break;
    				}else{
    					firsts = removeAllEpsilon(firsts);
    					current.addToFirst(firsts);
    					c++;
    				}
    			}
    			if(c>=ruleK.size()){
    				current.addToFirst("<epsilon>");
    			}
    			k++;
    		}
    	}
    	    	
    }
    
    public void generateFollowSets(){
    	for(String r1: this.rules.keySet()){
    		Rule r = this.rules.get(r1);
    		if(r.isStart()){
    			r.addToFollow("$");
    		}
    	}
    	int ok = 0;
    	while(ok++<100){
    	for(String r1: this.rules.keySet()){
    		Rule r = this.rules.get(r1);
    		int k = 0;
    		while(k < r.getRHS().size()){
    			ArrayList<String> ruleProd = r.getRHS().get(k);
    			int c = 0;
    			while(c < ruleProd.size()-1){
	    				String current = ruleProd.get(c);
	    				String next = ruleProd.get(c+1);
	    				if(Rule.isRule(current)){
	    					Rule toUpdate = this.rules.get(current);
	    					if(toUpdate == null) {
	    					    System.out.println("ToUpdate rule is null");
	    					}
	    					if(Rule.isRule(next)){
	    						Rule next1 = this.rules.get(next);
	    						if(next1 == null){
	    						    System.out.println("next1 is null: " + next1);
	    						}
	    						try{
	    						toUpdate.addToFollow(removeAllEpsilon(next1.getFirstSet()));
	    						}catch(Exception e){
	    						    System.out.println(e);
	    						}
	    						if(next1.getFirstSet().contains("<epsilon>")){
	    							toUpdate.addToFollow(r.getFollowSet());
	    						} else {
//	    						    System.out.println("Code says no ep");
	    						}
	    					}
	    					else{
	    						toUpdate.addToFollow(next);
	    					}
	    				}
	    				c++;
	    			}
	    			String current = ruleProd.get(c);
	    			if(Rule.isRule(current)){
	    				Rule toUpdate = this.rules.get(current);
	    				toUpdate.addToFollow(r.getFollowSet());
	    			}
	    			k++;
	    		}
	    	}
    	}

    }
    
    
    public ArrayList<String> removeAllEpsilon(ArrayList<String> input){
    	ArrayList<String> toReturn = new ArrayList<String>();
        for(String s:input){
            if(!s.contains("<epsilon>")){
                toReturn.add(s);
            }
        }
    	return toReturn;
    }
    
    public ArrayList<String> first(String x){
    	ArrayList<String> toReturn = new ArrayList<String>();
    	if(!Rule.isRule(x)){
    		toReturn.add(x);
    		return toReturn;
    	}
    	Rule temp = this.rules.get(x);
    	if(temp!= null){
	    	for(ArrayList<String> s: temp.getRHS()){
	    		toReturn.addAll(first(s.get(0)));
	    	}
    	}
    	
    	return toReturn;
    }
    
    public void printFirstSets(){
    	for(String rl : this.rules.keySet()){
    		this.rules.get(rl).printFirstSet();
    	}
    }
    
    public void printFollowSets(){
    	for(String rl : this.rules.keySet()){
    		this.rules.get(rl).printFollowSet();
    	}
    }
    
    
    
    public void printArray(ArrayList<String> input){
    	System.out.print("{");
    	for(String s: input){
    		System.out.print(" "+s+" ");
    	}
    	System.out.println("}");
    }
    
    
    
    
}
