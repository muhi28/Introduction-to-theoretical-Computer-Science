package ab2;

import ab2.fa.exceptions.IllegalCharacterException;

/**
 * Schnittstelle eines DFA. Der Automat numeriert die Zustände von 0 * bis numStates-1 (siehe {@link ab2.FAFactory}).
 * 
 * @author Raphael Wigoutschnigg
 * 
 */
public interface DFA extends NFA {

	/**
	 * Setzt den Automaten wieder auf den Startzustand.
	 */
	public void reset();

	/**
	 * 
	 * @return den aktuell aktiven Zustand des Automaten
	 */
	public int getActState();

	/**
	 * Veranlasst den Autoaten, ein Zeichen abzuarbeiten. Ausgehend vom aktuellen
	 * Zustand wird das Zeichen c abgearbeitet und der Automat befindet sich danach
	 * im Folgezustand. Ist das Zeichen c kein erlaubtes Zeichen, so wird eine
	 * IllegalArgumentException geworfen. Andernfalls liefert die Methode den neuen
	 * aktuellen Zustand. Ist kein Folgezustand definiert, wird eine
	 * IllegalStateException geworfen und der aktuell Zustand bleibt erhalten.
	 * 
	 * @param c
	 *            - das abzuarbeitende Zeichen
	 * @throws IllegalArgumentException
	 * @return den aktuellen Zustand nach der Abarbeitung des Zeichens
	 */
	public int doStep(char c) throws IllegalArgumentException, IllegalStateException;

	/**
	 * Liefert den Zustand, der erreicht wird, wenn im Zustand s das Zeichen c
	 * gelesen wird. Ist das Zeichen c kein erlaubtes Zeichen, so wird eine
	 * IllegalArgumentException geworfen. Ist der Zustand s kein erlaubter Zustand,
	 * so wird eine IllegalStateException geworfen.
	 * 
	 * @param s
	 *            - Zustand
	 * @param c
	 *            - Zeichen
	 * @return Folgezustand, oder null, wenn es keinen Folgezustand gibt
	 */
	public Integer getNextState(int s, char c) throws IllegalCharacterException, IllegalStateException;

	/**
	 * Liefert true, wenn der aktuelle Zustand ein aktzeptierender Zustand ist.
	 * 
	 * @return true, wenn der Zustand s ein Endzustand ist. Ansonsten false.
	 */
	public boolean isAcceptingState();

	/**
	 * Spezialisierte setTransiation-Methode, welche einen Character als Übergang bekommt.
	 * @param fromState
	 * @param c
	 * @param toState
	 * @throws IllegalStateException
	 * @throws IllegalCharacterException
	 */
	public void setTransition(int fromState, char c, int toState)
			throws IllegalStateException, IllegalCharacterException;

	/**
	 * Setzt einen Übergang. Wörter dürfen nur aus einem Zeichen bestehen. Das leere Wort ist ebenfalls nicht erlaubt.
	 * 
	 * @param fromState
	 *            Ausgangszustand
	 * @param s
	 *            gelesenes Wort
	 * @param toState
	 *            Folgezustand
	 * @throws IllegalStateException
	 * @throws IllegalCharacterException
	 */
	public void setTransition(int fromState, String s, int toState)
			throws IllegalStateException, IllegalCharacterException;
}