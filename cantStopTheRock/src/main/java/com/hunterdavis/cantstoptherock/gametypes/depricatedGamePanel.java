package com.hunterdavis.cantstoptherock.gametypes;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.cantstoptherock.types.Balloon;
import com.hunterdavis.easyaudiomanager.EasyAudioManager;
import com.hunterdavis.gameutils.credits.CreditsScreen;
import com.hunterdavis.gameutils.rendering.GameCanvasThread;
import com.hunterdavis.gameutils.rendering.GameSurfaceView;
import com.hunterdavis.gameutils.rendering.namedColor;
import com.hunterdavis.gameutils.time.GameClockCountDownTimer;

// TODO: Auto-generated Javadoc
/**
 * The Class baloonPanel.
 */
class depricatedGamePanel extends GameSurfaceView implements SurfaceHolder.Callback {
	// member variables
	/** The num baloons. */
	public int numBalloons = 18;

	/** The num baloons to win. */
	public int numBalloonsToWin = 3;

	/** The color to win. */
	public int colorToWin = 0;

	/** The positional reference value of colorToWin **/
	private int colorToWinReferenceOffset = 0;

	/** The color to win name. */
	public String colorToWinName = "";

	/** The canvasthread. */
	@SuppressWarnings("unused")
	private GameCanvasThread canvasthread;

	/** The surface created. */
	public Boolean surfaceCreated;

	/** The m context. */
	public Context mContext;

	/* The Default Game Clock Time */
	private long gameClockTime = 5000;

	/** The m width. */
	private int mWidth = 0;

	/** The m height. */
	private int mHeight = 0;

	/** The game over. */
	private boolean gameOver = false;
	private boolean gameWin = false;

	/** The first run. */
	private boolean firstRun = true;

	/** The game started. */
	private boolean gameStarted = false;

	/** The baloons. */
	private Balloon baloons[];

	/** The colors. */
	private namedColor colors[];

	/** The color hit count. */
	private int colorHitCount[];

	// variables related to the placement of 'lives' status balloons
	private int drawableStatusBaloonSize = 0;
	private int drawableStatusBaloonYOffset = 0;
	private int statusBackgroundLeft = 0;
	private int statusBackgroundTop = 0;
	private int statusBackgroundRight = 0;
	private int statusBackgroundBottom = 0;

	/** The paint. */
	private Paint paint = null;

	/** The audio manager. */
	private EasyAudioManager audioManager;

