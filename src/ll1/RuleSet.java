package ll1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import sg.Util;


public class RuleSet
{
    private HashMap<String, Rule> rules;
    private HashMap<String, ArrayList<String>> firstSets;
    
    
    public RuleSet(){
        this.rules = new HashMap<String, Rule>();
        this.firstSets = new HashMap<String, ArrayList<String>>();
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
        boolean first = true;
        while(null != (s=br.readLine())){
            Rule r = new Rule();
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
        rs.init("SampleGrammar3.txt");
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
//    					System.out.println("No epsilons");
//    					printArray(firsts);
    					current.addToFirst(firsts);
    					break;
    				}else{
//    					System.out.println("Contains epsilons");
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
//	    				    System.out.println("Current is a rule");
	    					Rule toUpdate = this.rules.get(current);
	    					if(Rule.isRule(next)){
	    						Rule next1 = this.rules.get(next);
//	    						next1.printFirstSet();
//	    						System.out.print("Before we remove all epsilons:\n\t");
//	    						Util.prettyPrint(next1.getFirstSet());
	    						toUpdate.addToFollow(removeAllEpsilon(next1.getFirstSet()));
	    						if(next1.getFirstSet().contains("<epsilon>")){
//	    						    System.out.println("contains ep");
	    							toUpdate.addToFollow(r.getFollowSet());
	    						} else {
//	    						    System.out.println("Code says no ep");
//	    						    Util.prettyPrint(next1.getFirstSet());
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
