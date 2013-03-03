
public class RegExEdge {
	private RegExNode a,b;
	
	public RegExEdge(RegExNode a, RegExNode b){
		this.a = a;
		this.b = b;
	}
	
	public boolean equals(Object o){
		if (o instanceof RegExEdge){
			RegExEdge e = (RegExEdge)o;
			if(this.a.equals(e.getA()) && this.b.equals(e.getB())){
				return true;
			}
		}
		return false;
	}

	public RegExNode getA() {
		return a;
	}

	public void setA(RegExNode a) {
		this.a = a;
	}

	public RegExNode getB() {
		return b;
	}

	public void setB(RegExNode b) {
		this.b = b;
	}
	
	
	
}
