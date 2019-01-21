package com.bellokano.receiptscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.util.SparseIntArray;
import android.view.Surface;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.io.IOException;
import java.nio.ByteBuffer;

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
}
