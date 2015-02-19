/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.games;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.View;

public class SampleImageView extends View {
	
	Context context;
	private String strFileName;
	public SampleImageView(Context context, String imageId) {
		super(context);
		this.strFileName = imageId + ".jpg";
		this.context = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try{
		File filePath = context.getFileStreamPath(strFileName);
		FileInputStream fi = new FileInputStream(filePath);
		Bitmap myBitmap = BitmapFactory.decodeStream(fi);
//		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.penguins);
		myBitmap = scaleImage(myBitmap, canvas.getWidth());
		
		int canvasHeight = canvas.getHeight();
		
		int top = (canvasHeight - myBitmap.getHeight()) / 2;
		
		canvas.drawColor(Color.parseColor("#000000"));
		
		canvas.drawBitmap(myBitmap, 0, 0, null);
		}catch(FileNotFoundException ex){}
	}

	private Bitmap scaleImage(Bitmap scaled, int newWidth) {
        int width = scaled.getWidth();
        int height = scaled.getHeight();
 
        float scaleWidth = ((float) newWidth) / width;
        float ratio = ((float) scaled.getWidth()) / newWidth;
        int newHeight = (int) (height / ratio);
        float scaleHeight = ((float) newHeight) / height;
 
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
 
        scaled = Bitmap.createBitmap(scaled, 0, 0, width, height, matrix, true);
        
        return scaled;
    }
}
