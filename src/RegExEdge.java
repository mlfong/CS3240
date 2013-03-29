
public class RegExEdge {
	private RegExNode src,dst;
	private String trans;
	
	public RegExEdge(RegExNode a, RegExNode b, String trans){
		this.src = a;
		this.dst = b;
		this.trans = trans;
	}
	
	public boolean equals(Object o){
		if (o instanceof RegExEdge){
			RegExEdge e = (RegExEdge)o;
			if(this.src.equals(e.getA()) && this.dst.equals(e.getB())){
				return true;
			}
		}
		return false;
	}

	public RegExNode getA() {
		return src;
	}

	public void setA(RegExNode a) {
		this.src = a;
	}

	public RegExNode getB() {
		return dst;
	}

	public void setB(RegExNode b) {
		this.dst = b;
	}

	public String getTrans() {
		return trans;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}
	
	
	public String toString2(){
		return "(" + this.src.getName() + " " + this.src.getStateID() + "-->" +
				 this.dst.getName() + " "+ this.dst.getStateID() + ")";
	}
	
	public String toString(){
		return "(" + this.src.getStateID() + "-->" +
				 this.dst.getStateID() + ") " + this.trans;
	}
	
	
	
}
