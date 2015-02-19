/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.common;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UIHelper {

	public UIHelper() {
	}

	/**
	 * Create and show an AlertDialog with Parameters
	 * 
	 * @param context
	 * @param title
	 *            AlertDialog title
	 * @param content
	 *            AlertDialog content
	 * @param negativeButton
	 *            custom Cancel button text
	 * @param positiveButton
	 *            custom Ok button text
	 * @param dialogListener
	 *            button click callback interface
	 */
	public static void createDialog(Context context, String title,
			String content, String negativeButton, String positiveButton,
			final DialogResultListener dialogListener) {

		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		// customize and set title and message content
		TextView customTitle = new TextView(context);

		customTitle.setText(title);
		customTitle.setPadding(10, 10, 10, 10);
		customTitle.setGravity(Gravity.CENTER);
		customTitle.setTextSize(20);

		builder.setCustomTitle(customTitle);

		TextView customMessage = new TextView(context);

		customMessage.setPadding(10, 40, 10, 40);
		customMessage.setText(content);
		customMessage.setGravity(Gravity.CENTER_HORIZONTAL);
		customMessage.setTextSize(20);

		builder.setView(customMessage);

		// handle cancel button click
		builder.setNegativeButton(negativeButton, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialogListener.onNegativeClick();
			}

		});

		// handle ok button click
		builder.setPositiveButton(positiveButton, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialogListener.onPositiveClick(null);
			}

		});

		builder.show();

	}

	/**
	 * Helper method used to show Toast Message with parameters
	 * 
	 * @param context
	 *            Activity Context
	 * @param messageContent
	 *            Content to show
	 * @param duration
	 */
	public static void showToastMessage(Context context, String messageContent,
			int duration) {
		Toast.makeText(context, messageContent, duration).show();
	}

	/**
	 * Create and show an AlertDialog with custom EditText and parameters
	 * 
	 * @param context
	 * @param title
	 *            AlertDialog title
	 * @param negativeButton
	 *            custom cancel button text
	 * @param positiveButton
	 *            custom ok button text
	 * @param dialogListener
	 *            button click callback interface
	 */
	public static void createInputDialog(final Context context, String title,
			String negativeButton, String positiveButton,
			final DialogResultListener dialogListener) {

		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		// customize and set title and message content
		TextView customTitle = new TextView(context);

		customTitle.setText(title);
		customTitle.setPadding(10, 10, 10, 10);
		customTitle.setGravity(Gravity.CENTER);
		customTitle.setTextSize(20);

		builder.setCustomTitle(customTitle);

		TextView customMessage = new TextView(context);

		customMessage.setPadding(10, 40, 10, 40);
		customMessage.setText("test input dialog");
		customMessage.setGravity(Gravity.CENTER_HORIZONTAL);
		customMessage.setTextSize(20);

		builder.setView(customMessage);

		// Set an EditText view to get user input
		final EditText input = new EditText(context);

		input.setPadding(10, 40, 10, 40);
		input.setGravity(Gravity.CENTER_HORIZONTAL);
		input.setTextSize(20);
		builder.setView(input);

		// handle cancel button click
		builder.setNegativeButton(negativeButton, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialogListener.onNegativeClick();
			}

		});

		// handle ok button click
		builder.setPositiveButton(positiveButton, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// return input value to delegated object
				dialogListener.onPositiveClick(input.getText());
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
			}

		});

		builder.show();

	}

	/**
	 * @author nmcuong
	 * 
	 *         Button click callback interface
	 */
	public interface DialogResultListener {
		public abstract void onPositiveClick(Object result);

		public abstract void onNegativeClick();
	}

	
	/**
	 * notify dialog when updating data
	 * @param context
	 * @param content
	 * @param imageType
	 * @return
	 */
	public static void showNotifyDialog(Context context,
			String content, OnClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(content)
		       .setCancelable(false)
		       .setPositiveButton("OK", onClickListener);
		
		AlertDialog alert = builder.create();
		alert.show();
	}

}
