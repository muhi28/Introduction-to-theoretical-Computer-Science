package ab1.impl.Auer_Siljic_Harden;

import ab1.RegEx;
import java.io.*;

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
	 * Ein URL | welches die Regeln 1-4 erfüllt. (1) Muss mit http | https oder ftp
	 * starten und danach ein :// enthalten. (2) Muss eine gülte Domain besitzen (3)
	 * Kann eine Portnummer enthalten (4) Kann Ziffern | Buchstaben | Punkte,
	 * Bindestriche und Schrägstriche enthalten
	 */
	@Override
	public String getRegexURL() {
		return "((http)|(https)|(ftp)):\\/\\/[a-zA-Z0-9.\\-\\/]+(:[0-9]+)?((\\/)?|(\\/[a-zA-Z0-9.\\-\\/]+)?)";
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

		/*
        String str = "";
		try {
			FileReader readerOfDespair = new FileReader(new File("ultraregex").getAbsolutePath());
//			FileReader readerOfDespair = new FileReader("ab1/impl/Auer_Siljic_Harden/ultraregex");
			BufferedReader bufferedFailer = new BufferedReader(readerOfDespair);

            str = bufferedFailer.readLine();

            readerOfDespair.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

//		Regex r = "(\\w(\\+|\\*)|(a\{[0-9]\}(\|)?)+";
//	    String str =
//		String str = "\\w\\+ | \\w* | a{1}|a{3}|a{9} | \\??? | a{1,8} | - | -+-+-+-+-+ | " +
//				"(\\w+?)\\7 | (\\ufacdf)*? | [A-Z0-9]+ | [a-fk-ov-z]* | (?:[aeiou]+)\\1+" +
//				"| [a\\-z\\-9] | (\\001)\\1 | (\\2)\\1";

//		String str = "\\w\\+ | \\w* | a{1}|a{3}|a{9} | \\??? | a{1,8} | - | -+-+-+-+-+ | " +
//				"(\\w+?)\\7 | (\\ufacdf)*? | [A-Z0-9]+ | [a-fk-ov-z]* | (?:[aeiou]+)\\1+" +
//				"| [a\\-z\\-9] | (\\001)\\1 | (\\2)\\1";
        return "w";
    }
}