package ab1.impl.Auer_Siljic;

import ab1.RegEx;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

public class RegExImpl implements RegEx {

	/**
	 * Domain-Name ohne Umlaute und Sonderzeichen (vereinfachte Form). Nur
	 * Kleinbuchstaben. TLD mit 2 oder 3 Zeichen.
	 * Sample: http://www.pcwebopedia.com/index.html, the domain name is pcwebopedia.com
	 */
	@Override
	public String getRegexDomainName() {
		return "[a-z]*.((at)|(com)|(eu))";	// TODO: Needs extension pending on tests
	}

	@Override
	public String getRegexEmail() {

		return null;
	}

	@Override
	public String getRegexURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String multiMatch1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String multiMatch2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String multiMatch3() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String multiMatch4() {
		// TODO Auto-generated method stub
		return null;
	}
}