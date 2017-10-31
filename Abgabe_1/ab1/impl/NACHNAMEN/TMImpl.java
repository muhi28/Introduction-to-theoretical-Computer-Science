package ab1.impl.NACHNAMEN;

import java.util.List;
import java.util.Set;

import ab1.TM;

public class TMImpl implements TM {

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNumberOfTapes(int numTapes) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSymbols(Set<Character> symbols) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Character> getSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTransition(int tape, int fromState, char symbolRead, int toState, char symbolWrite,
			Movement movement) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Integer> getStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfTapes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setInitialState(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialTapeContent(int tape, char[] content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doNextStep() throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isHalt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCrashed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<TMConfig> getTMConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TMConfig> getTMConfig(int tape) {
		// TODO Auto-generated method stub
		return null;
	}

}