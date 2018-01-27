package ab2.impl.Auer_Harden_Siljic;

import java.util.*;

import ab2.NFA;
import ab2.RSA;
import ab2.fa.exceptions.IllegalCharacterException;
import javafx.util.Pair;

// TODO: Rename to __NFAimpl pls
public class NfaImpl implements NFA {

    private Set<Character> characters;
    private Set<String>[][] transitions;
    private Set<Integer> acceptingStates;
    private int initialState;

    private final String EPSILON = "";

    // Initializer used by factory
    public NfaImpl(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState)
    {
        this.characters = characters;
        this.acceptingStates = acceptingStates;
        this.initialState= initialState;
        this.transitions = (Set<String>[][]) new TreeSet<?>[numStates][numStates];

        // Avoid possible Nullpointer
        for (int i = 0; i < this.transitions.length; i++) {
            for (int j = 0; j < this.transitions[i].length; j++) {
                this.transitions[i][j] = new TreeSet<String>();
            }
        }
    }

    // Constructor used for kleeneStar -- FIXME
    public NfaImpl(Set<Character> characters, Set<String>[][] transitions, int initialState) {
        this.characters = characters;
        this.transitions = transitions;
        this.initialState = initialState;

        // Missing accepting states added
        acceptingStates = this.getAcceptingStates();
    }

    private void setTransitions(Set<String>[][] transitions) {
        this.transitions = transitions;
    }

    public void setSymbols(Set<Character> characters) {
        this.characters = characters;
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
        if (s < 0 || s > transitions.length) throw new IllegalStateException();
        return acceptingStates.contains(s);
    }

    @Override
    public Set<String>[][] getTransitions() {
        return transitions;
    }

