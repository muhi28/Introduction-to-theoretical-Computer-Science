package ab2.impl.Auer_Harden_Siljic;

import java.util.Set;
import java.util.TreeSet;

import ab2.NFA;
import ab2.RSA;
import ab2.fa.exceptions.IllegalCharacterException;

public class NfaImpl implements NFA {

    private Set<Character> characters;
    private Set<String>[][] transitions;
    private Set<Integer> acceptingStates;
    private int initialState;

    public NfaImpl() {
        transitions = (Set<String>[][]) new TreeSet<?>[100][100];
        characters = new TreeSet<Character>();
        acceptingStates = new TreeSet<Integer>();
        initialState = 0;
    }

    public NfaImpl(Set<Character> characters, Set<String>[][] transitions, int initialState) {
        this.characters = characters;
        this.transitions = transitions;
        this.initialState = initialState;
        characters = new TreeSet<Character>();
        acceptingStates = new TreeSet<Integer>();
    }

    @Override
    public Set<Character> getSymbols() {
        return characters;
    }

    @Override
    public Set<Integer> getAcceptingStates() {
        return acceptingStates;
    }

    @Override
    public int getInitialState() {
        return initialState;
    }

    @Override
    public boolean isAcceptingState(int s) throws IllegalStateException {
        return acceptingStates.contains(s);
    }

    @Override
    public Set<String>[][] getTransitions() {
        return transitions;
    }

