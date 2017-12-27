package ab2.impl.Auer_Harden_Siljic;

public class State {
    private boolean isAccepting;
    private int id;

    public State(boolean isAccepting, int id) {
        this.isAccepting = isAccepting;
        this.id = id;
    }

    public boolean isAccepting() {
        return isAccepting;
    }

    public void setAccepting(boolean accepting) {
        isAccepting = accepting;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
