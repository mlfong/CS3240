package ll1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import sg.ScannerGenerator;
import sg.fa.DFA;
import sg.tw.InputToken;
import sg.tw.TableWalker;

public class LL1Walker {
	//Holds LL1Table
	private LL1Table llTable;
	//Holds the tokens to compare with top of the stack
	private ArrayList<InputToken> userTokens;
	//Index used for comparing the terminals from the top of the stack
	//With the inputted tokens
	private int index;
	
	
	private static final int debug = 1;
	//Stack used for parsing
	LinkedList<String> parsingStack;
	public LL1Walker(String specfile, String inputfile, String grammar){
		try
        {
			//Phase 1 stuff
			ScannerGenerator sg = null;
            sg = ScannerGenerator.init(specfile);
            DFA dfa = sg.getDFA();
            TableWalker reader = new TableWalker(inputfile);
            reader.tableWalk(dfa, false);
            
            //Init some instance variables
            userTokens = reader.getUserTokens();
            llTable = LL1Table.makeLL1Table(grammar);
            index = 0;
            
            //Push start rule on the
            parsingStack = new LinkedList<String>();
            RuleSet ruleSet = llTable.getRuleSet();
            //Need to put in the start variable
            HashMap<String, Rule> rules = ruleSet.getRules();
            String start = llTable.getStartRuleName();
            parsingStack.push(start);
//            parsingStack.push(ruleSet.getRules().get("begin"));
        } 
		catch (IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
		catch(Exception e){
			e.printStackTrace();
            System.exit(0);
		}
	}
	
	public boolean doLL1(){
		//<Rule, <Terminal, LL1Entry>>
		HashMap<String, HashMap<String, LL1Entry>> table = llTable.getLL1Table();
		debugPrint("***********Starting ll1 parsing");
		while(!parsingStack.isEmpty()){
			//Check to see if userTokens is at the end
			//If so, end
			if(userTokens.size() == index){
				debugPrint("UserToken's size = index");
				return false;
			}
			
			//Pop off element
			String value = parsingStack.pop();
			
			debugPrint("Value is: "+value);
			//Check to see if it's a rule
			//If so, go to LL1 table
			String token = userTokens.get(index).getTokenName();
			if(token.charAt(0) == '$'){
				token = token.substring(1);
			}
			debugPrint("Token is: "+token);
			if(Rule.isRule(value)){
				debugPrint("value was a rule");
				LL1Entry entry = table.get(value).get(token);
				if(entry == null){
					debugPrint("Entry was null");
					break;
				}
				ArrayList<String> newEntries = entry.getCorrectRHS();
				for(int x = newEntries.size() - 1; x >= 0; x--){
					parsingStack.push(newEntries.get(x));
				}
			}
			//It's a terminal so check usertokens to compare
			else{
				debugPrint("value was not a rule");
				//Top of stack does not equal input stack
				if(!token.equals(value)){
					debugPrint("Tokens did not equal value on stack");
					return false;
				}
				debugPrint("Value and token are the same!");
				index++;
			}
			debugPrint("");
		}
		
		//If either stack is full at the end, then it did not work
		if(!parsingStack.isEmpty() || index >= userTokens.size()){
			debugPrint("Parsing stack or usertokens are not empty");	
			return false;
		}

		return true;
	}
	
	private void debugPrint(String message){
		if(debug == 1)
			System.out.println(message);
	}
}