	/** The Game CountDown Timer */
	private GameClockCountDownTimer gameClockTimer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (getHolder()) {

			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				if (gameStarted == false) {
					gameStarted = true;
					gameClockTimer.start();
				} else if (!gameOver) {
					testBaloonsForPops(event);
				} else if (gameOver) {
					CreditsScreen.startCreditScreen(getContext(),
							R.raw.credits,
							R.raw.cantstopcredits,
							R.drawable.hunterredbaloon, "You popped "
									+ getBaloonPopCount(colorToWin) + " "
									+ colorToWinName + " balloons");
					doLose();
				}
				return true;
			} else if (action == MotionEvent.ACTION_MOVE) {
				return true;
			} else if (action == MotionEvent.ACTION_UP) {

				return true;
			}
			return true;
		}
	}

	/**
	 * Instantiates a new baloon panel.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public depricatedGamePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		surfaceCreated = false;

		getHolder().addCallback(this);
		setFocusable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder
	 * , int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder
	 * )
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//
		if (surfaceCreated == false) {
			createThread(holder);
			// Bitmap kangoo = BitmapFactory.decodeResource(getResources(),
			// R.drawable.kangoo);
			surfaceCreated = true;
			// 1.8 seconds per balloon, will be harder at higher numbers


            gameClockTime = numBalloonsToWin * 1800;

            // testing showed 1800ms per balloon was too hard on 'easy'
            if(numBalloonsToWin < 5) {
                gameClockTime = numBalloonsToWin * 3600;
            }


			gameClockTimer = new GameClockCountDownTimer(gameClockTime, 10,
					gameClockTime);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.
	 * SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceCreated = false;

	}

	public void setGameClockTime(long newTime) {
		gameClockTime = newTime;
	}

	/**
	 * Inits the game state.
	 */
	public void initGameState() {
		Random rand = new Random();

		// init colors array with some nice colors

		colorHitCount = new int[10];
		for (int i = 0; i < 10; i++) {
			colorHitCount[i] = 0;
		}
		colors = new namedColor[10];
		colors[0] = new namedColor(Color.BLACK, "Black");
		colors[1] = new namedColor(Color.BLUE, "Blue");
		colors[2] = new namedColor(Color.DKGRAY, "Dark Gray");
		colors[3] = new namedColor(Color.GRAY, "Gray");
		colors[4] = new namedColor(Color.GREEN, "Green");
		colors[5] = new namedColor(Color.argb(255,253, 215, 228), "Pink");
		colors[6] = new namedColor(Color.MAGENTA, "Magenta");
		colors[7] = new namedColor(Color.RED, "Red");
		colors[8] = new namedColor(Color.YELLOW, "Yellow");
		colors[9] = new namedColor(Color.CYAN, "Cyan");

		int colorRand = rand.nextInt(colors.length);
		colorToWinReferenceOffset = colorRand;
		colorToWin = colors[colorRand].color;
		colorToWinName = colors[colorRand].colorName;

		baloons = new Balloon[numBalloons];
		int numBaloonsCanWin = 0;
        int balloonSize = 6 + rand.nextInt(20);
        if(numBalloonsToWin < 5) {
            balloonSize = 25 + rand.nextInt(30);
        }else if (numBalloonsToWin < 10) {
            balloonSize = 20 + rand.nextInt(10);
        }

		for (int i = 0; i < numBalloons; i++) {
			baloons[i] = new Balloon(rand.nextInt(15 + (mWidth - 15)),
					rand.nextInt(15 + (mHeight - 15)),
					colors[rand.nextInt(colors.length)].color,
                    balloonSize);
			if (baloons[i].color == colorToWin) {
				numBaloonsCanWin++;
			}
		}
		for (int i = numBaloonsCanWin; i < numBalloonsToWin; i++) {
			makeOneBaloonInArrayColorToWin();
		}

		firstRun = false;
	}

	/**
	 * Make one baloon in array color to win.
	 */
	public void makeOneBaloonInArrayColorToWin() {
		for (int i = baloons.length - 1; i > 0; i--) {
			if (baloons[i].color != colorToWin) {
				baloons[i].color = colorToWin;
				return;
			}
		}
	}

	// we update the position of the baloons on screen in updateGameState
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hunterdavis.fiveseconds.gameutils.rendering.GameSurfaceView#
	 * updateGameState()
	 */
	public void updateGameState() {

		if (gameClockTimer != null) {
			if (gameClockTimer.gameOver == true) {
				gameOver = true;
			}
		}

		if (gameOver == true) {
			return;
		}

		// if first game state, init
		if ((mWidth > 0) && firstRun) {
			initGameState();
		}

		if (gameStarted) {
			// update current baloons a tick
			updateCurrentBaloonTick();
		}
	}

	/**
	 * Update current baloon tick.
	 */
	public void updateCurrentBaloonTick() {
		if (firstRun == true) {
			return;
		}
		int tickValue;
		if(numBalloons < 5) {
			tickValue = 1;
		}else if (numBalloons < 8) {
			tickValue = 2;
		}else {
			tickValue = 3;
		}
		for (int i = 0; i < baloons.length; i++) {
			baloons[i].age++;

			baloons[i].updateXandYLoc(baloons[i].xLocation,
					(baloons[i].yLocation - tickValue));

			if (baloons[i].yLocation - baloons[i].size - 2 < 0) {
				baloons[i].updateXandYLoc(baloons[i].xLocation, mHeight);
			}
		}
	}

	/**
	 * Test baloons for pops.
	 * 
	 * @param event
	 *            the event
	 */
	public void testBaloonsForPops(MotionEvent event) {
		float xVal = event.getX();
		float yVal = event.getY();
		for (int i = 0; i < baloons.length; i++) {
			if (baloons[i].isPointWithinBaloon(xVal, yVal)) {
				baloons[i].pop();
				playPoppingSound(getContext());
				updateBaloonPopCount(baloons[i].color);
			}
		}
	}

	/**
	 * Play popping sound.
	 * 
	 * @param context
	 *            the context
	 */
	public void playPoppingSound(Context context) {
		// knowing that the baloon pop sound is 0 is DIRRRRTY
		audioManager.playSound(0, context);
	}

	/**
	 * Update baloon pop count.
	 * 
	 * @param color
	 *            the color
	 */
	public void updateBaloonPopCount(int color) {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].color == color) {
				colorHitCount[i]++;
				if ((colorHitCount[i] >= numBalloonsToWin)
						&& (colors[i].color == colorToWin)) {
					gameWin = true;
					gameOver = true;
					gameClockTimer.cancel();

				}
			}
		}
	}

	public int getBaloonPopCount(int color) {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].color == color) {
				return colorHitCount[i];
			}
		}
		return 0;
	}

	/**
	 * Do lose.
	 */
	public void doLose() {
		// quit to mainmenu
		((Activity) mContext).finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hunterdavis.fiveseconds.gameutils.rendering.GameSurfaceView#onDraw
	 * (android.graphics.Canvas)
	 */
	@Override
	public void onDraw(Canvas canvas) {

		mWidth = canvas.getWidth();
		mHeight = canvas.getHeight();
		if (drawableStatusBaloonSize == 0) {
			drawableStatusBaloonSize = mHeight / 100;
			drawableStatusBaloonYOffset = mHeight / 20;
			statusBackgroundLeft = Math.min(mWidth
					- (int) ((mWidth / 20) * (numBalloonsToWin + 0.5)), mWidth
					- (int) ((mWidth / 20) * 4));
			statusBackgroundTop = drawableStatusBaloonYOffset
					+ drawableStatusBaloonSize * 4;
			statusBackgroundRight = mWidth - 2;
			statusBackgroundBottom = 2;
		}

		if (paint == null) {
			paint = new Paint();
			paint.setTextAlign(Paint.Align.CENTER);
		}

		paint.setColor(Color.WHITE);
		// clear the screen with the black painter.
		canvas.drawRect(0, 0, mWidth, mHeight, paint);

		// draw game over if game over
		if ((!gameOver) && (firstRun == false) && gameStarted) {
			drawBaloons(canvas, paint);
		}

		if (gameStarted == false) {
			paint.setColor(Color.MAGENTA);
			paint.setTextSize(30);
			canvas.drawText("Pop " + numBalloonsToWin + " " + colorToWinName
					+ " Balloons!", (mWidth / 2), mHeight / 4, paint);
		}

		if (gameOver == true) {
			paint.setTextSize(60);
			if (gameWin == true) {
				paint.setColor(Color.BLUE);
				canvas.drawText("YOU WIN!", (mWidth / 2), mHeight / 4, paint);
			} else {
				paint.setColor(Color.MAGENTA);
				canvas.drawText("YOU LOSE!", (mWidth / 2), mHeight / 4, paint);
			}
		}

		// draw how many lives you have left
		drawStatusBaloons(canvas, paint);

		// draw the game clock
		paint.setColor(Color.BLACK);
		paint.setTextSize(14);
		float gameTime = 0;
		if(gameClockTimer != null) {
			gameTime = gameClockTimer.gameClock;
		}
		canvas.drawText(String.format("%.2f", gameTime / 1000)
				.toString() + " Seconds", (mWidth - (mWidth / 10)),
				mHeight / 30, paint);

	}

	/**
	 * Draw baloons.
	 * 
	 * @param canvas
	 *            the canvas
	 * @param paint
	 *            the paint
	 */
	public void drawBaloons(Canvas canvas, Paint paint) {
		for (int i = 0; i < baloons.length; i++) {
			baloons[i].drawBaloon(canvas, paint);
			if (baloons[i].shouldThisBaloonDie()) {
				Random rand = new Random();
				baloons[i] = new Balloon(rand.nextInt(15 + (mWidth - 15)),
						rand.nextInt(15 + (mHeight - 15)),
						colors[rand.nextInt(colors.length)].color,
						6 + rand.nextInt(20));
			}
		}
	}

	/**
	 * Draw statusbaloons.
	 * 
	 * @param canvas
	 *            the canvas
	 * @param paint
	 *            the paint
	 */
	public void drawStatusBaloons(Canvas canvas, Paint paint) {

		if (firstRun) {
			return;
		}

		// first draw the background
		paint.setColor(Color.LTGRAY);
		paint.setStyle(Style.FILL);
		canvas.drawRect(statusBackgroundLeft, statusBackgroundTop,
                statusBackgroundRight, statusBackgroundBottom, paint);

		// draw a gray box
		canvas.drawRect(statusBackgroundLeft, statusBackgroundTop,
				statusBackgroundRight, statusBackgroundBottom, paint);
		// and a black border
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(statusBackgroundLeft, statusBackgroundTop,
				statusBackgroundRight, statusBackgroundBottom, paint);

		// draw each balloon
		for (int i = 0; i < numBalloonsToWin; i++) {
			int baloonXVal = mWidth - (int) ((mWidth / 20) * (i + 0.5));
			(new Balloon(baloonXVal, drawableStatusBaloonYOffset, colorToWin,
					drawableStatusBaloonSize)).drawBaloon(canvas, paint);

			paint.setColor(Color.RED);
			paint.setStrokeWidth(3);
			if (colorHitCount[colorToWinReferenceOffset] > i) {

				canvas.drawLine(baloonXVal - drawableStatusBaloonSize,
						drawableStatusBaloonYOffset - drawableStatusBaloonSize,
						baloonXVal + drawableStatusBaloonSize,
						drawableStatusBaloonYOffset + drawableStatusBaloonSize,
						paint);

				canvas.drawLine(baloonXVal + drawableStatusBaloonSize,
						drawableStatusBaloonYOffset - drawableStatusBaloonSize,
						baloonXVal - drawableStatusBaloonSize,
						drawableStatusBaloonYOffset + drawableStatusBaloonSize,
						paint);
			}
		}
	}

	/**
	 * Sets the num baloons to win.
	 * 
	 * @param numBaloonsToSet
	 *            the new num baloons to win
	 */
	public void setNumBaloonsToWin(int numBaloonsToSet) {
		numBalloonsToWin = numBaloonsToSet;
		if (numBalloonsToWin < 11) {
			numBalloons = numBalloonsToWin * 10;
		} else {
			numBalloons = 100;
		}

	}

	// setAudioManager is a setter method for the audio manager
	/**
	 * Sets the audio manager.
	 * 
	 * @param audioM
	 *            the new audio manager
	 */
	public void setAudioManager(EasyAudioManager audioM) {
		audioManager = audioM;
	}
} // end class