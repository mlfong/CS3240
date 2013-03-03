import java.util.HashMap;

public class SimplifyRegex{
	private static final char[] specialChars = {'(', ')', '*', '+', '|'};
	public static final int SIMPLIFIED_STRING = 0;
	public static final int SIMPLIFIED_MAP = 1;
	public static void main(String[] args){
		String[] tests = {"$SMALLCASE ($LETTER | $DIGIT)*", "AA(AA|AA)(AA)*", "", "AB", 
		"(A|(AA|BB|CC)+)*|(DD|ABC|EE)+", "(A)(B)(C)(A)(B)(C)",
		"AB+B", // ABB*B -> (AB)B*B -> 01*1
		"ABB", // 0
		"(AB)B" // 0
		};
		for(String test: tests){
			Object[] resultsMap = simplifyInput(test);
			System.out.println(resultsMap[SIMPLIFIED_STRING]);
		}
	}

	/**
			 * Stuff I need to do
			 * Simplified input
	 		* Also take care of +
			 * Make sure to check for |
			 * Key = number (as a string)
			 * Value = original string
	 		 */

	public static Object[] simplifyInput(String regex){
		HashMap<String, String> map = new HashMap<String, String>();
		int count = 0;
		//Used as the string to build form
		StringBuilder tempRegex = new StringBuilder(regex.replaceAll("\\s", ""));
		//Will store the final, simplified string
		StringBuilder finalRegex = new StringBuilder();
		//Used as the temp for storing each grouping
		StringBuilder tempGroup = new StringBuilder();

		//Makes sure we keep looking at the string if there still exists characters
		while(tempRegex.length() > 0){
			int index = 0;
			//Check to see if the first character is a special char
			//If so, add it to the finalRegex and delete it from the tempRegex
			//Then check again
			if(isSpecialChar(tempRegex.charAt(index))){
				finalRegex.append(tempRegex.charAt(index));
				tempRegex.deleteCharAt(0);
			}
			else{
				//Keep adding chars until it hits a special char
				while(index < tempRegex.length() && !isSpecialChar(tempRegex.charAt(index))){
					tempGroup.append(tempRegex.charAt(index));
					index++;
				}
				String grouping = tempGroup.toString();
				//Check to see if grouping exists in the map
				if(map.get(grouping) != null){
					finalRegex.append(map.get(grouping.toString()));
				}
				else{
					map.put(grouping.toString(), Integer.toString(count));
					finalRegex.append(count);
					count++;
				}
				//Add the special character that failed the loop above if we're not at the end of the string
				if(index < tempRegex.length()){
					finalRegex.append(tempRegex.charAt(index));
					//Delete the substring of tempGroup so we start at index 0 again
					tempRegex.delete(0, index + 1);
					//Clear tempGroup
					tempGroup.delete(0, tempGroup.length());
				}
				else{
					//Delete tempRegex if reached the end of the string so outer loop fails
					tempRegex.delete(0, tempRegex.length());
				}
				//Don't have to delete tempGroup if we reached the end of the string
			}

		}
		Object[] resultsMap = new Object[2];
		resultsMap[SIMPLIFIED_STRING] = finalRegex.toString();
		resultsMap[SIMPLIFIED_MAP] = map;
		return resultsMap;
	}
	private static boolean isSpecialChar(char input){
		for(char special: specialChars){
			if(input == special){
				return true;
			}
		}
		return false;
	}
}