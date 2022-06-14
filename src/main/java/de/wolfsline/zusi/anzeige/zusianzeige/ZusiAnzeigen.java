package de.wolfsline.zusi.anzeige.zusianzeige;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.List;

import de.wolfsline.zusi.anzeige.zusianzeige.manometer.Manometer;
import de.wolfsline.zusi.anzeige.zusianzeige.pzb.PZB;
import de.wolfsline.zusi.anzeige.zusianzeige.tasten.Tasten;
import javafx.application.Application;
import javafx.stage.Stage;

public class ZusiAnzeigen extends Application {

	public ZusiAnzeigen() {

	}
    
	@Override
	public void start(Stage primaryStage) {
		Parameters params = getParameters();
		List<String> listParams = params.getRaw();
		String ip = "127.0.0.1";
		int port = 1436;
		int size = 460;
		int sizeMax = -1;
		boolean border = false;
		boolean onTop = false;
		String type = "hll";
		for (String param :listParams) {
			if(param.startsWith("-ip:")) {
				//ip = ip.replace("-ip:", "");
				ip = param.replace("-ip:", "");
				System.out.println("IP: " + ip);
			} else if (param.startsWith("-port:")) {
				try {
					port = Integer.valueOf(param.replace("-port:", ""));
				} catch (Exception e) {
					
				}
			} else if (param.equals("-border")) {
				border = true;
			} else if (param.equals("-ontop")) {
				onTop = true;
			} else if (param.startsWith("-size:")) {
				try {
					size = Integer.valueOf(param.replace("-size:", ""));
					GraphicsDevice gda[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
					for (GraphicsDevice gDevice : gda) {
						if (gDevice == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()) {
							int height = gDevice.getDefaultConfiguration().getBounds().height;
							int width = gDevice.getDefaultConfiguration().getBounds().width;
							if (height < width) {
								sizeMax = height;
							} else {
								sizeMax = width;
							}
						}
					}
					/*if (size < 460) {
						size = 460;
					} else if (size > sizeMax) {
						size = sizeMax;
					}*/
				} catch (Exception e) {
					
				}
			} else if(param.startsWith("-type:")) {
				type = param.replace("-type:", "");
			}
		}
		if (type.equalsIgnoreCase(Manometer.TYPE_HLL) || type.equalsIgnoreCase(Manometer.TYPE_HBL) || type.equalsIgnoreCase(Manometer.TYPE_C) || type.equalsIgnoreCase(Manometer.TYPE_TACHO)) {
			new Manometer(type, size, border, onTop, ip, port);
		} else if (type.equalsIgnoreCase(PZB.TYPE_PZB)) {
			new PZB(size, border, onTop, ip, port);
		} else if (type.equalsIgnoreCase(Tasten.TYPE_PZB)) {
			new Tasten(size, border, onTop, ip, port);
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
		//Application.launch(ZusiAnzeigen.class, args);
	}
	
}
