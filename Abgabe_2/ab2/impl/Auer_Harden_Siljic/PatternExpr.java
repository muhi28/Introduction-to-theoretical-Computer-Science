package ab2.impl.Auer_Harden_Siljic;

public class PatternExpr {
    private String str;
    private boolean kleene; // can be repeated an arbitrary number of times
    private boolean wildcard; // read any symbol at this position

    public PatternExpr(String str, boolean kleene) {
        this.str = str;
        this.kleene = kleene;
        this.wildcard = false;
    }

    public void print() {
        System.out.println("*: "+kleene+", .: "+wildcard+", "+str);
    }

    public PatternExpr() {
        this.wildcard = true;
    }

    public static PatternExpr newWildcard() {
        return new PatternExpr();
    }

    public String getStr() {
        return str;
    }

    public boolean isKleene() {
        return kleene;
    }

    public boolean isWildcard() {
        return wildcard;
    }
}
