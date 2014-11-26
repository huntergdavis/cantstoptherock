package com.hunterdavis.gameutils.glrendering;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Handler;
import android.os.Message;

import com.hunterdavis.gameutils.core.SharedGameData;
import com.hunterdavis.gameutils.rendering.UIThreadMessages;

/**
 * The Class GameSurfaceView.
 */
public abstract class GLGameSurfaceViewRenderer implements
		GLSurfaceView.Renderer {

	// Our initialized context and handler
	// for messages back to UI threads
	private Context context;
	private Handler handler;

	// FPS timer
	double lastTick = -1;
	private long lastFrameDraw = 0;
	private long frameSamplesCollected = 0;
	private long frameSampleTime = 0;
	private double fps = 0;
	private boolean drawFPS = false;
	/** The texture pointer */

	// global singleton pointer to texture shared among all children
	/** The texture pointer */
	public int[] textures;
	public int[] textureDrawableReferences;

	private int mSurfaceWidth;
	private int mSurfaceHeight;

	// shared game data
	SharedGameData sharedGameData;

	/**
	 * Instantiates a new GL game surface view.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public GLGameSurfaceViewRenderer(Context contexta, Handler handlera,
			int[] texturesToLoad, SharedGameData shareData, boolean shouldWeDrawFPS) {
		this.context = contexta;
		this.handler = handlera;
		if (texturesToLoad != null) {
			if (texturesToLoad.length > 0) {
				textures = new int[texturesToLoad.length];
			}
		}
		textureDrawableReferences = texturesToLoad;
		sharedGameData = shareData;
		drawFPS = shouldWeDrawFPS;
	}

	/**
	 * let the game know a frame has been drawn
	 */
	public void incrementDrawnFrames() {
		sharedGameData.updateFrames();
	}

	// inherit this method and FPS drawing calculation is automatic
	public void onDrawFrame(GL10 gl) {

		long postRenderTime = System.currentTimeMillis();
		if (lastFrameDraw != 0) {
			long time = (long) (postRenderTime - lastFrameDraw);
			frameSampleTime += time;
			frameSamplesCollected++;
			if (frameSamplesCollected == 60) {
				fps = (60000 / frameSampleTime);
				frameSampleTime = 0;
				frameSamplesCollected = 0;
				if (drawFPS) {
					sharedGameData.updateFps(fps);
				}
			}
		}
		lastFrameDraw = postRenderTime;
		incrementDrawnFrames();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		// Load the textures
		if (textures != null) {
			for (int i = 0; i < textures.length; i++) {
				createGlobalTextureObject(gl, this.context, i);
			}
		}

		// should we really set default values in a parent class? think on this
		//gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping
		//gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		//gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Black Background
		//gl.glClearDepthf(1.0f); // Depth Buffer Setup
		//gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		//gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// perspective calculations
		//gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		checkGLError(gl);
		mSurfaceWidth = width;
		mSurfaceHeight = height;
		gl.glViewport(0, 0, width, height);

		Message msg = handler.obtainMessage();
		msg.what = UIThreadMessages.SCREENRESIZED.value();
		handler.sendMessage(msg);

	}

	public int getWidth() {
		return mSurfaceWidth;
	}

	public int getHeight() {
		return mSurfaceHeight;
	}

	private void createGlobalTextureObject(GL10 gl, Context context,
			int textureToLoadIndex) {

		// loading texture
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				textureDrawableReferences[textureToLoadIndex]);

		// generate one texture pointer
		gl.glGenTextures(1, textures, 0);

		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[textureToLoadIndex]);

		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// ensure that textures can wrap around
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
				GL10.GL_BLEND);

		// Use Android GLUtils to specify a two-dimensional texture image from
		// our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		// Clean up
		bitmap.recycle();
	}

	static void checkGLError(GL gl) {
		int error = ((GL10) gl).glGetError();
		if (error != GL10.GL_NO_ERROR) {
			throw new RuntimeException("GLError 0x"
					+ Integer.toHexString(error));
		}
	}
}
