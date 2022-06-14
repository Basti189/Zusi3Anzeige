package de.wolfsline.zusi.anzeige.zusianzeige.tasten;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Taste {

	private Canvas canvas;
	private GraphicsContext gc;
	
	private int mWidth, mHeight;
	private final int mStandartSize = 460;
	private double mFactor = 0.0D;
	
	private int mPos;
	private int mNumberOfTasten = 3;
	
	private double mStartPos;
	
	private boolean isRound;
	
	private String mText;
	
	private Font mFont;
	
	public Taste(Canvas canvas, int pos, boolean isRound, String text) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.isRound = isRound;
		this.mText = text;
		
		this.mPos = pos-1;
		
		System.out.println(canvas.getHeight());
        
        mWidth = (int) canvas.getWidth();
        mHeight = (int) canvas.getHeight();
        
        mStartPos = mWidth/mNumberOfTasten * mPos;
        
        //mFont = Font.font("sans-serif", FontWeight.BOLD, 35.0D);
        mFont = Font.font("", FontWeight.BOLD, 25.0D);
        
        center();
	}
	
	private void background() {
		
		gc.setFill(Color.DARKGRAY);
		int border = 10;
		gc.fillRect(mStartPos + border, border, mWidth/3-border*2, mHeight-border*2);
		
		gc.setFill(Color.BLACK);
		border = 30;
		gc.fillRect(mStartPos + border, border, mWidth/3-border*2, mHeight-border*2-100);
		
		gc.setFont(mFont);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(mText, mStartPos + border + (mWidth/3-border*2)/2, mHeight-border*2);
		
		
	}
	
	public void down() {
		gc.save();
		background();
		
		gc.setFill(Color.GRAY);
		int border = 45;
		if (isRound) {
			gc.fillOval(mStartPos + border, mHeight-border*2-115, mWidth/3-border*2, mWidth/3-border*2);
		} else {
			gc.fillRect(mStartPos + border, mHeight-border*2-115, mWidth/3-border*2, mWidth/3-border*2);
		}

		gc.restore();
	}
	
	public void center() {
		gc.save();
		background();
		
		int border = 45;
		
		gc.setFill(Color.GRAY);
		if(isRound) {
			gc.fillOval(mStartPos + border, mHeight-border*2-215, mWidth/3-border*2, mWidth/3-border*2);
		} else {
			gc.fillRect(mStartPos + border, mHeight-border*2-215, mWidth/3-border*2, mWidth/3-border*2);
		}
		
		
		gc.restore();
	}
	
	public void up() {
		gc.save();
		background();
		
		gc.setFill(Color.GRAY);
		int border = 45;
		if(isRound) {
			gc.fillOval(mStartPos + border, border, mWidth/3-border*2, mWidth/3-border*2);
		} else {
			gc.fillRect(mStartPos + border, border, mWidth/3-border*2, mWidth/3-border*2);
		}
		
		
		gc.restore();
	}
	
	public Canvas getCanvas() {
		return this.canvas;
	}
	
}
