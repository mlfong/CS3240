import java.util.ArrayList;


public class DGraph {
	private int vertices, edges;
	private ArrayList<RegExNode> vertList;
	private ArrayList<RegExEdge> edgeList;
	
	public DGraph(int numOfVert){
		this.vertices = numOfVert;
		this.edges = 0;
		for(int i = 0; i < numOfVert; i++){
			this.vertList.add(new RegExNode(i));
		}
	}
	
	public void addEdge(int start, int end){
		if(start<vertices && end<vertices){
			RegExNode str = vertList.get(start);
			RegExNode ed = vertList.get(end);
			str.addChild(ed);
			this.edgeList.add(new RegExEdge(str,ed));
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
	
	

}
