package de.wolfsline.zusi.anzeige.zusianzeige.manometer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Background {

	private Canvas canvas;
	private GraphicsContext gc;
	private String typeOfManometer;
	private boolean mBorder;
	
	private int mWidth, mHeight;
	private final int mStandartSizeManometer = 460;
	private final int mStandartSizeTacho = 460;
	private double mFactor = 0.0D;
	
	private double mMiddleX;
	private double mMiddleY;
	
	private Font mFont;
	
	public Background(String typeOfManometer, Canvas canvas, boolean border) {
		this.typeOfManometer = typeOfManometer;
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.mBorder = border;
		
		if (typeOfManometer.equalsIgnoreCase("hll") || typeOfManometer.equalsIgnoreCase("hbl") || typeOfManometer.equalsIgnoreCase("c")) {
			mFactor = canvas.getHeight() / mStandartSizeManometer;
        } else if (typeOfManometer.equalsIgnoreCase("tacho")) {
        	mFactor = canvas.getHeight() / mStandartSizeTacho;
        }
		
		System.out.println("Faktor: " + mFactor);
		
		mMiddleX = (canvas.getWidth()) / 2;
        mMiddleY = (canvas.getHeight()) / 2;
        
        mWidth = (int) canvas.getWidth() - 30;
        mHeight = (int) canvas.getHeight() - 30;
        
        //mFont = Font.font("sans-serif", FontWeight.BOLD, 35.0D);
        mFont = Font.font("", FontWeight.BOLD, 35.0D);
	}
	
	public void draw() {
		gc.save();
		if (typeOfManometer.equalsIgnoreCase("hll") || typeOfManometer.equalsIgnoreCase("hbl")) {
			drawBorder();
			drawBackground();
			drawTextBar();
			drawBigStepsHLL();
			drawSmallStepsHLL();
			drawStopPointHLL();
			drawTextHLL();
        } else if (typeOfManometer.equalsIgnoreCase("c")) {
        	drawBorder();
			drawBackground();
			drawTextBar();
			drawBigStepsC();
			drawSmallStepsC();
			drawStopPointC();
			drawTextC();
        } else if (typeOfManometer.equalsIgnoreCase("tacho")) {
        	//TODO
        	drawBorder();
			drawBackground();
			drawBigStepsTacho();
			drawSmallStepsTacho();
			drawTextTacho();
			drawTextTachoDescription();
        }
		gc.restore();
	}
	
	private void drawBorder() {
		if (mBorder) {
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		}
		
		gc.setFill(Color.DARKGRAY);
		gc.fillOval(0, 0, canvas.getWidth(), canvas.getHeight());
		
		gc.setFill(Color.GRAY);
		int border = 25;
		gc.fillOval(border/2, border/2, canvas.getWidth()-border, canvas.getHeight()-border);
	}
	
	private void drawBackground() {
		gc.translate(15, 15);
		gc.setFill(Color.BLACK);
        gc.fillOval(0, 0, mWidth, mHeight);
        gc.translate(-15, -15);
	}
	
	private void drawTextBar() {
		gc.setFont(mFont);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.WHITE);
		gc.fillText("bar", mMiddleX, mHeight - 40);
		
		gc.setFont(Font.font("", FontWeight.BOLD, 20.0D));
		gc.fillText("Cl. 1,0", mMiddleX + 80, mHeight - 40);
	}
	
	/*
	 * HLL - Hauptluftleitung / HBL - Hauptluftbeh�lterleitung
	 */
	
	private void drawStopPointHLL() {
		gc.translate(mMiddleX, mMiddleY);
		
		double winkel = ((22.5D * 6) +2.25D) / 180 * Math.PI;
        double x = Math.cos(winkel) * ((mWidth-140)/2);
        double y = Math.sin(winkel) * ((mWidth-140)/2);
        
        //Goldener Kreis
		gc.setFill(Color.rgb(174, 134, 61));
		int size = 12;
		gc.fillOval(x - size/2, y - size/2, size, size);
		
		//Schattierung innerer Kreis
		gc.setFill(Color.rgb(68, 24, 0));
		size = 8;
		gc.fillOval(x - size/2, y - size/2, size, size);
		
		//Innerer goldener Kreis
		gc.setFill(Color.rgb(174, 134, 61));
		size = 4;
		gc.fillOval(x - size/2, y - size/2, size, size);

		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	private void drawBigStepsHLL() {
		gc.translate(mMiddleX, mMiddleY);
		
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(7.0);
		for (int i = 6 ; i <= 18 ; i++) {
			double winkel = 22.5D * i / 180 * Math.PI;
			if (i == 6) {
				winkel = ((22.5D * i) + 4.5D) / 180 * Math.PI;
			}
            double startX = Math.cos(winkel) * ((mWidth-110)/2);
            double startY = Math.sin(winkel) * ((mWidth-110)/2);
            double stopX = Math.cos(winkel) * ((mWidth-30)/2);
            double stopY = Math.sin(winkel) * ((mWidth-30)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
        }
		
		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	private void drawSmallStepsHLL() {
		gc.translate(mMiddleX, mMiddleY);
	    
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(3.5);
		
        for (int i = 32 ; i <= 90 ; i++) {
        	if (i % 5 == 0) {
				continue;
			}
        	double winkel = 4.5D * i / 180 * Math.PI;
            double startX = Math.cos(winkel) * ((mWidth-80)/2);
            double startY = Math.sin(winkel) * ((mWidth-80)/2);
            double stopX = Math.cos(winkel) * ((mWidth-30)/2);
            double stopY = Math.sin(winkel) * ((mWidth-30)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
        }
		
		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	private void drawTextHLL() {
		gc.translate(mMiddleX, mMiddleY);
		
		Font font = new Font("", 35.0D);
		gc.setFont(font);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.WHITE);
		
		for (int i = 6 ; i <= 18 ; i++) {
			double winkel = 22.5D * i / 180 * Math.PI;
			if (i == 6) {
				winkel = ((22.5D * i) + 4.5D) / 180 * Math.PI;
			}
            double x = Math.cos(winkel) * ((mWidth-170)/2);
            double y = Math.sin(winkel) * ((mWidth-170)/2);
            gc.fillText(String.valueOf(i-6), x, y+5);
        }
		
		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	/*
	 * C-Druck - Bremszylinder
	 */
	
	private void drawStopPointC() {
		gc.translate(mMiddleX, mMiddleY);
		
		double winkel = ((132.5D + 27.5D * 0) + 3.0D) / 180 * Math.PI;
        double x = Math.cos(winkel) * ((mWidth-140)/2);
        double y = Math.sin(winkel) * ((mWidth-140)/2);
        
        //Goldener Kreis
		gc.setFill(Color.rgb(174, 134, 61));
		int size = 12;
		gc.fillOval(x - size/2, y - size/2, size, size);
		
		//Schattierung innerer Kreis
		gc.setFill(Color.rgb(68, 24, 0));
		size = 8;
		gc.fillOval(x - size/2, y - size/2, size, size);
		
		//Innerer goldener Kreis
		gc.setFill(Color.rgb(174, 134, 61));
		size = 4;
		gc.fillOval(x - size/2, y - size/2, size, size);

		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	private void drawBigStepsC() {
		gc.translate(mMiddleX, mMiddleY);
		
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(7.0);
		
		for (int i = 0 ; i <= 10 ; i++) {
			double winkel = (132.5D + 27.5D * i) / 180 * Math.PI;
			if (i == 0) {
				winkel = ((132.5D + 27.5D * i) + 5.5D) / 180 * Math.PI;
			}
			double startX = Math.cos(winkel) * ((mWidth-110)/2);
            double startY = Math.sin(winkel) * ((mWidth-110)/2);
            double stopX = Math.cos(winkel) * ((mWidth-30)/2);
            double stopY = Math.sin(winkel) * ((mWidth-30)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
		}
		
		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	private void drawSmallStepsC() {
		gc.translate(mMiddleX, mMiddleY);
	    
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(3.5);
		
        for (int i = 26 ; i <= 73 ; i++) {
        	if ((i - 4) % 5 == 0) {
        		continue;
        	}
        	double winkel = 5.5D * i / 180 * Math.PI;
            double startX = Math.cos(winkel) * ((mWidth-80)/2);
            double startY = Math.sin(winkel) * ((mWidth-80)/2);
            double stopX = Math.cos(winkel) * ((mWidth-30)/2);
            double stopY = Math.sin(winkel) * ((mWidth-30)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
        }
		
		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	private void drawTextC() {
		gc.translate(mMiddleX, mMiddleY);
		
		Font font = new Font("", 35.0D);
		gc.setFont(font);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.WHITE);
		
		for (int i = 0 ; i <= 10 ; i++) {
			if (i == 1 || i == 3 || i == 7 || i == 9) {
				continue;
			}
			double winkel = (132.5D + 27.5D * i) / 180 * Math.PI;
			if (i == 0) {
				winkel = ((132.5D + 27.5D * i) + 5.5D) / 180 * Math.PI;
			}
			double x = Math.cos(winkel) * ((mWidth-170)/2);
            double y = Math.sin(winkel) * ((mWidth-170)/2);

            gc.fillText(String.valueOf(i), x, y+5);
		}
		
		gc.translate(-mMiddleX, -mMiddleY);
	}

	/*
	 * Tacho - Geschwindigkeit
	 */
	
	private void drawBigStepsTacho() {
		gc.translate(mMiddleX, mMiddleY);
		
		gc.setStroke(Color.RED);
		gc.setLineWidth(7.0);
		for (int i = -1 ; i > -5 ; i--) {
			double winkel = (154.28D + 1.0D * i) / 180 * Math.PI;
			double startX = Math.cos(winkel) * ((mWidth-110)/2);
            double startY = Math.sin(winkel) * ((mWidth-110)/2);
            double stopX = Math.cos(winkel) * ((mWidth-30)/2);
            double stopY = Math.sin(winkel) * ((mWidth-30)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
		}
		
		
		
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(7.0);
		
		for (int i = 0 ; i <= 18 ; i++) {
			if (i == 0) {
				gc.setStroke(Color.WHITE);
			} else if (i % 4 == 0) {
				gc.setStroke(Color.YELLOW);
			} else {
				gc.setStroke(Color.WHITE);
			}
			double winkel = (154.28D + 12.85D * i) / 180 * Math.PI;
			double startX = Math.cos(winkel) * ((mWidth-110)/2);
            double startY = Math.sin(winkel) * ((mWidth-110)/2);
            double stopX = Math.cos(winkel) * ((mWidth-30)/2);
            double stopY = Math.sin(winkel) * ((mWidth-30)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
		}
		
		gc.translate(-mMiddleX, -mMiddleY);		
	}
	
	private void drawSmallStepsTacho() {
		gc.translate(mMiddleX, mMiddleY);		
		
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(3.5);
		
		for (int i = 0 ; i <= 17 ; i++) {
			double winkel = (160.7D + 12.85D * i) / 180 * Math.PI;
			double startX = Math.cos(winkel) * ((mWidth-90)/2);
            double startY = Math.sin(winkel) * ((mWidth-90)/2);
            double stopX = Math.cos(winkel) * ((mWidth-50)/2);
            double stopY = Math.sin(winkel) * ((mWidth-50)/2);
            gc.strokeLine(startX, startY, stopX, stopY);
		}
		
		gc.translate(-mMiddleX, -mMiddleY);		
	}
	
	private void drawTextTacho() {
		gc.translate(mMiddleX, mMiddleY);
		
		gc.setFont(mFont);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.WHITE);
		
		for (int i = 0 ; i <= 18 ; i+=2) {
			double winkel = (154.28D + 12.85D * i) / 180 * Math.PI;
            double x = Math.cos(winkel) * ((mWidth-190)/2);
            double y = Math.sin(winkel) * ((mWidth-190)/2);
            gc.fillText(String.valueOf(i*10), x, y+5);
        }
		
		gc.translate(-mMiddleX, -mMiddleY);
	}
	
	private void drawTextTachoDescription() {
		gc.setFont(mFont);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.YELLOW);
		//gc.fillText("bar", mMiddleX, mHeight - 40);
		
		int distance = 110;
		gc.setFont(Font.font("", FontWeight.BOLD, 30.0D));
		gc.fillText("V-ist", mMiddleX, mMiddleY + distance);
		gc.setFill(Color.RED);
		gc.fillText("V-soll", mMiddleX, mMiddleY + distance + 40);
	}
	
}
