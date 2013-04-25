package ll1;

import java.util.ArrayList;

public class LL1Entry
{
    private Rule rule;
    private int index;
    
    public LL1Entry(Rule r, int i){
        this.rule = r;
        this.index = i;
    }
    
    public Rule getRule(){
        return this.rule;
    }
    public int getIndex(){
        return this.index;
    }
    
    public ArrayList<String> getCorrectRHS(){
        return this.rule.getRHS().get(this.index);
    }
}
