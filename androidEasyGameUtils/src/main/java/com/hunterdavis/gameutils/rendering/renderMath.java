package com.hunterdavis.gameutils.rendering;

// TODO: Auto-generated Javadoc
/**
 * The Class renderMath.
 */
public class renderMath {
	
	/**
	 * Fdistance.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the float
	 */
	public static float fdistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
}
