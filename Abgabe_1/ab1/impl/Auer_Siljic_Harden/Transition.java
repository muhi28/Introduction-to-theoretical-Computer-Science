package ab1.impl.Auer_Siljic_Harden;

import ab1.TM;

public class Transition {
    private int tapeRead;
    private int tapeWrite;
    private int tapeMove;
    private int fromState;
    private int toState;
    private char symbolRead;
    private char symbolWrite;
    private ab1.TM.Movement movement;

    public Transition(int fromState, int tapeRead, char symbolRead, int toState, int tapeWrite, char symbolWrite, int tapeMove, TM.Movement movement) {

        this.tapeRead = tapeRead;
        this.tapeWrite = tapeWrite;
        this.fromState = fromState;
        this.tapeMove = tapeMove;
        this.toState = toState;
        this.symbolRead = symbolRead;
        this.symbolWrite = symbolWrite;
        this.movement = movement;
    }

    public boolean checkTransition(int fromState, char symbolRead) {
        return fromState != this.fromState || symbolRead != this.symbolRead;
    }

    public int getTapeRead() {
        return tapeRead;
    }

    public void setTapeRead(int tapeRead) {
        this.tapeRead = tapeRead;
    }

    public int getTapeWrite() {
        return tapeWrite;
    }

    public void setTapeWrite(int tapeWrite) {
        this.tapeWrite = tapeWrite;
    }

    public int getTapeMove() {
        return tapeMove;
    }

    public void setTapeMove(int tapeMove) {
        this.tapeMove = tapeMove;
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
