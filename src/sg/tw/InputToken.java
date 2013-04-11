package sg.tw;

/**
 * 
 * @author James Bowland - Gleason
 * @version 1.0
 * 
 * Wrapper class to hold results of table walking
 */
public class InputToken {
	private String value, tokenName;
		
	public InputToken(String value, String tokenName){
		this.value = value;
		this.tokenName = tokenName;
	}
	
	public String toString(){
		return this.tokenName + " " + this.value;
	}
}
