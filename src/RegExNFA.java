import java.util.ArrayList;


public class RegExNFA {
	private ArrayList<RegExNode> states;
	private ArrayList<RegExEdge> edges;
	
	public RegExNFA(){
		this.states = new ArrayList<RegExNode>();
		this.edges = new ArrayList<RegExEdge>();
		this.states.add(new RegExNode(0,"Start"));
	}

	public void addNode(RegExNode n){
		if(!this.states.contains(n)){
			this.states.add(n);
		}
		for(RegExNode child: n.getChildren()){
			RegExEdge toAdd = new RegExEdge(n, child);
			if(!this.edges.contains(toAdd)){
				this.edges.add(toAdd);
			}
		}
	}
	
	public void addEdge(RegExEdge e){
		if(!this.edges.contains(e)){
			this.edges.add(e);
		}
	}
	
	public boolean isEmpty(){
		return this.states.size()==1;
	}
	
	public RegExNode getNode(int ID){
		for(RegExNode node: this.states){
			if(node.getStateID()==ID){
				return node;
			}
		}
		return null;
	}
}
