package de.wolfsline.zusi.anzeige.zusianzeige.tasten;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Background {

	private Canvas canvas;
	private GraphicsContext gc;
	
	private int mWidth, mHeight;
	private final int mStandartSize = 460;
	private double mFactor = 0.0D;
	
	private double mMiddleX;
	private double mMiddleY;
	
	private Font mFont;
	
	public Background() {
		
	}

	public Background(Canvas canvasBackground) {
		this.canvas = canvasBackground;
		this.gc = canvas.getGraphicsContext2D();
		
		System.out.println(canvas.getHeight());
		
		mMiddleX = (canvas.getWidth()) / 2;
        mMiddleY = (canvas.getHeight()) / 2;
        
        mWidth = (int) canvas.getWidth() - 30;
        mHeight = (int) canvas.getHeight() - 30;
        
        //mFont = Font.font("sans-serif", FontWeight.BOLD, 35.0D);
        mFont = Font.font("", FontWeight.BOLD, 35.0D);
	}
	
	public void draw() {
		gc.save();
		
		drawBackground();
		
		gc.restore();
	}
	
	private void drawBackground() {
		gc.setFill(Color.BLACK);
		int border = 0;
		gc.fillRect(border/2, border/2, canvas.getWidth()-border, canvas.getHeight()-border);
	}
	
}
