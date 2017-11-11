package ab1.impl.Auer_Siljic_Harden;

import ab1.RegEx;

public class RegExImpl implements RegEx {

	@Override
	public String getRegexDomainName() {
		return "([w]{3}(\\.){0,1}[a-z]*(\\.){0,1}\\.[a-z]{2,3})|([a-z]*)\\.[a-z]{2,3}|([a-z]*\\.[a-z]*)\\.[a-z]{2,3}";
	}

	@Override
	public String getRegexEmail() {
		return "([a-z]+@[a-z]*\\.[a-z]{2,3})|([a-z]+\\.[a-z]+@[a-z]+\\.[a-z]{2,3})|([a-z]+\\-[a-z]+@[a-z]+\\.[a-z]{2,3})";
    }

	@Override
	public String getRegexURL() {
		return "([http|https|ftp])://"+getRegexDomainName()+"[0-9]{4}";
	}

	@Override
	public String multiMatch1() {
        return "[a-z]*(foo)[a-z]*";
    }

	@Override
	public String multiMatch2() {
        return "([a-z]*|[A-Z]*)(ick)";
    }

	@Override
	public String multiMatch3() {
        return "(([a-z]*)(fu))";
    }

	/**
	  Geben Sie eine RegEx an, die folgende Zeichenketten erkennt: \w+, \w*,
	  a{1}|a{3}|a{9}, \???, a{1,8}, -, -+-+-+-+-+, (\w+?)\7, (\ufacdf)*?,
	  [A-Z0-9]+, [a-fk-ov-z]*, (?:[aeiou]+)\1+, [a\-z\-9], (\001)\1, (\2)\1

	  Folgende Zeichenketten sollen nicht erkannt werden: \w++, \w**, a({1}|{3}|{9}), ???,
	  a{8,1}, +, +-+-+-+-+-, (\w?+)\7, (\ufacdf)?*, [Z-A0-9]+, [f-bp-iy-t]*,
	  (:?[abced]+)\1*, [a-z-9], (\1)\1, (\8)\2
	*/
	@Override
	public String multiMatch4() {
//		return "((w(\+|\*)))|(-[+-]*)|(((a{[0-9]})|(a{[0-9]})|(a{0-9})))|(\\\?\?\?)|(a{[0-9]\,[0-9]})|((\(\\w\+\?\))\\[0-9])|(\(\\[a-zA-Z]*\)\*\?)|(\[A\-Z0\-9\]\+)|(\(\\[0-9][0-9][0-9]\)\\)";
		return null;
	}
}