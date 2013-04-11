package sg.tw;


public class InputToken {
	private String value, tokenName;
	private MyToken token;
	
	public InputToken(String value, MyToken token){
		this.token = token;
		this.value = value;
	}
	
	public InputToken(String value, String tokenName){
		this.value = value;
		this.tokenName = tokenName;
		this.token = null;
	}
	
	public String toString(){
		if(this.token == null){
			return this.tokenName + " " + this.value;
		}
		return token.toString() + " " + value;
	}
}
