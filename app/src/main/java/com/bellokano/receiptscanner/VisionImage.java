package com.bellokano.receiptscanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.io.IOException;
import java.nio.ByteBuffer;

import androidx.annotation.RequiresApi;

import static android.content.Context.CAMERA_SERVICE;

public class VisionImage {

    private static final String TAG =   "MLKIT";
    private static final String MY_CAMERA_ID    =   "my_camera_id";

    private void imageFromBitmap(Bitmap bitmap) {
        FirebaseVisionImage image   =   FirebaseVisionImage.fromBitmap(bitmap);
    }

    private void imageFromMediaImage(Image mediaImage,  int rotation)   {
        FirebaseVisionImage image   =   FirebaseVisionImage.fromMediaImage(mediaImage, rotation);
    }

    private void imageFromBuffer(ByteBuffer buffer, int rotation)   {
        FirebaseVisionImageMetadata metadata    =   new FirebaseVisionImageMetadata.Builder()
                .setWidth(480)
                .setHeight(360)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation(rotation)
                .build();
        FirebaseVisionImage image   =   FirebaseVisionImage.fromByteBuffer(buffer,  metadata);
    }

    private void imageFromArray(byte[]  byteArray,  int rotation)   {
        FirebaseVisionImageMetadata metadata    =   new FirebaseVisionImageMetadata.Builder()
                .setWidth(480)
                .setHeight(360)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation(rotation)
                .build();
        FirebaseVisionImage image   =   FirebaseVisionImage.fromByteArray(byteArray,  metadata);
    }

    private void imageFromPath(Context  context, Uri    uri)    {
        FirebaseVisionImage image;
        try {
            image   =   FirebaseVisionImage.fromFilePath(context,   uri);
        }   catch (IOException  e)  {
            e.printStackTrace();
        }
    }

    private static final SparseIntArray ORIENTATION =   new SparseIntArray();
    static {
        ORIENTATION.append(Surface.ROTATION_0,  90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180,    270);
        ORIENTATION.append(Surface.ROTATION_270,    180);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getRotationCompensation(String   cameraId, Activity  activity, Context context)
        throws CameraAccessException    {
        int deviceRotation  =   activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation    =   ORIENTATION.get(deviceRotation);

        CameraManager   cameraManager   =   (CameraManager)   context.getSystemService(CAMERA_SERVICE);
        int sensorOrientation   =   cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);
        rotationCompensation    =   (rotationCompensation   +   sensorOrientation   +   270)    %   360;

        int result;
        switch (rotationCompensation)   {
            case    0:
                result  =   FirebaseVisionImageMetadata.ROTATION_0;
                break;
            case    90:
                result  =   FirebaseVisionImageMetadata.ROTATION_90;
                break;
            case    180:
                result  =   FirebaseVisionImageMetadata.ROTATION_180;
                break;
            case    270:
                result  =   FirebaseVisionImageMetadata.ROTATION_270;
                break;
            default:
                result  =   FirebaseVisionImageMetadata.ROTATION_0;
                Log.e(TAG,  "Bad rotation value: "  +   rotationCompensation);
        }
        return  result;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getCompensation(Activity   activity, Context context)
        throws CameraAccessException    {
        int rotation    =   getRotationCompensation(MY_CAMERA_ID,   activity,   context);
    }
}
