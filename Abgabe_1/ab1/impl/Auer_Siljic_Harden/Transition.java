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

    public Transition(int fromState, int tapeRead, char symbolRead,
                      int toState, int tapeWrite, char symbolWrite,
                      int tapeMove, TM.Movement movement) {
        this.tapeRead = tapeRead;
        this.tapeWrite = tapeWrite;
        this.fromState = fromState;
        this.tapeMove = tapeMove;
        this.toState = toState;
        this.symbolRead = symbolRead;
        this.symbolWrite = symbolWrite;
        this.movement = movement;
    }

    public int getTapeRead() {
        return tapeRead;
    }

    public int getTapeWrite() {
        return tapeWrite;
    }

    public int getTapeMove() {
        return tapeMove;
    }

    public int getFromState() {
        return fromState;
    }

    public int getToState() {
        return toState;
    }

    public char getSymbolRead() {
        return symbolRead;
    }

    public char getSymbolWrite() {
        return symbolWrite;
    }

    public TM.Movement getMovement() {
        return movement;
    }
}
