package ab2;

/**
 * Schnittstelle eines RSA. Der Automat numeriert die Zustände von 0 * bis numStates-1 (siehe {@link ab2.FAFactory}).
 * 
 * @author Raphael Wigoutschnigg
 * 
 */
public interface RSA extends DFA {
	/**
	 * Erzeugt einen minimalen RSA, der die selbe Sprache aktzeptiert
	 * @return minimaler Automat
	 */
	public RSA minimize();
}
