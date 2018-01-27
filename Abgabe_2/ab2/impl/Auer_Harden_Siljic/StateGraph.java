package ab2.impl.Auer_Harden_Siljic;

import java.util.ArrayList;

public class StateGraph implements Comparable {
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
