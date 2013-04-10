package rdp;
/****
 * Transition
 * 
 * Represents a state transition
 * @author mfong
 *
 */

public class Transition
{
//    public static final Character EPSILON = '\t';
    public static final Character EPSILON = 'ε';
    
    private Character transitionChar;
    private State destState;
    
    public Transition(Character tc, State ds)
    {
        this.transitionChar = tc;
        this.destState = ds;
    }
    
    public Transition(Transition old)
    {
        this.transitionChar = new Character(old.transitionChar.charValue());
        this.destState = new State(old.destState);
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
