package ab2.impl.Auer_Harden_Siljic;

import ab2.NFA;
import ab2.RSA;
import ab2.fa.exceptions.IllegalCharacterException;

import java.util.Set;

public class __NFA implements NFA {
    public __NFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
    }

    @Override
    public Set<Character> getSymbols() {
        return null;
    }

    @Override
    public Set<Integer> getAcceptingStates() {
        return null;
    }

    @Override
    public int getInitialState() {
        return 0;
    }

    @Override
    public boolean isAcceptingState(int s) throws IllegalStateException {
        return false;
    }

    @Override
    public Set<String>[][] getTransitions() {
        return new Set[0][];
    }

    @Override
    public void setTransition(int fromState, String s, int toState) throws IllegalStateException, IllegalCharacterException {

    }

    @Override
    public void clearTransitions(int fromState, String s) throws IllegalStateException {

    }

    @Override
    public Set<Integer> getNextStates(int state, String s) throws IllegalCharacterException, IllegalStateException {
        return null;
    }

    @Override
    public int getNumStates() {
        return 0;
    }

    @Override
    public NFA union(NFA a) {
        return null;
    }

    @Override
    public NFA intersection(NFA a) {
        return null;
    }

    @Override
    public NFA minus(NFA a) {
        return null;
    }

    @Override
    public NFA concat(NFA a) {
        return null;
    }

    @Override
    public NFA complement() {
        return null;
    }

    @Override
    public NFA kleeneStar() {
        return null;
    }

    @Override
    public NFA plus() {
        return null;
    }

    @Override
    public RSA toRSA() {
        return null;
    }

    @Override
    public Boolean accepts(String w) throws IllegalCharacterException {
        return null;
    }

    @Override
    public Boolean acceptsNothing() {
        return null;
    }

    @Override
    public Boolean acceptsEpsilonOnly() {
        return null;
    }

    @Override
    public Boolean acceptsEpsilon() {
        return null;
    }

    @Override
    public Boolean isInfinite() {
        return null;
    }

    @Override
    public Boolean isFinite() {
        return null;
    }

    @Override
    public Boolean subSetOf(NFA a) {
        return null;
    }

    @Override
    public boolean equals(Object b) {
        return false;
    }

    @Override
    public Boolean equalsPlusAndStar() {
        return null;
    }
}
