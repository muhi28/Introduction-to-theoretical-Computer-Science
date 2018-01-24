package ab2.impl.Auer_Harden_Siljic;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

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


		return new __DFAimpl(numStates, characters, acceptingStates, initialState); //new DFA(numStates, characters, acceptingStates, initialState);
	}

	@Override
	public RSA createRSA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
		// TODO Auto-generated method stub
		return new __RSAimpl(numStates, characters, acceptingStates, initialState);//new RSA(numStates, characters, acceptingStates, initialState);
	}

	@Override
	public RSA createPatternMatcher(String pattern) {
		ArrayList<PatternExpr> expressions = new ArrayList<>();
		Set<Character> symbols = new TreeSet<>();

		// take pattern apart
		char[] chars = pattern.toCharArray();
		for (int i=0; i<chars.length; i++) {
			if (chars[i] == '.') expressions.add(PatternExpr.newWildcard());
			if (chars[i] == '*') continue;
			else {
				// add character to alphabet
				symbols.add(chars[i]);
				// check for kleene star
				boolean kleene = false;
				if (i < chars.length-1 && chars[i+1] == '*') kleene = true;

				expressions.add(new PatternExpr(chars[i]+"", kleene));
			}
		}

		// init automaton
		// TODO test/check arguments for correctness/off-by-one-errors
		Set<Integer> accs = new TreeSet<>();
		accs.add(expressions.size()-1);
        RSA rsa = createRSA(expressions.size(), symbols, accs, 0);

        int state = 0;

        // build automaton
		for (PatternExpr expr: expressions) {
			// in case of wildcard, add a transition for every symbol in the alphabet
			if (expr.isWildcard()) {
				for (Character symbol: symbols) {
					rsa.setTransition(state, symbol, state+1);
				}
			} else if (expr.isKleene()) { // add a cycle
				rsa.setTransition(state, expr.getStr(), state);

				// link back to beginning for any unexpected symbol(see interface)
				for (Character symbol: symbols) {
					if (expr.getStr() != symbol+"")
						rsa.setTransition(state, symbol, 0);
				}
			} else { // add normal transition
				rsa.setTransition(state, expr.getStr(), state+1);

				// link back to beginning for any unexpected symbol(see interface)
				for (Character symbol: symbols) {
					if (expr.getStr() != symbol+"")
						rsa.setTransition(state, symbol, 0);
				}
			}

			state++;
		}

		return null;
	}
}
