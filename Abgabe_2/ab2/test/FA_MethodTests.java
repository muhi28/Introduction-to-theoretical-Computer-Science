package ab2.test;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;

/**
 * Tests various applications within Assignment 2
 * Specifically Test for Methods
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({T_NFA.class, T_DFA.class, T_RSA.class})
public class FA_MethodTests { }
