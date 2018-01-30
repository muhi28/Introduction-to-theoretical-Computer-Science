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

//    private ArrayList<StateGraph> graphs;

//    private ArrayList<ArrayList<Set<String>>> atomicTransitions;

    public final String EPSILON = "";

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

//        this.graphs = new ArrayList<>();
//        this.graphs.add(new StateGraph(this.initialState));


    }

    // Constructor used for kleeneStar -- FIXME
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

//        // add atomic transitions to graph(s)
//        StateGraph fromGraph = null;
//        StateGraph toGraph = null;
//        int fromIdx = 0, toIdx = 0;
//        int idx = 0;
//        for (StateGraph sg: this.graphs) {
//            if (fromGraph == null) {
//                StateGraph fromResult = getGraphNode(sg, fromState);
//                if (fromResult != null) {
//                    fromGraph = fromResult;
//                    fromIdx = idx;
//                }
//            }
//            if (toGraph == null) {
//                StateGraph toResult = getGraphNode(sg, toState);
//                if (toResult != null) {
//                    fromGraph = toResult;
//                    toIdx = idx;
//                }
//            }
//
//            if (fromGraph != null && toGraph != null) break;
//
//            idx++;
//        }
//        // make sure that subgraphs exist
//        if (fromGraph == null) {
//            graphs.add(new StateGraph(fromState));
//            fromIdx = graphs.size()-1;
//            fromGraph = graphs.get(fromIdx);
//        }
//        if (toGraph == null) {
//            graphs.add(new StateGraph(toState));
//            toIdx = graphs.size()-1;
//            toGraph = graphs.get(toIdx);
//        }
////        System.out.println("*** "+s);
//        // split s up into chars for intermediate states and add transitions
//        StateGraph fromNode = fromGraph;
//        char[] chars = s.toCharArray();
//        // edge case: EPSILON results in empty char array
//        if (s.equals(EPSILON)) {
//            chars = new char[]{'E'};
//        }
//
//        for (int ci = 0; ci<chars.length; ci++) {
//            StateGraph intNode;
////            System.out.println("+ "+chars[ci]);
//            if (ci == chars.length-1) intNode = toGraph;
//            else intNode = new StateGraph();
//
//            // IMPORTANT - Consider corner case with EPSILON:
//            String symStr = chars[ci]+"";
//            if (s.equals(EPSILON)) symStr = EPSILON;
//            // add subgraph/node
//            fromNode.addGraph(intNode, symStr);
//
////            System.out.println("ADD CHAR: "+chars[ci]+", "+ci);
//
//            fromNode = fromNode.getLast();
//        }
//
//        // add transition
////        fromGraph.addGraph(toGraph, s);
//        // check if fromGraph and toGraph are in separate subgraphs
//        if (fromIdx != toIdx) {
//            // FIXME
////            // TESTING
////            int nextNum = fromGraph.next.size();
//
//            graphs.remove(toIdx);
//
////            // TESTING
////            if (fromGraph.next.size() != nextNum || fromGraph.next.get(fromGraph.next.size()-1) == null)
////                System.out.println("FAIL");
//            if (toGraph == null) System.out.println("FAIL???");
//        }
//
//
//
////        char[] chars = s.toCharArray();
////        // look for node/subgraph with id fromState
////        StateGraph fromNode = getGraphNode(graphRoot, fromState);
////        // add transitions
////        for (int ci = 0; ci<chars.length; ci++) {
////            StateGraph newNode;
////            if (ci == chars.length-1) newNode = new StateGraph(toState);
////            else newNode = new StateGraph();
////
////            fromNode.addGraph(newNode, ci+"");
////
////            fromNode = fromNode.getLast();
////        }
//
////        graphs.get(0).print();
    }

    private StateGraph getGraphNode(StateGraph graph, int id) {
        if (graph.id == id) return graph;

        for (StateGraph g: graph.next) {
            StateGraph result = getGraphNode(g, id);
            if (result != null) return result;
        }

        return null;
    }

    private Set<StateGraph> getNextGraphNodes(Set<StateGraph> set, StateGraph graph, String s) {
        for (int i=0; i<graph.next.size(); i++) {
            if (graph.nextSymbols.get(i) == s) {
                if (!set.contains(graph.next.get(i))) {
                    set.addAll(getNextGraphNodes(set, graph.next.get(i), EPSILON));
                }
            }
            if (graph.nextSymbols.get(i) == EPSILON) {
                if (!set.contains(graph.next.get(i))) {
                    set.addAll(getNextGraphNodes(set, graph.next.get(i), s));
                }
            }
            set.add(graph.next.get(i));
        }

//        System.out.println("#################### "+graph.id+", "+set.iterator().next().id);

        return set;
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
        if (state < 0 || state >= this.getNumStates()) throw new IllegalStateException("geta nita");
        if (s.length() > 0 && !this.characters.contains(s.toCharArray()[0])) throw new IllegalCharacterException();

//        System.out.println("> "+s);

//        Set<Integer> results = new TreeSet<>();
//        String cs = s;
//        int cState = state;
//
//        for (int to=0; to<getNumStates(); to++) {
//            cs = s;
//            cState = state;
//
//            do {
//
//                int num = results.size();
//                for (String sym : transitions[cState][to]) {
//                    if (cs.startsWith(sym)) {
//                        cs = cs.substring(sym.length());
//                        cState = to;
//
//                        results.add(to);
//                    }
//                }
//                if (results.size() == num) break;
//
//            } while (true);
//        }


        return getNextStatesStep(new TreeSet<>(), new TreeSet<>(), state, s);

//        Set<Integer> set = new TreeSet<>();
//        Integer result = getNextStatesStep(state, s);
//        if (result != null) set.add(result);
//        return set;
    }

    // FIXME check if this is really correct(should avoid endless recursions now though)
    private Set<Integer> getNextStatesStep(Set<Integer> history, Set<Integer> results, int state, String s) {
        // add this state to history
        history.add(state);

        for (int to=0; to<getNumStates(); to++) {
            // ignore next states that were already visited in this branch
            if (history.contains(to)) continue;

            for (String sym: transitions[state][to]) {
                // if this transition is possible with current s
                if (s.startsWith(sym)) {
                    // remove this transition's symbol(s) from s and continue recursion with remaining part
                    results.addAll(getNextStatesStep(history, results, to, s.substring(sym.length())));
                }
            }
        }

        return results;
    }

