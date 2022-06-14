package de.wolfsline.zusi.anzeige.zusianzeige.pzb;

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
		
		drawBorder();
		drawBackground();
		drawLever();
		gc.restore();
	}
	
	private void drawBorder() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		gc.setFill(Color.GRAY);
		int border = 36;
		gc.fillRect(border/2, border/2, canvas.getWidth()-border, canvas.getHeight()-border);
	}
	
	private void drawBackground() {
		gc.setFill(Color.BLACK);
		int border = 40;
		gc.fillRect(border/2, border/2, canvas.getWidth()-border, canvas.getHeight()-border);
	}
	
	private void drawLever() {		
		gc.setFill(Color.DARKGRAY);
		int border = 0;
		gc.fillRoundRect(156+border, 312+border, 146-border*2, 100-border*2, 10, 10);
		gc.setFill(Color.BLACK);
		border = 2;
		gc.fillRoundRect(156+border, 312+border, 146-border*2, 100-border*2, 10, 10);
		gc.setFill(Color.DARKGRAY);
		border = 20;
		gc.fillRoundRect(156+border, 312+border, 146-border*2, 100-border*2, 10, 10);
		gc.setFill(Color.BLACK);
		border = 22;
		gc.fillRoundRect(156+border, 312+border, 146-border*2, 100-border*2, 10, 10);
		
		//Zeichen links
		gc.translate(98, 362);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2.0D);
		gc.strokeOval(0-15, 0-15, 30, 30);
		gc.translate(-98, -362);
		
		//Zeichen rechts
		gc.translate(360, 362);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2.0D);
		gc.strokeOval(0-15, 0-15, 30, 30);
		
		//Striche au�en
		for (int i = 0 ; i < 12 ; i++) {
			double winkel = 30.0D * i / 180 * Math.PI;
            double startX = Math.cos(winkel) * ((50)/2);
            double startY = Math.sin(winkel) * ((50)/2);
            double stopX = Math.cos(winkel) * ((70)/2);
            double stopY = Math.sin(winkel) * ((70)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
        }
		
		gc.translate(-360, -362);
	}
	
}
