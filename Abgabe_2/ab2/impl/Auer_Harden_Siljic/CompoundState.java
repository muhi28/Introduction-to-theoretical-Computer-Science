package ab2.impl.Auer_Harden_Siljic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class CompoundState implements Comparable {
//    public Set<Integer> thisState;
    public Set<StateGraph> thisState;
    public ArrayList<Pair<String, CompoundState>> nextStates;
    public boolean isAcceptingState;
    public CompoundState parent;

    public CompoundState(Set<StateGraph> state) {
        this.thisState = state;
        this.nextStates = new ArrayList<>();
    }

    public CompoundState getDuplicate(CompoundState possibleDup, String sym, boolean firstStep) {
        // check if duplicate
        if (this.equals(possibleDup)) {
            if (firstStep) {
                this.nextStates.add(new Pair<>(sym, this));
            }
            return this;
        } else {
            if (this.parent != null) { // if this is not the graph's root
                CompoundState result = this.parent.getDuplicate(possibleDup, sym, false);

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

    @Override
    public int compareTo(Object o) {
        CompoundState other = (CompoundState) o;
        if (this.thisState == other.thisState &&
                this.isAcceptingState == other.isAcceptingState) {
//            Iterator thisIter = this.nextStates.iterator();
//            Iterator thatIter = other.nextStates.iterator();
//            while(true) {
//                Pair<String, Integer>
//            }

            return 0;
        } else return 1;
    }
}
