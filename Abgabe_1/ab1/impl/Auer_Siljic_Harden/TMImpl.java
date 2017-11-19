package ab1.impl.Auer_Siljic_Harden;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ab1.TM;

public class TMImpl implements TM {
    private final int HALT_STATE = 0;

    private ArrayList<TMConfig> tapes = new ArrayList<>();
    private ArrayList<Transition> transitions = new ArrayList<Transition>();
    private Set<Character> symbols = new TreeSet<>();
    private Set<Integer> states = new TreeSet<Integer>();
    private ArrayList<Character> leftData = new ArrayList<>();
    private ArrayList<Character> rightData = new ArrayList<>();



    private int state;
    private int numTapes;
    private char below;

    private STATE currentState;


    private enum STATE {
        HALT,
        CRASHED
    }

    @Override
    public void reset() {

        numTapes = 1;

        tapes.clear();
        tapes.add(new TMConfig(new char[0], '#', new char[0]));

        states.clear();
        states.add(0);

        symbols.clear();
        transitions.clear();
    }

    @Override
    public void setNumberOfTapes(int numTapes) throws IllegalArgumentException {

        if (numTapes < 1) throw new IllegalArgumentException("Anzahl an Bändern zu klein !!!");

        this.numTapes = numTapes;

    }

    @Override
    public void setSymbols(Set<Character> symbols) throws IllegalArgumentException {

        if (!(symbols.contains('#'))) throw new IllegalArgumentException("Set der Symbole beinhaltet '#' nicht !!!");

        this.symbols = symbols;
    }

    @Override
    public Set<Character> getSymbols() {
        return symbols;
    }

    @Override
    public void addTransition(int fromState, int tapeRead, char symbolRead, int toState, int tapeWrite,
                              char symbolWrite, int tapeMove, Movement movement) throws IllegalArgumentException {

        if (fromState == HALT_STATE) {
            currentState = STATE.CRASHED;

            throw new IllegalArgumentException("Keine Transition von Haltezustand aus möglich !!!");
        }

        transitions.add(new Transition(fromState, tapeRead, symbolRead, toState, tapeWrite, symbolWrite, tapeMove, movement));

        states.add(fromState);
        states.add(toState);
    }

    @Override
    public Set<Integer> getStates() {

        return states;
    }

    @Override
    public int getNumberOfTapes() {
        return tapes.size();
    }

    @Override
    public void setInitialState(int state) {
        this.state = state;
    }

    @Override
    public void setInitialTapeContent(int tape, char[] content) {

        tapes.add(tape, new TMConfig(content, '#', new char[0]));
    }

    @Override
    public void doNextStep() throws IllegalStateException {

        if (state == HALT_STATE) {
            currentState = STATE.CRASHED;
            throw new IllegalStateException("Maschine befindet sich bereits im Haltezustand !!!");
        }

        Transition retTrans = null;

        for (Transition transition : transitions) {

            if (transition.getFromState() == state && transition.getSymbolRead() == tapes.get(transition.getTapeRead()).getBelowHead()) {

                retTrans = transition;

                TMConfig writeTape = tapes.get(transition.getTapeWrite());

                tapes.set(transition.getTapeWrite(), new TMConfig(writeTape.getLeftOfHead(), transition.getSymbolWrite(), writeTape.getRightOfHead()));


                TMConfig nextTape = tapes.get(transition.getTapeMove());

                leftData = changeToList(nextTape.getLeftOfHead());
                rightData = changeToList(nextTape.getRightOfHead());
                below = nextTape.getBelowHead();

                makeTrans(transition, leftData, below, rightData);


            }
        }

        if (retTrans == null) {
            currentState = STATE.CRASHED;

            throw new IllegalStateException("Transition nicht vorhanden !!!");
        }
    }


    /**
     * Converts a list to a char Array.
     *
     * @param data - the input list
     * @return - char array with list data
     */
    private ArrayList<Character> changeToList(char[] data) {

        ArrayList<Character> output = new ArrayList<>();

        for (char c : data) {

            output.add(c);
        }

        return output;
    }

    @Override
    public boolean isHalt() {

        return getActState() == HALT_STATE || currentState == STATE.HALT;
    }

    @Override
    public boolean isCrashed() {

		/*
                - außerhalb der Bänder
				- keine Transition von jetzigem State, mit gelesenem Symbol, aus vorhanden
				- State nicht vorhanden
		 */
        return currentState == STATE.CRASHED;

    }

    @Override
    public List<TMConfig> getTMConfig() {
        return tapes;
    }

    @Override
    public TMConfig getTMConfig(int tape) {
        return tapes.get(tape);
    }

    @Override
    public int getActState() {
        return state;
    }

    /**
     * Used to update the TM and change list data into arrays.
     *
     * @param transition - current Transition
     * @param left       - left of head data
     * @param below      - char below head
     * @param right      - right of head data
     */
    private void updateTM(Transition transition, ArrayList<Character> left, char below, ArrayList<Character> right) {

        char[] leftData = new char[left.size()];
        char[] rightData = new char[right.size()];

        for (int i = 0; i < leftData.length; i++) {

            leftData[i] = left.get(i);
        }

        for (int i = 0; i < rightData.length; i++) {
            rightData[i] = right.get(i);
        }

        tapes.set(transition.getTapeMove(), new TMConfig(leftData, below, rightData));

        state = transition.getToState();

        // if toState is HALT, change currentState to HALT
        if (state == HALT_STATE) {
            currentState = STATE.HALT;
        }
    }

    /**
     * Performs the current transition.
     *
     * @param transition - current transition to check whether the TM has to move left or right.
     * @param left       - data left of head
     * @param below      - char below head
     * @param right      - data right of head
     */
    private void makeTrans(Transition transition, ArrayList<Character> left, char below, ArrayList<Character> right) {

        if (transition.getMovement() == Movement.Left) {

            if (left.size() == 0) {
                currentState = STATE.CRASHED;
                return;
            }

            right.add(0, below);
            below = left.get(left.size() - 1);
            left.remove(left.size() - 1);


            right.removeIf(c -> c == '#');

        } else if (transition.getMovement() == Movement.Right) {

            if (right.isEmpty()) {
                left.add(below);
                below = '#';
            } else {
                left.add(below);
                below = right.get(0);
                right.remove(0);
            }


        }

        updateTM(transition, left, below, right);
    }

}
