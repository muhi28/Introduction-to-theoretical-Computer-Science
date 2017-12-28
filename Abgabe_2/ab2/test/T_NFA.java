package ab2.test;

import ab2.NFA;
import ab2.impl.Auer_Harden_Siljic.FAFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class T_NFA {
    FAFactoryImpl FA = new FAFactoryImpl();
    NFA nfa1, nfa2;
    int initState1, initState2;
    int numStates1, numStates2;
    Set<Character> characters1, characters2;
    Set<Integer> acceptingStates1, acceptingStates2;

    /**
     * Tests every monotonous method available to one and one NFA only
     */
    @Test
    public void NFA_Creation(){
        initState1 = 0;
        numStates1 = 3;
        characters1 = new TreeSet<>(Arrays.asList('a', 'b'));
        acceptingStates1 = new TreeSet<>(Arrays.asList(2));
        try{
            nfa1 = FA.createNFA(numStates1, characters1, acceptingStates1, initState1);
        }catch(Exception e){
            e.printStackTrace();
        }

        nfa1.setTransition(0, "a", 1);
        nfa1.setTransition(0, "a", 2);
        nfa1.setTransition(0, "b", 0);
        nfa1.setTransition(1, "a", 0);
        nfa1.setTransition(1, "b", 1);
        nfa1.setTransition(1, "b", 0);
        nfa1.setTransition(2, "a", 2);
        nfa1.setTransition(2, "b", 0);
        nfa1.setTransition(2, "ab", 1);


/*  Sample 1.:
    0:    0   --> a -->   1   0   --> b -->   0
          0   --> a -->   2
    1:    1   --> a -->   0   1   --> b -->   1
                              1   --> b -->   0
    2:    2   --> a -->   2   2   --> b -->   0
          2   --> ab ->   1

  ⬐--   +---+  -----a------>   +---+  ---⬎
  b     | 0 |                  | 1 |      b
  ↳---> +---+ <-----a,b------  +---+   ---↲
        |   Λ                  /
        |   |                 /
        a   b     ___ ab ____/
        |   |    /
        V   |   /
        +---+ ---⬎
        |!2!|    a
        +---+ ---↲
☩*/

        Assert.assertEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('a','b')));
        Assert.assertNotEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('a','c')));
        Assert.assertNotEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('d','b')));
        Assert.assertNotEquals(nfa1.getSymbols(), asTreeSet(Arrays.asList('d','c')));

        Assert.assertEquals(nfa1.getAcceptingStates(), asTreeSet(Arrays.asList(2)));
        Assert.assertNotEquals(nfa1.getAcceptingStates(), asTreeSet(Arrays.asList(0,2)));
        Assert.assertNotEquals(nfa1.getAcceptingStates(), asTreeSet(Arrays.asList(0,1)));

        Assert.assertEquals(nfa1.getInitialState(), 0);
        Assert.assertNotEquals(nfa1.getInitialState(), 1);
        Assert.assertNotEquals(nfa1.getInitialState(), Arrays.asList(0,2));

        Assert.assertTrue(nfa1.isAcceptingState(2));
        Assert.assertFalse(nfa1.isAcceptingState(0));

        Assert.assertEquals(nfa1.getNextStates(0, "a"), asTreeSet(Arrays.asList(1,2)));
        Assert.assertEquals(nfa1.getNextStates(2,"a"), asTreeSet(Arrays.asList(2)));
        Assert.assertEquals(nfa1.getNextStates(2,"ab"), asTreeSet(Arrays.asList(1)));
        Assert.assertNotEquals(nfa1.getNextStates(2, "a"), asTreeSet(Arrays.asList(1,2)));

        Assert.assertEquals(nfa1.getTransitions()[0][1], asTreeSet(Arrays.asList("a")));
        Assert.assertNull(nfa1.getTransitions()[1][2]);
        Assert.assertNotEquals(nfa1.getTransitions()[0][2], asTreeSet(Arrays.asList("a", "b")));

        nfa1.clearTransitions(2, "ab");
        Assert.assertNotEquals(nfa1.getNextStates(2, "ab"), asTreeSet(Arrays.asList(1)));

        // TODO: Find some test to fuck up numStates
        Assert.assertNotEquals(nfa1.getNumStates(), 2);
    }

    @Test
    public void NFA_Union(){
        Assert.assertFalse(true);
    }


    @Test
    public void NFA_Intersection()
    {
// TODO ::
        Assert.assertFalse(true);
    }

    @Test
    public void NFA_Complement()
    {
// TODO ::
        Assert.assertFalse(true);
    }

    @Test
    public void NFA_Concatination()
    {
        initState1 = 0;     initState2 = 0;
        numStates1 = 2;     numStates2 = 1;
        characters1 = new TreeSet<Character>(Arrays.asList('a'));
        characters2 = new TreeSet<Character>(Arrays.asList('c'));
        acceptingStates1 = new TreeSet<>(Arrays.asList(1));
        acceptingStates2 = new TreeSet<>(Arrays.asList(0));

        nfa1 = FA.createNFA(numStates1, characters1, acceptingStates1, initState1);
        nfa2 = FA.createNFA(numStates2, characters2, acceptingStates2, initState2);

        nfa1.setTransition(0,"a",1);
        nfa2.setTransition(0,"c",0);

/* Sample 2:
    NFA1:                       | NFA2:
    +---+              +---+    |   +---+ --⬎
    | 0 |   --- a ---> |!1!|    |   |!0!|   c
    +---+              +---+    |   +---+ --↲
                                |
☩*/

        NFA nfa3 = nfa1.concat(nfa2);

/* Sample 2.union:
    +---+              +---+            +---+ --⬎
    | 0 |   --- a ---> | 1 | ---ε--->   |!0!|   c
    +---+              +---+            +---+ --↲
                       ↳∉F3
☩*/
        /** FIXME :: Basic tests on NFA3 -- Needs confirmation
         * Following commentaries are probably depending on implementation on union
         */
        Assert.assertEquals(nfa3.getSymbols(), asTreeSet(Arrays.asList('a','c')));
        Assert.assertEquals(nfa3.getAcceptingStates(), asTreeSet(Arrays.asList(2)));
        Assert.assertEquals(nfa3.getInitialState(), 0);
        Assert.assertTrue(nfa3.isAcceptingState(2));
        Assert.assertFalse(nfa3.isAcceptingState(1));

        // FIXME :: Test 3 -- On nonexistant transition?
        Assert.assertEquals(nfa3.getTransitions()[0][1], asTreeSet(Arrays.asList("a")));
        Assert.assertEquals(nfa3.getTransitions()[1][2], asTreeSet(Arrays.asList("")));    // "" = ε
        Assert.assertNull(nfa3.getTransitions()[0][0]);

        Assert.assertTrue(nfa3.accepts("a"));
        Assert.assertTrue(nfa3.accepts("acccccccccccccccccccccccccccccccccc"));
        Assert.assertFalse(nfa3.accepts("cac"));
    }
    @Test
    public void NFA_KleeneStar()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_Plus()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_Minus()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_toRsa()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_Acceptance(){
        initState1 = 0;
        numStates1 = 2;
        characters1 = new TreeSet<Character>(Arrays.asList('a'));
        acceptingStates1 = new TreeSet<>(Arrays.asList(1));
        nfa1 = FA.createNFA(numStates1, characters1, acceptingStates1, initState1);

        nfa1.setTransition(0,"a", 1);
        Assert.assertTrue(nfa1.accepts("a"));
    }
    @Test
    public void NFA_AcceptsNothing()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_AcceptsEpsilonOnly()
    {
// TODO ::
        Assert.assertFalse(true);
    }

    @Test
    public void NFA_AcceptsEpsilon()
    {
// TODO ::
        Assert.assertFalse(true);
    }

    @Test
    public void NFA_isInfinite()
    {
// TODO ::
        Assert.assertFalse(true);
    }

    @Test
    public void NFA_isFinite()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_subSetOf()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_equals()
    {
// TODO ::
        Assert.assertFalse(true);
    }
    @Test
    public void NFA_equalsPlusAndStar()
    {
// TODO ::
        Assert.assertFalse(true);
    }

    private TreeSet<String> asTreeSet(List l)
    {
        return new TreeSet<String>(l);
    }
}
