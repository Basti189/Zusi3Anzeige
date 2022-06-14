package de.wolfsline.zusi.anzeige.zusianzeige.manometer;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Pointer {

	//private String typeOfPointer;
	//private Canvas canvas;
	private GraphicsContext gc;
	
	private int mWidth, mHeight;
	private final int mStandartSizeManometer = 460;
	private final int mStandartSizeTacho = 460;
	private double mFactor = 0.0D;
	
	private double mMiddleX;
	private double mMiddleY;
	
	private Image mImageOfPointer;
	
	private double mMinSpeed = 0.0D;
	private double mMinPos = 0.0D;
	private double mRotate = 0.0D;
	
	public Pointer(String typeOfPointer, Canvas canvas) {
		//this.typeOfPointer = typeOfPointer;
		//this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		
		if (typeOfPointer.equalsIgnoreCase("hll") || typeOfPointer.equalsIgnoreCase("hbl") || typeOfPointer.equalsIgnoreCase("c")) {
			mFactor = canvas.getHeight() / mStandartSizeManometer;
        } else if (typeOfPointer.equalsIgnoreCase("tachoIst") || typeOfPointer.equalsIgnoreCase("tachoSoll")) {
        	mFactor = canvas.getHeight() / mStandartSizeTacho;
        }
		
		mMiddleX = (canvas.getWidth()) / 2;
        mMiddleY = (canvas.getHeight()) / 2;
        
        mWidth = (int) canvas.getWidth() - 30;
        mHeight = (int) canvas.getHeight() - 30;
        
        if (typeOfPointer.equalsIgnoreCase("hll")) {
        	mMinSpeed = 0.02D;
        	mMinPos = -130.5D;
        	mImageOfPointer = renderHLLPointer();
        } else if (typeOfPointer.equalsIgnoreCase("hbl")) {
        	mMinSpeed = 0.02D;
        	mMinPos = -130.5D;
        	mImageOfPointer = renderHBLPointer();
        } else if (typeOfPointer.equalsIgnoreCase("c")) {
        	mMinSpeed = 0.02D;
        	mMinPos = -132.0D;
        	mImageOfPointer = renderCPointer();
        } else if (typeOfPointer.equalsIgnoreCase("tachoIst")) {
        	mMinSpeed = 0.02D;
        	mMinPos = -132.0D;
        	mImageOfPointer = renderTachoIstPointer();
        } else if (typeOfPointer.equalsIgnoreCase("tachoSoll")) {
        	mMinSpeed = 0.02D;
        	mMinPos = -115.65D;
        	mImageOfPointer = renderTachoSollPointer();
        }
        set(mMinPos);
        mRotate = mMinPos;
	}
	
	public void draw(double rotateTo) {
		if (rotateTo < mMinPos) {
			rotateTo = mMinPos;
		}
		mRotate = round(mRotate, 2);
		rotateTo = round(rotateTo, 2);
		if (mRotate < rotateTo) {
			mRotate = mRotate + getStep(mRotate, rotateTo);
		} else if (mRotate > rotateTo) {
			mRotate = mRotate - getStep(mRotate, rotateTo);
		}
		set(mRotate);
	}
	
	public void set(double rotateTo) {
		gc.save();
		gc.clearRect(0, 0, mWidth, mHeight);
		gc.translate(mMiddleX, mMiddleY);
		gc.rotate(rotateTo);
		gc.translate(-mMiddleX, -mMiddleY);        
		
		gc.drawImage(mImageOfPointer, mMiddleX - mImageOfPointer.getWidth() / 2, mMiddleY - mImageOfPointer.getHeight() / 2);

        gc.restore();
	}
	
	private WritableImage renderHLLPointer() {
		gc.save();

		//Spitze zeichnen
		gc.setLineWidth(4.0D);
		gc.setFill(Color.rgb(189, 53, 29));
		gc.beginPath();
		gc.moveTo(mMiddleX-10, mMiddleY);
		gc.lineTo(mMiddleX-0.5D, 40);
		gc.lineTo(mMiddleX+0.5D, 40);
		gc.lineTo(mMiddleX+10, mMiddleY);
		gc.lineTo(mMiddleX-10, mMiddleY);
		gc.closePath();
		gc.fill();
		
		//Schattierung zeichnen
		gc.setStroke(Color.GREEN);
		//gc.setLineWidth(0.25D);
		gc.setLineWidth(0.25D);
		gc.beginPath();
		gc.moveTo(mMiddleX-10.5, mMiddleY);
		gc.lineTo(mMiddleX-1.0D, 41);
		gc.lineTo(mMiddleX+1.0D, 41);
		gc.lineTo(mMiddleX+10.5, mMiddleY);
		gc.lineTo(mMiddleX-10.5, mMiddleY);
		gc.closePath();
		gc.stroke();
		
		//Gewicht zeichnen
		gc.setFill(Color.rgb(189, 53, 29));
		gc.setLineWidth(4.0D);
		gc.beginPath();
		gc.moveTo(mMiddleX, mMiddleY);
		gc.lineTo(mMiddleX-30, mMiddleY+76);
		gc.lineTo(mMiddleX, mMiddleY+88);
		gc.lineTo(mMiddleX+30, mMiddleY+76);
		gc.lineTo(mMiddleX, mMiddleY);
		gc.closePath();
		gc.fill();
		
		//Korpus zeichnen
		gc.setFill(Color.rgb(189, 53, 29));
		double size = 54.0D;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Schattierung goldener Kreis
		gc.setFill(Color.rgb(130, 112, 41));
		size = 40.0D;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Goldener Kreis
		gc.setFill(Color.rgb(174, 134, 61));
		size = 38.0D;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Schattierung innerer Kreis
		gc.setFill(Color.rgb(68, 24, 0));
		size = 10.0D;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Innerer goldener Kreis
		gc.setFill(Color.rgb(174, 134, 61));
		size = 6.0D;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		gc.restore();
		
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image = gc.getCanvas().snapshot(parameters, null);
		return image;
	}
	
	private WritableImage renderHBLPointer() {
		gc.save();
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		
		//Spitze zeichnen
		gc.setLineWidth(4.0D);
		gc.beginPath();
		gc.moveTo(mMiddleX-10, mMiddleY);
		gc.lineTo(mMiddleX-0.5D, 40);
		gc.lineTo(mMiddleX+0.5D, 40);
		gc.lineTo(mMiddleX+10, mMiddleY);
		gc.lineTo(mMiddleX-10, mMiddleY);
		gc.closePath();
		gc.fill();
		
		//Schattierung zeichnen
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(0.25D);
		gc.beginPath();
		gc.moveTo(mMiddleX-10.5, mMiddleY);
		gc.lineTo(mMiddleX-1D, 41);
		gc.lineTo(mMiddleX+1D, 41);
		gc.lineTo(mMiddleX+10.5, mMiddleY);
		gc.lineTo(mMiddleX-10.5, mMiddleY);
		gc.closePath();
		gc.stroke();
		
		//Gewicht zeichnen
		gc.setFill(Color.WHITE);
		gc.setLineWidth(4.0D);
		gc.beginPath();
		gc.moveTo(mMiddleX, mMiddleY);
		gc.lineTo(mMiddleX-30, mMiddleY+76);
		gc.lineTo(mMiddleX, mMiddleY+88);
		gc.lineTo(mMiddleX+30, mMiddleY+76);
		gc.lineTo(mMiddleX, mMiddleY);
		gc.closePath();
		gc.fill();
		
		//Korpus zeichnen
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		int size = 52;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		gc.restore();
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image = gc.getCanvas().snapshot(parameters, null);
		return image;
	}
	
	private WritableImage renderCPointer() {
		gc.save();
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		
		//Spitze zeichnen
		gc.setLineWidth(4.0D);
		gc.beginPath();
		gc.moveTo(mMiddleX-10, mMiddleY);
		gc.lineTo(mMiddleX-0.5D, 40);
		gc.lineTo(mMiddleX+0.5D, 40);
		gc.lineTo(mMiddleX+10, mMiddleY);
		gc.lineTo(mMiddleX-10, mMiddleY);
		gc.closePath();
		gc.fill();
		
		//Gewicht zeichnen
		gc.setFill(Color.WHITE);
		gc.setLineWidth(4.0D);
		gc.beginPath();
		gc.moveTo(mMiddleX, mMiddleY);
		gc.lineTo(mMiddleX-30, mMiddleY+76);
		gc.lineTo(mMiddleX, mMiddleY+88);
		gc.lineTo(mMiddleX+30, mMiddleY+76);
		gc.lineTo(mMiddleX, mMiddleY);
		gc.closePath();
		gc.fill();
		
		//Schattierung zeichnen
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(0.25D);
		gc.beginPath();
		gc.moveTo(mMiddleX-10.5, mMiddleY);
		gc.lineTo(mMiddleX-1D, 41);
		gc.lineTo(mMiddleX+1D, 41);
		gc.lineTo(mMiddleX+10.5, mMiddleY);
		gc.lineTo(mMiddleX-10.5, mMiddleY);
		gc.closePath();
		gc.stroke();
		
		//Korpus zeichnen
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		int size = 52;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Schattierung wei�er  Kreis
		gc.setFill(Color.GRAY);
		size = 40;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Goldener Kreis
		gc.setFill(Color.WHITE);
		size = 38;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Schattierung innerer Kreis
		gc.setFill(Color.rgb(68, 24, 0));
		size = 10;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		//Innerer goldener Kreis
		gc.setFill(Color.rgb(174, 134, 61));
		size = 6;
		gc.fillOval(mMiddleX - size/2, mMiddleY - size/2, size, size);
		
		gc.restore();
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image = gc.getCanvas().snapshot(parameters, null);
		return image;
	}
	
	private WritableImage renderTachoIstPointer() {
		gc.save();
		//TODO
		gc.restore();
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image = gc.getCanvas().snapshot(parameters, null);
		return image;
	}
	
	private WritableImage renderTachoSollPointer() {
		gc.save();
		//TODO
		gc.setFill(Color.YELLOW);
		
		//Spitze zeichnen
		gc.setLineWidth(4.0D);
		gc.beginPath();
		gc.moveTo(mMiddleX-10, mMiddleY);
		gc.lineTo(mMiddleX-0.5D, 40);
		gc.lineTo(mMiddleX+0.5D, 40);
		gc.lineTo(mMiddleX+10, mMiddleY);
		gc.lineTo(mMiddleX-10, mMiddleY);
		gc.closePath();
		gc.fill();
		gc.restore();
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image = gc.getCanvas().snapshot(parameters, null);
		return image;
	}
	
	public double getRotate() {
		return mRotate;
	}
	
	private double getStep(double from, double to) {
		double diff = 0.0D;
		if (to >= from) {
			diff = round(to - from, 2);
		} else {
			diff = round(from - to, 2);
		}
		if (diff * mMinSpeed < mMinSpeed) {
			return mMinSpeed;
		}
		return diff * mMinSpeed;
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
}
