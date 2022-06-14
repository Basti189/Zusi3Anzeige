package de.wolfsline.zusi.anzeige.zusianzeige.pzb;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Leuchtmelder {

	private GraphicsContext gc;
	
	private int mRow;
	private int mColumn;
	
	private Color mBlueDark = Color.rgb(0, 0, 102);
	private Color mBlueBright = Color.rgb(51, 153, 255);
	
	private Color mYellowDark = Color.rgb(153, 102, 0);
	private Color mYellowBright = Color.rgb(255, 255, 0);
	
	private Color mRedDark = Color.rgb(153, 0, 0);
	private Color mRedBright = Color.rgb(255, 51, 51);
	
	private Color mWhiteDark = Color.rgb(102, 102, 102);
	private Color mWhiteBright = Color.rgb(255, 255, 255);
	
	private Color mDark;
	private Color mBright;
	
	private Color mBackground = Color.rgb(51, 51, 51);
	
	private double mSize = 116.0D;
	private double mDistanceBorder = 40.0D;
	private double mDistanceLM = 15.0D;
	
	private Font mFont;
	private String mText;
	private Color mTextColorDark;
	private Color mTextColorBright;
	private int mPos = 0;
	
	private boolean mIsDark = true;
	
	public Leuchtmelder(Canvas canvas, int row, int column) {
		this.gc = canvas.getGraphicsContext2D();
		this.mRow = row;
		this.mColumn = column;
		mFont = Font.font("", FontWeight.NORMAL, 50.0D);
		if (row == 0) {
			mDark = mBlueDark;
			mBright = mBlueBright;
			mPos = 15;
			if (column == 0) {
				mText = "55";
				mTextColorDark = Color.rgb(128, 128, 128);
				mTextColorBright = Color.rgb(255, 255, 255);
			} else if (column == 1) {
				mText = "70";
				mTextColorDark = Color.rgb(128, 128, 128);
				mTextColorBright = Color.rgb(255, 255, 255);
			} else if (column == 2) {
				mText = "85";
				mTextColorDark = Color.rgb(128, 128, 128);
				mTextColorBright = Color.rgb(255, 255, 255);
			}
		} else {
			mFont = Font.font("", FontWeight.NORMAL, 25.0D);
			mPos = -5;
			if (column == 0) {
				mDark = mWhiteDark;
				mBright = mWhiteBright;
				mText = "Befehl\n40";
				mTextColorDark = Color.rgb(0, 0, 0);
				mTextColorBright = Color.rgb(0, 0, 0);
			} else if (column == 1) {
				mDark = mRedDark;
				mBright = mRedBright;
				mText = "500\nHz";
				mTextColorDark = Color.rgb(0, 0, 0);
				mTextColorBright = Color.rgb(0, 0, 0);
			} else if (column == 2) {
				mDark = mYellowDark;
				mBright = mYellowBright;
				mText = "1000\nHz";
				mTextColorDark = Color.rgb(0, 0, 0);
				mTextColorBright = Color.rgb(0, 0, 0);
			}
		}
		dark();
	}
	
	private void background() {
		gc.setFill(mBackground);
		int border = 8;
		gc.fillRoundRect(mDistanceBorder + (mSize*mColumn + mDistanceLM * mColumn) - border/2, mDistanceBorder + (mSize*mRow + mDistanceLM * mRow) - border/2, mSize + border, mSize + border, 15, 15);
	}
	
	private void text(Color color) {
		gc.setFont(mFont);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(color);
		gc.fillText(mText, mDistanceBorder + (mSize*mColumn + mDistanceLM * mColumn) + mSize/2, mDistanceBorder + (mSize*mRow + mDistanceLM * mRow) + mSize/2 + mPos);
	}
	
	public void bright() {
		gc.save();
		background();
		gc.setFill(mBright);
		//gc.fillRect(mDistanceBorder + (mSize*mColumn + mDistanceLM * mColumn), mDistanceBorder + (mSize*mRow + mDistanceLM * mRow), mSize, mSize);
	    gc.fillRoundRect(mDistanceBorder + (mSize*mColumn + mDistanceLM * mColumn), mDistanceBorder + (mSize*mRow + mDistanceLM * mRow), mSize, mSize, 15, 15);
	    text(mTextColorBright);
		gc.restore();
		mIsDark = false;
	}
	
	public void dark() {
		gc.save();
		background();
		gc.setFill(mDark);
		gc.fillRoundRect(mDistanceBorder + (mSize*mColumn + mDistanceLM * mColumn), mDistanceBorder + (mSize*mRow + mDistanceLM * mRow), mSize, mSize, 15, 15);
		text(mTextColorDark);
		gc.restore();
		mIsDark = true;
	}
	
	public void toogle() {
		if (mIsDark) {
			bright();
		} else {
			dark();
		}
	}
	
	public void set(boolean state) {
		if (state) {
			bright();
		} else {
			dark();
		}
	}
	
}
