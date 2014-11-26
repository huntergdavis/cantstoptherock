package com.hunterdavis.gameutils.rendering;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

// TODO: Auto-generated Javadoc
/**
 * The Class Effects.
 */
public class Effects {
	
	/**
	 * Draw cracks.
	 *
	 * @param canvas the canvas
	 * @param left the left
	 * @param right the right
	 * @param top the top
	 * @param bottom the bottom
	 * @param numCracks the num cracks
	 * @param random the random
	 */
	public static void drawCracks(Canvas canvas, int left, int right, int top,
			int bottom, int numCracks, Random random) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1);
		int xa, xb, ya, yb;
		int width = Math.abs(right - left);
		int height = Math.abs(top - bottom);
		for (int i = 0; i < numCracks; i++) {
			xa = random.nextInt(width) + left;
			xb = random.nextInt(width) + left;
			ya = random.nextInt(height) + bottom;
			yb = random.nextInt(height) + bottom;
			canvas.drawLine(xa, ya, xb, yb, paint);
		}
	}

	/**
	 * Draw mr yuck effect.
	 *
	 * @param canvas the canvas
	 * @param left the left
	 * @param right the right
	 * @param top the top
	 * @param bottom the bottom
	 */
	public void drawMrYuckEffect(Canvas canvas, int left, int right, int top,
			int bottom) {
		int x = (left + right) / 2;
		int y = (top + bottom) / 2;
		int radius = (right - left) / 2;

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1);
		canvas.drawCircle(x, y, radius, paint);

		canvas.drawLine(x - radius / 2, y + radius / 2, x + radius / 2, y
				+ radius / 2, paint);

		int eyestop = y - radius / 4;
		int eyesbottom = y - radius / 2;
		int lefteyeleft = x - radius / 2;
		int lefteyeright = x - radius / 4;
		int righteyeleft = x + radius / 4;
		int righteyeright = x + radius / 2;

		// left eye x
		canvas.drawLine(lefteyeleft, eyestop, lefteyeright, eyesbottom, paint);
		canvas.drawLine(lefteyeright, eyestop, lefteyeleft, eyesbottom, paint);

		// right eye x
		canvas.drawLine(righteyeleft, eyestop, righteyeright, eyesbottom, paint);
		canvas.drawLine(righteyeright, eyestop, righteyeleft, eyesbottom, paint);

	}
}
