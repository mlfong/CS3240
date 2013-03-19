
public class MyToken {
	private String name;
	private RegExNFA nfa;
	private boolean nfaCreated;
	
	public MyToken(String name){
		this.name = name;
		this.nfaCreated = false;
	}
	
	public void makeNFA(String[] regex){
		this.nfa = new RegExNFA(regex);
		this.nfaCreated = true;
	}
	
	public boolean check(String[] input){
		if(this.nfaCreated){
			return this.nfa.check(input);
		}
		else{
			System.out.println("NFA HAS NOT BEEN GENERATED YET");
			return false;
		}
	}
	
	
	
}
