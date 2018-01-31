package ab2.impl.Auer_Harden_Siljic;

import ab2.DFA;
import ab2.NFA;
import ab2.RSA;
import ab2.fa.exceptions.IllegalCharacterException;

import java.util.HashSet;
import java.util.Set;

public class __NFA implements NFA {
    private int numstates;
    private Set<Character> characters;
    private Set<Integer> acceptingStates;
    private int initialState;

    private Set<String>[][] transitions;
    public final String EPSILON = "";
    public final int DEBUG = 1;             /** Debug-Codes:

                                                */

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
//        if(s > numstates || s < numstates) throw new IllegalStateException();

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
//        if(!characters.contains(s)) throw new IllegalCharacterException();
        // State Exception
//        if(fromState < numstates || fromState > numstates ||
//                toState < numstates || toState > numstates) throw new IllegalStateException();

        // Put string
        transitions[fromState][toState].add(s);
    }

    @Override
    public void clearTransitions(int fromState, String s) throws IllegalStateException {
        // State Exception
//        if(fromState > numstates || fromState < numstates) throw new IllegalStateException();

        for(int i = 0; i < transitions[0].length; i++) {
            if(transitions[fromState][i].contains(s)){
                transitions[fromState][i].remove(s);
            }
        }

    }

    @Override
    public Set<Integer> getNextStates(int state, String s) throws IllegalCharacterException, IllegalStateException {
        // Exceptions
//        if(!characters.contains(s)) throw new IllegalCharacterException();
//        if(state > numstates || state < numstates) throw new IllegalStateException();

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
        // Change of params.
        /** numstates = n^r, n = #states, r = #characters -- In Worst Case**/
        // FIXME
        int rsaNumstates = (numstates + 3)*characters.size();

        /** New Alphabet. RSA does not contain Epsilon.**/
        Set<Character> rsaCharacters = new HashSet<>();
        for(Character c: characters){
            // FIXME how the fuck is this done
            if(c.compareTo(EPSILON.toCharArray()[0]) != 0){
                // if C != Epsilon -> Add to rsaChars
                rsaCharacters.add(c);
            }
        }
        /**New Initialstate. Set up just to be sure**/
        int rsaInitialstate = 0;

        // FIXME subject to change
        Set<Integer> rsaAcceptingStates = this.acceptingStates;

        RSA morph = new __RSA(rsaNumstates, rsaCharacters, rsaAcceptingStates, rsaInitialstate);

        /** Complete overhaul of tables and states in general -- Dont overwrite old one as to have a lookup**/
        // Fixme Alternatives to this fuckshit of a ploughin code
        //// Problem: We have no method to properly handle the transitiontables

        // Copy table to allow view & recreate this 1
        Set<String>[][] nfaTransitions = this.transitions;
        transitions = initializeTransitionTable(rsaNumstates);

        for(int i = 0; i < nfaTransitions.length; i++){            // For every fromstate
            for(int j = 0; j < nfaTransitions[0].length; j++){     // .. for every toState
                for(String s: nfaTransitions[i][j]){               // .. for every String within table
                    char[] singleTransition = s.toCharArray();        // .. transform to transition
                    if(singleTransition.length > 0){                  // .. (but only if string not empty)
                        for(int k = 0; k < singleTransition.length; k++){
                            // FIXME transition table is off. Vgl.: Skizze
                            if(k == singleTransition.length - 1){
                                // This is the last element of the string. Point towards correct state
                                setTransition(i, singleTransition[k]+"", j);
                            }else{
                                // Required offset not to point to ''correct'' state
                                setTransition(i+numstates, singleTransition[k]+"", j+numstates);
                            }
                        }
                    }
                }
            }
        }

        return morph;
    }

    @Override
    public Boolean accepts(String w) throws IllegalCharacterException {
        RSA rsa = this.toRSA();
        for(Character c : w.toCharArray()){
            rsa.doStep(c);
        }

        if(rsa.getAcceptingStates().contains(rsa.getActState())) return true;
        else return false;
    }

    @Override
    public Boolean acceptsNothing() {
    // FIXME This only assumes true if set is empty.
        if(acceptingStates.isEmpty()) return true;
        else if(acceptsEpsilonOnly() && !acceptsEpsilon()) return true;
        return false;
    }

    @Override
    public Boolean acceptsEpsilonOnly() {
        if(acceptsEpsilon() && !testAcceptance()) return true;
        else return false;
    }

    /**
     * Generate inline-Tests to determine whether this accepts Epsilon only or not.
     * @return true if some word in Alphabet has been accepted
     */
    private boolean testAcceptance() {
        Set<String> superSet = new HashSet<>();
        // Kreuzprodukt über alle möglichen Charaktere.
        for(Character c1 : characters){
            for(Character c2: characters){
                superSet.add(c1.toString()+c2.toString());
            }
            superSet.add(c1.toString());
        }
        // If something is accepted, return true.
        for(String s : superSet){
            if(this.accepts(s)) return true;
        }
        return false;

    }

    @Override
    public Boolean acceptsEpsilon() {
        if(accepts(EPSILON)) return true;
        else return false;

    }

    @Override
    public Boolean isInfinite() {
        // Taken from master
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
// TODO
        return null;
    }

    @Override
    public boolean equals(Object b) {
        // Enable overload to check on equal terms
        // FIXME -thods below. Not sure if correct
        if(this instanceof NFA) return equals((NFA) b);
        if(this instanceof DFA) return equals((DFA) b);
        if(this instanceof RSA) return equals((RSA) b);
        // ABORT MISSION
        return false;
    }

    private boolean equals(NFA b){
        if(this.complement().intersection(b.complement()).getAcceptingStates().isEmpty()) return true;
        return false;
    }

    private boolean equals(DFA b){
        if(this.complement().intersection(b.complement()).getAcceptingStates().isEmpty()) return true;

        return false;
    }

    private boolean equals(RSA b){
        if(this.complement().intersection(b.complement()).getAcceptingStates().isEmpty()) return true;

        return false;
    }


    @Override
    public Boolean equalsPlusAndStar() {
// L* = L+ u {EPSILON}
        return acceptsEpsilon();
    }
}
