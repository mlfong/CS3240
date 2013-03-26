import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class InputReader {
	private File file;
	private ArrayList<MyToken> tokens;
	private ArrayList<InputToken> userTokens;
	
	public InputReader(String filename, ArrayList<MyToken> tokens){
		this.file = new File(filename);
		this.tokens = tokens;
		this.userTokens = new ArrayList<InputToken>();
	}
	
	
	public void doStuff(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(this.file));
			String currentLine;
			while((currentLine = br.readLine())!= null){
				int index = 0;
				int start = index;
				boolean matchFound = false;
				ArrayList<String> testStrings = new ArrayList<String>();
				MyToken lastMatch = null;
				String lastInputTokeName = "";
				while(currentLine.charAt(index)!='\n'){
					char current = currentLine.charAt(index);
					testStrings.add(Character.toString(current));
					lastInputTokeName+=current;
					
					System.out.println("Current Buffer: "+lastInputTokeName);
					if(hasMatch(makeArray(testStrings))){
						System.out.println("Match found");
						matchFound = true;
						lastMatch = firstMatch(makeArray(testStrings));
						index++;
					}else{
						if(matchFound){
							System.out.println("Succes found before. Moving on.");
							lastInputTokeName = lastInputTokeName.substring(0,lastInputTokeName.length()-1);
							this.userTokens.add(new InputToken(lastInputTokeName,lastMatch));
							index = lastInputTokeName.length();
							start = index;
							lastInputTokeName = "";
							testStrings.clear();
							matchFound = false;
						}
						else if(index == currentLine.length()-1){
							System.out.println("Break after no match");
							index = ++start;
							lastInputTokeName = "";
							testStrings.clear();
							if(start >= currentLine.length()){
								break;
							}
						}
						else{
							System.out.println(" defaults");
							index++;
						}
					}
				}
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	
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
	
	public String[] makeArray(ArrayList<String> stringList){
		String[] toReturn = new String[stringList.size()];
		int i = 0;
		for(String s : stringList){
			toReturn[i] = s;
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
}
