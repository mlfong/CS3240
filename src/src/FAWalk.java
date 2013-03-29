import java.util.ArrayList;


public class FAWalk {
    private boolean[] marks;
    
    
    public FAWalk(DGraph g, int s) {
        this.marks = new boolean[g.getNumVertices()];
        dfs(g, s);
    }
    
    public FAWalk(DGraph g, ArrayList<Integer> IDs) {
        this.marks = new boolean[g.getNumVertices()];
        for (int v : IDs)
            dfs(g, v);
    }
    
    
    private void dfs(DGraph g, int v) { 
        marks[v] = true;
        for (RegExNode nxt : g.getChildren(v)) {
            if (!marks[nxt.getStateID()]){
            	dfs(g, nxt.getStateID());
            }
        }
    }
    
    public boolean marked(int v) {
        return marks[v];
    }

}
