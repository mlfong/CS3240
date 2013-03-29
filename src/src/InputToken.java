
public class InputToken {
	private String value;
	private MyToken token;
	
	public InputToken(String value, MyToken token){
		this.token = token;
		this.value = value;
	}
	
	public String toString(){
		return token.toString() + " " + value;
	}
}
