import java.util.ArrayList;


public class DGraph {
	private int vertices, edges;
	private ArrayList<RegExNode> vertList;
	private ArrayList<RegExEdge> edgeList;

	public DGraph(int numOfVert){
		this.vertices = numOfVert;
		this.edges = 0;
		this.vertList = new ArrayList<RegExNode>();
		this.edgeList = new ArrayList<RegExEdge>();
		for(int i = 0; i < numOfVert; i++){
			this.vertList.add(new RegExNode(i));
		}
	}
	
	public void addEdge(int start, int end, String transition){
		if(start<vertices && end<vertices){
			RegExNode str = vertList.get(start);
			RegExNode ed = vertList.get(end);
			str.addChild(ed);
			this.edgeList.add(new RegExEdge(str,ed,transition));
			this.edges++;
		}
		else{
			System.out.println("Invalid Edges Being Added");
		}
	}
	
	public ArrayList<RegExNode> getChildren(int index){
		return this.vertList.get(index).getChildren();
	}

	public int getNumVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public int getNumEdges() {
		return edges;
	}

	public void setEdges(int edges) {
		this.edges = edges;
	}

	public ArrayList<RegExNode> getVertList() {
		return vertList;
	}

	public void setVertList(ArrayList<RegExNode> vertList) {
		this.vertList = vertList;
	}

	public ArrayList<RegExEdge> getEdgeList() {
		return edgeList;
	}

	public void setEdgeList(ArrayList<RegExEdge> edgeList) {
		this.edgeList = edgeList;
	}
	
	public void nameStates(String[] input){
		for(int i = 0; i<input.length; i++){
			this.vertList.get(i).setName(input[i]);
		}
	}
	
	public void setGoal(int goalID){
		this.vertList.get(goalID).setTerminal(true);
	}
}
