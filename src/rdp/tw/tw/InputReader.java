package rdp.tw;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;import rdp.DFA;




public class InputReader {
	private File file;
	private ArrayList<MyToken> tokens;
	private ArrayList<InputToken> userTokens;
	
	public InputReader(String filename, ArrayList<MyToken> tokens){
		this.file = new File(filename);
		this.tokens = tokens;
		this.userTokens = new ArrayList<InputToken>();
	}
	
	public InputReader(){
		this("", null);
	}
	
	
	public void doStuff(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(this.file));
			String currentLine;
			while((currentLine = br.readLine())!= null){
				int index = 0;
				int start = index;
				boolean matchFound = false, failFound = false, restart = false;
				ArrayList<String> testStrings = new ArrayList<String>();
				MyToken lastMatch = null;
				String lastInputTokeName = "";
				while(index < currentLine.length()){
					char current = currentLine.charAt(index);
					testStrings.add(Character.toString(current));
					lastInputTokeName+=current;
					
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(Character.toString(current));
					
					System.out.println("Current Buffer: "+lastInputTokeName);
					printArray(makeArray(testStrings));
					failFound = false;
					if(hasMatch(makeArray(temp))){
						//System.out.println("Match temp found");
						failFound = true;
					}
					
					if(hasMatch(makeArray(testStrings))){
						System.out.println("Match found");
						matchFound = true;
						lastMatch = firstMatch(makeArray(testStrings));
						index++;
						if(index == currentLine.length()){
							System.out.println("Adding prvious success to list. Adding: " + lastInputTokeName);
							this.userTokens.add(new InputToken(lastInputTokeName,lastMatch));
							lastInputTokeName = "";
							testStrings.clear();
							matchFound = false;
						}
					}else{
						System.out.println("Fail");
						if(matchFound){
							lastInputTokeName = lastInputTokeName.substring(0,lastInputTokeName.length()-1);
							System.out.println("Adding prvious success to list. Adding: " + lastInputTokeName);
							this.userTokens.add(new InputToken(lastInputTokeName,lastMatch));
							lastInputTokeName = "";
							testStrings.clear();
							matchFound = false;
							start = index;
						}
						else{
							if(failFound == false) {
								System.out.println(" defaults");
								index++;
							}
							else {
							//	System.out.println("Space clear");
								lastInputTokeName = "";
								testStrings.clear();
							}
							if(index == currentLine.length()) {
								System.out.println("Invalid input, exiting generator...");
								System.exit(0);
							}
						}
					}
				}
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public void tableWalk(DFA dfa){
		try{
			BufferedReader br = new BufferedReader(new FileReader(this.file));
			String currentLine;
			while((currentLine = br.readLine())!= null){
				int index = 0;
				int start = index;
				boolean matchFound = false, failFound = false, restart = false;
				ArrayList<String> testStrings = new ArrayList<String>();
				String lastMatch = null;
				String lastInputTokeName = "";
				currentLine = sanitize(currentLine);
				while(index < currentLine.length()){
					char current = currentLine.charAt(index);
					testStrings.add(Character.toString(current));
					lastInputTokeName+=current;
					
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(Character.toString(current));
					
					System.out.println("Current Buffer: "+lastInputTokeName);
					printArray(makeArray(testStrings));
					failFound = false;
					
					Object[] answers = dfa.specialValidate(makeString(testStrings));
					boolean match = ((Boolean)answers[0]).booleanValue();
					if(match){
						//System.out.println("Match temp found");
						failFound = true;
					}
					
					if(match){
						System.out.println("Match found");
						matchFound = true;
						lastMatch = (String)answers[1];
						index++;
						if(index == currentLine.length()){
							System.out.println("Adding prvious success to list. Adding: " + lastInputTokeName);
							this.userTokens.add(new InputToken(lastInputTokeName,lastMatch));
							lastInputTokeName = "";
							testStrings.clear();
							matchFound = false;
						}
					}else{
						System.out.println("Fail");
						if(matchFound){
							lastInputTokeName = lastInputTokeName.substring(0,lastInputTokeName.length()-1);
							System.out.println("Adding prvious success to list. Adding: " + lastInputTokeName);
							this.userTokens.add(new InputToken(lastInputTokeName,lastMatch));
							lastInputTokeName = "";
							testStrings.clear();
							matchFound = false;
							start = index;
						}
						else{
							if(failFound == false) {
								System.out.println(" defaults");
								index++;
							}
							else {
							//	System.out.println("Space clear");
								lastInputTokeName = "";
								testStrings.clear();
							}
							if(index == currentLine.length()) {
								System.out.println("Invalid input, exiting generator...");
								System.exit(0);
							}
						}
					}
				}
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String sanitize(String input){
		return input.replace(" ", "");
	}
	
	public MyToken firstMatch(String[] checkString){
		for(MyToken token: tokens){
			if(token.check2(checkString)){
				return token;
			}
		}
		return null;
	}
	
	public boolean hasMatch(String[] checkString){
		for(MyToken token: tokens){
			if(token.check2(checkString)){
				return true;
			}
		}
		return false;
	}
	
	public String makeString(ArrayList<String> stringList){
		StringBuilder sb = new StringBuilder();
		for(String s : stringList){
			 sb.append(s);
		}		
		return sb.toString();
	}
	
	
	public String[] makeArray(ArrayList<String> stringList){
		String[] toReturn = new String[stringList.size()];
		//System.out.println(toReturn.length + " length");
		for(int i = 0 ; i < toReturn.length; i++){
			toReturn[i] = stringList.get(i);
		}
		return toReturn;
	}


	public ArrayList<InputToken> getUserTokens() {
		return userTokens;
	}
	
	public void printUserTokens(){
		for(InputToken t: this.userTokens){
			System.out.println(t);
		}
	}
	
	public void printArray(String[] array){
		for(int i = 0; i < array.length; i++){
			System.out.print(array[i]);
		}
		System.out.println();
	}
}
