package com.skript.photobombgo.photoeditor.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import com.skript.photobombgo.photoeditor.graphics.commands.ImageProcessingCommand;

import java.util.LinkedList;

/**
 * ImageProcessor class works as a thread pool with only one working thread.
 * 
 */

public class ImageProcessor {
	// Singleton
	private static ImageProcessor instance = null;
	private  boolean modified = false;
	private ImageProcessorListener processListener;
	private LinkedList<ImageProcessingCommand> queue;
	private Bitmap savedBitmap;
	private Bitmap lastResultBitmap;
	private Matrix imageMatrix;

    public Matrix getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(Matrix viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    private Matrix viewMatrix;

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    private Rect rect;
	private Context applicationContext;
	private Handler uiThreadHandler;


	public  boolean isModified(){
		return modified;

	}
	public  void resetModificationFlag(){
		 modified = false;

	}

	public static ImageProcessor getInstance() {
		if (instance == null) {
			instance = new ImageProcessor();
		}
		return instance;
	}

	private ImageProcessor() {
		queue = new LinkedList<ImageProcessingCommand>();
		workingThread.start();
	}

	// ===========================

	private Thread workingThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				ImageProcessingCommand cmd;
				try {
					// Queue object works as monitor
					synchronized (queue) {
						while (queue.isEmpty()) {
							queue.wait();
						}
						cmd = queue.poll();
					}
					// Process command
					onProcessStart();
					lastResultBitmap = cmd.process(savedBitmap);
					cmd = null;
					onProcessEnd();
					modified=true;
				} catch (InterruptedException e) {
					Log.e("Working Thread",
							"InterruptedException handled:" + e.getMessage());
					break; // Terminate
				}
			}
		}

		private void onProcessStart() {
			if (uiThreadHandler!= null && processListener != null) {
				uiThreadHandler.post(new Runnable() {
					@Override
					public void run() {
						processListener.onProcessStart();
					}
				});
			}
		}

		private void onProcessEnd() {
			// do postprocessing HERE
			// notify UI thread about new bitmap and save results
			if (uiThreadHandler!= null && processListener != null) {
				uiThreadHandler.post(new Runnable() {
					@Override
					public void run() {
						Log.i("Photo Editor",
								"Notify Listener: STOP");
						processListener.onProcessEnd(lastResultBitmap);
					}
				});
			}
		}
	});


	public void setBitmap(Bitmap bitmap) {
		this.savedBitmap = bitmap;
	}


	public Bitmap getBitmap() {
		return savedBitmap;
	}


	/**
	 * Runs {@code ImageProcessingCommand} or add to queue if currently some
	 * command is running
	 *
	 * @param command
	 */
	public void runCommand(ImageProcessingCommand command) {
		conditionallyAddToQueue(command);
	}

	/**
	 * Conditionally add new command to processing queue. If last element is the
	 * same type but with different argument as added one, removes last before
	 * adding. This method is helpful in slider handling, which send command in
	 * every value change.
	 */
	private void conditionallyAddToQueue(ImageProcessingCommand command) {
		synchronized (queue) {
			if (!queue.isEmpty()) {
				ImageProcessingCommand c = queue.getLast();
				Log.i("Photo Editor", "Command Added TO queue ID = " + c);
				if (c.getId().equals(command.getId())) {
					queue.removeLast();
					Log.i("Photo Editor", "Queue Element Skipped");
				}
			}
			queue.add(command);
			queue.notify();
		}
	}

	public Matrix getImageMatrix() {
		return imageMatrix;
	}

	public void setImageMatrix(Matrix iMatrix) {
		this.imageMatrix = iMatrix;
	}

	public void save() {
		if (lastResultBitmap != null) {
			if (savedBitmap != lastResultBitmap && savedBitmap != null) {
				savedBitmap.recycle();
			}
			savedBitmap = lastResultBitmap;
			lastResultBitmap = null;
		}
	}

	public ImageProcessorListener getProcessListener() {
		return processListener;
	}

	public void setProcessListener(ImageProcessorListener processListener) {
		this.processListener = processListener;
	}

	public void clearProcessListener() {
		this.processListener = null;
		if (lasResultCanBeRecycled()) {
			lastResultBitmap.recycle();
		}
		this.lastResultBitmap = null;
	}

	private boolean lasResultCanBeRecycled() {
		return lastResultBitmap != savedBitmap && lastResultBitmap != null;
	}

	public Bitmap getLastResultBitmap() {
		return lastResultBitmap;
	}

	public void setLastResultBitmap(Bitmap lastResultBitmap) {
		this.lastResultBitmap = lastResultBitmap;
	}
	
	/**
	 * This method should be called in UI thread, or handler won't be able to receive messages.
	 */
	public void setApplicationCotnext(Context applicationContext){
		if (this.applicationContext == null){
			uiThreadHandler = new Handler();	
		}
		this.applicationContext = applicationContext;
	}
}
