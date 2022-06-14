package de.wolfsline.zusi.anzeige.zusianzeige.pzb;

import java.util.ArrayList;
import java.util.List;

import de.wolf.Zusi3Schnittstelle.Zusi3Schnittstelle;
import de.wolf.Zusi3Schnittstelle.Interfaces.ZusiData;
import de.wolf.Zusi3Schnittstelle.Interfaces.ZusiEvent;
import de.wolf.Zusi3Schnittstelle.Values.FstAnz;
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

public class PZB {

	public static String TYPE_PZB = "pzb";
	
	private double xOffset = 0;
    private double yOffset = 0;
    
    private Pane mRoot;
    private Stage mStage = new Stage();
    
    private AnimationTimer mTimer;
    
    private int mWidth = 460, mHeight = 460;
    
    private Zusi3Schnittstelle zusi;
    
    private List<Leuchtmelder> listLM = new ArrayList<Leuchtmelder>();
    
    private long lastUpdate = 0L;
	
	public PZB(int size, boolean border, boolean onTop, String ip, int port) {
		this.mWidth = size;
    	this.mHeight = size;
    	
    	mRoot = new Pane();
    	
    	double position[] = Config.load(TYPE_PZB);
    	mStage.setX(position[0]);
    	mStage.setY(position[1]);
    	
    	zusi = new Zusi3Schnittstelle(ip, port, "PZB " + " by Sebastian Wolf");
    	zusi.register(this);
    	zusi.reqFstAnz(FstAnz.Status_Zugbeeinflussung);
    	
    	Canvas canvasBackground = new Canvas(mWidth, mHeight);
    	mRoot.getChildren().add(canvasBackground);
        Background background = new Background(canvasBackground);
        background.draw();
        
        for (int row = 0 ; row < 2 ; row++) {
			for (int column = 0; column < 3 ; column++) {
				Canvas canvas = new Canvas(mWidth, mHeight);
				mRoot.getChildren().add(canvas);
				Leuchtmelder lm = new Leuchtmelder(canvas, row, column);
				listLM.add(lm);
			}
		}
    	
    	 mTimer = new AnimationTimer() {
        	 public void handle(long currentNanoTime) {
        		if (lastUpdate + 500 < System.currentTimeMillis()) {
        			int count = 0 + (int)(Math.random() * 6);
            		listLM.get(count).toogle();
        			
        			/*listLM.get(4).toogle();
        			listLM.get(5).toogle();*/
            		lastUpdate = System.currentTimeMillis();
        		}
        	 }
        };
        //mTimer.start();
    	
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
	
	@ZusiData(FstAnz.Status_Zugbeeinflussung)
	public void pzb(int lm, boolean state) {

		switch (lm) {
		case 5: listLM.get(5).set(state);
			break;
		case 6: listLM.get(0).set(state);
			break;
		case 7: listLM.get(1).set(state);
			break;
		case 8: listLM.get(2).set(state);
			break;
		case 10: listLM.get(4).set(state);
			break;
		case 11: listLM.get(3).set(state);
			break;
		default:
			break;
		}
	}
}
