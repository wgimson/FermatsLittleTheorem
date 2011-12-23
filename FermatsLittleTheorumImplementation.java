// the Java BigInteger class provides a method for modular exponentiation
// - one of the reasons it is so useful for implementing primality testing 
// algorithms, which frequently make use of modular exponentiation
import java.math.BigInteger; 
// java.util contains the Random() class we need to generate random 
// coprimes to BigInteger n
import java.util.*;

public class myFermatsLittleTheoremImplementation {

    // this is the random number generator, from the Java Random class
    // also used in previous Miller-Rabin implementation
    private final static Random myRandom = new Random();

    // this method will apply the test a^(n-1) = 1 (mod n) for the BigInteger n
    // and the coprime BigInteger a constructed in the following method - 
    // numberOfIterations can easily be varied to either decrease run time or
    // increase certainty of result
    private static boolean fermatMethod(BigInteger n, int numberOfIterations) {
        // if n = 1, of course, n is not prime
        if (n.equals(BigInteger.ONE)) {
            return false;
        }

        // Fermat's Primality test is probabilistic - becomes more certain
        // as number of iterations is increased
        for (int i = 0; i < numberOfIterations; i++) {
            // construct a coprime to n, a, and use it to test the primality
            // of n
            BigInteger a = constructRandomCoPrime(n);
            // perform modular exponentiation on BigInteger a - Fermat's
            // Theorem states that, if a^(n-1) = 1 (mod n), n is a (likely)
            // prime
            a = a.modPow(n.subtract(BigInteger.ONE), n);

            // so, again, if a^(n-1) != 1 (mod n), BigInteger n is retruned
            // as composite
            if (!a.equals(BigInteger.ONE))
                return false;
        }

        // otherwise, a^(n-1) = 1 (mod 1) for all interations = numberOfIterations,
        // and BigInteger n is returned as a (likely) prime 
        return true;
    }

    private static BigInteger constructRandomCoPrime(BigInteger n) {
        // Rejection method: ask for a random integer but reject it if it isn't
        // in the acceptable set.

        while (true) {
            // BigInteger a will be a randomly constructed BigInteger of the 
            // same bitlength as BigInteger n, such that 1 <= a < n
            final BigInteger a = new BigInteger (n.bitLength(), myRandom);
            // the compareTo method of the BigInteger class returns -1, 0, or 
            // 1 depending on whether the calling BigInteger is less than, 
            // equal to of greater than the parameter BigInteger, respectively
            // - here we are determining a value for a which is greater than or
            // equal to 0 AND less than BigInteger n - though the Fermat Base
            // a *could* be any number in the set of integers, most implementations
            // rely on a | 0 <= a < n, where n is the number to be tested for
            // primality
            if (BigInteger.ONE.compareTo(a) <= 0 && a.compareTo(n) < 0) {
                return a; 
            }
        }
    }

    public static void main(String[] args) {
        // initialize an int variable representing the number to be tested
        // for primailty - long values startTime and endTime
        long testNumber = 1;
        long startTime, endTime;
        // ask user for number to be tested
        System.out.println("\nPlease enter number to be tested for primality: ");

        // construct scanner to put continuous int-interpretable input in
        // variable testNumber
        Scanner myScanner = new Scanner(System.in);
        if (myScanner.hasNextLong()) {
            testNumber = myScanner.nextLong();
        }

        // cast testNumber as a long variable and set it to BigInteger n
        BigInteger n = BigInteger.valueOf(testNumber);
        // if fermatMethod returns true for BigInteger n, n is (likely) prime - 
        // otherwise n is (likely) composite - begin timing algorithm here
        startTime = System.currentTimeMillis();
        System.out.println( fermatMethod(n, 20) ? 
                ("\n" + testNumber + " is prime.\n") : 
                ("\n" + testNumber + " is composite.\n"));
        // stop timing algorithm here
        endTime = System.currentTimeMillis();
        System.out.println("\nTotal run time for " + testNumber + " was "
                + (endTime - startTime) + " milliseconds.\n");
    }
}
