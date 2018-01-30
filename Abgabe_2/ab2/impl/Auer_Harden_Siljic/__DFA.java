package ab2.impl.Auer_Harden_Siljic;

import ab2.DFA;
import ab2.NFA;
import ab2.fa.exceptions.IllegalCharacterException;

import java.util.Set;

public class __DFA extends __NFA implements DFA{

    public __DFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
        super(numStates, characters, acceptingStates, initialState);
    }

    @Override
    public void reset() {

    }

    @Override
    public int getActState() {
        return 0;
    }

    @Override
    public int doStep(char c) throws IllegalArgumentException, IllegalStateException {
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
