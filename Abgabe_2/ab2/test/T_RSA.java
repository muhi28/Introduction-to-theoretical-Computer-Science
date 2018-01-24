package ab2.test;

import ab2.RSA;
import ab2.impl.Auer_Harden_Siljic.FAFactoryImpl;
import ab2.impl.Auer_Harden_Siljic.__RSAimpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class T_RSA {

// TODO :: Methods for RSA

    private FAFactoryImpl FA = new FAFactoryImpl();

    private int initState, secondInitState;
    private int numStates, secondNumStates;
    private Set<Character> characters, secondCharacters;
    private Set<Integer> acceptingStates, secondAcceptingStates;
    private RSA rsa, secondRsa;


    @Before
    public void initializeAutomaton() {

        initState = 0;
        numStates = 4;

        characters = new TreeSet<>(Arrays.asList('a', 'b'));
        acceptingStates = new TreeSet<>(Arrays.asList(3));


        rsa = FA.createRSA(numStates, characters, acceptingStates, initState);


        rsa.setTransition(0, 'a', 1);
        rsa.setTransition(0, 'b', 2);
        rsa.setTransition(1, 'a', 1);
        rsa.setTransition(1, 'b', 3);
        rsa.setTransition(2, 'a', 2);
        rsa.setTransition(2, 'b', 3);
        rsa.setTransition(3, 'a', 3);
        rsa.setTransition(3, 'b', 3);


        initMinimizedAutomaton();
    }


    @Test
    public void RSA_minimize()
    {

        Assert.assertEquals(secondRsa, rsa.minimize());
    }

    private void initMinimizedAutomaton() {
        secondInitState = 0;
        secondNumStates = 3;

        secondCharacters = new TreeSet<>(Arrays.asList('a', 'b'));
        secondAcceptingStates = new TreeSet<>(Arrays.asList(2));


        secondRsa = FA.createRSA(numStates, characters, acceptingStates, initState);


        secondRsa.setTransition(0, 'a', 1);
        secondRsa.setTransition(0, 'b', 1);
        secondRsa.setTransition(1, 'a', 1);
        secondRsa.setTransition(1, 'b', 2);
        secondRsa.setTransition(2, 'a', 2);
        secondRsa.setTransition(2, 'b', 2);

    }
}
