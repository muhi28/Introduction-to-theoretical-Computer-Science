package ab1.impl.Auer_Siljic_Harden;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

public class RegEx_UnitTests {
    RegExImpl regExImpl;
    String regEx;

    @Before
    public void setup() {
        regExImpl = new RegExImpl();
        regEx = "null"; // lol
    }

    /**
     * Domain-Name ohne Umlaute und Sonderzeichen (vereinfachte Form). Nur
     * Kleinbuchstaben. TLD mit 2 oder 3 Zeichen.
     */
    @Test
    public void test_RegexDomainName() {
        regEx = regExImpl.getRegexDomainName();

        /* ***********************************************************************************
         * these cases should be true
         * **********************************************************************************/

        Assert.assertTrue(Pattern.matches(regEx, "domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, "domain.at"));
        Assert.assertTrue(Pattern.matches(regEx, "example.domain.at"));
        Assert.assertTrue(Pattern.matches(regEx, "example.domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, "www.domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, "www.domain.at"));
        Assert.assertTrue(Pattern.matches(regEx, "www.google.com"));

        /* ************************************************************************************
         * these cases should be false
         * ***********************************************************************************/

        // TLD should be 2 or 3 symbols
        Assert.assertFalse(Pattern.matches(regEx, "domain.a"));
        Assert.assertFalse(Pattern.matches(regEx, "www.domain.a"));
        Assert.assertFalse(Pattern.matches(regEx, "domain.aaaa"));
        Assert.assertFalse(Pattern.matches(regEx, "www.domain.aaaa"));

        // some strange cases
        Assert.assertFalse(Pattern.matches(regEx, ""));
        Assert.assertFalse(Pattern.matches(regEx, "."));
        Assert.assertFalse(Pattern.matches(regEx, ".."));
        Assert.assertFalse(Pattern.matches(regEx, "com"));
        Assert.assertFalse(Pattern.matches(regEx, "andthensomethinghappened..com"));
        Assert.assertFalse(Pattern.matches(regEx, "andthensomethinghappenedcom"));
        Assert.assertFalse(Pattern.matches(regEx, ".andthensomethinghappenedcom"));
        Assert.assertFalse(Pattern.matches(regEx, "andthensomethinghappenedcom."));
        Assert.assertFalse(Pattern.matches(regEx, ".andthensomethinghappenedcom."));
        Assert.assertFalse(Pattern.matches(regEx, ".andthensomethinghappenedcom.."));
        Assert.assertFalse(Pattern.matches(regEx, ".and.then.somethinghappenedcom.."));


    }

    /**
     * Email-Adresse (1) Benutzerteil ohne Umlaute beginnend mit einem Buchstaben.
     * Punkte und Bindestriche sowie Buchstaben und Ziffern sind erlaubt (2)
     * Domainteil wie im Bsp getRegexDomainName()
     */
    @Test
    public void test_RegexEmail() {

        regEx = regExImpl.getRegexEmail();

        /* *************************************************************************************************************
         * these test cases should be true
         * ************************************************************************************************************/

        // these test cases should be true
        Assert.assertTrue(Pattern.matches(regEx, "christianbauer@domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, "christianbauer@domain.at"));
        Assert.assertTrue(Pattern.matches(regEx, "christian-bauer@domain.at"));
        Assert.assertTrue(Pattern.matches(regEx, "christian.bauer@domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, "some.awesome@domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, "a.even.more.awesome@domain.com"));


        /* ************************************************************************************************************
         * these test cases should be false
         * ***********************************************************************************************************/

        // email addresses have to start with a letter, not a number
        Assert.assertFalse(Pattern.matches(regEx, "1awesome@domain.at"));
        Assert.assertFalse(Pattern.matches(regEx, "1awesome@domain.com"));

        // some strange cases
        Assert.assertFalse(Pattern.matches(regEx, "@domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "somebadboi.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "@domain..com"));
        Assert.assertFalse(Pattern.matches(regEx, "@@domain.com"));

        // even stranger things

        // lets see with raphis tests if false pattern
        // Assert.assertFalse(Pattern.matches(regEx, "name--lastname@domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "@chicken@domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, ".give@domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "..me@domain.at"));
        Assert.assertFalse(Pattern.matches(regEx, ".@tendies@domain.at"));
        Assert.assertFalse(Pattern.matches(regEx, ".r@@@@@@@@@@@domain.at"));
    }


    /**
     * Ein URL, welches die Regeln 1-4 erfüllt. (1) Muss mit http, https oder ftp
     * starten und danach ein :// enthalten. (2) Muss eine gültige Domain besitzen (3)
     * Kann eine Portnummer enthalten (4) Kann Ziffern, Buchstaben, Punkte,
     * Bindestriche und Schrägstriche enthalten
     */
    @Test
    public void test_RegexURL() {

        regEx = regExImpl.getRegexURL();

        String http = "http";
        String https = "https";
        String ftp = "ftp";
        String dotDotSlashSlash = "://";


        /* ************************************************************************************************************
        * these test cases should be true
        * *************************************************************************************************************/

        // http
        Assert.assertTrue(Pattern.matches(regEx, http + dotDotSlashSlash + "www.domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, http + dotDotSlashSlash + "www.example.com/index.html"));

        // http ports
        Assert.assertTrue(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:1234"));
        Assert.assertTrue(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:1234/"));

        // combined test cases
        Assert.assertTrue(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:80/index.html"));
        Assert.assertTrue(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:80/index/index123/1-2-3.Tendies.txt"));

        // https
        Assert.assertTrue(Pattern.matches(regEx, https + dotDotSlashSlash + "www.domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, https + dotDotSlashSlash + "www.example.com/index.html"));

        // https ports
        Assert.assertTrue(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:1234"));
        Assert.assertTrue(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:1234/"));

        // combined test cases
        Assert.assertTrue(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:80/index.html"));
        Assert.assertTrue(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:80/index/index123/1-2-3.Tendies.txt"));

        // ftp
        Assert.assertTrue(Pattern.matches(regEx, ftp + dotDotSlashSlash + "www.domain.com"));
        Assert.assertTrue(Pattern.matches(regEx, ftp + dotDotSlashSlash + "www.example.com/index.html"));

        // ftp ports
        Assert.assertTrue(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:1234"));
        Assert.assertTrue(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:1234/"));

        // combined test cases
        Assert.assertTrue(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:80/index.html"));
        Assert.assertTrue(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:80/index/index123/1-2-3.Tendies.txt"));


        /* ************************************************************************************************************
        * these test cases should be false
        * ************************************************************************************************************/

        // not a complete url
        Assert.assertFalse(Pattern.matches(regEx, "www.domain.com"));

        // wrong http/https/ftp and ://
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, https + http + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, https + https + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + https + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, dotDotSlashSlash + https + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, http + https + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, dotDotSlashSlash + dotDotSlashSlash + dotDotSlashSlash + "www.domain.com"));


        // http with correct http but wrong ://
        Assert.assertFalse(Pattern.matches(regEx, http + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, http + ":/www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, http + ":www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, http + "//www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, http + "/:/www.domain.com/index.html"));

        // wrong http but correct ://
        Assert.assertFalse(Pattern.matches(regEx, "h" + dotDotSlashSlash + "www.google.com/skynet.ftw"));
        Assert.assertFalse(Pattern.matches(regEx, "htp" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "htttp" + dotDotSlashSlash + "www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, "htttp" + dotDotSlashSlash + "www.google-ultron.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, "httt" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "ttt" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "httb" + dotDotSlashSlash + "www.domain.com"));

        // correct https but wrong ://
        Assert.assertFalse(Pattern.matches(regEx, https + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, https + ":/www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, https + ":www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, https + "//www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, https + "/:/www.domain.com/index.html"));

        // wrong http but correct ://
        Assert.assertFalse(Pattern.matches(regEx, "htps" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "htttps" + dotDotSlashSlash + "www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, "htttps" + dotDotSlashSlash + "www.google-ultron.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, "httts" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "ttts" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "httbs" + dotDotSlashSlash + "www.domain.com"));

        // correct ftp but wrong ://
        Assert.assertFalse(Pattern.matches(regEx, ftp + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + ":/www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + ":www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + "//www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + "/:/www.domain.com/index.html"));

        // wrong ftp but correct ://
        // wrong http but correct ://
        Assert.assertFalse(Pattern.matches(regEx, "f" + dotDotSlashSlash + "www.domain-name.com"));
        Assert.assertFalse(Pattern.matches(regEx, "ft" + dotDotSlashSlash + "www.domain.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, "ptf" + dotDotSlashSlash + "www.google-ultron.com/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, "fpp" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "fpf" + dotDotSlashSlash + "www.domain.com"));
        Assert.assertFalse(Pattern.matches(regEx, "ppp" + dotDotSlashSlash + "www.domain.com"));

        // http ports
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:80:80"));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at/index.html:80"));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:80."));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at::80"));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:"));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:/"));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:8-0"));
        Assert.assertFalse(Pattern.matches(regEx, http + dotDotSlashSlash + "aau.at:8.0"));

        // https ports
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:80:80"));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at/index.html:80"));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:80."));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at::80"));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:"));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:/"));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:8-0"));
        Assert.assertFalse(Pattern.matches(regEx, https + dotDotSlashSlash + "aau.at:8.0"));

        // ftp ports
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:80:80"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at/index.html:80"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:80."));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at::80"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:/index.html"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:/"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:8-0"));
        Assert.assertFalse(Pattern.matches(regEx, ftp + dotDotSlashSlash + "aau.at:8.0"));

    }

    /**
     * Geben Sie eine RegEx an, die folgende Wörter erkennt: afoot, catfoot,
     * dogfoot, fanfoot, foody, foolery, foolish, fooster, footage, foothot, footle,
     * footpad, footway, hotfoot, jawfoot, mafoo, nonfood, padfoot, prefool, sfoot,
     * unfool
     * <p>
     * Folgende Wörter sollen nicht erkannt werden: Atlas, Aymoro, Iberic, Mahran,
     * Ormazd, Silipan, altared, chandoo, crenel, crooked, fardo, folksy, forest,
     * hebamic, idgah, manlike, marly, palazzi, sixfold, tarrock, unfold
     */
    @Test
    public void test_MultiMatch_1() {

        regEx = regExImpl.multiMatch1();

        /* ************************************************************************************************************
        * these cases should be true
        * ************************************************************************************************************/
        Assert.assertTrue(Pattern.matches(regEx, "afoot"));
        Assert.assertTrue(Pattern.matches(regEx, "catfoot"));
        Assert.assertTrue(Pattern.matches(regEx, "dogfoot"));
        Assert.assertTrue(Pattern.matches(regEx, "fanfoot"));
        Assert.assertTrue(Pattern.matches(regEx, "foody"));
        Assert.assertTrue(Pattern.matches(regEx, "foolery"));
        Assert.assertTrue(Pattern.matches(regEx, "foolish"));
        Assert.assertTrue(Pattern.matches(regEx, "fooster"));
        Assert.assertTrue(Pattern.matches(regEx, "footage"));
        Assert.assertTrue(Pattern.matches(regEx, "foothot"));
        Assert.assertTrue(Pattern.matches(regEx, "footle"));
        Assert.assertTrue(Pattern.matches(regEx, "footpad"));
        Assert.assertTrue(Pattern.matches(regEx, "footway"));
        Assert.assertTrue(Pattern.matches(regEx, "hotfoot"));
        Assert.assertTrue(Pattern.matches(regEx, "jawfoot"));
        Assert.assertTrue(Pattern.matches(regEx, "mafoo"));
        Assert.assertTrue(Pattern.matches(regEx, "nonfood"));
        Assert.assertTrue(Pattern.matches(regEx, "padfoot"));
        Assert.assertTrue(Pattern.matches(regEx, "prefool"));
        Assert.assertTrue(Pattern.matches(regEx, "prefool"));
        Assert.assertTrue(Pattern.matches(regEx, "sfoot"));
        Assert.assertTrue(Pattern.matches(regEx, "unfool"));


        /* *************************************************************************************************************
         * these test cases should be false
         * ************************************************************************************************************/

        Assert.assertFalse(Pattern.matches(regEx, "Atlas"));
        Assert.assertFalse(Pattern.matches(regEx, "Aymoro"));
        Assert.assertFalse(Pattern.matches(regEx, "Iberic"));
        Assert.assertFalse(Pattern.matches(regEx, "Mahran"));
        Assert.assertFalse(Pattern.matches(regEx, "Ormazd"));
        Assert.assertFalse(Pattern.matches(regEx, "Silipan"));
        Assert.assertFalse(Pattern.matches(regEx, "altared"));
        Assert.assertFalse(Pattern.matches(regEx, "chandoo"));
        Assert.assertFalse(Pattern.matches(regEx, "crenel"));
        Assert.assertFalse(Pattern.matches(regEx, "crooked"));
        Assert.assertFalse(Pattern.matches(regEx, "fardo"));
        Assert.assertFalse(Pattern.matches(regEx, "folksy"));
        Assert.assertFalse(Pattern.matches(regEx, "forest"));
        Assert.assertFalse(Pattern.matches(regEx, "hebamic"));
        Assert.assertFalse(Pattern.matches(regEx, "idgah"));
        Assert.assertFalse(Pattern.matches(regEx, "manlike"));
        Assert.assertFalse(Pattern.matches(regEx, "marly"));
        Assert.assertFalse(Pattern.matches(regEx, "palazzi"));
        Assert.assertFalse(Pattern.matches(regEx, "sixfold"));
        Assert.assertFalse(Pattern.matches(regEx, "tarrock"));
        Assert.assertFalse(Pattern.matches(regEx, "unfold"));
    }

    /**
     * Geben Sie eine RegEx an, die folgende Zeichenketten erkennt: Mick, Rick,
     * allocochick, backtrick, bestick, candlestick, counterprick, heartsick,
     * lampwick, lick, lungsick, potstick, quick, rampick, rebrick, relick, seasick,
     * slick, tick, unsick, upstick
     * <p>
     * Folgende Zeichenketten sollen nicht erkannt werden: Kickapoo , Nickneven,
     * Rickettsiales, billsticker, borickite, chickell, fickleness, finickily,
     * kilbrickenite, lickpenny, mispickel, quickfoot, quickhatch, ricksha,
     * rollicking, slapsticky, snickdrawing, sunstricken, tricklingly, unlicked,
     * unnickeled
     */
    @Test
    public void test_MultiMatch_2() {
        regEx = regExImpl.multiMatch2();
        /* ************************************************************************************************************
        * these test cases should be true
        * ************************************************************************************************************/
        Assert.assertTrue(Pattern.matches(regEx, "Mick"));
        Assert.assertTrue(Pattern.matches(regEx, "Rick"));
        Assert.assertTrue(Pattern.matches(regEx, "allocochick"));
        Assert.assertTrue(Pattern.matches(regEx, "backtrick"));
        Assert.assertTrue(Pattern.matches(regEx, "bestick"));
        Assert.assertTrue(Pattern.matches(regEx, "candlestick"));
        Assert.assertTrue(Pattern.matches(regEx, "counterprick"));
        Assert.assertTrue(Pattern.matches(regEx, "heartsick"));
        Assert.assertTrue(Pattern.matches(regEx, "lampwick"));
        Assert.assertTrue(Pattern.matches(regEx, "lungsick"));
        Assert.assertTrue(Pattern.matches(regEx, "potstick"));
        Assert.assertTrue(Pattern.matches(regEx, "rampick"));
        Assert.assertTrue(Pattern.matches(regEx, "rebrick"));
        Assert.assertTrue(Pattern.matches(regEx, "relick"));
        Assert.assertTrue(Pattern.matches(regEx, "seasick"));
        Assert.assertTrue(Pattern.matches(regEx, "tick"));
        Assert.assertTrue(Pattern.matches(regEx, "unsick"));
        Assert.assertTrue(Pattern.matches(regEx, "upstick"));


        /* ************************************************************************************************************
        * these test cases should be false
        * ************************************************************************************************************/
        Assert.assertFalse(Pattern.matches(regEx, "Kickapoo"));
        Assert.assertFalse(Pattern.matches(regEx, "Nickneven"));
        Assert.assertFalse(Pattern.matches(regEx, "Rickettsiales"));
        Assert.assertFalse(Pattern.matches(regEx, "billsticker"));
        Assert.assertFalse(Pattern.matches(regEx, "borickite"));
        Assert.assertFalse(Pattern.matches(regEx, "chickell"));
        Assert.assertFalse(Pattern.matches(regEx, "fickleness"));
        Assert.assertFalse(Pattern.matches(regEx, "finickily"));
        Assert.assertFalse(Pattern.matches(regEx, "kilbrickenite"));
        Assert.assertFalse(Pattern.matches(regEx, "lickpenny"));
        Assert.assertFalse(Pattern.matches(regEx, "mispickel"));
        Assert.assertFalse(Pattern.matches(regEx, "quickfoot"));
        Assert.assertFalse(Pattern.matches(regEx, "quickhatch"));
        Assert.assertFalse(Pattern.matches(regEx, "ricksha"));
        Assert.assertFalse(Pattern.matches(regEx, "rollicking"));
        Assert.assertFalse(Pattern.matches(regEx, "slapsticky"));
        Assert.assertFalse(Pattern.matches(regEx, "snickdrawing"));
        Assert.assertFalse(Pattern.matches(regEx, "sunstricken"));
        Assert.assertFalse(Pattern.matches(regEx, "tricklingly"));
        Assert.assertFalse(Pattern.matches(regEx, "unlicked"));
        Assert.assertFalse(Pattern.matches(regEx, "unnickeled"));
    }

    /**
     * Geben Sie eine RegEx an, die folgende Zeichenketten erkennt: fu, tofu, snafu
     * <p>
     * Folgende Zeichenketten sollen nicht erkannt werden: futz, fusillade, functional,
     * discombobulated
     */
    @Test
    public void test_MultiMatch_3() {
        regEx = regExImpl.multiMatch3();

        /* ************************************************************************************************************
        * these test cases should be true
        * ************************************************************************************************************/
        Assert.assertTrue(Pattern.matches(regEx, "fu"));
        Assert.assertTrue(Pattern.matches(regEx, "tofu"));
        Assert.assertTrue(Pattern.matches(regEx, "snafu"));


        /* ************************************************************************************************************
        * these test cases should be false
        * ************************************************************************************************************/
        Assert.assertFalse(Pattern.matches(regEx, "futz"));
        Assert.assertFalse(Pattern.matches(regEx, "fusillade"));
        Assert.assertFalse(Pattern.matches(regEx, "functional"));
        Assert.assertFalse(Pattern.matches(regEx, "discombobulated"));
    }

    /**
     * Geben Sie eine RegEx an, die folgende Zeichenketten erkennt: \w+, \w*,
     * a{1}|a{3}|a{9}, \???, a{1,8}, -, -+-+-+-+-+, (\w+?)\7, (\ufacdf)*?,
     * [A-Z0-9]+, [a-fk-ov-z]*, (?:[aeiou]+)\1+, [a\-z\-9], (\001)\1, (\2)\1
     * <p>
     * Folgende Zeichenketten sollen nicht erkannt werden: \w++, \w**, a({1}|{3}|{9}), ???,
     * a{8,1}, +, +-+-+-+-+-, (\w?+)\7, (\ufacdf)?*, [Z-A0-9]+, [f-bp-iy-t]*,
     * (:?[abced]+)\1*, [a-z-9], (\1)\1, (\8)\2
     */
    @Test
    public void test_MultiMatch_4() {
        regEx = regExImpl.multiMatch4();

        /* ************************************************************************************************************
        * these test cases should be true
        * ************************************************************************************************************/

        // \w* if empty
        Assert.assertTrue(Pattern.matches(regEx, ""));
        // a{1}
        Assert.assertTrue(Pattern.matches(regEx, "a"));
        // a{3}
        Assert.assertTrue(Pattern.matches(regEx, "aaa"));
        // a{9}
        Assert.assertTrue(Pattern.matches(regEx, "aaaaaaaaa"));

        Assert.assertTrue(Pattern.matches(regEx, "-"));
        Assert.assertTrue(Pattern.matches(regEx, "-+-+-+-+-+"));


        /* ************************************************************************************************************
        * these test cases should be false
        * ************************************************************************************************************/

        Assert.assertFalse(Pattern.matches(regEx, "+"));
        Assert.assertFalse(Pattern.matches(regEx, "+-+-+-+-+-"));
    }
}