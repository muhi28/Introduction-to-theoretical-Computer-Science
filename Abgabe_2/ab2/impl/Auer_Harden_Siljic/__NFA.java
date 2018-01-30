package ab2.impl.Auer_Harden_Siljic;

import ab2.NFA;
import ab2.RSA;
import ab2.fa.exceptions.IllegalCharacterException;

import java.util.HashSet;
import java.util.Set;

/**
 * Methods generating new NFA lose their Transmissions -> Implement setTransmissionTable
 */

public class __NFA implements NFA {
    private int numstates;
    private Set<Character> characters;
    private Set<Integer> acceptingStates;
    private int initialState;

    private Set<String>[][] transitions;
    public final String EPSILON = "";

    public __NFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
    this.numstates = numStates;
    this.characters = characters;
    this.acceptingStates = acceptingStates;
    this.initialState = initialState;

    transitions = initializeTransitionTable(numstates);
    }

    /**
     * Initializes an empty TransitionTable using numstates, in order to avoid nullptr
     * @param numstates
     * @return TransitionTable full of empty Hashsets.
     */
    private Set<String>[][] initializeTransitionTable(int numstates){
        Set<String>[][] transitions = (Set<String>[][]) new HashSet<?>[numstates][numstates];
        for(int i = 0; i < transitions.length; i++){
            for(int j = 0; j < transitions[0].length; j++){
                transitions[i][j] = new HashSet<>();
            }
        }
        return transitions;
    }

    @Override
    public Set<Character> getSymbols() {
        return this.characters;
    }

    @Override
    public Set<Integer> getAcceptingStates() {
        return this.acceptingStates;
    }

    @Override
    public int getInitialState() {
        return this.initialState;
    }

    @Override
    public boolean isAcceptingState(int s) throws IllegalStateException {
        if(s > numstates || s < numstates) throw new IllegalStateException();

        if(acceptingStates.contains(s)) return true;
        else return false;
    }

    @Override
    public Set<String>[][] getTransitions() {
        return this.transitions;
    }

    @Override
    public void setTransition(int fromState, String s, int toState) throws IllegalStateException, IllegalCharacterException {
        // Char Exception
        if(!characters.contains(s)) throw new IllegalCharacterException();
        // State Exception
        if(fromState < numstates || fromState > numstates ||
                toState < numstates || toState > numstates) throw new IllegalStateException();

        // Put string
        transitions[fromState][toState].add(s);
    }

    @Override
    public void clearTransitions(int fromState, String s) throws IllegalStateException {
        // State Exception
        if(fromState > numstates || fromState < numstates) throw new IllegalStateException();

        for(int i = 0; i < transitions[0].length; i++) {
            if(transitions[fromState][i].contains(s)){
                transitions[fromState][i].remove(s);
            }
        }

    }

    @Override
    public Set<Integer> getNextStates(int state, String s) throws IllegalCharacterException, IllegalStateException {
        // Exceptions
        if(!characters.contains(s)) throw new IllegalCharacterException();
        if(state > numstates || state < numstates) throw new IllegalStateException();

        Set<Integer> nextStates = new HashSet<Integer>();

        // FIXME -- Not sure if this is what is requested for
        for(int i = 0; i<transitions[0].length; i++){
            if(transitions[state][i].contains(s)){
                nextStates.add(i);
            }
        }

        return nextStates;
    }

    @Override
    public int getNumStates() {
        return this.numstates;
    }

    /**
     * Overwrites Transitiontable of given NFA with given Table
     * @param nfa
     * @param newTable
     * @return full NFA including overwritten Table
     */
    private NFA setTableAndFinalize(NFA nfa, Set<String>[][] newTable){
        for(int i = 0; i < newTable.length; i++){
            for(int j = 0; j < newTable[0].length; j++){
// FIXME Not sure if this works properly
                for(String s: newTable[i][j]){
                    nfa.setTransition(i, s, j);
                }
            }
        }
        return nfa;
    }

    @Override
    public NFA union(NFA a) {
        /** Numstates + new starting State **/
        int onionStates = this.numstates + a.getNumStates() + 1;

        /** Characters **/
        Set<Character> onionCharacters = this.characters;
        onionCharacters.addAll(a.getSymbols());

        /** New Initialstate ! Our Alpha is hereby 0. Thus: Offset of +1 for each machine**/
        int onionInitialState = 0;

        // Initialize new Transitions
        Set<String>[][] onionTransitions = initializeTransitionTable(onionStates);

        // Find initialstates of previous machines
        int this_offset = 1;
        int a_offset = this.getNumStates()+1;

        // FIXME Not sure if correct offset
        // Initialize Edge from Alpha to the respective initialstates.
        onionTransitions[0][this_offset].add(EPSILON);
        onionTransitions[0][a_offset].add(EPSILON);

        // FIXME Onionoverflow + Object to Loop Fusion for better performance
        for(int i = 0; i < transitions.length; i++){
            for(int j = 0; j < transitions[0].length; j++) {
                if(!transitions[i][j].isEmpty()){
                    onionTransitions[i+this_offset][j+this_offset] = transitions[i][j];
                }
            }
        }
        for(int i = 0; i < a.getTransitions().length; i++){
            for(int j = 0;  j < a.getTransitions()[0].length; j++) {
                if (!a.getTransitions()[i][j].isEmpty()) {
                    onionTransitions[i + a_offset][j + a_offset] = a.getTransitions()[i][j];
                }
            }
        }

        /** Accepting States **/
        Set<Integer> onionAcceptingStates = new HashSet<>();
        for(Integer i : acceptingStates){
            onionAcceptingStates.add(i + this_offset);
        }
        for(Integer i : a.getAcceptingStates()){
            onionAcceptingStates.add(i + a_offset);
        }

        // Overwrite *this*
        this.numstates = onionStates;
        this.characters = onionCharacters;
        this.acceptingStates = onionAcceptingStates;
        this.initialState = onionInitialState;
        this.transitions = onionTransitions;

        return this;
        /*
        NFA onionNFA = setTableAndFinalize(new __NFA(onionStates, onionCharacters, onionAcceptingStates, onionInitialState), onionTransitions);
        return onionNFA;
         */

    }

    @Override
    public NFA intersection(NFA a) {
        // FIXME :: Demorgan should be correct.
        return (this.complement().union(a.complement())).complement();
    }

    @Override
    public NFA minus(NFA a) {
        //A - B <=> A n B' <=> '(A' u B)
        return (this.complement().union(a)).complement();
    }

    // FIXME had a overwrite
    @Override
    public NFA concat(NFA a) {
        /** New Numstates **/
        int catNumstates = this.numstates+a.getNumStates();     // As in Union

        /** New Characters **/
        Set<Character> catCharacters = this.characters;
        catCharacters.addAll(a.getSymbols());

        /** New initState **/
        int catInitialState = 0;    // new Alpha
        // Find initialstates of previous machines
        int a_offset = this.getNumStates()+1;


        /** Transition Table ---> Accepting States**/
        Set<String>[][] catTransitions = initializeTransitionTable(catNumstates);
        Set<Integer> catAcceptingStates = new HashSet<>();

        // FIXME Copy Transitionstables from this + a into new one
        for(int i = 0; i < transitions.length; i++){
            for(int j = 0; j < transitions[0].length; j++) {
                if(!transitions[i][j].isEmpty()){
                    catTransitions[i][j] = transitions[i][j];
                }
                // TODO: Not sure if correct <-> Epsilon-Transitions from accepting M1 to  initial M2-states
                if(acceptingStates.contains(j)){
                    catTransitions[i][j].add(EPSILON);
                }
            }
        }

        for(int i = 0; i < a.getTransitions().length; i++){
            for(int j = 0;  j < a.getTransitions()[0].length; j++) {
                if (!a.getTransitions()[i][j].isEmpty()) {
                    catTransitions[i + a_offset][j + a_offset] = a.getTransitions()[i][j];
                }
            }
        }
        // Setup new Accepting States
        for(Integer i : acceptingStates){
            catAcceptingStates.add(i+a_offset);
        }

        // Overwrite this automata
        this.transitions = catTransitions;
        this.numstates = catNumstates;
        this.characters = catCharacters;
        this.acceptingStates = catAcceptingStates;
        this.initialState = catInitialState;
        return this;
        /*
        NFA catNFA = setTableAndFinalize(new __NFA(catNumstates, catCharacters, catAcceptingStates, catInitialState), catTransitions);
        return catNFA;

 */
    }

    @Override
    public NFA complement() {
        this.toRSA();

        // Numstates, Characters, initialstate and transition remain the same.
        Set<Integer> complainedAcceptingStates = new HashSet<>();
        /** Flip Accepting States! **/
        for(Integer as: acceptingStates){
            if(!acceptingStates.contains(as)){
                complainedAcceptingStates.add(as);
            }
        }
        this.acceptingStates = complainedAcceptingStates;
        return this;
    }

    @Override
    public NFA kleeneStar() {
        this.numstates = numstates+1;   // Add Alpha

        // Offset on transitions
        for(int i = 0; i < transitions.length; i++){
            for(int j = 0; j < transitions[0].length; j++){
                transitions[i+1][j+1] = transitions[i][j];
                if(acceptingStates.contains(i)){
                    transitions[i+1][initialState].add(EPSILON);
                }
            }
        }
        // Setup Epsilonedge from new initialstate
        this.initialState = 0;
        transitions[0][initialState].add(EPSILON);

        return this;
    }

    @Override
    public NFA plus() {
// TODO AcceptingStates -> Epsilon to Initial state
        for(int i = 0; i < transitions.length; i++){
            for(Integer a : acceptingStates) {

            }
        }

        return null;
    }

    @Override
    public RSA toRSA() {
// TODO
        return null;
    }

    @Override
    public Boolean accepts(String w) throws IllegalCharacterException {
// TODO

        return null;
    }

    @Override
    public Boolean acceptsNothing() {
    // FIXME This only assumes true if set is empty.
        if(acceptingStates.isEmpty()) return true;
        return false;
    }

    @Override
    public Boolean acceptsEpsilonOnly() {
// TODO
        return null;
    }

    @Override
    public Boolean acceptsEpsilon() {
// TODO
        return null;
    }

    @Override
    public Boolean isInfinite() {
// TODO
       return null;
    }

    @Override
    public Boolean isFinite() {
// TODO
        return null;
    }

    @Override
    public Boolean subSetOf(NFA a) {
// TODO
        return null;
    }

    @Override
    public boolean equals(Object b) {
// TODO
        return false;
    }

    @Override
    public Boolean equalsPlusAndStar() {
// TODO
        return null;
    }
}