    @Override
    public void setTransition(int fromState, String s, int toState) throws IllegalStateException, IllegalCharacterException {
        // Exceptionhandling :: IllegalStateException
        if (fromState < 0 || fromState > transitions.length ||
                toState < 0 || toState > transitions[0].length) throw new IllegalStateException("ArrayIndexOutOfBounds");
        // Exceptionhandling :: IllegalCharacterException
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!characters.contains(c)) throw new IllegalCharacterException();
        }
        //Redundant check on null-sets within transitions
        //  if(transitions[fromState][toState] == null){
        //      transitions[fromState][toState] = new TreeSet<String>();
        //  }
        transitions[fromState][toState].add(s);
    }

    @Override
    public void clearTransitions(int fromState, String s) throws IllegalStateException {
        // Exceptionhandling :: IllegalStateException
        if (fromState < 0 || fromState >= transitions.length) throw new IllegalStateException("ArrayIndexOutOfBounds");

        // Clear S from each state
        for (int i=0; i<transitions[0].length; i++) {
            //Redundant check on null-sets
            //   if (transitions[fromState][i] == null) continue;
            transitions[fromState][i].remove(s);
        }
    }

    @Override
    public Set<Integer> getNextStates(int state, String s) throws IllegalCharacterException, IllegalStateException {
        // Exceptionhandling :: IllegalChar
        if(state < getNumStates() || state > getNumStates()) { throw new IllegalStateException("Error in NFAImpl::getNextStates");}

        // TODO  -- Exceptionhandling :: IllegalState

        return getNextStatesStep(new TreeSet<Integer>(), state, s);
    }

    /** Liefert Set aus Zuständen zurück die mit S erreichbar sind
     * @param set      = Recursive filled set containing all reachable states
     * @param state    = State to check
     * @param s        = Full String to check || Epsilon to check
     * @return set
     */
    private Set<Integer> getNextStatesStep(Set<Integer> set, int state, String s) {
        for (int i = 0; i < transitions[0].length; i++) {                       // For each fromState
            if (!set.contains(i)) {
                if (!s.isEmpty() && transitions[state][i].contains(s)) {        // There is a transition so that fromstate->tostate using S
                        set.add(i);                                             //  Add to set
                        set.addAll(getNextStatesStep(set, i, ""));           //   Repeat using epsilon-paths
                }
                if (transitions[state][i].contains("")) {                       // fromstate->tostate using epsilon
                        set.add(i);
                        set.addAll(getNextStatesStep(set, i, s));
                }
            }
        }
        return set;
    }

    @Override
    public int getNumStates() {
        return transitions.length;
    }

    @Override
    public NFA union(NFA a) {
        //// Setup new NFA.
        // @par:: Characters
        Set<Character> chars = this.characters;
        chars.addAll(a.getSymbols());

        int nStates = this.getNumStates() + a.getNumStates() + 1;

        // @par: transitions
        // Create new array of transitions containing both NFAs and starting state S
        Set<String>[][] newTransitions = (Set<String>[][]) new TreeSet<?>[nStates][nStates];
        // Avoid possible Nullpointers
        for (int i = 0; i < newTransitions.length; i++) {
            for (int j = 0; j < newTransitions[i].length; j++) {
                newTransitions[i][j] = new TreeSet<String>();
            }
        }
        // add S (state connecting both NFAs via epsilon)
        newTransitions[0][1].add(EPSILON);
        newTransitions[0][this.getNumStates() + 1].add(EPSILON); // FIXME possible off-by-one error

        // add transitions of this NFA
        for (int i=0; i<this.getNumStates(); i++) {
            for (int n=0; n<this.getNumStates(); n++) {
                newTransitions[i+1][n+1] = this.transitions[i][n];
            }
        }
//        System.out.println("n1: "+this.getNumStates()+", a: "+a.getNumStates());

        // add transitions of NFA a
        int ii = 0, nn = 0; // second pair to offet the offset
        for (int i=this.getNumStates()+1; i<a.getTransitions().length; i++) {
            for (int n=this.getNumStates()+1; n<a.getTransitions()[0].length; n++) {
//                System.out.println("i: "+i+", n: "+n);
                newTransitions[i][n].addAll(a.getTransitions()[ii][nn]);
                nn++;
            }
            nn = 0;
            ii++;
        }

        // @par: acceptingStates
        Set<Integer> newSet = new TreeSet<>();
        newSet.addAll(this.getAcceptingStates());
        newSet.addAll(a.getAcceptingStates());

        // @par: initialstate

        // FIXME: Return is object to change
        // Standardbuilder
        NfaImpl resultingNfa = new NfaImpl(newTransitions.length, chars, newSet, 0);
        resultingNfa.setTransitions(newTransitions);
        return resultingNfa;
        //  Old returns -- old constructors
        //return new NfaImpl(chars, newTransitions, this.initialState);
        //return new NfaImpl(this.getNumStates() + a.getNumStates(), chars, newSet, this.initialState);

    }

    @Override
    public NFA intersection(NFA a) {
        return (this.complement().union(a.complement())).complement();
    }

    @Override
    public NFA complement() {
        // FIXME: Not fully implemented
        return this.toRSA();
    }

    @Override
    public NFA concat(NFA a) {
        // New NFA
        // @par NumStates
        int offset = this.getNumStates();
        int aNum = a.getNumStates();
        int newSize = offset + aNum;

        // @par Characters
        Set<Character> chars = this.characters;
        chars.addAll(a.getSymbols());

        // @par transitions
        Set<String>[][] newTransitions = (Set<String>[][]) new TreeSet<?>[newSize][newSize];
        for (int f = 0; f < transitions.length; f++) {
            for (int t = 0; t < transitions[0].length; t++) {
                newTransitions[f][t] = transitions[f][t];
            }
        }
        for (int f = 0; f < a.getTransitions().length; f++) {
            for (int t = 0; t < a.getTransitions()[0].length; t++) {
                newTransitions[f+offset][t+offset] = a.getTransitions()[f][t];
            }
        }

        NfaImpl newNfa = new NfaImpl(newSize, chars, a.getAcceptingStates(), this.getInitialState());

        //NfaImpl newNfa = new NfaImpl(chars, newTransitions, this.initialState);

        // Setup Concat of T1 and T2 -- Acceptors of T1 point to Starters of T2
        for (Integer fromState: this.getAcceptingStates()) {
            newNfa.setTransition(fromState, "", a.getInitialState()+offset);
        }

        // Dont refactor this. its a fucking pain
        Integer[] acc = new Integer[a.getAcceptingStates().size()]; // SET<Integer>
        a.getAcceptingStates().toArray(acc);
        for (int i = 0; i < acc.length; i++) {
            acc[i] += offset;
        }

        newNfa.setAcceptingStates(new TreeSet<Integer>(Arrays.asList(acc)));
        return newNfa;
    }

    /** Adds states to accepting states
     * @param states
     */
    private void setAcceptingStates(Set<Integer> states) {
        this.acceptingStates = states;
    }

    @Override
    public NFA kleeneStar() {
        //NfaImpl newNfa = new NfaImpl(this.getNumStates(), this.characters, this.acceptingStates, this.initialState);
        // FIXME: Do we lose anything using 2nd Constructor?
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
        Set<CompoundState> newStates = new TreeSet<>();
        CompoundState state;
        state = new CompoundState(new TreeSet<>(this.initialState));
        newStates.add(state);
        Iterator iter = newStates.iterator();

        Set<Integer> newAcceptingStates = new TreeSet<>();

//        Set<Integer>[] nextStatesFor = new <Set<Integer>>[this.getNumStates()];
        Set<CompoundState> processedStates = new TreeSet<>();
//        Set<Integer> remainingStates = new TreeSet<>();

        while(processedStates.size() < this.getNumStates()) {
            state = (CompoundState)iter.next();
            for (Character c : this.characters) {
                Set<Integer> next = new TreeSet<>();
                boolean isAcceptingState = false;
                for (Integer segment: state.thisState) {
                    next.addAll(this.getNextStates(segment, "" + c));
                }
                // create next state in RSA that's reachable from current state and check if it's accepting
                CompoundState nextCs = new CompoundState(next);
                for (Integer j: next) {
                    if (this.getAcceptingStates().contains(j)) {
                        isAcceptingState = true;
                        break;
                    }
                }
                nextCs.isAcceptingState = isAcceptingState;
                // now add new RSA state to newStates
                state.nextStates.add(new Pair<>(""+c, nextCs));
                newStates.add(nextCs);
            }
            processedStates.add(state);
        }

        Set<String>[][] rsaTransitions = (Set<String>[][]) new TreeSet<?>[newStates.size()][newStates.size()];
        for (int i = 0; i < rsaTransitions.length; i++) {
            for (int n = 0; n < rsaTransitions[0].length; n++) {
                rsaTransitions[i][n] = new TreeSet<>();
            }
        }

        CompoundState[] newStatesArr = (CompoundState[]) newStates.toArray();
        for (int i=0; i<newStates.size() - 1; i++) {
//        for (CompoundState cs: new) {
            for (Pair<String, CompoundState> next: newStatesArr[i].nextStates) {
                // extract transition symbol and next state
                String sym = next.getKey();
                CompoundState nextState = next.getValue();
                // find index of next state
                int nextIdx = -1;
                for (int k = 0; k<newStatesArr.length; k++) {
                    if (newStatesArr[k].equals(nextState)) {
                        nextIdx = k;
                        break;
                    }
                }
                if (nextIdx == -1) System.out.println("SCHEISSE");

                // set transition between the 2 RSA states
                rsaTransitions[i][nextIdx].add(sym);

                // maybe register current RSA state as accepting state
                if(newStatesArr[i].isAcceptingState) newAcceptingStates.add(i);
            }
        }



////        Set<String>[][] newTransitions
//
//        ArrayList<Integer> newStates = new ArrayList<>();
//        newStates.add(this.initialState);
//        int tsize = this.getNumStates() * 10;
//        Set<String>[][] newTransitions = (Set<String>[][]) new TreeSet<?>[tsize][tsize];
//
//        int state = 0;
//        Set<Integer>[] nextStatesFor = new <Set<Integer>>[this.getNumStates()];
//        Set<Integer> processedStates = new TreeSet<>();
//        Set<Integer> remainingStates = new TreeSet<>();
//        remainingStates.add(this.initialState);
//
//        while(processedStates.size() < this.getNumStates()) {
//            state = remainingStates.iterator().next();
//            for (Character c : this.characters) {
//                nextStatesFor[state] = this.getNextStates(state, "" + c);
//                remainingStates.addAll(nextStatesFor[state]);
//            }
//            processedStates.add(state);
//            remainingStates.remove(state);
//        }
//
////
////        for (int s = 0; s < this.getNumStates(); s++) {
////            for (Character c: this.characters) {
////
////            }
////        }

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
        String c = "";
        String ws = "";
        if (!w.equals("")) {
            c = String.valueOf(w.charAt(0));
            ws = w.substring(1);
        }

        Set<Integer> states = this.getNextStates(s, c);

        if (w.isEmpty()) {
            for (Integer state: states) {
                if (acceptingStates.contains(state)) {
                    return true;
                }
            }
            return false;
        }
        for (Integer state: states) {
            if (acceptsRecursive(state, ws)) {
                System.out.println("Echo::Case2.1");
                return true;
            }
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

    /**Checks whether T contains some infinite loop
     * @param checked
     * @param s
     * @param hasCycle
     * @return
     */
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
        return !isInfinite();   // BOIIIIIIIIIIIIIIII
    }

    @Override
    public Boolean subSetOf(NFA a) {
        // Check :: Alphabet != same -> Error
        if (!a.getSymbols().containsAll(this.characters)) return false;

        // Check :: Transitions
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
        // Check :: Alphabet != same -> Error
        if (!this.characters.equals(nfa.getSymbols())) return false;

        // Check :: Transitions
        // FIXME: Better check on substrings, not whole words
        for (int f=0; f<this.transitions.length; f++) {
            for (int t=0; t<this.transitions[0].length; t++) {
                Set<String> s = this.transitions[f][t];
                if (!s.equals(nfa.getTransitions()[f][t])) return false;
            }
        }
        return true;
        // TODO Holzhammermethode?
    }
}
