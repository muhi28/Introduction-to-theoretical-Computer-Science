package ab2.impl.Auer_Harden_Siljic;

import java.util.Set;

import ab2.FAFactory;
import ab2.DFA;
import ab2.NFA;
import ab2.RSA;

public class FAFactoryImpl implements FAFactory {
	@Override
	public NFA createNFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
		return new NfaImpl(numStates, characters, acceptingStates, initialState);
	}

	@Override
	public DFA createDFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
		// TODO Auto-generated method stub
		return null;//new DFA(numStates, characters, acceptingStates, initialState);
	}

	@Override
	public RSA createRSA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
		// TODO Auto-generated method stub
		return null;//new RSA(numStates, characters, acceptingStates, initialState);
	}

	@Override
	public RSA createPatternMatcher(String pattern) {
		// TODO Auto-generated method stub
		return null;
	}
}
