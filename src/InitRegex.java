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
	private static final String testCases[] = {"a+", "aaa+", "ab+", "abbb+", "(a)+", "(ab)+", 
											"\\+", "\\t+", "\\)", "(ab(cd))+", "(ab(cd)*)+",
											"a+b+c+", "(ab(cd)+)+", "abc", "a*b*c*", "(a+)+", "(a)"};
	private static final String testResults[] = {"(aa*)", "(aaaa*)", "(abb*)", "(abbbb*)", "((a)(a)*)", "((ab)(ab)*)", 
											"(\\+)", "(\\t\\t*)", "(\\))", "((ab(cd))(ab(cd))*)", "((ab(cd)*)(ab(cd)*)*)",
											"(aa*bb*cc*)", "((ab(cd)(cd)*)(ab(cd)(cd)*)*)", "(abc)", "(a*b*c*)", "((aa*)(aa*)*)", "(a)"};
	public static void main(String[] args){
		//runTest("(ab(cd)+)+", "(ab(cd)(cd)*)(ab(cd)(cd)*)*");
		runAllTests();
		
	}


	/*
		This is the method that other people should use to run this code
	*/
	public static String initializeRegex(String regex){
		return appendParens(convertPlus(regex));
	}

	//Runs a single tests
	public static void runTest(String input, String expected){
		String converted = initializeRegex(input);
		System.out.println(converted);
		if(!converted.equals(expected)){
			System.out.println("\tERROR: Expected "+expected+" from "+input);
		}
	}

	//Runs all the testcases
	public static void runAllTests(){
		if(testCases.length != testResults.length){
			System.out.println("Error: testCases and testResults lengths do not match");
		}
		else{
			for(int i = 0; i < testCases.length; i++){
				String test = testCases[i];
				String result = testResults[i];
				String converted = initializeRegex(test);
				System.out.println(converted);
				if(!result.equals(converted)){
					System.out.println("\tERROR: Expected "+result+" from "+test);
				}
			}
		}
	}

	/*
		Private implementation code
	*/

	private static String appendParens(String regex){
		//Check to make sure there are not already parens around the entire regex
		if(regex.charAt(0) == LEFT_PARENS && regex.charAt(regex.length() - 1) == RIGHT_PARENS)
			return regex;

		return "("+regex+")";
	}	

	private static String convertPlus(String regex){
		StringBuilder newString = new StringBuilder();
		for(int regexIndex = 0; regexIndex < regex.length(); regexIndex++){
			//Check for + AND not \+
			if(regex.charAt(regexIndex) == PLUS && regexIndex > 0 && regex.charAt(regexIndex - 1) != BACKSLASH){
				int previous = checkPrevious(regex, regexIndex);
				if(previous == PARENS){
					//System.out.println("PARENS!");
					newString.append(getParensSubstring(newString.toString()));
				}
				else if (previous == SLASH){
					//System.out.println("SLASH");
					newString.append(BACKSLASH);
					newString.append(regex.charAt(regexIndex - 1));
					newString.append(STAR);
				}
				else{
					//System.out.println("RegularChar");
					newString.append(regex.charAt(regexIndex - 1));
					newString.append(STAR);
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

		if(regex.charAt(index - 1) == RIGHT_PARENS){
			//Checks for the case of \)
			if(index > 1){
				if(regex.charAt(index - 2) == BACKSLASH){
					return SLASH;
				}
			}
			return PARENS;
		}
		else if(index > 1 && regex.charAt(index - 2) == BACKSLASH){
			return SLASH;
		}
		else{
			return REGULARCHAR;
		}
	}

	//If the char previous to + was a ), get the entire grouping within that parens
	//With the * appended
	private static String getParensSubstring(String regex){
		//Uses a stack to keep track of parens
		//Going from right to left
		//Pushes when it hits a right parens,
		//Pops when it hits a left paren
		//Ends when stack is empty
		Stack<Character> parens = new Stack<Character>();
		int beginIndex = regex.length() - 1;

	//	System.out.println("The whole regex is: "+regex);
	//	System.out.println("The current index is: "+tempIndex);
		while(beginIndex >= 0){
			char lastChar = regex.charAt(beginIndex);
			//System.out.println(lastChar);
			if(lastChar == LEFT_PARENS){
	//			System.out.println("LEFT_PARENS");
				parens.pop();
			}
			else if(lastChar == RIGHT_PARENS){
				parens.push(RIGHT_PARENS);
	//			System.out.println("RIGHT_PARENS");
			}

			if(parens.empty())
				break;

			beginIndex--;
		}
		return regex.substring(beginIndex) + STAR;
	}

 
}