package fr.dabsunter.eldaria.commons;

import java.util.Random;

/**
 * Created by David on 09/07/2017.
 */
public class Utils {

	public static int randInRange(Random rng, int min, int max) {
		if (max < min)
			throw new IllegalArgumentException("max: " + max + " < min: " + min);
		return rng.nextInt(max - min + 1) + min;
	}

	public static boolean hasSomething(Object... things) {
		for (Object o : things)
			if (o != null)
				return true;
		return false;
	}
}
