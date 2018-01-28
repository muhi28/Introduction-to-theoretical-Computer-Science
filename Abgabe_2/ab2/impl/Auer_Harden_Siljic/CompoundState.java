package ab2.impl.Auer_Harden_Siljic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class CompoundState implements Comparable {
    public static ArrayList<CompoundState> compoundStates = new ArrayList<CompoundState>();
//    private static int nextID = 0;

    public int id;
    public Set<StateGraph> thisState;
//    public ArrayList<Pair<String, CompoundState>> nextStates;
    public ArrayList<Pair<String, Integer>> nextStates;
    public boolean isAcceptingState;
    public CompoundState parent;

    public CompoundState(Set<StateGraph> state) {
        this.thisState = state;
        this.nextStates = new ArrayList<>();
        this.id = compoundStates.size();

        compoundStates.add(this);
    }

    public Integer getDuplicate(CompoundState possibleDup, String sym, boolean firstStep) {
        // check if duplicate
        if (this.equals(possibleDup)) {
            if (firstStep) {
//                this.nextStates.add(new Pair<>(sym, this));
                this.nextStates.add(new Pair<>(sym, this.id));
            }
            return this.id;
        } else {
            if (this.parent != null) { // if this is not the graph's root
                Integer result = this.parent.getDuplicate(possibleDup, sym, false);

                if (result == null) return null;
                else {
                    if (firstStep) {
                        this.nextStates.add(new Pair<>(sym, result));
                    }
                    return result;
                }
            } else { // root and not found any duplicate -> return null
                return null;
            }
        }
    }

    public void print() {
        System.out.println("CompoundState:");
        System.out.println("id: "+this.id);
        System.out.println("parent: "+(this.parent!=null));
        System.out.println("states:");
        for (StateGraph sg: this.thisState) {
            System.out.println("> "+sg.id);
        }
        System.out.println("next:");
        for (Pair<String, Integer> pair: nextStates) {
            System.out.println("-> "+pair.getKey()+" | "+compoundStates.get(pair.getValue()).id);
        }

        System.out.println("Children:");
        for (Pair<String, Integer> pair: nextStates) {
            compoundStates.get(pair.getValue()).print();
        }
    }

    @Override
    public int compareTo(Object o) {
        Integer other = (Integer) o;
        System.out.println("CompareTo: "+this.id+", "+other);

        if (this.id == other) {
            return 0;
        } else {
            if (this.id > other) {
                return 1;
            } else return -1;
        }

//        CompoundState other = (CompoundState) o;
//
//        System.out.println("compareTo: "+(this.thisState == other.thisState)+", "+(this.isAcceptingState == other.isAcceptingState));
//        System.out.println("this: "+thisState.size());
//        System.out.println("other: "+other.thisState.size());
//
//        if (this.thisState == other.thisState &&
//                this.isAcceptingState == other.isAcceptingState) {
////            Iterator thisIter = this.nextStates.iterator();
////            Iterator thatIter = other.nextStates.iterator();
////            while(true) {
////                Pair<String, Integer>
////            }
//
//            return 0;
//        } else return 1;
    }
}
