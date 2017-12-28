package ab2.test;

import ab2.NFA;
import ab2.impl.Auer_Harden_Siljic.FAFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class NFA_Test {

    private final FAFactoryImpl FA = new FAFactoryImpl();

    NFA nfa1, nfa2;
    int initState1,
            initState2;

    int numStates1,
            numStates2;

    Set<Character> characters1,
            characters2;

    Set<Integer> acceptingStates1,
            acceptingStates2;


    /**
     * Test the creation of an NFA.
     */
    @Test
    public void createNFA() {

        initState1 = 0;
        numStates1 = 3;

        characters1 = new TreeSet<>(Arrays.asList('a', 'b'));
        acceptingStates1 = new TreeSet<>(Arrays.asList(2));

        nfa1 = FA.createNFA(numStates1, characters1, acceptingStates1, initState1);

        nfa1.setTransition(0, "a", 1);
        nfa1.setTransition(0, "a", 2);
        nfa1.setTransition(0, "b", 0);
        nfa1.setTransition(1, "a", 0);
        nfa1.setTransition(1, "b", 1);
        nfa1.setTransition(1, "b", 0);
        nfa1.setTransition(2, "a", 2);
        nfa1.setTransition(2, "b", 0);
        nfa1.setTransition(2, "ab", 1);

        Assert.assertEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('a', 'b')));
        Assert.assertNotEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('a', 'c')));
        Assert.assertNotEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('d', 'b')));
        Assert.assertNotEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('d', 'c')));

        Assert.assertEquals(nfa1.getAcceptingStates(), asTreeSet(Arrays.asList(2)));
        Assert.assertNotEquals(nfa1.getAcceptingStates(), asTreeSet(Arrays.asList(0, 2)));
        Assert.assertNotEquals(nfa1.getAcceptingStates(), asTreeSet(Arrays.asList(0, 1)));

        Assert.assertEquals(nfa1.getInitialState(), 0);
        Assert.assertNotEquals(nfa1.getInitialState(), 1);
        Assert.assertNotEquals(nfa1.getInitialState(), Arrays.asList(0, 2));

        Assert.assertTrue(nfa1.isAcceptingState(2));
        Assert.assertFalse(nfa1.isAcceptingState(0));

        Assert.assertEquals(nfa1.getNextStates(2, "a"), asTreeSet(Arrays.asList(2)));
        Assert.assertEquals(nfa1.getNextStates(2, "ab"), (Arrays.asList(1)));
        Assert.assertNotEquals(nfa1.getNextStates(2, "a"), (Arrays.asList(1, 2)));

        // FIXME :: Testing on Transitions -- 2nd. Test: On nonexistant transition?
        Assert.assertEquals(nfa1.getTransitions()[0][1], Arrays.asList("a"));
        Assert.assertEquals(nfa1.getTransitions()[1][2], Arrays.asList(null));
        Assert.assertNotEquals(nfa1.getTransitions()[0][2], Arrays.asList("a", "b"));

        nfa1.clearTransitions(2, "ab");
        Assert.assertNotEquals(nfa1.getNextStates(2, "ab"), Arrays.asList(1));

        // TODO: Find some test to fuck up numStates
        Assert.assertNotEquals(nfa1.getNumStates(), 2);

    }

    private TreeSet<String> asTreeSet(List l) {
        return new TreeSet<String>(l);
    }

}
