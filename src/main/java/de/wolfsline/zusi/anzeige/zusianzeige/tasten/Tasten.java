package de.wolfsline.zusi.anzeige.zusianzeige.tasten;

import java.util.ArrayList;
import java.util.List;

import de.wolf.Zusi3Schnittstelle.Interfaces.ZusiData;
import de.wolf.Zusi3Schnittstelle.Interfaces.ZusiEvent;
import de.wolf.Zusi3Schnittstelle.Zusi3Schnittstelle;
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

public class Tasten {

	public static String TYPE_PZB = "tasten_pzb";
	
	private double xOffset = 0;
    private double yOffset = 0;
    
    private Pane mRoot;
    private Stage mStage = new Stage();
    
    private AnimationTimer mTimer;
    
    private int mWidth = 460, mHeight = 460;
    
    private Zusi3Schnittstelle zusi;
    
    List<Taste> listTaste = new ArrayList<Taste>();
	
	public Tasten(int size, boolean border, boolean onTop, String ip, int port) {
		this.mWidth = size;
    	this.mHeight = size;
    	
    	mRoot = new Pane();
    	
    	double position[] = Config.load(TYPE_PZB);
    	mStage.setX(position[0]);
    	mStage.setY(position[1]);
    	
    	zusi = new Zusi3Schnittstelle(ip, port, "PZB " + " by Sebastian Wolf");
    	zusi.register(this);
    	
    	Canvas canvasBackground = new Canvas(mWidth, mHeight);
    	mRoot.getChildren().add(canvasBackground);
        Background background = new Background(canvasBackground);
        background.draw();
        
		Taste taste = new Taste(new Canvas(mWidth, mHeight), 1, false, "Befehl");
		mRoot.getChildren().add(taste.getCanvas());
		listTaste.add(taste);
		
		taste = new Taste(new Canvas(mWidth, mHeight), 2, false, "Frei");
		mRoot.getChildren().add(taste.getCanvas());
		listTaste.add(taste);
		
		taste = new Taste(new Canvas(mWidth, mHeight), 3, true, "Wachsam");
		mRoot.getChildren().add(taste.getCanvas());
		listTaste.add(taste);
       
    	
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
            		Config.save(TYPE_PZB, mStage.getX(), mStage.getY());
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
    	//mTimer.stop();
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
	
	@ZusiData(0x50)
	public void wachsam(boolean is_pressed) {
		if (is_pressed) {
			System.out.println("gedrückt");
			listTaste.get(2).down();
		} else {
			System.out.println("nicht gedrückt");
			listTaste.get(2).center();
		}
	}
	
	@ZusiData(0x51)
	public void frei(boolean is_pressed) {
		if (is_pressed) {
			System.out.println("gedr�ckt");
			listTaste.get(1).down();
		} else {
			System.out.println("nicht gedr�ckt");
			listTaste.get(1).center();
		}
	}
	
	@ZusiData(0x52)
	public void befehl(boolean is_pressed) {
		if (is_pressed) {
			System.out.println("gedr�ckt");
			listTaste.get(0).down();
		} else {
			System.out.println("nicht gedr�ckt");
			listTaste.get(0).center();
		}
	}

	
	
}
