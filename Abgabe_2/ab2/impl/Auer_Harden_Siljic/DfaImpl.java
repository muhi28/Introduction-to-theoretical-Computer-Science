package ab2.impl.Auer_Harden_Siljic;

import ab2.DFA;
import ab2.fa.exceptions.IllegalCharacterException;

import java.util.Set;
import java.util.TreeSet;

public class DfaImpl extends NfaImpl implements DFA {

    public DfaImpl(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
        super(numStates, characters, acceptingStates, initialState);
    }

    private int currentState;

    @Override
    public void reset() {
        currentState = getInitialState();
    }

    @Override
    public int getActState() {
        return currentState;
    }

    @Override
    public int doStep(char c) throws IllegalArgumentException, IllegalStateException {
        if (!getSymbols().contains(c)) throw new IllegalArgumentException();

        Integer result = getNextState(currentState, c);
        if (result != null) currentState = result;
        else throw new IllegalStateException();
        return currentState;
    }

    @Override
    public Integer getNextState(int s, char c) throws IllegalCharacterException, IllegalStateException {
        if (!getSymbols().contains(c)) throw new IllegalArgumentException();
        if (s < 0 || s > getTransitions().length) throw new IllegalStateException();

        // look for the fitting transition
        for (int i=0; i<getTransitions()[0].length; i++) {
            Set<String> transitions = getTransitions()[currentState][i];
            if (transitions != null &&
                    transitions.size() > 0 &&
                    transitions.contains(c + "")) {
                return i;
            }
        }
        // nothing found
        return null;
    }

    @Override
    public boolean isAcceptingState() {
        // TODO what the hell is the difference between an accepting and end state? (see interface)

        return getAcceptingStates().contains(currentState);
    }

    @Override
    public void setTransition(int fromState, char c, int toState) throws IllegalStateException, IllegalCharacterException {
        this.setTransition(fromState, c+"", toState);
    }

    @Override
    public void setTransition(int fromState, String s, int toState) throws IllegalStateException, IllegalCharacterException {
        if (s.isEmpty() || s.length() > 1 ||
                !getSymbols().contains(s.charAt(0))) throw new IllegalCharacterException();
        if (fromState < 0 || fromState > getTransitions().length ||
                toState < 0 || toState > getTransitions().length) throw new IllegalStateException();

        if (getTransitions()[fromState][toState] == null) {
            getTransitions()[fromState][toState] = new TreeSet<>();
        }
        getTransitions()[fromState][toState].add(s);
    }
}