////    /** Liefert Set aus Zuständen zurück die mit S erreichbar sind
////     * @param state    = State to check
////     * @param s        = Full String to check || Epsilon to check
////     * @return set
////     */
////    private Set<Integer> getNextStatesStep(Set<Integer> set, int state, String s) {
//    private Integer getNextStatesStep(int state, String s) {
////        System.out.println(">>> "+transitions.length+", "+transitions[0].length);
//        for (int i = 0; i < this.transitions[0].length; i++) {
////            System.out.println("transitions: "+this.transitions[state][i].toString());
//            for (String transition: this.transitions[state][i]) {
//                if (s.startsWith(transition)) {
////                    System.out.println("transition: "+transition+", s: "+s+", substr: "+s.substring(transition.length())+", STATE: "+state);
//                    Integer result = getNextStatesStep(i, s.substring(transition.length()));
//                    if (result != null) return result;
//                }
//            }
//        }
//
//        if (s.length() == 0 && this.acceptingStates.contains(state)) return state;
//        return null;
//
////        for (int i = 0; i < transitions[0].length; i++) {                       // For each fromState
////            if (!set.contains(i)) {
////                if (!s.isEmpty() && transitions[state][i].contains(s)) {        // There is a transition so that fromstate->tostate using S
////                        set.add(i);                                             //  Add to set
////                        set.addAll(getNextStatesStep(set, i, ""));           //   Repeat using epsilon-paths
////                }
////                if (transitions[state][i].contains("")) {                       // fromstate->tostate using epsilon
////                        set.add(i);
////                        set.addAll(getNextStatesStep(set, i, s));
////                }
////            }
////        }
////        return set;
//    }

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
                allTransitions[i][j] = new TreeSet<String>();
            }
        }
        // add S (state connecting both NFAs via epsilon)
        allTransitions[0][1].add(EPSILON);
        allTransitions[0][offset].add(EPSILON); // FIXME possible off-by-one error
//        allTransitions[0][this.getNumStates()].add(EPSILON); // FIXME possible off-by-one error

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


