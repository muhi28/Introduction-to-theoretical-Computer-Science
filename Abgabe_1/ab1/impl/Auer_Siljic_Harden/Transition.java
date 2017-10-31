package ab1.impl.Auer_Siljic_Harden;

import ab1.TM;

public class Transition {
    private int tape;
    private int fromState;
    private int toState;
    private char symbolRead;
    private char symbolWrite;
    private ab1.TM.Movement movement;

    public Transition(int tape, int fromState, int toState, char symbolRead, char symbolWrite, TM.Movement movement) {
        this.tape = tape;
        this.fromState = fromState;
        this.toState = toState;
        this.symbolRead = symbolRead;
        this.symbolWrite = symbolWrite;
        this.movement = movement;
    }

    public boolean checkTransition(int fromState, char symbolRead) {
        return fromState != this.fromState || symbolRead != this.symbolRead;
    }

    public int getTape() {
        return tape;
    }

    public void setTape(int tape) {
        this.tape = tape;
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

    public char getSymbolRead() {
        return symbolRead;
    }

    public void setSymbolRead(char symbolRead) {
        this.symbolRead = symbolRead;
    }

    public char getSymbolWrite() {
        return symbolWrite;
    }

    public void setSymbolWrite(char symbolWrite) {
        this.symbolWrite = symbolWrite;
    }

    public TM.Movement getMovement() {
        return movement;
    }

    public void setMovement(TM.Movement movement) {
        this.movement = movement;
    }
}
