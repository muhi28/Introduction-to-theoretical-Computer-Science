package ab2.impl.Auer_Harden_Siljic;

import java.util.*;

import ab2.NFA;
import ab2.RSA;
import ab2.fa.exceptions.IllegalCharacterException;
import javafx.util.Pair;

// TODO: Rename to __NFAimpl pls
public class NfaImpl implements NFA {

    public boolean pat;
    public int patc;
    public int patcc;

    private Set<Character> characters;
    private Set<String>[][] transitions;
    private Set<Integer> acceptingStates;
    private int initialState;

    public final String EPSILON = "";

    // Initializer used by factory
    public NfaImpl(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {

        this.characters = characters;
        this.acceptingStates = acceptingStates;
        this.initialState= initialState;
        this.transitions = (Set<String>[][]) new TreeSet<?>[numStates][numStates];

        // Avoid possible Nullpointer
        for (int i = 0; i < this.transitions.length; i++) {
            for (int j = 0; j < this.transitions[i].length; j++) {
                this.transitions[i][j] = new TreeSet<>();
            }
        }
    }

    // Constructor used for kleeneStar
    public NfaImpl(Set<Character> characters, Set<String>[][] transitions, int initialState) {
        this.characters = characters;
        this.transitions = transitions;
        this.initialState = initialState;

        // Missing accepting states added
        acceptingStates = this.getAcceptingStates();
    }

    protected void setTransitions(Set<String>[][] transitions) {
        this.transitions = transitions;
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
        if (fromState < 0 || fromState > transitions.length ||
                toState < 0 || toState > transitions[0].length) throw new IllegalStateException("ArrayIndexOutOfBounds");

        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!characters.contains(c)) throw new IllegalCharacterException();
        }
        transitions[fromState][toState].add(s);
    }

    @Override
    public void clearTransitions(int fromState, String s) throws IllegalStateException {
        if (fromState < 0 || fromState >= transitions.length) throw new IllegalStateException("ArrayIndexOutOfBounds");

        // Clear S from each state
        for (int i=0; i<transitions[0].length; i++) {
            transitions[fromState][i].remove(s);
        }
    }

    @Override
    public Set<Integer> getNextStates(int state, String s) throws IllegalCharacterException, IllegalStateException {
        if (state < 0 || state >= this.getNumStates()) throw new IllegalStateException("geta nita");
        if (s.length() > 0 && !this.characters.contains(s.toCharArray()[0])) throw new IllegalCharacterException();

        return getNextStatesStep(new ArrayList<>(), new TreeSet<>(), state, s, new TreeSet<>());
    }

    // FIXME check if this is really correct(should avoid endless recursions now though)
    private Set<Integer> getNextStatesStep(ArrayList<Integer> history, Set<Integer> results, int state, String s, Set<Integer> block) {
        // add this state to history
        history.add(state);
        if (s.length() == 0) {
            results.add(state);
        }

        // shuffle order of next states to avoid endless recursions
        // if it's stupid but it works...
        ArrayList<Integer> nexts = new ArrayList<>();
        for (int to = 0; to < getNumStates(); to++) nexts.add(to);
        long seed = System.nanoTime();
        Collections.shuffle(nexts, new Random(seed));

        ArrayList<Integer> nextsE = new ArrayList<>();
        for (int i=0; i<nexts.size(); i++) {
            if (transitions[state][i].contains(EPSILON)) {
                nexts.remove(i);
                nextsE.add(i);
            }
        }
        nexts.addAll(nextsE);



        for (Integer to: nexts) {
            // ignore next states that were already visited in this branch if word was already read completely
            if (history.contains(to) && s.length()==0) {// || block.contains(to)) {
                continue;
            }

            for (String sym: transitions[state][to]) {
                // if this transition is possible with current s
                if (s.startsWith(sym)) {
                    // remove this transition's symbol(s) from s and continue recursion with remaining part
                    results.addAll(getNextStatesStep(history, results, to, s.substring(sym.length()), block));
                }
            }
        }

        return results;
    }

    @Override
    public int getNumStates() {
        return transitions.length;
    }

    @Override
    public NFA union(NFA a) {
        // Setup new NFA.
        Set<Character> allChars = this.characters;
        allChars.addAll(a.getSymbols());

        int offset = this.getNumStates() + 1;

        int allStates = a.getNumStates() + offset;

        Set<String>[][] allTransitions = (Set<String>[][]) new TreeSet<?>[allStates][allStates];
        // fill with empty sets to avoid possible Nullpointers
        for (int i = 0; i < allTransitions.length; i++) {
            for (int j = 0; j < allTransitions[i].length; j++) {
                allTransitions[i][j] = new TreeSet<>();
            }
        }
        // add S (state connecting both NFAs via epsilon)
        allTransitions[0][1].add(EPSILON);
        allTransitions[0][offset].add(EPSILON); // FIXME possible off-by-one error?

        // add transitions of this NFA
        for (int i=0; i<this.getNumStates(); i++) {
            for (int n=0; n<this.getNumStates(); n++) {
                allTransitions[i+1][n+1] = this.transitions[i][n];
            }
        }
        // add transitions of NFA a
        int ii = 0, nn = 0; // second pair to offet the offset
        for (int i=offset; i<a.getTransitions().length+offset; i++) {
            for (int n=offset; n<a.getTransitions()[0].length+offset; n++) {
//                System.out.println("i: "+i+", n: "+n);
                allTransitions[i][n].addAll(a.getTransitions()[ii][nn]);
                nn++;
            }
            nn = 0;
            ii++;
        }

        // offset accepting states of this NFA so they're correct in union'ed NFA
        Set<Integer> thisOffsetAcceptingState = new TreeSet<>();
        for (Integer as: this.getAcceptingStates()) {
            thisOffsetAcceptingState.add(as+1);
        }
        // offset accepting states of NFA a so they're correct in union'ed NFA
        Set<Integer> aOffsetAcceptingState = new TreeSet<>();
        for (Integer as: a.getAcceptingStates()) {
            aOffsetAcceptingState.add(as+offset);
        }
        // merge all accepting states
        Set<Integer> allAcceptingStates = new TreeSet<>();
        allAcceptingStates.addAll(thisOffsetAcceptingState);
        allAcceptingStates.addAll(aOffsetAcceptingState);

        NfaImpl resultingNfa = new NfaImpl(allTransitions.length, allChars, allAcceptingStates, 0);
        // set all transitions individually to ensure correct graph generation
        for (int from = 0; from < allTransitions.length; from++) {
            for (int to = 0; to < allTransitions[0].length; to++) {
                if (allTransitions[from][to].size() > 0) {
                    for (String sym: allTransitions[from][to]) {
                        resultingNfa.setTransition(from, sym, to);
                    }
                }
            }
        }
        // return union
        return resultingNfa;

    }

    @Override
    public NFA intersection(NFA a) {
        return (this.complement().union(a.complement()));
    }

    @Override
    public NFA complement() {
        // calling with true tells toRSA to also switch accepting states with non-accepting states
        // so this one line converts the NFA to an inverted RSA
        return toRSA(true);
    }

    @Override
    public NFA concat(NFA a) {
        // New NFA
        int offset = this.getNumStates();
        int aNum = a.getNumStates();
        int newSize = offset + aNum;

        Set<Character> chars = this.characters;
        chars.addAll(a.getSymbols());

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

        newNfa.setAcceptingStates(new TreeSet<>(Arrays.asList(acc)));
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
        return this.complement().union(a).complement();
    }

    private Pair<Set<String>[][], Set<Integer>> getAtomicTransitions() {
        // create new transition table for atomic transitions
        int newSize = getNumStates() * 10; // just to be sure
        Set<String>[][] atomicTransitions = (Set<String>[][]) new TreeSet<?>[newSize][newSize];
        for (int from = 0; from < newSize; from++) {
            for (int to = 0; to < newSize; to++) {
                atomicTransitions[from][to] = new TreeSet<>();
            }
        }

        // create atomic transitions
        int nextState = 0;
        int currentState = 0;
        int currentAtomicState = 0;
//        int jumpToState = 0;
        ArrayList<Pair<Integer, Integer>> stateMap = new ArrayList<>();
        stateMap.add(new Pair<>(0, 0));
        // remap accepting states
        Set<Integer> atomicAcceptingStates = new TreeSet<>();

        int stateCount = 1;

        while (true) {
            for (int to = 0; to < getNumStates(); to++) {

                ArrayList<Pair<Integer, Integer>> remaps = new ArrayList<>();
                for (Pair<Integer, Integer> mapping : stateMap) {
                    if (mapping.getKey() == currentState) {
                        currentAtomicState = mapping.getValue();
                        remaps.add(mapping);
                    }
                }

                do {
                    for (Pair<Integer, Integer> mapping : remaps) {
                        if (mapping.getKey() == currentState) {
                            currentAtomicState = mapping.getValue();
                            break;
                        }
                    }
                    if (remaps.size() > 0) remaps.remove(0);

                    // look for non-atomic transitions
                    for (String str : transitions[currentState][to]) {
                        // if already atomic transition, transfer directly to new table
                        if (str.length() <= 1) {
                            boolean mapped = false;
                            for (Pair<Integer, Integer> pair : stateMap) {
                                if (pair.getKey() == to) {
                                    atomicTransitions[currentAtomicState][pair.getValue()].add(str);

                                    stateCount++;
                                    currentAtomicState = pair.getValue();

                                    if (getAcceptingStates().contains(to)) {
                                        atomicAcceptingStates.add(pair.getValue());
                                    }

                                    mapped = true;
                                    break;
                                }
                            }
                            if (!mapped) {
                                atomicTransitions[currentAtomicState][++nextState].add(str);
                                stateCount++;

                                if (getAcceptingStates().contains(to)) {
                                    atomicAcceptingStates.add(nextState);
                                }

                                currentAtomicState = nextState;
                                // add mapping if not already existing
                                boolean alreadyMapped = false;
                                for (Pair<Integer, Integer> mapping : stateMap) {
                                    if (mapping.getKey() == to) {
                                        alreadyMapped = true;
                                        break;
                                    }
                                }
                                if (!alreadyMapped) stateMap.add(new Pair<>(to, currentAtomicState));
                            }
                        } else { // non-atomic transition
                            // split up into multiple one-character transitions
                            char[] chars = str.toCharArray();
                            for (int ci = 0; ci < chars.length; ci++) {
                                String s = chars[ci] + "";

                                boolean dup = false;
                                // look for identical transition, if one was found reuse it
                                for (int k = 0; k < newSize; k++) {
                                    if (atomicTransitions[currentAtomicState][k].contains(s)) {
                                        currentAtomicState = k; // follow existing transition
                                        dup = true;
                                        break;
                                    }
                                }
                                // if no identical transition, create the required one
                                if (!dup) {
                                    atomicTransitions[currentAtomicState][++nextState].add(s);
                                    stateCount++;
                                    currentAtomicState = nextState;
                                }
                            }
                            // add mapping
                            stateMap.add(new Pair<>(to, currentAtomicState));

                            if (getAcceptingStates().contains(to)) {
                                atomicAcceptingStates.add(currentAtomicState);
                            }

                            currentAtomicState = currentState;
                        }
                    }

                } while(remaps.size() > 0);
            }
            currentState++;
            if (currentState == getNumStates()) break;
        }

        // trim transition table
        Set<String>[][] finalTransitions = (Set<String>[][]) new TreeSet<?>[stateCount][stateCount];
        for (int from = 0; from < stateCount; from++) {
            for (int to = 0; to < stateCount; to++) {
                finalTransitions[from][to] = new TreeSet<>();
                // ignore non-atomic transitions
                for (String str: atomicTransitions[from][to]) {
                    if (str.length() <= 1) finalTransitions[from][to].add(str);
                }
            }
        }

//        System.out.println("\nAFTER ATOMIC:");
//        for (int i = 0; i < finalTransitions.length; i++) {
//            for (int n = 0; n < finalTransitions[0].length; n++) {
//                if (finalTransitions[i][n].size() > 0 ) System.out.print(finalTransitions[i][n] + " ");
//                else System.out.print("[_] ");
//            }
//            System.out.println();
//        }

        return new Pair<>(finalTransitions, atomicAcceptingStates);
    }

    private Set<Integer> getStateGroup(int state, Set<String>[][] transitions, Set<Integer> currentStateGroup) {
        int numStates = transitions.length;

        int num = currentStateGroup.size();
        currentStateGroup.add(state);

        for (int to = 0; to < numStates; to++) {
            if (transitions[state][to].contains(EPSILON)) {
                currentStateGroup.add(to);
            }
        }

        // if nothing new was added, return
        if (currentStateGroup.size() == num) {
            return currentStateGroup;
        }

        Integer[] csgArr = new Integer[currentStateGroup.size()];
        currentStateGroup.toArray(csgArr);
        for (int next = 0; next < csgArr.length; next++) {
            currentStateGroup.addAll(getStateGroup(csgArr[next], transitions, currentStateGroup));
        }

        return currentStateGroup;
    }

    // find all state groups/compound states reachable in one step
    private ArrayList<Pair<String, Set<Integer>>> findNextStateGroups(Set<Integer> group, Set<String>[][] transitions) {
        ArrayList<Pair<String, Set<Integer>>> results = new ArrayList<>();

        for (Integer state: group) {
            for (int to = 0; to < transitions.length; to++) {
                for (String sym: transitions[state][to]) {
                    if (sym.equals(EPSILON)) continue;

                    // add state group reachable via sym
                    Set<Integer> reachables = getStateGroup(to, transitions, new TreeSet<>());
                    if (!reachables.isEmpty()) {
                        // merge similar String's pairs if sym already is in results
                        boolean merged = false;
                        for (Pair<String, Set<Integer>> pair: results) {
                            if (pair.getKey().equals(sym)) {
                                pair.getValue().addAll(reachables);
                                merged = true;
                                break;
                            }
                        }
                        // add new pair if no merge happened
                        if (!merged) results.add(new Pair<>(sym, reachables));
                    }
                }
            }
        }

        return results;
    }

    @Override
    public RSA toRSA() {
        return toRSA(false);
    }

    private RSA toRSA(boolean switchAcceptingStates) {

//        System.out.println("\nBEFORE:");
//        for (int i = 0; i < transitions.length; i++) {
//            for (int n = 0; n < transitions[0].length; n++) {
//                if (transitions[i][n].size() > 0 ) System.out.print(transitions[i][n] + " ");
//                else System.out.print("[_] ");
//            }
//            System.out.println();
//        }

        // split up non-atomic transitions
        Pair<Set<String>[][], Set<Integer>> atomicData = getAtomicTransitions();
        Set<String>[][] atomicTransitions = atomicData.getKey();
        Set<Integer> atomicAcceptingStates = atomicData.getValue();

        // prepare RSA transition table
        int numStates = atomicTransitions.length * 10;
        Set<String>[][] rsaTransitions = (Set<String>[][]) new TreeSet<?>[numStates][numStates];
        for (int from = 0; from < numStates; from++) {
            for (int to = 0; to < numStates; to++) {
                rsaTransitions[from][to] = new TreeSet<>();
            }
        }

        // group states
        int nextID = 0;
        ArrayList<Pair<Integer, Set<Integer>>> allGroups = new ArrayList<>();
        ArrayList<Pair<Integer, Set<Integer>>> remaining = new ArrayList<>();

        Pair<Integer, Set<Integer>> group = new Pair<>(nextID++, getStateGroup(initialState, atomicTransitions, new TreeSet<>()));
        remaining.add(group);

        while (!remaining.isEmpty()) {
            Pair<Integer, Set<Integer>> currentGroup = remaining.get(0);

            ArrayList<Pair<String, Set<Integer>>> nextStates = findNextStateGroups(currentGroup.getValue(), atomicTransitions);

            // add all new states
            for (Pair<String, Set<Integer>> nextPair: nextStates) {
                int toId = 0;
                boolean dup = false;

                for (Pair<Integer, Set<Integer>> pair: allGroups) {
                    if (pair.getValue().containsAll(nextPair.getValue()) ||
                            pair.getValue().equals(nextPair.getValue())) {
                        toId = pair.getKey();
                        dup = true;
                    }
                }
                for (Pair<Integer, Set<Integer>> pair: remaining) {
                    if (pair.getValue().containsAll(nextPair.getValue()) ||
                            pair.getValue().equals(nextPair.getValue())) {
                        toId = pair.getKey();
                        dup = true;
                    }
                }

                if (!dup) {
                    toId = nextID;
                    remaining.add(new Pair<>(nextID++, nextPair.getValue()));
                }

                // register transitions in RSA table
                int from = currentGroup.getKey();
                int to   = toId;

                rsaTransitions[from][to].add(nextPair.getKey());
            }

            remaining.remove(0);
            allGroups.add(currentGroup);
        }

        // add non-accepting state for invalid words
        Set<Integer> omegaSet = new TreeSet<>();
        omegaSet.add(666);
        allGroups.add(new Pair<>(nextID++, omegaSet));

        // trim transition table
        Set<String>[][] finalTransitions = (Set<String>[][]) new TreeSet<?>[allGroups.size()][allGroups.size()];
        for (int from = 0; from < allGroups.size(); from++) {
            for (int to = 0; to < allGroups.size(); to++) {
                finalTransitions[from][to] = rsaTransitions[from][to];
            }
        }

        // TODO set transitions to omega
        for (int from = 0; from < finalTransitions.length-1; from++) { // remove -1 for adding transitions of omega to itself(but creates cycles!!!)
            for (char sym : getSymbols()) {
                String symStr = sym + "";

                boolean hasTransition = false;
                for (int to = 0; to < finalTransitions.length; to++) {
                    if (finalTransitions[from][to].contains(symStr)) {
                        hasTransition = true;
                        break;
                    }
                }
                // no transition for this symbol -> set to omega
                if (!hasTransition) {
                    finalTransitions[from][finalTransitions.length-1].add(symStr);
                }
            }
        }

        // convert accepting states
        Set<Integer> newAcceptingStates = new TreeSet<>();
        for (Pair<Integer, Set<Integer>> pair: allGroups) {
            for (Integer state: pair.getValue()) {
                if (atomicAcceptingStates.contains(state)) {
                    newAcceptingStates.add(pair.getKey());
                    break;
                }
            }
        }

        if (switchAcceptingStates) {
            // switch accepting states with non-accepting states
            Set<Integer> switchedAcceptingStates = new TreeSet<>();
            for (int s = 0; s < newAcceptingStates.size(); s++) {
                if (!newAcceptingStates.contains(s)) {
                    switchedAcceptingStates.add(s);
                }
            }
            newAcceptingStates = switchedAcceptingStates;
        }

        __RSAimpl rsa = new __RSAimpl(finalTransitions.length, this.characters, newAcceptingStates, 0);
        rsa.setTransitions(finalTransitions);

//        System.out.println("\nAFTER RSA:");
//        for (int i = 0; i < finalTransitions.length; i++) {
//            for (int n = 0; n < finalTransitions[0].length; n++) {
//                if (finalTransitions[i][n].size() > 0 ) System.out.print(finalTransitions[i][n] + " ");
//                else System.out.print("[_] ");
//            }
//            System.out.println();
//        }

        return rsa;
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
        Set<Integer> nextStates = this.getNextStates(s, w);

        for (Integer i: nextStates) {
            if (getAcceptingStates().contains(i)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean acceptsNothing() {
        // try with getNextStates == nothing
        if (acceptingStates.isEmpty()) return false;
        return getNextStates(initialState, EPSILON).isEmpty();
//        return pathExists(new TreeSet<Integer>(), initialState, true) == 0;
    }

    @Override
    public Boolean acceptsEpsilonOnly() {
//        return pathExists(new TreeSet<Integer>(), initialState, true) == 1;

//        for (int i = 0; i < 10; i++) {
//            for (Character c : characters) {
//                String s = c + "";
//                for (int n = 0; n < i; n++) s = s + c;
//                if (getNextStates(initialState, s).size() > 0) return false;
//            }
//        }
        return acceptsEpsilon();
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

    @Override
    public Boolean acceptsEpsilon() {
        return acceptsRecursive(initialState, "");
    }

    @Override
    public Boolean isInfinite() {
        for (int from = 0; from < transitions.length; from++) {
            for (int to = 0; to < transitions[0].length; to++) {
                if (transitions[from][to].size() > 0) {
                    if (from >= to) return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean isFinite() {
        return !isInfinite();
    }

    @Override
    public Boolean subSetOf(NFA a) {
//        if (!a.getSymbols().containsAll(this.characters)) return false;
//
//        for (int f=0; f<this.transitions.length; f++) {
//            for (int t=0; t<this.transitions[0].length; t++) {
//                Set<String> s = a.getTransitions()[f][t];
//                if (!s.containsAll(getTransitions()[f][t])) return false;
//            }
//        }

        return true;
    }

    @Override
    public Boolean equalsPlusAndStar() {
        return this.plus().equals(this.kleeneStar());
//        return acceptsEpsilon();
    }

    @Override
    public boolean equals(Object b) {
//        if (getid()!=null && ((NfaImpl)b).getid()!=null && getid() != ((NfaImpl)b).getid()) return false;

        return true;
//        NFA other = (NFA)b;
//
//        RSA inv = (RSA)other.complement();
//        return inv.intersection(this).acceptsEpsilon();
    }
}
