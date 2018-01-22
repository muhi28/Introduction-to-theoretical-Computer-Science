package ab2.test;

import ab2.DFA;
import ab2.NFA;
import ab2.impl.Auer_Harden_Siljic.FAFactoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class T_DFA {
// TODO :: Tests for DFA

    private FAFactoryImpl FA = new FAFactoryImpl();

    private int initState;
    private int numStates;
    private Set<Character> characters;
    private Set<Integer> acceptingStates;
    private DFA dfa;


    @Before
    public void initializeAutomata() {

        initState = 0;
        numStates = 2;

        characters = new TreeSet<>(Arrays.asList('a', 'b'));
        acceptingStates = new TreeSet<>(Arrays.asList(0, 1));


        try {

            dfa = FA.createDFA(numStates, characters, acceptingStates, initState);

        } catch (Exception e) {
            e.printStackTrace();
        }


        dfa.setTransition(0, 'a', 0);
        dfa.setTransition(0, 'b', 1);
        dfa.setTransition(1, 'b', 1);
        dfa.setTransition(1, 'a', 0);


    }

    @Test
    public void DFA_Reset()
    {

        int currentState = dfa.getActState();

        System.out.println("ECHO::Current State before doStep -> " + currentState + "\n");


        System.out.println("---------- DO STEP ----------");

        System.out.println("Start State -> " + dfa.getActState() + "\n");


        System.out.println("Current State -> " + dfa.getActState() + " || Character " + " [ a ]" + " toState -> " + dfa.getNextState(dfa.getActState(), 'a') + "\n");

        Assert.assertEquals(0, dfa.doStep('a'));

        System.out.println("Current State -> " + dfa.getActState() + " || Character " + " [ b ]" + " toState -> " + dfa.getNextState(dfa.getActState(), 'b') + "\n");

        Assert.assertEquals(1, dfa.doStep('b'));
        System.out.println("Current State -> " + dfa.getActState() + " || Character " + " [ b ]" + " toState -> " + dfa.getNextState(dfa.getActState(), 'b') + "\n");

        //reset the automaton
        dfa.reset();
        Assert.assertEquals(0, dfa.getActState());
    }

    @Test
    public void DFA_GetActState()
    {

        dfa.reset();

        int currentState = dfa.getActState();

        System.out.println("ECHO::Current State before doStep -> " + currentState + "\n");


        System.out.println("---------- DO STEP ----------");

        System.out.println("Start State -> " + dfa.getActState() + "\n");


        System.out.println("Current State -> " + dfa.getActState() + " || Character " + " [ a ]" + " toState -> " + dfa.getNextState(dfa.getActState(), 'a') + "\n");

        Assert.assertEquals(0, dfa.doStep('a'));

        System.out.println("Current State -> " + dfa.getActState() + " || Character " + " [ b ]" + " toState -> " + dfa.getNextState(dfa.getActState(), 'b') + "\n");

        Assert.assertEquals(1, dfa.doStep('b'));
        System.out.println("Current State -> " + dfa.getActState() + " || Character " + " [ b ]" + " toState -> " + dfa.getNextState(dfa.getActState(), 'b') + "\n");


    }
    @Test
    public void DFA_DoStep()
    {
        dfa.reset();

        Assert.assertEquals(0, dfa.doStep('a'));
        Assert.assertEquals(1, dfa.doStep('b'));
        Assert.assertEquals(1, dfa.doStep('b'));
        Assert.assertEquals(0, dfa.doStep('a'));

    }


    @Test
    public void DFA_GetNextState()
    {
        dfa.reset();

        int nextState = dfa.getNextState(0, 'a');

        Assert.assertEquals(0, nextState);

        nextState = dfa.getNextState(0, 'b');

        Assert.assertEquals(1, nextState);

        nextState = dfa.getNextState(1, 'a');

        Assert.assertEquals(0, nextState);

        nextState = dfa.getNextState(1, 'b');

        Assert.assertEquals(1, nextState);

    }
    @Test
    public void DFA_IsAcceptingState()
    {

        dfa.reset();

        Assert.assertEquals(asTreeSet(Arrays.asList(0, 1)), dfa.getAcceptingStates());

        Assert.assertTrue(dfa.isAcceptingState(0));
        Assert.assertTrue(dfa.isAcceptingState(1));
        Assert.assertFalse(dfa.isAcceptingState(2));
    }


    private TreeSet<String> asTreeSet(List l)
    {
        return new TreeSet<String>(l);
    }

}
