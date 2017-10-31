package ab1;

/**
 * 
 * @author Raphael Wigoutschnigg
 * 
 */
public interface RegEx {

	/**
	 * Domain-Name ohne Umlaute und Sonderzeichen (vereinfachte Form). Nur
	 * Kleinbuchstaben. TLD mit 2 oder 3 Zeichen.
	 */
	public String getRegexDomainName();

	/**
	 * Email-Adresse (1) Benutzerteil ohne Umlaute beginnend mit einem Buchstaben.
	 * Punkte und Bindestriche sowie Buchstaben und Ziffern sind erlaubt (2)
	 * Domainteil wie im Bsp getRegexDomainName()
	 */
	public String getRegexEmail();

	/**
	 * Ein URL, welches die Regeln 1-4 erfüllt. (1) Muss mit http, https oder ftp
	 * starten und danach ein :// enthalten. (2) Muss eine gülte Domain besitzen (3)
	 * Kann eine Portnummer enthalten (4) Kann Ziffern, Buchstaben, Punkte,
	 * Bindestriche und Schrägstriche enthalten
	 */
	public String getRegexURL();

	/**
	 * Geben Sie eine RegEx an, die folgende Wörter erkennt: afoot, catfoot,
	 * dogfoot, fanfoot, foody, foolery, foolish, fooster, footage, foothot, footle,
	 * footpad, footway, hotfoot, jawfoot, mafoo, nonfood, padfoot, prefool, sfoot,
	 * unfool
	 * 
	 * Folgende Wörter sollen nicht erkannt werden: Atlas, Aymoro, Iberic, Mahran,
	 * Ormazd, Silipan, altared, chandoo, crenel, crooked, fardo, folksy, forest,
	 * hebamic, idgah, manlike, marly, palazzi, sixfold, tarrock, unfold
	 */
	public String multiMatch1();

	/**
	 * Geben Sie eine RegEx an, die folgende Zeichenketten erkennt: Mick, Rick,
	 * allocochick, backtrick, bestick, candlestick, counterprick, heartsick,
	 * lampwick, lick, lungsick, potstick, quick, rampick, rebrick, relick, seasick,
	 * slick, tick, unsick, upstick
	 * 
	 * Folgende Zeichenketten sollen nicht erkannt werden: Kickapoo , Nickneven,
	 * Rickettsiales, billsticker, borickite, chickell, fickleness, finickily,
	 * kilbrickenite, lickpenny, mispickel, quickfoot, quickhatch, ricksha,
	 * rollicking, slapsticky, snickdrawing, sunstricken, tricklingly, unlicked,
	 * unnickeled
	 */
	public String multiMatch2();

	/**
	 * Geben Sie eine RegEx an, die folgende Zeichenketten erkennt: fu, tofu, snafu
	 * 
	 * Folgende Zeichenketten sollen nicht erkannt werden: futz, fusillade, functional,
	 * discombobulated
	 */
	public String multiMatch3();

	/**
	 * Geben Sie eine RegEx an, die folgende Zeichenketten erkennt: \w+, \w*,
	 * a{1}|a{3}|a{9}, \???, a{1,8}, -, -+-+-+-+-+, (\w+?)\7, (\ufacdf)*?,
	 * [A-Z0-9]+, [a-fk-ov-z]*, (?:[aeiou]+)\1+, [a\-z\-9], (\001)\1, (\2)\1
	 * 
	 * Folgende Zeichenketten sollen nicht erkannt werden: \w++, \w**, a({1}|{3}|{9}), ???,
	 * a{8,1}, +, +-+-+-+-+-, (\w?+)\7, (\ufacdf)?*, [Z-A0-9]+, [f-bp-iy-t]*,
	 * (:?[abced]+)\1*, [a-z-9], (\1)\1, (\8)\2
	 */
	public String multiMatch4();
}