//        System.out.println("\nTHIS:");
//        for (int i = 0; i < this.transitions.length; i++) {
//            for (int n = 0; n < this.transitions[0].length; n++) {
//                if (transitions[i][n].size() > 0 ) System.out.print(transitions[i][n] + " ");
//                else System.out.print("[_] ");
//            }
//            System.out.println();
//        }
//        System.out.println("\nA:");
//        for (int i = 0; i < a.getTransitions().length; i++) {
//            for (int n = 0; n < a.getTransitions()[0].length; n++) {
//                if (a.getTransitions()[i][n].size() > 0 ) System.out.print(a.getTransitions()[i][n] + " ");
//                else System.out.print("[_] ");
//            }
//            System.out.println();
//        }
//        System.out.println("\nUNION:");
//        for (int i = 0; i < allTransitions.length; i++) {
//            for (int n = 0; n < allTransitions[0].length; n++) {
//                if (allTransitions[i][n].size() > 0 ) System.out.print(allTransitions[i][n] + " ");
//                else System.out.print("[_] ");
//            }
//            System.out.println();
//        }




        NfaImpl resultingNfa = new NfaImpl(allTransitions.length, allChars, allAcceptingStates, 0);
        // set all transitions individually to ensure correct graph generation
        for (int from = 0; from < allTransitions.length; from++) {
            for (int to = 0; to < allTransitions[0].length; to++) {
                if (allTransitions[from][to].size() > 0) {
                    for (String sym: allTransitions[from][to]) {
                        resultingNfa.setTransition(from, sym, to);
//                        System.out.println("set: "+from+" -- "+sym+" -> "+to);
                    }
                }
            }
        }
        // return union
        return resultingNfa;

//        resultingNfa.setTransitions(allTransitions);


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
//         FIXME: NOT TESTED/VERIFIED

//        // switch accepting states with non-accepting states
//        Set<Integer> newAcceptingStates = new TreeSet<>();
//        for (int s = 0; s < getNumStates(); s++) {
//            if (!getAcceptingStates().contains(s)) {
//                newAcceptingStates.add(s);
//            }
//        }

        // calling with true tells toRSA to also switch accepting states with non-accepting states
        // so this one line converts the NFA to an inverted RSA
        return toRSA(true);
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

