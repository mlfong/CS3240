package rdp;

public class Transition
{
    public static final Character EPSILON = '\t';
    
    private Character transitionChar;
    private State destState;
    
    public Transition(Character tc, State ds)
    {
        this.transitionChar = tc;
        this.destState = ds;
    }
    
    public Character getTransitionChar()
    {
        return this.transitionChar;
    }
    public State getDestState()
    {
        return this.destState;
    }
    
    public void setTransitionChar(Character tc)
    {
        this.transitionChar = tc;
    }
    public void setDestState(State s)
    {
        this.destState = s;
    }
    
}
