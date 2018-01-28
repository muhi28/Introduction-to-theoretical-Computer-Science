package ab2.impl.Auer_Harden_Siljic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class StateGraph implements Comparable {
    private static final String EPSILON = "";

    private static int nextIntermediateID = -1;

    public int id;
    public ArrayList<StateGraph> next;
    public ArrayList<String> nextSymbols;

    public StateGraph() {
        this.id = nextIntermediateID--; // intermediate state/node
        this.next = new ArrayList<>();
        this.nextSymbols = new ArrayList<>();
    }

    public StateGraph(int id) {
        this.id = id;
        this.next = new ArrayList<>();
        this.nextSymbols = new ArrayList<>();
    }

    public CompoundState asCompoundStates() {
//        ArrayList<CompoundState> results = new ArrayList<>();

//        return this.compileCompoundStates(results, EPSILON, true);
        return this.compileCompoundStates(null, null);
    }

//    private ArrayList<CompoundState> compileCompoundStates(ArrayList<CompoundState> states) {//}, String currentSymbol, boolean processedWord) {
    private CompoundState compileCompoundStates(CompoundState parent, String usedSym) {

        // create lists for grouping all following states with identical symbols
        ArrayList<String> nextUniqueSymbols = new ArrayList<>();
        for (String s: nextSymbols) {
            if (!nextUniqueSymbols.contains(s)) {
                nextUniqueSymbols.add(s);
            }
        }
        ArrayList<ArrayList<StateGraph>> nextCompoundCandidates = new ArrayList<>();
        for (int n=0; n<nextUniqueSymbols.size(); n++) {
            nextCompoundCandidates.add(new ArrayList<>());
        }

        // find all following states for each symbol
        // afterwards all following states are grouped into symbols, ready to be collected into CompoundStates
        for (int i=0; i<next.size(); i++) {
            for (int u=0; u<nextUniqueSymbols.size(); u++) {
                if (nextSymbols.get(i) == nextUniqueSymbols.get(u)) {
                    nextCompoundCandidates.get(u).add(next.get(i));
                    break;
                }
            }
        }
        // identify the index of the EPSILON-symbol states(if any)
        boolean hasEpsilonTransitions = false;
        int epsilonIdx = 0;
        for (String s: nextUniqueSymbols) {
            if (s.length() == 0) {
                hasEpsilonTransitions = true;
                break;
            }
            epsilonIdx++;
        }
        Set<CompoundState> epsilonNexts = new TreeSet<>();
        if (hasEpsilonTransitions) {
            for (int k = 0; k < nextCompoundCandidates.get(epsilonIdx).size(); k++) {
                epsilonNexts.add(nextCompoundCandidates.get(epsilonIdx).get(k).asCompoundStates());
            }
        }



        // create CompoundState from this StateGraph instance and all following states reachable by epsilon transitions
        Set<StateGraph> thisStates = new TreeSet<>();
        thisStates.add(this);
        if (hasEpsilonTransitions) thisStates.addAll(nextCompoundCandidates.get(epsilonIdx));

        CompoundState thisCS = new CompoundState(thisStates);
        thisCS.parent = parent;

        // check for duplicates if this is not root
        if (parent != null) {
            CompoundState original = parent.getDuplicate(thisCS, usedSym, true);
            if (original != null) {
                return null;
            }
        }




        // now get the following states for all states in this CompoundState
        for (int i=0; i<nextUniqueSymbols.size(); i++) {
            CompoundState newCS = new CompoundState(new TreeSet<>());

            for (int k = 0; k < nextCompoundCandidates.get(i).size(); k++) {
                CompoundState tempCS = nextCompoundCandidates.get(i).get(k).compileCompoundStates(thisCS, nextUniqueSymbols.get(i));
                if (tempCS != null) {
                    newCS.thisState.add(nextCompoundCandidates.get(i).get(k));
                    newCS.nextStates.addAll(tempCS.nextStates);
                }
            }

            thisCS.nextStates.add(new Pair<>(nextUniqueSymbols.get(i), newCS));
        }

        System.out.print("new CompoundState: ");
        for(StateGraph sg: thisCS.thisState) {
            System.out.print(sg.id);
        }
        if (thisCS.parent != null ) System.out.println(" | "+thisCS.parent.thisState.iterator().next().id);
        else System.out.println();

        return thisCS;

//        // now recursively add next following states for each symbol
//        if (!processedWord) {
//            for (int i = 0; i < nextUniqueSymbols.size(); i++) {
//                Set<CompoundState> nextTemp = new TreeSet<>(); // FIXME maybe do a real duplicate check instead of relying on set?
//
//                for (int k = 0; k < nextCompoundCandidates.get(i).size(); k++) {
//                    nextTemp.addAll(nextCompoundCandidates.get(i).get(k).asCompoundStates());
//                }
//                // add epsilon transitions if any
//                if (hasEpsilonTransitions && i != epsilonIdx) {
//                    nextTemp.addAll(epsilonNexts);
//                }
//            }
//        }
//        if (processedWord || !processedWord)
//
//        // check if resulting CompoundStates are duplicates, and add to list if not
//        boolean addedNew = false;
//        for (int i=0; i<nextUniqueSymbols.size(); i++) {
//            // if no following states were found for this symbol, skip to next
//
//
//            Set<StateGraph> set = new TreeSet<>();
//            set.addAll(nextCompoundCandidates.get(i));
//
//            CompoundState newCS = new CompoundState(set);
//
//            // check if this CS already exists in list
//            if (!states.contains(newCS)) {
//                states.add(newCS);
//            }
//        }
//
//        // return expanded list
//        return states;

    }

    public int size() {
        int s = 1;
        for (StateGraph sg: this.next) {
            s += sg.size();
        }

        return s;
    }

    public void print() {
        System.out.println(id);

        for (int i = 0; i < next.size(); i++) {
            System.out.println(" -- "+nextSymbols.get(i)+" --> ");
            next.get(i).print();
        }
    }

    public StateGraph getLast() {
        return this.next.get(this.next.size()-1);
    }

    public void addGraph(StateGraph g, String sym) {
        this.next.add(g);
        this.nextSymbols.add(sym);
    }

    @Override
    public int compareTo(Object o) {
        StateGraph other = (StateGraph) o;

        return this.id > other.id ? 1 : this.id < other.id ? -1 : 0;
    }
}
