package ab1.impl.Auer_Siljic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ab1.TM;

public class TMImpl implements TM {

    private ArrayList<ArrayList<Character>> tapes = new ArrayList<ArrayList<Character>>();
    private ArrayList<Transition> transitions = new ArrayList<Transition>();
    private Set<Character> symbols = null;
    private Set<Integer> states = new TreeSet<Integer>();

    private int head = 0;
    private int state = 0;
    private int tape = 0;

	@Override
	public void reset() {
	    head = 0;
	    state = 0;
		tape = 0;
	    tapes.clear();
	    tapes.add(new ArrayList<Character>());
	    states.clear();
	    states.add(0);
	    transitions.clear();
	}

	@Override
	public void setNumberOfTapes(int numTapes) throws IllegalArgumentException {
	    if (numTapes < 1) throw new IllegalArgumentException();

	    if (tapes.size() < numTapes) {
			for (int i=0; i < tapes.size() - numTapes; i++) {
				tapes.remove(0);
			}
		} else {
			for (int i=0; i < numTapes-tapes.size(); i++) {
				tapes.add(new ArrayList<Character>());
			}
		}
	}

	@Override
	public void setSymbols(Set<Character> symbols) throws IllegalArgumentException {
	    if (!symbols.contains('#')) throw new IllegalArgumentException();

	    this.symbols = symbols;
	}

	@Override
	public Set<Character> getSymbols() {
		return this.symbols;
	}

	@Override
	public void addTransition(int tape, int fromState, char symbolRead, int toState, char symbolWrite,
			Movement movement) throws IllegalArgumentException {
	    for (Transition t: transitions) {
	    	if (!t.checkTransition(fromState, symbolRead)) throw new IllegalArgumentException();
		}
	    if (fromState == 0 && symbolRead != 0 ||
				!symbols.contains(symbolRead) ||
				!symbols.contains(symbolWrite) ||
				tape >= tapes.size()) throw new IllegalArgumentException();

	    states.add(toState);

	    transitions.add(new Transition(tape, fromState, toState, symbolRead, symbolWrite, movement));
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
		tapes.get(tape).clear();
		for (int i=0; i<content.length; i++) {
			tapes.get(tape).add(content[i]);
		}
	}

	@Override
	public void doNextStep() throws IllegalStateException {
	    if (state == 0) throw new IllegalStateException();

	    char symbol = tapes.get(tape).get(head);

	    Transition transition = null;
	    for (Transition t: transitions) {
	    	if (t.getFromState() == state &&
					t.getSymbolRead() == symbol) {
				transition = t;
				break;
			}
		}

		if (transition == null) throw new IllegalStateException();

		state = transition.getToState();
		if (transition.getSymbolWrite() != 0)
			tapes.get(tape).set(head, transition.getSymbolWrite());
		if (transition.getMovement() == Movement.Left) head--;
		if (transition.getMovement() == Movement.Right) head++;

		if (head < 0) throw new IllegalStateException();
	}

	@Override
	public boolean isHalt() {
		return state == 0;
	}

	@Override
	public boolean isCrashed() {
		return head >= tapes.get(tape).size() || !states.contains(state);
	}

	@Override
	public List<TMConfig> getTMConfig() {
	    List<TMConfig> list = new ArrayList<TMConfig>();

	    for (int i=0; i<tapes.size(); i++) {
			list.addAll( getTMConfig(i) );
		}

		return this.isCrashed() ? null : list;
	}

	@Override
	public List<TMConfig> getTMConfig(int tape) {
        char[] left = new char[head-1];
        for (int i=0; i<left.length; i++) {
            left[i] = tapes.get(tape).get(i);
        }
        char headChar = tapes.get(tape).get(head);
        char[] right = new char[tapes.get(tape).size()-head];
        for (int i=0; i<right.length; i++) {
            right[i] = tapes.get(tape).get(head + 1 + i); //FIXME Uberlauf wegen +1?
        }

		List<TMConfig> list = new ArrayList<TMConfig>();
        TMConfig config = new TMConfig(left, headChar, right);
		list.add(config);

		return this.isCrashed() ? null : list;
	}

}
