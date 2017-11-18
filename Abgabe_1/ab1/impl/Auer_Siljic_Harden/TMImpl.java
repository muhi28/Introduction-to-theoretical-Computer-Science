package ab1.impl.Auer_Siljic_Harden;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ab1.TM;

public class TMImpl implements TM {


    private ArrayList<TMConfig> tapes = new ArrayList<>();
    private ArrayList<Transition> transitions = new ArrayList<Transition>();
    private Set<Character> symbols = null;
    private Set<Integer> states = new TreeSet<Integer>();
    private ArrayList<Character> left = new ArrayList<>();
    private ArrayList<Character> right = new ArrayList<>();


    private int state;
    private int numTapes;

    private STATE currentState;

    private char below;

    private enum STATE {
        HALT,
        CRASHED
    }

	@Override
	public void reset() {
        state = 0;
        numTapes = 1;

        tapes.clear();
        tapes.add(new TMConfig(new char[0], '#', new char[0]));

        states.clear();
        states.add(0);

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

        if (fromState == 0) {
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

        if (state == 0) {
            currentState = STATE.CRASHED;
            throw new IllegalStateException("Maschine befindet sich bereits im Haltezustand !!!");
        }

        boolean found = false;

        Transition trans = null;

        for (Transition transition : transitions) {

            if (transition.getFromState() == state && transition.getSymbolRead() == tapes.get(transition.getTapeRead()).getBelowHead()) {

                found = true;
                trans = transition;

                TMConfig writeTape = tapes.get(transition.getTapeWrite());

                tapes.set(transition.getTapeWrite(), writeTape);


                TMConfig moveTape = tapes.get(transition.getTapeMove());

                left = changeToList(moveTape.getLeftOfHead());
                right = changeToList(moveTape.getRightOfHead());
                below = moveTape.getBelowHead();

                makeTrans(transition.getMovement(), left, below, right);

			}
		}

        if (!found) {
            currentState = STATE.CRASHED;

            throw new IllegalStateException("Transition nicht vorhanden !!!");
        }

        updateTM(trans, left, below, right);
    }

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
    }

    private void makeTrans(Movement movement, ArrayList<Character> left, char below, ArrayList<Character> right) {

        if (movement == Movement.Left) {

            if (left.size() == 0) {
                currentState = STATE.CRASHED;
                return;
            }

            right.add(0, below);
            below = left.get(left.size() - 1);
            left.remove(left.size() - 1);

        } else if (movement == Movement.Right) {

            if (right.isEmpty()) {
                right.add(0, '#');
            }

            left.add(below);
            below = right.get(0);
            right.remove(0);
        }

    }

    private ArrayList<Character> changeToList(char[] data) {

        ArrayList<Character> output = new ArrayList<>();

        for (char c : data) {

            output.add(c);
        }

        return output;
    }

	@Override
	public boolean isHalt() {

        return getActState() == 0;
    }

	@Override
	public boolean isCrashed() {

		/*
                - außerhalb der Bänder
				- keine Transition vorhanden von State mit gelesenem Symbol bla bla blaaaaa
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

}
