import java.util.ArrayList;


public class RegExGroup {
	private RegExGroup subGroup;
	private ArrayList<String> members;
	
	
	public RegExGroup(ArrayList<String> grouping){
		this.members = grouping;
	}
	
	//LBC
	public RegExNFA something(ArrayList<String> input){
		String[] r = {"g","f"};
		RegExNFA ourNFA = new RegExNFA(r);
		int id = 1;
		int parentId = 0;
		boolean or,star;
		or = star = false;
		for(int i = 0; i < input.size() ; i++){
			String temp = input.get(i);
			if(temp.contains("(")){
				//TODO Group cases
				parentId = id;
			}
			else if(temp.contains("*")){
				star = true;
				
			}
			else if(temp.contains("|")){
				or = true;
			}
			else if(temp.contains(")")){
				
			}
			else{
				RegExNode node = new RegExNode(id++,temp);
			
			}
		}
		return null;
	}
	
	private RegExNFA subNFA(int start,ArrayList<String> input ){
		return null;
	}
	
}
