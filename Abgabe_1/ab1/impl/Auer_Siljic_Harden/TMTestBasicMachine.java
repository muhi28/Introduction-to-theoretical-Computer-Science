package ab1.impl.Auer_Siljic_Harden;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ab1.impl.Auer_Siljic_Harden.TMImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ab1.TM;

public class TMTestBasicMachine {

    private static TM machine;

    @BeforeClass
    public static void setup() {
        machine = new TMImpl();

        machine.setNumberOfTapes(1);
        machine.setSymbols(new HashSet<>(Arrays.asList('a', '#')));
        machine.addTransition(0, 3, '#', 1, '#', TM.Movement.Left);
        machine.addTransition(0, 1, 'a', 2, '#', TM.Movement.Stay);
        machine.addTransition(0, 2, '#', 1, '#', TM.Movement.Left);
        machine.addTransition(0, 1, '#', 0, '#', TM.Movement.Stay);
        machine.setInitialState(3);
        machine.setInitialTapeContent(0, "#aaaaa".toCharArray());

        while (!machine.isHalt())
            machine.doNextStep();
    }

    @Test
    public void testLeft() {
        TM.TMConfig config = machine.getTMConfig(0);
        Assert.assertArrayEquals(config.getLeftOfHead(), new char[0]);

        Arrays.toString(config.getLeftOfHead());
    }

    @Test
    public void testHead() {
        TM.TMConfig config = machine.getTMConfig(0);
        Assert.assertEquals(config.getBelowHead(), '#');
    }

    @Test
    public void testRight() {
        TM.TMConfig config = machine.getTMConfig(0);

        Assert.assertArrayEquals(config.getRightOfHead(), new char[0]);

    }


}
