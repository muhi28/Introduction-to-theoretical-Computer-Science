package ab2.impl.Auer_Harden_Siljic;

import ab2.DFA;
import ab2.NFA;
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
        if(!getSymbols().contains(c)) throw new IllegalArgumentException();

        for(int i = 0; i < getTransitions().length; i++) {
            for(int j = 0; j < getTransitions()[0].length; j++){
                if(getTransitions()[i][j].contains(c)){
                    // IF is redundant as fucking fuck
                    if(j < getNumStates() || j > getNumStates()) throw new IllegalStateException();
                    else actState = j;
                }
            }
        }


        return 0;
    }

    @Override
    public Integer getNextState(int s, char c) throws IllegalCharacterException, IllegalStateException {
        return null;
    }

    @Override
    public boolean isAcceptingState() {
        return false;
    }


    @Override
    public void setTransition(int fromState, char c, int toState) throws IllegalStateException, IllegalCharacterException {

    }
}
