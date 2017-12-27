package ab2.impl.Auer_Harden_Siljic;

public class Transition {
    private int fromState;
    private int toState;
    private String str;

    public Transition(int fromState, int toState, String str) {
        this.fromState = fromState;
        this.toState = toState;
        this.str = str;
    }

    public int getFromState() {
        return fromState;
    }

    public void setFromState(int fromState) {
        this.fromState = fromState;
    }

    public int getToState() {
        return toState;
    }

    public void setToState(int toState) {
        this.toState = toState;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
