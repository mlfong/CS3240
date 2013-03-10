import java.util.Stack;
public class InitRegex{
	private static final char PLUS = '+';
	private static final char LEFT_PARENS = '(';
	private static final char RIGHT_PARENS = ')';
	private static final char BACKSLASH = '\\';
	private static final int PARENS = 1;
	private static final int SLASH = -1;
	private static final int REGULARCHAR = 0;
	private static final String STAR = "*";
	private static final String testCases[] = {"a+", "aaa+", "ab+", "abbb+", "(a)+", "(ab)+", "\\+", "\\t+"};
	private static final String testResults[] = {"aa*", "aaaa*", "abb*", "abbbb*", "(a)(a)*", "(ab)(ab)*", "\\+", "\\t\\t*"};
	public static void main(String[] args){

		if(testCases.length != testResults.length){
			System.out.println("Error: testCases and testResults lengths do not match");
		}
		else{
			for(int i = 0; i < testCases.length; i++){
				String test = testCases[i];
				String result = testResults[i];
				String converted = convertPlus(test);
				System.out.println(converted);
				if(!result.equals(converted)){
					System.out.println("\tERROR: Got "+converted+" but expected "+result);
				}
			}
		}
	}

	public static String appendParens(String regex){
		return "("+regex+")";
	}	

	public static String convertPlus(String regex){
		StringBuilder newString = new StringBuilder();
		for(int regexIndex = 0; regexIndex < regex.length(); regexIndex++){
			//Check for + AND not \+
			if(regex.charAt(regexIndex) == PLUS && regexIndex > 0 && regex.charAt(regexIndex - 1) != BACKSLASH){
				int previous = checkPrevious(regex, regexIndex);
				if(previous == PARENS){
					System.out.println("PARENS!");
					newString.append(getParensSubstring(regex, regexIndex));
				}
				else if (previous == SLASH){
					System.out.println("SLASH");
					newString.append(regex.charAt(regexIndex - 1));
					newString.append("\\");
					newString.append(regex.charAt(regexIndex - 1));
					newString.append("*");
				}
				else{
					System.out.println("RegularChar");
					newString.append(regex.charAt(regexIndex - 1));
					newString.append("*");
				}
			}
			else{
				newString.append(regex.charAt(regexIndex));
			}
		}
		return newString.toString();
	}

	//Checks to see what the character before the + is 
	//To determine if it's a ), \ or a regular char
	//index currently points to the +
	private static int checkPrevious(String regex, int index){
		//Shouldn't happen, but just double check
		if(index == 0){
			return 0;
		}
		char lastChar = regex.charAt(index - 1);

		if(lastChar == RIGHT_PARENS){
			if(index > 1){
				if(regex.charAt(index - 2) == BACKSLASH){
					return SLASH;
				}
			}
			return PARENS;
		}
		else if(lastChar == BACKSLASH){
			return SLASH;
		}
		else{
			return REGULARCHAR;
		}
	}


	//If the char previous to + was a ), get the entire grouping within that paren
	private static String getParensSubstring(String regex, int index){
		Stack<Character> parens = new Stack<Character>();
		//Current index is pointed at + 
		//So make sure to push index - 1 to get right parens
		//And tempIndex starts the first character to the left of right parens
		parens.push(regex.charAt(index - 1));
		int tempIndex = index - 2;
		while(tempIndex >= 0){
			char lastChar = regex.charAt(tempIndex);
			//System.out.println(lastChar);
			if(lastChar == LEFT_PARENS){
				parens.pop();
			}
			else if(lastChar == RIGHT_PARENS){
				parens.push(lastChar);
			}

			if(parens.empty())
				break;

			tempIndex--;
		}
		StringBuilder newString = new StringBuilder(regex.substring(tempIndex, index));
		newString.append("*");
		return newString.toString();
	}

 
}

//(asdf)+ 
//(asdf)(asdf)*
//(asdfasdf(asdasdfasdf))+
// regex:aa+bbb+
// newstring:aaa*
// aaa*bbbb*
// (aaa)*
// aaa*

// (aaa)+




// \)+
// \)\)*