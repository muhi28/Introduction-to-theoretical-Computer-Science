package ab1;

import java.util.List;
import java.util.Set;

/**
 * Interface zur Implementierung einer Turingmaschine
 * 
 * @author Raphael Wigoutschnigg, Alfred Müller
 * 
 */
public interface TM {
	/**
	 * Setzt die Turingmaschine zurueck und loescht alle internen Daten. Dies
	 * umfasst Ueberfuehrungen, Zustaende und Bandinhalt. Ausgangszustand ist ganz
	 * links am leeren Band im Haltezustand.
	 */
	public void reset();

	/**
	 * Gibt die Anzahl an zu verwendenen Schreib/Lese-Bändern an.
	 * 
	 * @param numTapes
	 *            Anzahl an Schreib/Lese-Bändern
	 * @throws IllegalArgumentException
	 *             wenn numTapes < 1 ist
	 */
	public void setNumberOfTapes(int numTapes) throws IllegalArgumentException;

	/**
	 * Gibt die Menge der von der Turingmaschine verarbeiteten Symbole an. Es ist
	 * sicherzustellen, dass das Zeichen '#' auf jeden Fall in der Menge enthalten
	 * ist.
	 * 
	 * @param symbols
	 *            Menge der Symbole
	 * @throws IllegalArgumentException
	 *             wenn die Menge nicht das Leerzeichen '#' enthält.
	 */
	public void setSymbols(Set<Character> symbols) throws IllegalArgumentException;

	/**
	 * Liefert die Menge der verarbeiteten Symbole.
	 * 
	 * @return Menge der Symbole
	 */
	public Set<Character> getSymbols();

	/**
	 * Fuegt eine Ueberfuehrung hinzu. fromState und toState sind ganze Zahlen (0,
	 * 1, ...). 0 steht hierbei fuer den Haltezustand. Wird eine Zustandsnummer
	 * verwendet, die bisher nicht verwendet wurde, ist die Zustandsmenge intern
	 * entsprechend zu erweitern. Die optionale Bewegung wird nach der
	 * Schreiboperation ausgeführt. tape=0 steht für das erste Schreib/Lese-Band.
	 * 
	 * @param tape
	 *            Band
	 * @param fromState
	 *            Ausgangszustand
	 * @param symbolRead
	 *            gelesenes Symbol
	 * @param toState
	 *            Folgezustand
	 * @param symbolWrite
	 *            geschriebenes Symbol, wenn null keines
	 * @param movement
	 *            Bewegung des Schreib-/Lesekopfes, wenn null keine
	 * 
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn ausgehend vom Haltezustand ein Zeichen
	 *             gelesen wird, wenn eine Transition nicht eindeutig ist
	 *             (fromState, symbolRead), wenn ein Symbol nicht verarbeitet werden
	 *             kann oder wenn das Band nicht existiert.
	 */
	public void addTransition(int tape, int fromState, char symbolRead, int toState, char symbolWrite,
			Movement movement) throws IllegalArgumentException;

	/**
	 * Liefert die Menge der Zustaende, welche durch addTransition erzeugt wurden.
	 * Der Haltezustand (0) ist in jedem Fall Teil dieser Menge.
	 * 
	 * @return
	 */
	public Set<Integer> getStates();

	/**
	 * Liefert die Anzahl an Bändern zurück.
	 * 
	 * @return Anzahl der Bänder
	 */
	public int getNumberOfTapes();

	/**
	 * Setzt den initialen Zustand der Maschine.
	 * 
	 * @param state
	 *            Startzutand
	 */
	public void setInitialState(int state);

	/**
	 * Setzt den initialen Inhalt des definierten Bandes und setzt den
	 * Schreib-/Lesekopf hinter das letzte Zeichen des Inhaltes. "#abc" liefert
	 * somit den Inhalt "#abc#...." wobei der Schreib-/Lesekopf unter dem zweiten #
	 * steht.
	 * 
	 * @param tape
	 * @param content
	 */
	public void setInitialTapeContent(int tape, char[] content);

	/**
	 * Fuehrt einen Abarbeitungsschritt der Turingmaschine aus.
	 * 
	 * @throws IllegalStateException
	 *             Wird geworfen, wenn die Maschine bereits im Haltezustand ist,
	 *             keine passende Transition vorhanden ist oder die Maschine links
	 *             in die "Wand" laeuft.
	 */
	public void doNextStep() throws IllegalStateException;

	/**
	 * Liefert true, wenn sich die Maschine im Haltezustand befindet.
	 * 
	 * @return
	 */
	public boolean isHalt();

	/**
	 * Liefert true, wenn die Maschine haengt (Bandende ueberschritten oder
	 * unbekannter Zustand).
	 */
	public boolean isCrashed();

	/**
	 * Liefert die Konfiguration der Maschine für jedes Band. Ist isCrashed() ==
	 * true, wird null zurück geliefert.
	 * 
	 * @return Konfiguration der Maschine.
	 */
	public List<TMConfig> getTMConfig();

	/**
	 * Liefert die Konfiguration der Maschine für das angegebene Band. Ist
	 * isCrashed() == true, wird null zurück geliefert.
	 * 
	 * @param tape
	 * @return Konfiguration des angegebenen Bandes
	 */
	public List<TMConfig> getTMConfig(int tape);

	/**
	 * @author Raphael Wigoutschnigg
	 * Bewegerichtungen des Schreib/Lese-Kopfes
	 *
	 */
	public enum Movement {
		/**
		 * Kopf bewegt sich nach links
		 */
		Left,
		
		/**
		 * Kopf bewegt sich nach rechts
		 **/
		Right,
		
		/**
		 * Kopf bewegt sich nicht
		 */
		Stay
	}
	
	/**
	 * 
	 * @author Raphael Wigoutschnigg
	 * 
	 */
	public class TMConfig {
		/**
		 * Alle Zeichen auf dem Band vom Beginn des Bandes bis zur Stelle links vom Kopf (darf leer sein).
		 */
		private char[] leftOfHead;
		/**
		 * Das Zeichen, das sich aktuell unter dem Kopf befindet.
		 */
		private char belowHead;
		/**
		 * Alle Zeichen von der Stelle rechts vom Kopf bis zum letzten von '#' verschiedenen Zeichen (darf leer sein).
		 */
		private char[] rightOfHead;

		public char[] getLeftOfHead() {
			return leftOfHead;
		}

		public char getBelowHead() {
			return belowHead;
		}

		public char[] getRightOfHead() {
			return rightOfHead;
		}

		public TMConfig(char[] leftOfHead, char belowHead, char[] rightOfHead) {
			super();
			this.leftOfHead = leftOfHead;
			this.belowHead = belowHead;
			this.rightOfHead = rightOfHead;
		}

	}
}