//        System.out.println("################################## concat ##### "+newSize);
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

    private Set<String>[][] getAtomicTransitions() {
//        System.out.println("\nBEFORE:");
//        for (int i = 0; i < transitions.length; i++) {
//            for (int n = 0; n < transitions[0].length; n++) {
//                if (transitions[i][n].size() > 0 ) System.out.print(transitions[i][n] + " ");
//                else System.out.print("[_] ");
//            }
//            System.out.println();
//        }

        // transfer all transitions
        int newSize = getNumStates() * 10; // just to be sure
        Set<String>[][] atomicTransitions = (Set<String>[][]) new TreeSet<?>[newSize][newSize];
        for (int from = 0; from < newSize; from++) {
            for (int to = 0; to < newSize; to++) {
                if (from < getNumStates() && to < getNumStates()) {
                    atomicTransitions[from][to] = transitions[from][to];
                } else {
                    atomicTransitions[from][to] = new TreeSet<>();
                }
            }
        }
        // create atomic transitions
        int nextState = getNumStates();
        for (int from = 0; from < getNumStates(); from++) {
            for (int to = 0; to < getNumStates(); to++) {
                // look for non-atomic transitions
                for (String str: atomicTransitions[from][to]) {
                    if (str.length() > 1) {
                        System.out.println(str);
                        // split up into more transitions with single characters as symbols/words
                        int currentFrom = from;
                        char[] chars = str.toCharArray();
                        for (int i=0; i<chars.length; i++) {
                            String s = chars[i]+"";
//                            System.out.println("char: "+s);
                            boolean dup = false;
                            for (int n=0; n<newSize; n++) {
//                                System.out.println(atomicTransitions[currentFrom][n]+", "+s);
                                if (atomicTransitions[currentFrom][n].contains(s)) {
                                    currentFrom = n;

                                    dup = true;
                                    break;
                                }
                            }
                            if (dup) continue;

//                            System.out.println("nodup, " + currentFrom);

                            int newStateIdx;
                            if (i == chars.length - 1) { // if this is the last transition left
                                newStateIdx = to;
                            } else {
                                newStateIdx = nextState++;
                            }

                            atomicTransitions[currentFrom][newStateIdx].add(s);
//                            System.out.println(atomicTransitions[currentFrom][newStateIdx]);
                            currentFrom = newStateIdx;
                        }
                    }
                }
            }
        }
        // trim transition table
        Set<String>[][] finalTransitions = (Set<String>[][]) new TreeSet<?>[nextState][nextState];
        for (int from = 0; from < nextState; from++) {
            for (int to = 0; to < nextState; to++) {
                finalTransitions[from][to] = new TreeSet<>();
                // ignore non-atomic transitions
                for (String str: atomicTransitions[from][to]) {
                    if (str.length() <= 1) finalTransitions[from][to].add(str);
                }
            }
        }

        System.out.println("\nAFTER ATOMIC:");
        for (int i = 0; i < finalTransitions.length; i++) {
            for (int n = 0; n < finalTransitions[0].length; n++) {
                if (finalTransitions[i][n].size() > 0 ) System.out.print(finalTransitions[i][n] + " ");
                else System.out.print("[_] ");
            }
            System.out.println();
        }

        return finalTransitions;
    }

    private Set<Integer> getStateGroup(int state, Set<String>[][] transitions, Set<Integer> currentStateGroup) {
        int numStates = transitions.length;

        int num = currentStateGroup.size();
//        Set<Integer> currentStateGroup = new TreeSet<>();
        currentStateGroup.add(state);

        for (int to = 0; to < numStates; to++) {
            if (transitions[state][to].contains(EPSILON)) {
//                transitions[state][to].remove(EPSILON);

                currentStateGroup.add(to);
            }
        }

        // if nothing new was added, return
        if (currentStateGroup.size() == num) {
            return currentStateGroup;
        }

        // recursively add further ones if any
//        for (int next = 1; next < currentStateGroup.size(); next++) {
//            currentStateGroup.addAll(getStateGroup(next, transitions, currentStateGroup));
//        }
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
                    if (sym.equals(EPSILON)) continue; // FIXME ???

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

//    private boolean containsAny(Set<Integer> set, Set<Integer> other) {
//        for (Integer i: other) {
//            if (set.contains(i)) return true;
//        }
//        return false;
//    }

//    private boolean containsAll(Set<Integer> set, Set<Integer> other) {
//        for (Integer i: other) {
//            if (!set.contains(i)) return false;
//        }
//        return true;
//    }

    @Override
    public RSA toRSA() {
        return toRSA(false);
    }

    private RSA toRSA(boolean switchAcceptingStates) {

        System.out.println("\nBEFORE:");
        for (int i = 0; i < transitions.length; i++) {
            for (int n = 0; n < transitions[0].length; n++) {
                if (transitions[i][n].size() > 0 ) System.out.print(transitions[i][n] + " ");
                else System.out.print("[_] ");
            }
            System.out.println();
        }


        // split up non-atomic transitions
        Set<String>[][] atomicTransitions = getAtomicTransitions();
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

            if (nextStates.size() > 0) {
                System.out.println("current: "+currentGroup.getValue()+", next: "+nextStates.iterator().next().getValue());
            } else {
                System.out.println("current: "+currentGroup.getValue());
            }
            // add all new states
            for (Pair<String, Set<Integer>> nextPair: nextStates) {
                int toId = 0;
                boolean dup = false;

                for (Pair<Integer, Set<Integer>> pair: allGroups) {
//                    if (containsAll(pair.getValue(),nextPair.getValue()) ||
                    if (pair.getValue().containsAll(nextPair.getValue()) ||
                            pair.getValue().equals(nextPair.getValue())) {
                        toId = pair.getKey();
                        dup = true;
                    }
                }
                for (Pair<Integer, Set<Integer>> pair: remaining) {
//                    if (containsAll(pair.getValue(), nextPair.getValue()) ||
                    if (pair.getValue().containsAll(nextPair.getValue()) ||
                            pair.getValue().equals(nextPair.getValue())) {
                        toId = pair.getKey();
                        dup = true;
                    }
                }

//                // check if duplicate
//                int toId = 0;
//                boolean dup = false;
//                for (Pair<Integer, Set<Integer>> pair: allGroups) {
//                    if (dup) break;
//                    System.out.println(nextPair.getValue()+" == "+pair.getValue()+"? => "+nextPair.getValue().equals(pair.getValue()));
//                    if (nextPair.getValue().equals(pair.getValue())) {
//                        toId = pair.getKey();
//                        dup = true;
//                    }
//                }
//                for (Pair<Integer, Set<Integer>> pair: remaining) {
//                    if (dup) break;
//                    System.out.println(nextPair.getValue()+" == "+pair.getValue()+"? => "+nextPair.getValue().equals(pair.getValue()));
//                    if (nextPair.getValue().equals(pair.getValue())) {
//                        toId = pair.getKey();
//                        dup = true;
//                    }
//                }
                if (!dup) {
                    toId = nextID;
                    remaining.add(new Pair<>(nextID++, nextPair.getValue()));
                    System.out.println("add "+nextPair.getValue());
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
        for (int from = 0; from < getNumStates(); from++) {
            for (char sym : getSymbols()) {
                String symStr = sym + "";

                boolean hasTransition = false;
                for (int to = 0; to < getNumStates(); to++) {
                    if (finalTransitions[from][to].contains(symStr)) {
                        hasTransition = true;
                        break;
                    }
                }
                // no transition for this symbol -> set to omega
                if (!hasTransition) {
                    finalTransitions[from][getNumStates()-1].add(symStr);
                }
            }
        }

        // convert accepting states
        Set<Integer> newAcceptingStates = new TreeSet<>();
        for (Pair<Integer, Set<Integer>> pair: allGroups) {
            for (Integer state: pair.getValue()) {
                if (getAcceptingStates().contains(state)) {
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


        System.out.println("\nAFTER:");
        for (int i = 0; i < finalTransitions.length; i++) {
            for (int n = 0; n < finalTransitions[0].length; n++) {
                if (finalTransitions[i][n].size() > 0 ) System.out.print(finalTransitions[i][n] + " ");
                else System.out.print("[_] ");
            }
            System.out.println();
        }

        return rsa;

//        boolean dup = false;
//        for (Pair<Integer, Set<Integer>> pair: allGroups) {
//            if (pair.getValue().equals(group.getValue())) {
//                dup = true;
//                break;
//            }
//        }
//        if (dup) continue;

//        for (int state = 0; state < numStates; state++) {
//
//
//            Set<Pair<String, Set<Integer>>> nextStates = findNextStateGroups(group, atomicTransitions);
//        }

//return null;


//        this.atomicTransitions = new
//        for (int i=0; i<this.getNumStates(); i++) {
//            this.atomicTransitions.add(new ArrayList<>());
//            for (int n=0; n<this.getNumStates(); n++) {
////                this.atomicTransitions.get(i).add(new TreeSet<>());
//                this.atomicTransitions.get(i).get(n) = transitions[i][n];
//            }
//        }

////        graphs.get(0).print();
//
//        CompoundState rsaGraph = graphs.get(0).asCompoundStates();
//        ArrayList<CompoundState> newStates = new ArrayList<>();
//        ArrayList<CompoundState> remaining = new ArrayList<>();
//        Set<Integer> newAcceptingStates = new TreeSet<>();
//
//        // add root first, then add next CompoundStates in while loop
//        newStates.add(rsaGraph);
//
//        CompoundState currentState = rsaGraph;
//
//        currentState.print();
//
////        Set<String> processedTransitions = new TreeSet<>();
//        while(true) {
//            ArrayList<Pair<String, Integer>> nextArr = currentState.nextStates;
////            System.out.println("len: "+nextArr.size());
//
//            for (Pair<String,Integer> pair: nextArr) {
////                if (processedTransitions.contains(pair.))
//
//                boolean isDup = false;
//                for (CompoundState cs : newStates) {
////                        System.out.println("dup?");
//
////                    if (cs.equals(pair.getValue())) {
//                    if (cs.compareTo(pair.getValue()) == 0) {
////                        System.out.println("dup");
//                        isDup = true;
//                        break;
//                    }
//                }
//                if (!isDup) {
//                    CompoundState addedCS = CompoundState.compoundStates.get(pair.getValue());
//                    // accepting state?
//                    for (StateGraph sg: CompoundState.compoundStates.get(pair.getValue()).thisState) {
//                        if (this.acceptingStates.contains(sg.id)) {
////                            System.out.println("add acc: "+newStates.size());
//                            addedCS.isAcceptingState = true;
//                        }
//                    }
//
//                    newStates.add(addedCS);
//                    remaining.add(addedCS);
//
////                    processedTransitions.add(pair.getKey());
////                    System.out.println("add "+addedCS.thisState.iterator().next().id);
//                }
//            }
//
//            if (remaining.size() == 0) break;
//            currentState = remaining.remove(0);
//        }
//
//        System.out.println("pre-rsa size: "+newStates.size());
//        Set<StateGraph> omegaSet = new TreeSet<>();
//        omegaSet.add(new StateGraph(666));
//        newStates.add(new CompoundState(omegaSet));
//
////        System.out.println("################################## toRSA ##### "+newStates.size());
//        Set<String>[][] rsaTransitions = (Set<String>[][]) new TreeSet<?>[newStates.size()][newStates.size()];
//        for (int i = 0; i < rsaTransitions.length; i++) {
//            for (int n = 0; n < rsaTransitions[0].length; n++) {
//                rsaTransitions[i][n] = new TreeSet<>();
//            }
//        }
//
////        CompoundState[] newStatesArr = (CompoundState[]) newStates.toArray();
//        CompoundState[] newStatesArr = new CompoundState[newStates.size()];
//        int z = 0;
//        for (CompoundState cs: newStates) {
//            newStatesArr[z++] = cs;
//        }
//
//        for (int i=0; i<newStatesArr.length; i++) {
////        for (CompoundState cs: new) {
//            for (Pair<String, Integer> next: newStatesArr[i].nextStates) {
//                // extract transition symbol and next state
//                String sym = next.getKey();
//                CompoundState nextState = CompoundState.compoundStates.get(next.getValue());
//                // find index of next state
//                int nextIdx = -1;
//                for (int k = 0; k<newStatesArr.length; k++) {
//                    if (newStatesArr[k].equals(nextState)) {
//                        nextIdx = k;
//                        break;
//                    }
//                }
//                if (nextIdx == -1) System.out.println("SCHEISSE");
//
//                // set transition between the 2 RSA states
//                rsaTransitions[i][nextIdx].add(sym);
//
//            }
//
//            // maybe register current RSA state as accepting state
//            if(newStatesArr[i].isAcceptingState) {
//                newAcceptingStates.add(i);
////                    System.out.println("newAcc: "+i);
//            }
//        }
//
//        // add non-accepting state
//        for (int i = 0; i < rsaTransitions.length-1; i++) {
//            for(char c: this.characters) {
//                boolean transitionExists = false;
//                for (int n = 0; n < rsaTransitions[0].length-1; n++) {
//                    if (rsaTransitions[i][n].contains(c+"")) {
//                        transitionExists = true;
//                        break;
//                    }
//                }
//                if (!transitionExists) {
//                    rsaTransitions[i][rsaTransitions.length-1].add(c+"");
//                }
//            }
//        }
////        for (char c: this.characters) {
////            rsaTransitions[rsaTransitions.length - 1][rsaTransitions.length - 1].add(c+"");
////        }
//
//        __RSAimpl rsa = new __RSAimpl(newStates.size(), this.characters, newAcceptingStates, 0);
////        rsa.setTransitions(rsaTransitions);
//        // set all transitions individually to ensure correct graph generation
//        for (int from = 0; from < rsaTransitions.length; from++) {
//            for (int to = 0; to < rsaTransitions[0].length; to++) {
//                if (rsaTransitions[from][to].size() > 0) {
//                    for (String sym: rsaTransitions[from][to]) {
//                        rsa.setTransition(from, sym, to);
//                    }
//                }
//            }
//        }
//
//        return rsa;
    }

    @Override
    public Boolean accepts(String w) throws IllegalCharacterException {


        char[] chars = w.toCharArray();
        for (char c: chars) {
            if (!characters.contains(c)) throw new IllegalCharacterException();
        }
//        System.out.println("accepts: "+w+", from: "+initialState);
//        System.out.println(this.acceptingStates.contains());
        return acceptsRecursive(initialState, w);
    }

    private boolean acceptsRecursive(int s, String w) {
        Set<Integer> endState = this.getNextStates(s, w);

        return endState.size() > 0;

//        for (int as: this.acceptingStates) {
//            if (reachableStates.contains(as)) {
//                return true;
//            }
//        }
//        return false;

//        String c = "";
//        String ws = "";
//        if (!w.equals(EPSILON)) {
//            c = w.charAt(0)+"";//String.valueOf(w.charAt(0));
//            ws = w.substring(1);
//        }
//
//        Set<Integer> states = this.getNextStates(s, c);
//
//        if (w.isEmpty()) {
//            for (Integer state: states) {
//                if (acceptingStates.contains(state)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//        for (Integer state: states) {
//            if (acceptsRecursive(state, ws)) {
//                System.out.println("Echo::Case2.1");
//                return true;
//            }
//        }
//        return false;
    }

    @Override
    public Boolean acceptsNothing() {
        // FIXME - NOTE: old approach(commented out) may be correct, error could be in function nfa.complement() (called in tests before this)
        // try with getNextStates == nothing
        return getNextStates(initialState, EPSILON).isEmpty();

//        return pathExists(new TreeSet<Integer>(), initialState, true) == 0;
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

//    /**Checks whether T contains some infinite loop
//     * @param checked
//     * @param s
//     * @param hasCycle
//     * @return
//     */
//    private boolean cyclingPathExists(Set<Integer> checked, int s, boolean hasCycle) {
//
//
////        if (this.acceptingStates.contains(s)) {
////            return hasCycle;
////        }
////
////        boolean result = false;
////
////        for (int t=0; t<this.transitions[0].length; t++) {
////            if (checked.contains(t)) {
////                hasCycle = true;
////                continue;
////            }
////            checked.add(t);
////
////            result = hasCycle || cyclingPathExists(checked, t, hasCycle);
////            if (result) return true;
////        }
////
////        return result;
//    }

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

//        return cyclingPathExists(new TreeSet<Integer>(), initialState, false);
    }

    @Override
    public Boolean isFinite() {
        return !isInfinite();   // BOIIIIIIIIIIIIIIII
    }

//    private boolean containsAllStrings(Set<String> set, Set<String> other) {
//        for (String s : other) {
//            if (!set.contains(s)) return false;
//        }
//        return true;
//    }

    @Override
    public Boolean subSetOf(NFA a) {
//        // FIXME temporary attempt to bullshit the tests
//        // check if a has any transitions at all
//        boolean hasTransitions = false;
//        for (int from = 0; from < getNumStates(); from++) {
//            for (int to = 0; to < getNumStates(); to++) {
//                if (getTransitions()[from][to].size() > 0) {
//                    hasTransitions = true;
//                    break;
//                }
//            }
//        }
//        if (!hasTransitions) return true;

        // Check :: Alphabet != same -> Error
        if (!a.getSymbols().containsAll(this.characters)) return false;

        // Check :: Transitions
        for (int f=0; f<this.transitions.length; f++) {
            for (int t=0; t<this.transitions[0].length; t++) {
                Set<String> s = a.getTransitions()[f][t];
//                if (!s.equals(a.getTransitions()[f][t])) return false;    // BULLSHIT!
//                if (!containsAllStrings(s, a.getTransitions()[f][t])) return false;
                if (!s.containsAll(getTransitions()[f][t])) return false;
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
        RSA thisRsa = this.toRSA();
        RSA otherRsa = ((NFA) b).toRSA();

        // Check :: Alphabet != same -> Error
        if (!this.characters.equals(otherRsa.getSymbols())) return false;

        if (otherRsa.getNumStates() != thisRsa.getNumStates()) return false;

        // Check :: Transitions
        // FIXME: Better check on substrings, not whole words
        for (int f=0; f<thisRsa.getTransitions().length; f++) {
            for (int t=0; t<thisRsa.getTransitions()[0].length; t++) {
                Set<String> s = thisRsa.getTransitions()[f][t];
                if (!s.equals(otherRsa.getTransitions()[f][t])) return false;
            }
        }
        return true;
        // TODO Holzhammermethode?
    }
}
