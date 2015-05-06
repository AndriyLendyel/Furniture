package com.softserveinc.furniture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.GestureHandlerAndroid;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.BoundingBox;
import com.metaio.sdk.jni.GestureHandler;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.ImageStruct;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

public class CameraActivity extends ARViewActivity {

	private MetaioSDKCallbackHandler mCallbackHandler;
	private GestureHandlerAndroid mGestureHandler;
	private int mGestureMask;
	private File mImageFile;
	private TrackingValues mTrackingValues;
	boolean mImageTaken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGestureMask = GestureHandler.GESTURE_ALL;
		mCallbackHandler = new MetaioSDKCallbackHandler();
		mGestureHandler = new GestureHandlerAndroid(metaioSDK, mGestureMask);
		mImageTaken = false;
		mImageFile = new File(Environment.getExternalStorageDirectory(),
				"14.jpg");
	}

	@Override
	protected void onStart() {
		super.onStart();

		// if a tracking target image exists, then the app is still running in
		// the background
		if (mImageFile.exists() && mTrackingValues != null) {
			// the tracking target has to be reset and so are the tracking
			// values
			metaioSDK.setImage(mImageFile);
			metaioSDK.setCosOffset(1, mTrackingValues);

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		super.onTouch(v, event);

		mGestureHandler.onTouch(v, event);

		return true;
	}
	
	@Override
	public void onDrawFrame() {
		super.onDrawFrame();

		// reset the location and scale of the geometries
		if (mImageTaken == true) {
			// load the dummy tracking config file
			boolean result = metaioSDK.setTrackingConfiguration("DUMMY");
			MetaioDebug.log("Tracking data Dummy loaded: " + result);

			metaioSDK.setCosOffset(1, mTrackingValues);

			mImageTaken = false;
		}

	}

	@Override
	protected int getGUILayout() {
		// Attaching layout to the activity
		return R.layout.camera_activity;
	}
	
	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler()
	{
		return mCallbackHandler;
	}

	public void onButtonClick(View v) {
		finish();
	}

	@Override
	protected void loadContents() {

		try {
			// Getting a file path for tracking configuration XML file
			File trackingConfigFile = AssetsManager
					.getAssetPathAsFile(getApplicationContext(),
							"TutorialHelloWorld/Assets/TrackingData_MarkerlessFast.xml");

			// Assigning tracking configuration
			boolean result = metaioSDK
					.setTrackingConfiguration(trackingConfigFile);
			MetaioDebug.log("Tracking data loaded: " + result);

			String modelFileName = getIntent().getExtras().getString(
					"modelFileName");
			// Getting a file path for a 3D geometry
			File metaioManModel = AssetsManager.getAssetPathAsFile(
					getApplicationContext(), "TutorialHelloWorld/Assets/"
							+ modelFileName);
			if (metaioManModel != null) {
				// Loading 3D geometry
				IGeometry geometry = metaioSDK.createGeometry(metaioManModel);
				if (geometry != null) {
					// Set geometry properties
					BoundingBox boundingBox = geometry.getBoundingBox();
					Vector3d max = boundingBox.getMax();
					Vector3d min = boundingBox.getMin();
					float absX = Math.abs(max.getX() - min.getX());
					float absY = Math.abs(max.getY() - min.getY());
					float absZ = Math.abs(max.getZ() - min.getZ());
					float maxD = Math.max(Math.max(absX, absY), absZ);
					geometry.setScale(250f / maxD);
					geometry.setRotation(new Rotation((float) (Math.PI/2d), 0, (float) (Math.PI/2d)));
					mGestureHandler.addObject(geometry, 1);
				} else
					MetaioDebug.log(Log.ERROR, "Error loading geometry: "
							+ metaioManModel);
			}
		} catch (Exception e) {
			MetaioDebug.printStackTrace(Log.ERROR, e);
		}
	}

	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// Not used in this tutorial
	}

	// called when the save screenshot button has been pressed
	public void onSaveScreen(View v) {
		// request screen shot
		metaioSDK.requestScreenshot();

		// take a picture using the SDK and save it to external storage
		metaioSDK.requestCameraImage(mImageFile);
	}

	final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {
		/**
		 * Get path to Pictures directory if it exists
		 * 
		 * @return Path to Pictures directory on the device if found, else
		 *         <code>null</code>
		 */
		private File getPicturesDirectory() {
			File picPath = null;

			try {
				picPath = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File path = new File(picPath, "Metaio Example");
				boolean success = path.mkdirs() || path.isDirectory();
				if (!success) {
					path = new File(Environment.getExternalStorageDirectory(),
							"Pictures");
				}
				success = path.mkdirs() || path.isDirectory();
				if (!success) {
					path = Environment.getDataDirectory();
				}

				return path.getAbsoluteFile();
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public void onSDKReady() {
			// show GUI
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mGUIView.setVisibility(View.VISIBLE);
				}
			});
		}

		// callback function for taking images using SDK
		@Override
		public void onCameraImageSaved(final File filePath) {
			// save the tracking values in case the application exits improperly
			mTrackingValues = metaioSDK.getTrackingValues(1);
			// mImageTaken = true;

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (filePath.getPath().length() > 0) {
						metaioSDK.setImage(filePath);
					}
				}
			});

		}

		@Override
		public void onScreenshotImage(ImageStruct image) {
			final File directory = getPicturesDirectory();
			if (directory == null) {
				image.release();
				image.delete();

				MetaioDebug
						.log(Log.ERROR,
								"Could not find pictures directory, not saving screenshot");
				return;
			}

			// Creating directory
			directory.mkdirs();

			try {
				// Creating file
				final File screenshotFile = new File(directory, "screenshot_"
						+ System.currentTimeMillis() + ".jpg");
				screenshotFile.createNewFile();

				FileOutputStream stream = new FileOutputStream(screenshotFile);

				boolean result = false;
				Bitmap bitmap = image.getBitmap();
				try {
					result = bitmap.compress(CompressFormat.JPEG, 100, stream);
				} finally {
					// release screenshot ImageStruct
					image.release();
					image.delete();

					stream.close();
				}

				if (!result) {
					MetaioDebug.log(Log.ERROR, "Failed to save screenshot to "
							+ screenshotFile);
					return;
				}

				final String url = MediaStore.Images.Media.insertImage(
						getContentResolver(), bitmap,
						"screenshot_" + System.currentTimeMillis(),
						"screenshot");

				// Recycle the bitmap
				bitmap.recycle();
				bitmap = null;

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String message = "The screenshot has been added to the gallery.";
						if (url == null) {
							message = "Unable to add the screen shot to the gallery";
						} else {
							MediaScannerConnection.scanFile(
									getApplicationContext(),
									new String[] { screenshotFile
											.getAbsolutePath() },
									new String[] { "image/jpg" },
									new OnScanCompletedListener() {
										@Override
										public void onScanCompleted(
												String path, Uri uri) {
											MetaioDebug
													.log("Screen saved at path "
															+ path);
										}
									});
						}

						Toast toast = Toast.makeText(getApplicationContext(),
								message, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					
					}
				});
			} catch (IOException e) {
				MetaioDebug.printStackTrace(Log.ERROR, e);
			}
		}
	}
}
