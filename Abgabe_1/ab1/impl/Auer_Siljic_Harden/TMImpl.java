package ab1.impl.Auer_Siljic_Harden;

import java.util.*;

import ab1.TM;

public class TMImpl implements TM {

    private ArrayList<TMConfig> tapes = new ArrayList<>();
    private ArrayList<Transition> transitions = new ArrayList<>();
    private Set<Character> symbols = null;
    private Set<Integer> states = new TreeSet<>();

    private int state;

    private TMConfig createEmptyTape() {
        return new TMConfig(new char[0], '#', new char[0]);
    }

	@Override
	public void reset() {
        state = 0;

        tapes.clear();
        tapes.add(createEmptyTape());

        states.clear();
        states.add(0);

        symbols.clear();
        transitions.clear();
    }

    @Override
    public int getActState() {
        return state;
    }

	@Override
	public void setNumberOfTapes(int numTapes) throws IllegalArgumentException {
        if (numTapes < 1) throw new IllegalArgumentException("Anzahl an Bändern zu klein.");

        while (tapes.size() < numTapes) {
            tapes.add(createEmptyTape());
        }
	}

	@Override
	public void setSymbols(Set<Character> symbols) throws IllegalArgumentException {
        if (!(symbols.contains('#'))) throw new IllegalArgumentException("Set der Symbole beinhaltet kein '#'");

        this.symbols = symbols;
    }

	@Override
	public Set<Character> getSymbols() {
        return symbols;
    }

	@Override
    public void addTransition(int fromState, int tapeRead, char symbolRead,
                              int toState, int tapeWrite, char symbolWrite,
                              int tapeMove, Movement movement) throws IllegalArgumentException {

        if (fromState == 0) {
            throw new IllegalArgumentException("Kein Übergang von Haltezustand aus möglich!");
        }

        transitions.add(
                new Transition(fromState, tapeRead, symbolRead,
                        toState, tapeWrite, symbolWrite,
                        tapeMove, movement));

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
        tapes.set(tape, new TMConfig(content, '#', new char[0]));
    }


	@Override
	public void doNextStep() throws IllegalStateException {
        System.out.println("current state: "+state);
        System.out.println("config(0): "+ Arrays.toString(tapes.get(0).getLeftOfHead())+" | "+
                        tapes.get(0).getBelowHead()+" | "+
                        Arrays.toString(tapes.get(0).getRightOfHead()));
        if (tapes.size() > 1) {
            System.out.println("config(1): " + Arrays.toString(tapes.get(1).getLeftOfHead()) + " | " +
                    tapes.get(1).getBelowHead() + " | " +
                    Arrays.toString(tapes.get(1).getRightOfHead()));
        }

        if (state <= 0) {
            throw new IllegalStateException("Maschine befindet sich bereits im Haltezustand.");
        }

        boolean found = false;

        for (Transition transition: transitions) {

            if (transition.getFromState() == state &&
                    transition.getSymbolRead() == tapes.get(transition.getTapeRead()).getBelowHead()) {

                found = true;

                TMConfig writeConfig = tapes.get(transition.getTapeWrite());
                TMConfig newConfig = new TMConfig(writeConfig.getLeftOfHead(),
                                            transition.getSymbolWrite(), // change symbol below head
                                            writeConfig.getRightOfHead());

                tapes.set(transition.getTapeWrite(), newConfig);

                // movement
                TMConfig moveTape = tapes.get(transition.getTapeMove());

                char[] left = moveTape.getLeftOfHead();
                char head = moveTape.getBelowHead();
                char[] right = moveTape.getRightOfHead();

                TMConfig movedTape = processMovement(left, head, right, transition.getMovement());
                tapes.set(transition.getTapeMove(), movedTape);

                // update state
                state = transition.getToState();
                System.out.println("new state: "+state);
                System.out.println("==========================================================");

                break;
            }
		}

        if (!found) {
            throw new IllegalStateException("Transition nicht vorhanden!");
        }
    }

    private TMConfig processMovement(char[] left, char head, char[] right, Movement movement) {
        if (movement == Movement.Stay) return new TMConfig(left, head, right);

        char[] newLeft = null;
        char[] newRight = null;
        char newHead = '#';
        if (movement == Movement.Left) {
            // check tape bounds
            if (left.length == 0) {
                state = -1;
                return new TMConfig(left, head, right);
            }

            newLeft = new char[left.length-1];
            if (head != '#' || right.length > 0) newRight = new char[right.length+1];
            else newRight = right; // blanks get ignored instead of filled into right
            newHead = left[left.length-1];

            for (int i=0; i<newLeft.length; i++) {
                newLeft[i] = left[i];
            }
            if (head != '#' || right.length > 0) {
                for (int i = 0; i < right.length; i++) {
                    newRight[i + 1] = right[i];
                }
                newRight[0] = head;
            }
        }
        if (movement == Movement.Right) {
            // check tape bounds
            if (right.length == 0) {
                right = new char[]{'#'};
            }

            newLeft = new char[left.length+1];
            newRight = new char[right.length-1];
            newHead = right[0];

            for (int i=0; i<left.length; i++) {
                newLeft[i] = left[i];
            }
            newLeft[left.length] = head;
            for (int i=0; i<newRight.length; i++) {
                newRight[i] = right[i+1];
            }
        }

        return new TMConfig(newLeft, newHead, newRight);
    }

	@Override
	public boolean isHalt() {
        if (getActState() == 0) {
            System.out.println("\n\n");
        }

        return getActState() == 0;
    }

	@Override
	public boolean isCrashed() {
        return getActState() < 0 || !states.contains(state);
	}

	@Override
	public List<TMConfig> getTMConfig() {
        if (isCrashed()) return null;
        else return tapes;
    }

	@Override
	public TMConfig getTMConfig(int tape) {
        if (isCrashed()) return null;
        else return tapes.get(tape);
    }

}
