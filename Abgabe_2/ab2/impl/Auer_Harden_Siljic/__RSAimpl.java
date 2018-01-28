package ab2.impl.Auer_Harden_Siljic;

import ab2.FAFactory;
import ab2.RSA;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class __RSAimpl extends __DFAimpl implements RSA {

    public __RSAimpl(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState) {
        super(numStates, characters, acceptingStates, initialState);
    }

    @Override
    public RSA minimize() {

//        System.out.println("Minimize_begin(): "+this.getNumStates());

        // INIT
        //
        // create tuple sets
        ArrayList<Tup> dTups = new ArrayList<>();
        ArrayList<Tup> equivTups = new ArrayList<>();
        // fill tuple sets
        for (int left = 0; left < getTransitions().length; left++) {
            for (int right = 0; right < getTransitions()[0].length; right++) {
                if (left == right) {
                    dTups.add(new Tup(left, right));
                } else if (isAcceptingState(left) == isAcceptingState(right)) {
                    equivTups.add(new Tup(left, right));
                }
            }
        }

        // MINIMIZE
        boolean shrinked;
        ArrayList<Tup> removed = new ArrayList<>();

        do {
            shrinked = false;

            // look at all tuples for redundancies
            for (Tup tup: equivTups) {
                boolean shouldStay = true;
                // check for all symbols
                for (Character symbol: getSymbols()) {
                    int left = getNextState(tup.left, symbol);
                    int right = getNextState(tup.right, symbol);
                    Tup checkTup = new Tup(left, right);

                    if (!equivTups.contains(checkTup) && !dTups.contains(checkTup)) {
                        shouldStay = false;
                        break;
                    }
                }

                if (!shouldStay) {
                    // mark tup for removal
                    removed.add(tup);
                }
            }

            // shrink tuple set and repeat if necessary
            if (removed.size() > 0) {
                equivTups.removeAll(removed);
                removed.clear();
                shrinked = true;
            }
        } while (shrinked);

        // UPDATE
        //
        // calculate new number and set of states
        ArrayList<Tup> newStates = getNewStates(equivTups);
        int numStates = newStates.size();

        // update accepting states
        Set<Integer> newAcceptingStates = updateAcceptingStates(newStates);

        // REBUILD

        FAFactory factory = new FAFactoryImpl();
        RSA rsa = factory.createRSA(numStates, getSymbols(), newAcceptingStates, getInitialState());

        // define transitions
        for (int from = 0; from < getTransitions().length; from++) {
            for (int to = 0; to < getTransitions()[0].length; to++) {
                Set<String> transitions = getTransitions()[from][to];
                if (transitions != null && transitions.size() > 0) {
                    // map transitions to new set with merged states
                    int newFrom = mapToNewState(from, newStates);
                    int newTo = mapToNewState(to, newStates);
                    // add transitions
                    for (String str: transitions) {
                        rsa.setTransition(newFrom, str, newTo);
                    }
                }
            }
        }


//        System.out.println("Minimize_end(): "+rsa.getNumStates());

        return rsa;
    }

    private ArrayList<Tup> getNewStates(ArrayList<Tup> equivTups) {
        ArrayList<Tup> newStates = new ArrayList<>();
//        int numStates = equivTups.size();

        for (int state = 0; state < getTransitions().length; state++) {
            boolean inTup = false;
            for (Tup tup: equivTups) {
                if (tup.left == state || tup.right == state) {
                    inTup = true;
                }
            }

            if (!inTup) {
//                numStates++;
                newStates.add(new Tup(state, -1));
            }
        }
        newStates.addAll(equivTups);

        return newStates;
    }

    private Set<Integer> updateAcceptingStates(ArrayList<Tup> newStates) {
        Set<Integer> newAcceptingStates = new TreeSet<>();

        for (int n=0; n<newStates.size(); n++) {
            Tup tup = newStates.get(n);
            if (getAcceptingStates().contains(tup.left) ||
                    (tup.right >= 0 && getAcceptingStates().contains(tup.right)) ) {
                newAcceptingStates.add(n);
            }
        }

        return newAcceptingStates;
    }

    private int mapToNewState(int state, ArrayList<Tup> tups) {
        for (int n=0; n<tups.size(); n++) {
            Tup tup = tups.get(n);
            if (tup.left == state || tup.right == state)
                return n;
        }

        throw new IllegalStateException("Didn't find state in new states. This shouldn't ever happen.");
    }
}
