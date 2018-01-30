package ab2;

import java.util.Set;

import ab2.fa.exceptions.IllegalCharacterException;

/**
 * Schnittstelle eines NFA. Der Automat numeriert die Zustände von 0 * bis numStates-1 (siehe {@link ab2.FAFactory}).
 * 
 * @author Raphael Wigoutschnigg
 * 
 */
public interface NFA {
	/**
	 * @return Menge der möglichen Zeichen
	 */
	public Set<Character> getSymbols();

	/**
	 * @return Menge der Endzustände
	 */
	public Set<Integer> getAcceptingStates();

	/**
	 * @return Startzustand
	 */
	public int getInitialState();

	/**
	 * @param s
	 *            - zu testender Zustand
	 * @throws IllegalArgumentException Wenn es den Zustand nicht gibt
	 * @return true, wenn der Zustand s ein Endzustand ist. Ansonsten false.
	 */
	public boolean isAcceptingState(int s) throws IllegalStateException;

	/**
	 * Liefert die Transitionsmatrix. Jeder Eintrag der Matrix ist eine Menge,
	 * welche die Wörter angibt, die für diesen Übergang definiert sind. Das Wort ""
	 * entspricht dem leeren Wort.
	 * 
	 * @return Die Transiationsmatrix mit allen Übergängen
	 */
	public Set<String>[][] getTransitions();

	/**
	 * Setzt einen Übergang.
	 * 
	 * @param fromState
	 *            Ausgangszustand
	 * @param s
	 *            gelesenes Wort. "" entspricht dem leeren Wort
	 * @param toState
	 *            Folgezustand
	 * @throws IllegalStateException Falls die Zustände nicht existieren
	 * @throws IllegalCharacterException Falls ein Zeichen des Wortes nicht erlaubt ist.
	 */
	public void setTransition(int fromState, String s, int toState) throws IllegalStateException, IllegalCharacterException;

	/**
	 * Löscht alle Transitionen mit dem String s, welche von dem gegebenen Zustand ausgehen
	 * 
	 * @param fromState
	 * @param s
	 * @throws IllegalStateException
	 */
	public void clearTransitions(int fromState, String s) throws IllegalStateException;

	/**
	 * Liefert die Menge aller Zustände, zu denen man durch Abarbeitung des Wortes
	 * kommt.
	 * 
	 * @param state
	 * @param s
	 * @return
	 * @throws IllegalCharacterException
	 * @throws IllegalStateException
	 */
	public Set<Integer> getNextStates(int state, String s) throws IllegalCharacterException, IllegalStateException;
	
	/**
	 * Liefert die Anzahl der Zustände des Automaten
	 * @return
	 */
	public int getNumStates();
	
	
	/**
	 * Erzeugt einen Automaten, der die Vereinigung des Automaten mit dem übergebenen Automaten darstellt.
	 * @param a der zu vereinigende Automat
	 * @return neuer Automat
	 */
	public NFA union(NFA a);
	
	/**
	 * Erzeugt einen Automaten, der den Durchschnitt des Automaten mit dem übergebenen Automaten darstellt.
	 * @param a der zu schneidende Automat
	 * @return neuer Automat
	 */
	public NFA intersection(NFA a);
	
	/**
	 * Erzeugt die Differenz zu dem übergebenen Automaten
	 * @return neuer Automat
	 */
	public NFA minus(NFA a);
	
	/**
	 * Hängt den Automaten a an den Automaten an
	 * @param der anzuhängende Automat
	 * @return neuer Automat
	 */
	public NFA concat(NFA a);
	
	/**
	 * Erzeugt das Komplement des Automaten
	 * @return neuer Automat
	 */
	public NFA complement();
	
	/**
	 * Bildet den Kleene-Stern des Automaten
	 * @return neuer Automat
	 */
	public NFA kleeneStar();
	
	/**
	 * Erzeugt L(a)+
	 * @return neuer Automat
	 */
	public NFA plus();

	/**
	 * Erzeugt einen RSA, der die selbe Sprache akzeptiert
	 * @return ein RSA
	 */
	public RSA toRSA();
	
	/**
	 * Prüft, ob das Wort w durch den Automaten akzeptiert wird.
	 * @throws IllegalCharacterException falls das Wort aus nicht erlaubten Zeichen besteht
	 * @param w
	 *            - das abzuarbeitende Wort
	 * @return true, wenn das Wort w akzeptiert wird, andernfalls false.
	 */
	public Boolean accepts(String w) throws IllegalCharacterException;

	/**
	 * Überprüft, ob der Automat nichts (die leere Menge) aktzeptiert.
	 * @return true, wenn der Automat nichts akzeptiert, andernfalls false.
	 */
	public Boolean acceptsNothing();

	/**
	 * Überprüft, ob der Automat nur das leere Wort aktzeptiert
	 * @return true, wenn nur das leere Wort akzeptiert wird, andernfalls false.
	 */
	public Boolean acceptsEpsilonOnly();

	/**
	 * Überprft, ob der Automat das leere Wort aktzeptiert (und womöglich auch mehr).
	 * @return true, wenn das leere Wort aktzeptiert wird, andernfalls false.
	 */
	public Boolean acceptsEpsilon();

	/**
	 * Überprüft, ob der Automat unendlich viele Wörter akzeptiert.
	 * @return true, wenn der Automat unendlich viele Wörter aktzeptiert, andernfalls false.
	 */
	public Boolean isInfinite();

	/**
	 * Überprüft, ob der Automat nur endlich viele Wörter akzeptiert.
	 * @return true, wenn der Automat nur endlich viele Wörter aktzeptiert, andernfalls false.
	 */
	public Boolean isFinite();
	
	/**
	 * Überprüft, ob die Sprache des Automaten eine Teilmenge der Sprache des Automaten a ist
	 * @param endlicher Automat
	 * @return 
	 */
	public Boolean subSetOf(NFA a);
	
	/**
	 * Überprüft, ob der übergebene Automat die selbe Sprache akzeptiert
	 * @return 
	 */
	@Override
	public boolean equals(Object b);

	/**
	 * Überprüft, ob L+ = L*
	 * @return 
	 */
	public Boolean equalsPlusAndStar();
}