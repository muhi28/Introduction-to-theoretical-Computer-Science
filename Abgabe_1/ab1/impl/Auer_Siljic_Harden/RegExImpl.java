package ab1.impl.Auer_Siljic_Harden;

import ab1.RegEx;

public class RegExImpl implements RegEx {

	@Override
	public String getRegexDomainName() {
		return "([w]{3}(\\.)?[a-z]+(\\.)?\\.[a-z]{2,3})|([a-z]+)\\.[a-z]{2,3}|([a-z]+\\.[a-z]+)\\.[a-z]{2,3}";
	}

	@Override
	public String getRegexEmail() {
		return "([a-z]+[a-z.]*@[a-z]*\\.[a-z]{2,3})|([a-z]+\\.[a-z]+@[a-z]+\\.[a-z]{2,3})|([a-z]+\\-[a-z]+@[a-z]+\\.[a-z]{2,3})";
    }

	/**
	 * Ein URL, welches die Regeln 1-4 erfüllt. (1) Muss mit http, https oder ftp
	 * starten und danach ein :// enthalten. (2) Muss eine gülte Domain besitzen (3)
	 * Kann eine Portnummer enthalten (4) Kann Ziffern, Buchstaben, Punkte,
	 * Bindestriche und Schrägstriche enthalten
	 */
	@Override
	public String getRegexURL() {
		return "(http)|(https)|(https)://[a-zA-Z0-9.-/]+";
		//return "(((http)|(https)|(ftp))\\:\\/\\/[a-z]+\\.[a-z]+(\\.[a-z]+)?(\\:[0-9]+\\/?)?([a-z]+(((.)|(\\/))?)([a-z]+)?){0,})";
	}

	@Override
	public String multiMatch1() {
        return "[a-z]*(foo)[a-z]*";
    }

	@Override
	public String multiMatch2() {
        return "^[a-zA-Z]+(ick)$";
    }

	@Override
	public String multiMatch3() {
        return "^[a-z]*(fu)$";
    }



	@Override
	public String multiMatch4() {
		return "";
	}
}