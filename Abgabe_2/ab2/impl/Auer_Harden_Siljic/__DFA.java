package ab2.impl.Auer_Harden_Siljic;

import ab2.DFA;
import ab2.fa.exceptions.IllegalCharacterException;

import java.util.Set;

public class __DFA extends __NFA implements DFA{
    private int actState = 0;

    public __DFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
        super(numStates, characters, acceptingStates, initialState);
    }

    @Override
    public void reset() {
        this.actState = getInitialState();
    }

    @Override
    public int getActState() {
        return this.actState;
    }

    @Override
    public int doStep(char c) throws IllegalArgumentException, IllegalStateException {
        return getNextState(this.actState, c);
    }

    @Override
    public Integer getNextState(int s, char c) throws IllegalCharacterException, IllegalStateException {
        // TODO: Exceptionhandling
        int nextState = -1;
        for(int i = 0; i < getTransitions().length; i++){
           if(getTransitions()[s][i].contains(c+"")){
               nextState = i;
           }
        }
        return nextState;
    }

    @Override
    public boolean isAcceptingState() {
        if(getAcceptingStates().contains(actState)) return true;
        else return false;
    }


    @Override
    public void setTransition(int fromState, char c, int toState) throws IllegalStateException, IllegalCharacterException {
        //if(fromState < getNumStates() || fromState > getNumStates() ||
        //        toState < getNumStates() || toState > getNumStates()) throw new IllegalStateException();

        setTransition(fromState, c+"", toState);
    }
}
