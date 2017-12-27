package ab2;

import java.util.Set;

/**
 * Schnittstelle zur Erzeugung von endlichen Automaten (Factory Pattern)
 * 
 * @author Raphael Wigoutschnigg
 *
 */
public interface FAFactory {
	/**
	 * Erzeugt einen NFA. Die Zustände des Automaten sind 0-indiziert, dh der erste
	 * Zustand trägt den Wert 0
	 * 
	 * @param numStates
	 *            Anzahl Zustände des Automaten
	 * @param characters
	 *            Mögliche Zeichenmenge
	 * @param acceptingStates
	 *            Akzeptierender Zustand (0-indiziert)
	 * @param initialState
	 *            Startzutand (0-indiziert)
	 * @return einen NFA
	 */
	public NFA createNFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState);

	/**
	 * Erzeugt einen DFA. Die Zustände des Automaten sind 0-indiziert, dh der erste
	 * Zustand trägt den Wert 0
	 * 
	 * @param numStates
	 *            Anzahl Zustände des Automaten
	 * @param characters
	 *            Mögliche Zeichenmenge
	 * @param acceptingStates
	 *            Akzeptierender Zustand (0-indiziert)
	 * @param initialState
	 *            Startzutand (0-indiziert)
	 * @return einen DFA
	 */
	public DFA createDFA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState);

	/**
	 * Erzeugt einen RSA. Die Zustände des Automaten sind 0-indiziert, dh der erste
	 * Zustand trägt den Wert 0
	 * 
	 * @param numStates
	 *            Anzahl Zustände des Automaten
	 * @param characters
	 *            Mögliche Zeichenmenge
	 * @param acceptingStates
	 *            Akzeptierender Zustand (0-indiziert)
	 * @param initialState
	 *            Startzutand (0-indiziert)
	 * @return einen RSA
	 */
	public RSA createRSA(int numStates, Set<Character> characters, Set<Integer> acceptingStates, int initialState);

	/**
	 * Erzeugt einen RSA, der zur Suche des übergebenen Patterns in Texten verwendet
	 * werden kann. Das Muster darf nur aus Zeichen und den Symbolen * und .
	 * bestehen. Das Symbol * bedeutet, dass das Zeichen davor beliebig oft
	 * vorkommen kann. Das Symbol . bedeutet, dass ein beliebiges Zeichen gelesen
	 * werden kann. Beispiele für Muster wären "abcd." oder "ab*cd". Der Automat
	 * muss sich immer dann in einem akzeptierenden Zustand befinden, wenn das
	 * Muster im Text gefunden wurde. Das bedeutet, dass der Automat jedes Vorkommen
	 * des Musters im Text anzeigt (findet). Beispielsweise muss sich der Automat
	 * für das Muster "abab" im Text "ababab" zweimal in einem akzeptierenden
	 * Zustand befinden. Einmal beim vierten gelesenen Zeichen und einmal beim
	 * sechten gelesenen Zeichen.
	 * 
	 * @param pattern
	 *            Das Muster, mach welchem gesucht werden soll.
	 * @return
	 */
	public RSA createPatternMatcher(String pattern);
}