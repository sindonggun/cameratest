package cameratest.example.com.cameratest.overcamera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;

import cameratest.example.com.cameratest.ui.custom.AutoFitTextureView;

/**
 * Created by snowbean on 16-7-30.
 */

public class CameraModule implements CameraPrototype {
    private CameraOpenHelper mCameraOpenHelper;
    private OverCamera mOverCamera;

    private AutoFitTextureView mTextureView;
    private Size mPreviewSize;

    private OnFocusStateChangeListener mOnFocusStateChangeListener;
    private OnPreviewStartListener mOnPreviewStartListener;

    private int mWhichCamera = CameraOpenHelper.REAR_CAMERA;


    public CameraModule(Activity activity, final AutoFitTextureView textureView, Handler mainHandler) {
        mTextureView = textureView;

        mCameraOpenHelper = new CameraOpenHelper(activity, textureView, mainHandler);
        mCameraOpenHelper.setOnCameraOpenCallback(new CameraOpenHelper.OnCameraOpenCallback() {
            @Override
            public void onCameraOpened(OverCamera camera, Size previewSize) {
                mOverCamera = camera;
                mPreviewSize = previewSize;

                mOverCamera.setOnPreviewStartListener(mOnPreviewStartListener);
                mOverCamera.setOnFocusStateChangeListener(mOnFocusStateChangeListener);

                SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
                surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
                Surface surface = new Surface(surfaceTexture);

                startPreview(surface);
            }

            @Override
            public void onCameraDisconnected() {
                close();
                mOverCamera= null;
            }

            @Override
            public void onCameraError(int error) {
                close();
                mOverCamera= null;
            }
        });

    }

    public void resume() {
        openCamera(mWhichCamera);
    }

    private void openCamera(int which) {
        mWhichCamera = which;
        if (mTextureView.isAvailable()) {
            mCameraOpenHelper.openCamera(mPreviewSize.getWidth(), mPreviewSize.getHeight(), which);
        } else {
            mTextureView.setSurfaceTextureListener(mCameraOpenHelper.getSurfaceTextureListener());
        }
    }

    public void pause() {
        close();
    }


    public void switchCamera() {
        if (isBackCamera()) {
            close();
            openCamera(CameraOpenHelper.FRONT_CAMERA);
        } else {
            close();
            openCamera(CameraOpenHelper.REAR_CAMERA);
        }
    }

    @Override
    public void startPreview(Surface surface) {
        if (mOverCamera != null)
            mOverCamera.startPreview(surface);
    }

    @Override
    public void triggerFocusArea(float x, float y) {
        if (mOverCamera != null)
            mOverCamera.triggerFocusArea(x, y);
    }

    @Override
    public void takePicture(PhotoCaptureParameters parameters) {
        if (mOverCamera != null)
            mOverCamera.takePicture(parameters);
    }

    @Override
    public boolean isFrontCamera() {
        return mOverCamera != null && mOverCamera.isFrontCamera();
    }

    @Override
    public boolean isBackCamera() {
        return mOverCamera != null && mOverCamera.isBackCamera();
    }

    @Override
    public Size[] getSupportSizes() {
        if (mOverCamera != null)
            return mOverCamera.getSupportSizes();
        return new Size[0];
    }

    @Override
    public Size[] getPreviewSizes() {
        if (mOverCamera!= null)
            return mOverCamera.getPreviewSizes();
        return new Size[0];
    }

    @Override
    public Size chooseOptimalPreviewSize() {
        return null;
    }

    @Override
    public void setZoom(float zoom) {
        if (mOverCamera != null)
            mOverCamera.setZoom(zoom);
    }

    @Override
    public float getMaxZoom() {
        if (mOverCamera != null)
            return mOverCamera.getMaxZoom();
        return -1f;
    }

    @Override
    public boolean isFlashSupport() {
        return mOverCamera != null && mOverCamera.isFlashSupport();
    }

    @Override
    public boolean setFlashMode(FlashMode flashMode) {
        return mOverCamera != null && mOverCamera.setFlashMode(flashMode);
    }

    @Override
    public void setOnFocusStateChangeListener(OnFocusStateChangeListener onFocusStateChangeListener) {
        mOnFocusStateChangeListener = onFocusStateChangeListener;
    }

    @Override
    public void setOnPreviewStartListener(OnPreviewStartListener onPreviewStartListener) {
        mOnPreviewStartListener = onPreviewStartListener;
    }

    @Override
    public void close() {
        if (mOverCamera != null)
            mOverCamera.close();
    }

    public int getWhichCamera() {
        return mWhichCamera;
    }

}
