package ab2.impl.Auer_Harden_Siljic;

import javafx.util.Pair;

import java.util.Set;
import java.util.TreeSet;

public class CompoundState {
    public Set<Integer> thisState;
    public Set<Pair<String, CompoundState>> nextStates;
    public boolean isAcceptingState;

    public CompoundState(Set<Integer> state) {
        this.thisState = state;
        this.nextStates = new TreeSet<>();
    }
}
