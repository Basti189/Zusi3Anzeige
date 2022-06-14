package de.wolfsline.zusi.anzeige.zusianzeige.manometer;

import de.wolf.Zusi3Schnittstelle.Zusi3Schnittstelle;
import de.wolf.Zusi3Schnittstelle.Interfaces.ZusiData;
import de.wolf.Zusi3Schnittstelle.Interfaces.ZusiEvent;
import de.wolf.Zusi3Schnittstelle.Values.FstAnz;
import de.wolf.Zusi3Schnittstelle.Values.Fuehrerstandsanzeigen;
import de.wolfsline.zusi.anzeige.zusianzeige.menu.Config;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Manometer {
	
	public static String TYPE_HLL = "hll";
	public static String TYPE_HBL = "hbl";
	public static String TYPE_C = "c";
	public static String TYPE_TACHO = "tacho";

	private double xOffset = 0;
    private double yOffset = 0;
    
    private Pane mRoot;
    private Stage mStage = new Stage();
    	
    private String mTypeOfManometer;
    
    private AnimationTimer mTimer;
    
    private int mWidth = 460, mHeight = 460;
	
    private double rotateToHLL = -135.0D;
    private double rotateToHBL = -135.0D;
    private double rotateToC = -135.0D;
    private double rotateToIst = -135.0D;

    private Pointer pointerHLL;
    private Pointer pointerHBL;
    private Pointer pointerC;
    private Pointer pointerTachoIst;
    private Pointer pointerTachoSoll;
    
    private Zusi3Schnittstelle zusi;
    
    public Manometer(String typeOfManometer, int size, boolean border, boolean onTop, String ip, int port) {
    	this.mWidth = size;
    	this.mHeight = size;
    	this.mTypeOfManometer = typeOfManometer;
    	mRoot = new Pane();
    	String name;
    	if (typeOfManometer.equalsIgnoreCase("hll") || typeOfManometer.equalsIgnoreCase("hbl")) {
    		name = "HLL/HBL";
    	} else if (typeOfManometer.equalsIgnoreCase("c")) {
    		name = "C-Druck";
    	} else if (typeOfManometer.equalsIgnoreCase("tacho")) {
    		name = "Tacho";
    		//mHeight = 600;
    		//mWidth = 600;
    	} else {
    		throw new IllegalArgumentException("Wrong type of manometer");
    	}
    	
    	double position[] = Config.load(typeOfManometer);
    	mStage.setX(position[0]);
    	mStage.setY(position[1]);
    	
    	zusi = new Zusi3Schnittstelle(ip, port, "Manometer " + name + " by Sebastian Wolf");
    	zusi.register(this);
    	
    	Canvas canvasBackground = new Canvas(mWidth, mHeight);
    	mRoot.getChildren().add(canvasBackground);
        Background background = new Background(typeOfManometer, canvasBackground, border);
        background.draw();
        
        if (typeOfManometer.equalsIgnoreCase("hll") || typeOfManometer.equalsIgnoreCase("hbl")) {
        	Canvas canvasZeigerHBL = new Canvas(mWidth, mHeight);
        	Canvas canvasZeigerHLL = new Canvas(mWidth, mHeight);
        	mRoot.getChildren().add(canvasZeigerHBL);
        	mRoot.getChildren().add(canvasZeigerHLL);
        	pointerHLL = new Pointer("hll", canvasZeigerHLL);
            pointerHBL = new Pointer("hbl", canvasZeigerHBL);
            zusi.reqFstAnz(FstAnz.Druck_Hauptluftleitung);
        	zusi.reqFstAnz(FstAnz.Druck_Hauptluftbehaelter);
        } else if (typeOfManometer.equalsIgnoreCase("c")) {
        	Canvas canvasZeigerC = new Canvas(mWidth, mHeight);
        	mRoot.getChildren().add(canvasZeigerC);
        	pointerC = new Pointer(typeOfManometer, canvasZeigerC);
        	zusi.requestFuehrerstandsanzeigen(FstAnz.Druck_Bremszylinder);
        } else if (typeOfManometer.equalsIgnoreCase("tacho")) {
        	Canvas canvasZeigerTachoIst = new Canvas(mWidth, mHeight);
        	Canvas canvasZeigerTachoSoll = new Canvas(mWidth, mHeight);
        	mRoot.getChildren().add(canvasZeigerTachoIst);
        	mRoot.getChildren().add(canvasZeigerTachoSoll);
        	pointerTachoIst = new Pointer("tachoIst", canvasZeigerTachoIst);
        	pointerTachoSoll = new Pointer("tachoSoll", canvasZeigerTachoSoll);
        	zusi.reqFstAnz(FstAnz.Geschwindigkeit);
        	zusi.reqFstAnz(FstAnz.AFB_Sollgeschwindigkeit);
        }
        
        
        Scene scene = new Scene(mRoot, mWidth, mHeight);
        if (!border) {
        	scene.setFill(Color.TRANSPARENT);
            mStage.initStyle(StageStyle.TRANSPARENT);
        }
        if (onTop) {
    		mStage.setAlwaysOnTop(true);
    	}
        mStage.setScene(scene);
        
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			if (event.getButton() == MouseButton.PRIMARY) {
    				xOffset = mStage.getX() - event.getScreenX();
    			    yOffset = mStage.getY() - event.getScreenY();
    			}	
    		}
    	});
    	
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if (event.getButton() == MouseButton.PRIMARY) {
            		mStage.setX(event.getScreenX() + xOffset);
            		mStage.setY(event.getScreenY() + yOffset);
            		Config.save(mTypeOfManometer, mStage.getX(), mStage.getY());
    			}
            }
        });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent event) {
    			if (event.getCode() == KeyCode.ESCAPE) {
    				close();
    			}
    		}
    	});
        
        mTimer = new AnimationTimer() {
        	 public void handle(long currentNanoTime) {
        		 if (typeOfManometer.equalsIgnoreCase("hll") || typeOfManometer.equalsIgnoreCase("hbl")) {
        			 pointerHLL.draw(rotateToHLL);
        			 pointerHBL.draw(rotateToHBL);
        		 } else if (typeOfManometer.equalsIgnoreCase("c")) {
        			 pointerC.draw(rotateToC);
        		 } else if (typeOfManometer.equalsIgnoreCase("tacho")) {
        			 pointerTachoSoll.draw(rotateToIst);
        		 }
        	 }
        };
        mTimer.start();
        
        mStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				System.out.println("Beendet");
				close();
				
			}
		});
        
        mStage.show();
        zusi.connect();
    }
    
    public void close() {
    	System.out.println("Stoppe");
    	mTimer.stop();
    	zusi.close();
		mStage.close();
    }
    
    public Pane getLayout() {
    	return mRoot;
    }

	@ZusiEvent(0x00)
	public void onConnectionChanged(int status, int count) {
		if (status == 0) {
			System.out.println("Verbindung getrennt");
		} else if (status == 1) {
			System.out.println("Verbunden");
		} else if (status == 2) {
			System.out.println("Verbindung zum Server verloren");
		} else if (status == 3) {
			System.out.println("Verbindgsversuch " + count);
		}
	}
	
	@ZusiEvent(0x10)
	public void onConnectionCreated(String version, String verbindungsinfo, boolean client_aktzeptiert) {
		System.out.println("Zusi-Version: " + version);
		System.out.println("Zusi-Verbindungsinfo: " + verbindungsinfo);
		System.out.println("Client ok? " + client_aktzeptiert);
	}
	
	@ZusiData(FstAnz.Druck_Hauptluftleitung)
	public void druckHLL(double bar) {
		rotateToHLL = (bar * 22.5D)-135.0D;
	}
	
	@ZusiData(FstAnz.Druck_Hauptluftbehaelter)
	public void druckHBL(double bar) {
		rotateToHBL = (bar * 22.5D)-135.0D;
	}
	
	@ZusiData(FstAnz.Druck_Bremszylinder)
	public void druckC(double bar) {
		rotateToC = (bar * 27.5D)-137.5D;
	}
	
	@ZusiData(Fuehrerstandsanzeigen.Geschwindigkeit)
	public void geschwindigkeit(int geschwindigkeit) {
		rotateToIst = (geschwindigkeit * 12.85D)/10-115.65D;
		System.out.println("speed");
	}
	
	@ZusiData(FstAnz.AFB_Sollgeschwindigkeit)
	public void geschwindigkeitAFB(float speedAFB) {
		//TODO
	}
	
}
