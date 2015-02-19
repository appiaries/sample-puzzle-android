/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.games;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TileBoardView extends RelativeLayout implements OnTouchListener {

	private TileBoardViewListener listener;

	public enum Direction {
		Up(1), Right(2), Down(3), Left(4);

		private int code;

		private Direction(int code) {
			this.code = code;
		}

		public int val() {
			return code;
		}
	}

	private Bitmap originalImage;

	// Tile size on Game Board
	private int tileWidth;
	private int tileHeight;

	// Game Board object
	private TileBoard board;

	// List to hold array of Tiles (ImageView)
	private List<ImageView> tiles;

	// Selected ImageView (Tile)
	private ImageView draggedTile;

	// Tracked Drag direction
	private int draggedDirection;

	private Context appContext;

	// Last touched point
	private int lastMoveX = 0;
	private int lastMoveY = 0;

	// Total moved distance
	private int totalTranslateX = 0;
	private int totalTranslateY = 0;

	// Selected Tile coordinate
	private Point movingTilePoint = null;

	public TileBoardView(Context context) {
		super(context);

		this.appContext = context;

		this.setOnTouchListener(this);
	}

	public TileBoardView(Context context, AttributeSet attributeSets) {
		super(context, attributeSets);

		this.appContext = context;

		this.setOnTouchListener(this);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v == null) {
			return false;
		}
		if(this.board.isAllTilesCorrect()){
			return false;
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Begin
			Log.d("Touch Event", "Begin");

			// Save last move x,y
			this.lastMoveX = (int) event.getX();
			this.lastMoveY = (int) event.getY();

			// Save dragged tile
			this.assignDraggedTileAtPoint(new Point((int) event.getX(),
					(int) event.getY()));
			break;
		case MotionEvent.ACTION_MOVE:
			// Moving
			Log.d("Touch Event", "Moving");

			if (this.draggedTile == null) {
				break;
			}

			// Calculate translation distance
			int dx = (int) event.getX() - this.lastMoveX;
			int dy = (int) event.getY() - this.lastMoveY;

			// Update Total Translation
			this.totalTranslateX += dx;
			this.totalTranslateY += dy;

			// Update last move x,y
			this.lastMoveX = (int) event.getX();
			this.lastMoveY = (int) event.getY();

			// Move the tile
			this.movingDraggedTile(new Point(dx, dy));
			break;
		case MotionEvent.ACTION_UP:
			// End
			Log.d("Touch Event", "End");

			if (this.draggedTile == null) {
				break;
			}

			// Check if tab event
			if (this.totalTranslateX <= 2 && this.totalTranslateY <= 2) {
				// Move selected tile
				this.moveTile(this.draggedTile, this.draggedDirection,
						this.movingTilePoint);
			} else {
				// Snap Dragged Tile
				this.snapDraggedTile();
			}

			// Reset
			this.resetAllMoveVariable();
			break;
		default:
			break;
		}

		return true;
	}

	public void setTileBoardViewListener(TileBoardViewListener listener) {
		this.listener = listener;
	}

	/**
	 * Initial TileBoard with Bitmap and Size
	 */
	public void playWithImage(Bitmap image, int xSize, int ySize,
			int screenWidth) {
		// initial the game Board
		this.board = new TileBoard(xSize, ySize);

		// Save the Original Image
		this.originalImage = image;

		// Resize Original Image to fit Screen width
		Bitmap scaledBitmap = scaleImage(image, screenWidth);

		// Calculate tile's size based on Resized Bitmap
		this.tileWidth = scaledBitmap.getWidth() / xSize;
		this.tileHeight = scaledBitmap.getHeight() / ySize;

		// Slice image to tiles array
		this.tiles = this.sliceImageToAnArray(scaledBitmap);
	}

	/**
	 * Scaling Original Image to fit the screen width and remaining the ratio.
	 * 
	 * @param originalImage
	 * @param newWidth
	 * @return
	 */
	private Bitmap scaleImage(Bitmap originalImage, int newWidth) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float ratio = ((float) originalImage.getWidth()) / newWidth;
		int newHeight = (int) (height / ratio);
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		originalImage = Bitmap.createBitmap(originalImage, 0, 0, width, height,
				matrix, true);

		return originalImage;
	}

	/**
	 * Slicing original image to tile images
	 * 
	 * @return ImageView List
	 */
	private List<ImageView> sliceImageToAnArray(Bitmap imageToSlice) {
		List<ImageView> slices = new ArrayList<ImageView>();
		Bitmap bitmap;

		for (int j = 0; j < this.board.getXSize(); j++) {
			for (int i = 0; i < this.board.getYSize(); i++) {
				// initial ImageView from bitmap sliced
				ImageView imageView = new ImageView(this.appContext);

				int value = this.board
						.tileAtCoordinate(new Point(i + 1, j + 1));

				// slice
				bitmap = Bitmap.createBitmap(imageToSlice,
						((int) this.tileWidth) * i,
						((int) this.tileHeight) * j, (int) this.tileWidth,
						(int) tileHeight);

				// draw border lines
				Canvas canvas = new Canvas(bitmap);
				Paint paint = new Paint();
				paint.setColor(Color.parseColor("#ffffff"));
				paint.setStrokeWidth(4f);

				canvas.drawLine(0, 0, 0, tileHeight, paint);
				canvas.drawLine(0, tileHeight, tileWidth, tileHeight, paint);
				canvas.drawLine(tileWidth, tileHeight, tileWidth, 0, paint);
				canvas.drawLine(tileWidth, 0, 0, 0, paint);

				// ***** Testing only ******
				/*
				 * Paint paint2 = new Paint(); paint2.setColor(Color.YELLOW);
				 * paint2.setTextSize(30.0f); paint2.setShadowLayer(1f, 0f, 1f,
				 * Color.RED); String text = String.valueOf(value);
				 * 
				 * Rect bounds = new Rect(); paint.getTextBounds(text, 0,
				 * text.length(), bounds); int drawX = (bitmap.getWidth() -
				 * bounds.width()) / 2; int drawY = (bitmap.getHeight() +
				 * bounds.height()) / 2;
				 * 
				 * canvas.drawText(text, drawX, drawY, paint2);
				 */
				// *************************

				if (bitmap != null) {
					// Create ImageView from sliced bitmap
					imageView.setImageBitmap(bitmap);
					slices.add(imageView);
				}

			}
		}

		return slices;
	}

	/**
	 * Shuffle the game Board
	 */
	public void shuffle() {
		// Shuffle
		this.board.shuffle();

		// draw tiles
		this.drawTiles(false);
	}

	private void drawTiles(boolean isFinished) {
		// Remove all sub views in Layout
		this.removeAllViews();

		for (int j = 1; j <= this.board.getYSize(); j++) {
			for (int i = 1; i <= this.board.getXSize(); i++) {

				// Get tile by coordinate
				int value = this.board.tileAtCoordinate(new Point(i, j));

				Log.d("Board", "" + value);

				ImageView tileView;

				if (value == 0) {
					continue;
				} else {
					tileView = this.tiles.get(value - 1);
				}

				// setting image position
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						this.tileWidth, this.tileHeight);

				params.topMargin = tileHeight * (j - 1);
				params.leftMargin = tileWidth * (i - 1);

				// add TileView to Layout
				addView(tileView, params);
			}
		}
	}

	private void tileWasMoved() {
		if (this.board.isAllTilesCorrect()) {
			
			// Draw completed board.
			// Get tile by coordinate
			int value = this.board.tileAtCoordinate(new Point(this.board
					.getXSize(), this.board.getYSize()));

			ImageView tileView = this.tiles.get(this.tiles.size() - 1);

			// setting image position
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					this.tileWidth, this.tileHeight);

			params.topMargin = tileHeight * (this.board.getXSize() - 1);
			params.leftMargin = tileWidth * (this.board.getYSize() - 1);

			// add TileView to Layout
			addView(tileView, params);

			listener.onFinish();
		}
	}

	private Point coordinateFromPoint(Point point) {

		if (point.x == 0 && point.y == 0) {
			return new Point(1, 1);
		}

		return new Point((int) (point.x / this.tileWidth) + 1,
				(int) (point.y / this.tileHeight) + 1);
	}

	private void assignDraggedTileAtPoint(Point position) {
		Point coor = this.coordinateFromPoint(position);

		this.movingTilePoint = coor;

		// Cannot move this tile
		if (!this.board.canMoveTile(coor)) {
			this.draggedDirection = 0;
			this.draggedTile = null;
			return;
		}

		if (this.board.tileAtCoordinate(new Point(coor.x, coor.y - 1)) == 0) {
			this.draggedDirection = Direction.Up.val();
		} else if (this.board.tileAtCoordinate(new Point(coor.x + 1, coor.y)) == 0) {
			this.draggedDirection = Direction.Right.val();
		} else if (this.board.tileAtCoordinate(new Point(coor.x, coor.y + 1)) == 0) {
			this.draggedDirection = Direction.Down.val();
		} else if (this.board.tileAtCoordinate(new Point(coor.x - 1, coor.y)) == 0) {
			this.draggedDirection = Direction.Left.val();
		}

		for (ImageView tile : this.tiles) {
			if (this.rectContainsPoint(tile, position)) {
				this.draggedTile = tile;
				break;
			}
		}
	}

	private boolean rectContainsPoint(ImageView rect, Point point) {
		return (rect.getX() < point.x && rect.getY() < point.y
				&& rect.getX() + rect.getWidth() > point.x && rect.getY()
				+ rect.getHeight() > point.y);
	}

	private void movingDraggedTile(Point translationPoint) {
		int x = 0, y = 0;

		Log.d("TranslationPoint", "x=" + translationPoint.x + "/" + "y="
				+ translationPoint.y);

		Point translation = translationPoint;

		switch (this.draggedDirection) {
		case 1: // Up
			if (this.totalTranslateY > 0)
				y = 0;
			else if (this.totalTranslateY < -this.tileHeight - 1)
				// y = -this.tileHeight;
				break;
			else
				y = translation.y;
			break;
		case 2: // Right
			if (this.totalTranslateX < 0)
				x = 0;
			else if (this.totalTranslateX > this.tileWidth + 1)
				// x = this.tileWidth;
				break;
			else
				x = translation.x;
			break;
		case 3: // Down
			if (this.totalTranslateY < 0)
				y = 0;
			else if (this.totalTranslateY > this.tileHeight + 1)
				// y = this.tileHeight;
				break;
			else
				y = translation.y;
			break;
		case 4: // Left
			if (this.totalTranslateX > 0)
				x = 0;
			else if (this.totalTranslateX < -this.tileWidth - 1)
				// x = -this.tileWidth;
				break;
			else
				x = translation.x;
			break;
		default:
			break;
		}

		this.draggedTile.setX(this.draggedTile.getX() + x);
		this.draggedTile.setY(this.draggedTile.getY() + y);

		requestLayout();
	}

	private void moveTile(ImageView tile, int direction, Point tilePoint) {
		int deltaX = 0;
		int deltaY = 0;

		switch (direction) {
		case 1: // Up
			deltaY = -1;
			break;
		case 2: // Right
			deltaX = 1;
			break;
		case 3: // Down
			deltaY = 1;
			break;
		case 4: // Left
			deltaX = -1;
			break;
		default:
			break;
		}

		final float newX = (tilePoint.x + deltaX - 1) * this.tileWidth;
		final float newY = (tilePoint.y + deltaY - 1) * this.tileHeight;

		// Move tile to new coordinate
		tile.setX(newX);
		tile.setY(newY);

		if (direction != 0) {
			this.board.shouldMove(true, tilePoint);
			this.tileWasMoved();
		}
	}

	private void snapDraggedTile() {
		// Check if dragged distance is over 50% tileWidth or tileHeight =>
		// allow move tile
		if (this.draggedDirection == Direction.Up.val()) {
			if (this.totalTranslateY < -(this.tileHeight / 2)) {
				this.moveTile(this.draggedTile, this.draggedDirection,
						this.movingTilePoint);
			} else {
				this.moveTile(this.draggedTile, 0, this.movingTilePoint);
			}
		} else if (this.draggedDirection == Direction.Right.val()) {
			if (this.totalTranslateX > (this.tileWidth / 2)) {
				this.moveTile(this.draggedTile, this.draggedDirection,
						this.movingTilePoint);
			} else {
				this.moveTile(this.draggedTile, 0, this.movingTilePoint);
			}
		} else if (this.draggedDirection == Direction.Down.val()) {
			if (this.totalTranslateY > (this.tileHeight / 2)) {
				this.moveTile(this.draggedTile, this.draggedDirection,
						this.movingTilePoint);
			} else {
				this.moveTile(this.draggedTile, 0, this.movingTilePoint);
			}
		} else if (this.draggedDirection == Direction.Left.val()) {
			if (this.totalTranslateX < -(this.tileWidth / 2)) {
				this.moveTile(this.draggedTile, this.draggedDirection,
						this.movingTilePoint);
			} else {
				this.moveTile(this.draggedTile, 0, this.movingTilePoint);
			}
		}

		requestLayout();
	}

	private void resetAllMoveVariable() {
		this.lastMoveX = 0;
		this.lastMoveY = 0;

		this.totalTranslateX = 0;
		this.totalTranslateY = 0;

		this.draggedTile = null;
		this.draggedDirection = 0;

		this.movingTilePoint = null;
	}

	public interface TileBoardViewListener {
		void onFinish();
	}

}
