package ab1.test;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import ab1.TM;
import ab1.TM.Movement;
import ab1.TM.TMConfig;
import ab1.impl.Auer_Siljic_Harden.TMImpl;

public class TMTest {

    public TM getMachineOneTape() {
        TM tm = new TMImpl();

        tm.setNumberOfTapes(1);

        tm.setSymbols(new HashSet<Character>(Arrays.asList('a', '#')));

        tm.addTransition(1, 0, 'a', 2, 0, '#', 0, Movement.Stay);
        tm.addTransition(1, 0, '#', 0, 0, '#', 0, Movement.Stay);
        tm.addTransition(2, 0, '#', 1, 0, '#', 0, Movement.Left);
        tm.addTransition(3, 0, '#', 1, 0, '#', 0, Movement.Left);

        tm.setInitialState(3);
        tm.setInitialTapeContent(0, "#aaaaa".toCharArray());

        return tm;
    }

    public TM getMachineTwoTapes() {
        TM tm = new TMImpl();

        tm.setNumberOfTapes(2);

        tm.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));

        int band1 = 0;
        int band2 = 1;

        int h = 0;
        int z1 = 1;
        int z2 = 2;
        int z3 = 3;
        int z4 = 4;
        int z5 = 5;
        int z6 = 6;
        int z7 = 7;
        int z0 = 8;

		/*
         * Kopiere von Band 1 auf Band 2
		 */
        tm.addTransition(z0, band1, '#', z1, band1, '#', band1, Movement.Left);
        // lese Zeichen von Band 1 und schreibe es auf Band 2. Gehe auf Band 0 nach
        // links
        tm.addTransition(z1, band1, 'a', z2, band2, 'a', band1, Movement.Left);
        tm.addTransition(z1, band1, 'b', z2, band2, 'b', band1, Movement.Left);
        tm.addTransition(z1, band1, 'c', z2, band2, 'c', band1, Movement.Left);
        // Gehe auf Band 2 nach rechts
        tm.addTransition(z2, band2, 'a', z1, band2, 'a', band2, Movement.Right);
        tm.addTransition(z2, band2, 'b', z1, band2, 'b', band2, Movement.Right);
        tm.addTransition(z2, band2, 'c', z1, band2, 'c', band2, Movement.Right);
        // Wechse in Zustand 3 und gehe auf Band 2 nach links
        tm.addTransition(z1, band1, '#', z3, band2, '#', band2, Movement.Left);

		/*
		 * Gehe auf Band 2 nach links bis zum ersten Zeichen nach dem #
		 */
        tm.addTransition(z3, band2, 'a', z3, band2, 'a', band2, Movement.Left);
        tm.addTransition(z3, band2, 'b', z3, band2, 'b', band2, Movement.Left);
        tm.addTransition(z3, band2, 'c', z3, band2, 'c', band2, Movement.Left);
        tm.addTransition(z3, band2, '#', z4, band2, '#', band2, Movement.Right);

        // Gehe auf Band 1 einen Schritt nach rechts
        tm.addTransition(z4, band1, '#', z5, band1, '#', band1, Movement.Right);

		/*
		 * Kopiere von Band 2 auf Band 1
		 */
        tm.addTransition(z5, band2, 'a', z6, band1, 'a', band2, Movement.Right);
        tm.addTransition(z5, band2, 'b', z6, band1, 'b', band2, Movement.Right);
        tm.addTransition(z5, band2, 'c', z6, band1, 'c', band2, Movement.Right);
        // Gehe auf Band 1 nach rechts gehen
        tm.addTransition(z6, band1, 'a', z5, band1, 'a', band1, Movement.Right);
        tm.addTransition(z6, band1, 'b', z5, band1, 'b', band1, Movement.Right);
        tm.addTransition(z6, band1, 'c', z5, band1, 'c', band1, Movement.Right);

        // Wechse in Zustand 7 und gehe auf Band 2 nach links
        tm.addTransition(z5, band2, '#', z7, band2, '#', band2, Movement.Left);
		
		/*
		 * Lösche Band 2
		 */
        tm.addTransition(z7, band2, 'a', z7, band2, '#', band2, Movement.Left);
        tm.addTransition(z7, band2, 'b', z7, band2, '#', band2, Movement.Left);
        tm.addTransition(z7, band2, 'c', z7, band2, '#', band2, Movement.Left);

        tm.addTransition(z7, band2, '#', h, band2, '#', band2, Movement.Right);

        tm.setInitialState(z0);
        tm.setInitialTapeContent(0, "#abcabc".toCharArray());
        tm.setInitialTapeContent(1, "#".toCharArray());

        return tm;
    }


    //4 Pts
    @Test
    public void testMachineOneTape() {
        TM tm = getMachineOneTape();

        // Maschine hat 1 Band
        Assert.assertEquals(1, tm.getNumberOfTapes());

        // Lasse die Maschine bis zum Haltezustand laufen
        while (!tm.isHalt())
            tm.doNextStep();

        // Prüfe den Bandinhalt
        TMConfig config = tm.getTMConfig(0);
        Assert.assertArrayEquals(new char[0], config.getLeftOfHead());
        Assert.assertEquals('#', config.getBelowHead());
        Assert.assertArrayEquals(new char[0], config.getRightOfHead());

        Assert.assertEquals(config, tm.getTMConfig().get(0));
    }


    //4 Pts
    @Test
    public void testMachineTwoTapes() {
        TM tm = getMachineTwoTapes();

        // Maschine hat 2 Bänder
        Assert.assertEquals(2, tm.getNumberOfTapes());

        // Lasse die Maschine bis zum Haltezustand laufen
        while (!tm.isHalt()) {
            //System.out.println(tm.getActState() + " " + tm.getTMConfig());
            tm.doNextStep();
        }
        //System.out.println(tm.getActState() + " " + tm.getTMConfig());

        // Prüfe den Bandinhalt
        TMConfig config = tm.getTMConfig(0);
        Assert.assertArrayEquals(new char[]{'#', 'c', 'b', 'a', 'c', 'b', 'a'}, config.getLeftOfHead());
        Assert.assertEquals('#', config.getBelowHead());
        Assert.assertArrayEquals(new char[0], config.getRightOfHead());

        config = tm.getTMConfig(1);
        Assert.assertArrayEquals(new char[]{'#'}, config.getLeftOfHead());
        Assert.assertEquals('#', config.getBelowHead());
        Assert.assertArrayEquals(new char[0], config.getRightOfHead());
    }
}
