import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegExNode {
	private ArrayList<RegExNode> children;
	private String state;
	private int stateID;
	private boolean terminal;
	private Map<String, RegExNode> transitions;
	
	public RegExNode(int ID,String state){
		this.state = state;
		this.stateID = ID;
		this.terminal = false;
		this.children = new ArrayList<RegExNode>();
	}
	
	public RegExNode(int ID){
		this.state = null;
		this.stateID = ID;
		this.terminal = false;
		this.children = new ArrayList<RegExNode>();
	}
	
	//add children
	/*public void addChild(int id, String nextState){
		this.children.add(new RegExNode(id,nextState));
	}*/
	
	public void addChild(RegExNode child){
		this.children.add(child);
	}
	
	
	public int getStateID() {
		return stateID;
	}

	public void setStateID(int stateID) {
		this.stateID = stateID;
	}

	public ArrayList<RegExNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<RegExNode> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	public boolean equals(Object o){
		if(o instanceof RegExNode){
			RegExNode n = (RegExNode)o;
			if(this.stateID == n.getStateID()){
				return true;
			}
		}
		return false;
	}
	
	
}
