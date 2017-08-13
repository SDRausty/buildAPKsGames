package eu.veldsoft.russian.triple.model;

import java.util.Random;

/**
 * Utilities class with common used variables and methods.
 * 
 * @author Todor Balabanov
 */
class Util {
	/**
	 * No trump comparator.
	 */
	static NoTrumpComparator noTrumpComparator = new NoTrumpComparator();

	/**
	 * Pseudo-random number generator.
	 */
	static final Random PRNG = new Random();
}