    @Override
    public void setTransition(int fromState, String s, int toState) throws IllegalStateException, IllegalCharacterException {
        if (fromState < 0 || fromState >= transitions.length ||
                toState < 0 || toState >= transitions[0].length) throw new IllegalStateException("ArrayIndexOutOfBounds");
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!characters.contains(c)) throw new IllegalCharacterException();
        }

        transitions[fromState][toState].add(s);
    }

    @Override
    public void clearTransitions(int fromState, String s) throws IllegalStateException {
        if (fromState < 0 || fromState >= transitions.length) throw new IllegalStateException("ArrayIndexOutOfBounds");

        for (int i=0; i<transitions[0].length; i++) {
                transitions[fromState][i].remove(s);
        }
    }

    @Override
    public Set<Integer> getNextStates(int state, String s) throws IllegalCharacterException, IllegalStateException {
        return getNextStatesStep(new TreeSet<Integer>(), state, s);
    }

    private Set<Integer> getNextStatesStep(Set<Integer> set, int state, String s) {
        for (int i = 0; i < transitions[0].length; i++) {
            if (!set.contains(i)) {
                if (!s.isEmpty() && transitions[state][i].contains(s)) {
                    set.add(i);
                    set.addAll(getNextStatesStep(set, i, ""));
                }
                if (transitions[state][i].contains("")) {
                    set.add(i);
                    set.addAll(getNextStatesStep(set, i, s));
                }
            }
        }

        return set;
    }

    @Override
    public int getNumStates() {
        int num = 0;
        for (int f = 0; f < transitions.length; f++) {
            for (int t = 0; t < transitions[0].length; t++) {
                if (transitions[f][t].size() > 0) num++;
            }
        }

        return num;
    }

    @Override
    public NFA union(NFA a) {
        Set<Character> chars = this.characters;
        chars.addAll(a.getSymbols());
        Set<String>[][] newTransitions = this.transitions;
        for (int i=0; i<a.getTransitions().length; i++) {
            for (int n=0; n<a.getTransitions()[0].length; n++) {
                newTransitions[i][n].addAll(a.getTransitions()[i][n]);
            }
        }


        return new NfaImpl(chars, newTransitions, this.initialState);
    }

    @Override
    public NFA intersection(NFA a) {
//        return complement(complement().union(complement(a)));
        return null;
    }

    @Override
    public NFA complement() {
//        return new NfaImpl().toRSA();
        return null;
    }

    @Override
    public NFA concat(NFA a) {
        Set<Character> chars = this.characters;
        chars.addAll(a.getSymbols());
        int offset = this.getNumStates();
        int aNum = a.getNumStates();
        int newSize = offset + aNum;
        Set<String>[][] newTransitions = (Set<String>[][]) new TreeSet<?>[newSize][newSize];

        for (int f = 0; f < transitions.length; f++) {
            for (int t = 0; t < transitions[0].length; t++) {
                newTransitions[f][t] = transitions[f][t];
            }
        }
        for (int f = 0; f < aNum; f++) {
            for (int t = 0; t < aNum; t++) {
                newTransitions[f+offset][t+offset] = a.getTransitions()[f][t];
            }
        }
        NfaImpl newNfa = new NfaImpl(chars, newTransitions, this.initialState);
        for (Integer fromState: this.getAcceptingStates()) {
            newNfa.setTransition(fromState, "", a.getInitialState()+offset);
        }
        newNfa.setAcceptingStates(a.getAcceptingStates());

        return newNfa;
    }

    private void setAcceptingStates(Set<Integer> states) {
        this.acceptingStates = states;
    }

    @Override
    public NFA kleeneStar() {
        NfaImpl newNfa = new NfaImpl(this.characters, this.transitions, this.initialState);
        Set<Integer> newAcceptingStates = this.acceptingStates;
        newAcceptingStates.add(initialState);
        newNfa.setAcceptingStates(newAcceptingStates);

        for (Integer state: newNfa.getAcceptingStates()) {
            newNfa.setTransition(state, "", initialState);
        }

        return newNfa;
    }

    @Override
    public NFA plus() {
        NfaImpl newNfa = new NfaImpl(this.characters, this.transitions, this.initialState);
        Set<Integer> newAcceptingStates = this.acceptingStates;
        newNfa.setAcceptingStates(newAcceptingStates);

        for (Integer state: newNfa.getAcceptingStates()) {
            newNfa.setTransition(state, "", initialState);
        }

        return newNfa;
    }

    @Override
    public NFA minus(NFA a) {
//        return complement(complement(NFA).union(a));
        return null;
    }

    @Override
    public RSA toRSA() {
        return null;
    }

    @Override
    public Boolean accepts(String w) throws IllegalCharacterException {
        char[] chars = w.toCharArray();
        for (char c: chars) {
            if (!characters.contains(c)) throw new IllegalCharacterException();
        }

        return acceptsRecursive(initialState, w);
    }

    private boolean acceptsRecursive(int s, String w) {
        String c = w.charAt(0) + "";
        String ws = w.substring(1);

        Set<Integer> states = this.getNextStates(s, c);

        if (w.isEmpty()) {
            for (Integer state: states) {
                if (acceptingStates.contains(state)) return true;
            }
            return false;
        }

        for (Integer state: states) {
            if (acceptsRecursive(state, ws)) return true;
        }
        return false;
    }

    @Override
    public Boolean acceptsNothing() {
        return pathExists(new TreeSet<Integer>(), initialState, true) == 0;
    }

    @Override
    public Boolean acceptsEpsilonOnly() {
        return pathExists(new TreeSet<Integer>(), initialState, true) == 1;
    }

    // 0: no path exists
    // 1: path exists for epsilon only
    // 2: path exists for !epsilon
    private int pathExists(Set<Integer> checked, int s, boolean epsilonOnly) {
        if (this.acceptingStates.contains(s)) {
            if (epsilonOnly) return 1;
            else return 2;
        }

        int result = 0;

        for (int t=0; t<this.transitions[0].length; t++) {
            if (checked.contains(t)) {
                continue;
            }
            checked.add(t);

            if (epsilonOnly) {
                for (String str : this.transitions[s][t]) {
                    if (!str.isEmpty()) epsilonOnly = false;
                }
            }

            result = Math.max(result, pathExists(checked, t, epsilonOnly));
            if (result == 2) { return result; }
        }

        return result;
    }

    private boolean cyclingPathExists(Set<Integer> checked, int s, boolean hasCycle) {
        if (this.acceptingStates.contains(s)) {
            return hasCycle;
        }

        boolean result = false;

        for (int t=0; t<this.transitions[0].length; t++) {
            if (checked.contains(t)) {
                hasCycle = true;
                continue;
            }
            checked.add(t);

            result = hasCycle || cyclingPathExists(checked, t, hasCycle);
            if (result) return true;
        }

        return result;
    }

    @Override
    public Boolean acceptsEpsilon() {
        return acceptsRecursive(initialState, "");
    }

    @Override
    public Boolean isInfinite() {
        return cyclingPathExists(new TreeSet<Integer>(), initialState, false);
    }

    @Override
    public Boolean isFinite() {
        return !isInfinite();
    }

    @Override
    public Boolean subSetOf(NFA a) {
        if (!a.getSymbols().containsAll(this.characters)) return false;

        // ???
        for (int f=0; f<this.transitions.length; f++) {
            for (int t=0; t<this.transitions[0].length; t++) {
                Set<String> s = this.transitions[f][t];
                if (!s.equals(a.getTransitions()[f][t])) return false;
            }
        }

        return true;
    }

    @Override
    public Boolean equalsPlusAndStar() {
        return this.plus().equals(this.kleeneStar());
    }

    @Override
    public boolean equals(Object b) {
        NFA nfa = (NFA) b;

        if (!this.characters.equals(nfa.getSymbols())) return false;

        // ???
        for (int f=0; f<this.transitions.length; f++) {
            for (int t=0; t<this.transitions[0].length; t++) {
                Set<String> s = this.transitions[f][t];
                if (!s.equals(nfa.getTransitions()[f][t])) return false;
            }
        }

        return true;

        // Holzhammermethode?
        // TODO
    }

}
