package de.wolfsline.zusi.anzeige.zusianzeige.menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class Util {

	public static void saveSnapshot(GraphicsContext gc, String filename) {
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage image = gc.getCanvas().snapshot(parameters, null);
		BufferedImage bImage = convert(image);
		try {
			File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
			ImageIO.write(bImage, "png", new File(jarDir.getAbsolutePath() + "\\" + filename + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage convert(Image fxImage) {
		int width = (int) Math.ceil(fxImage.getWidth());
		int height = (int) Math.ceil(fxImage.getHeight());

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		int[] buffer = new int[width];

		PixelReader reader = fxImage.getPixelReader();
		WritablePixelFormat<IntBuffer> format =
				PixelFormat.getIntArgbInstance();
		for (int y = 0; y < height; y++) {
			reader.getPixels(0, y, width, 1, format, buffer, 0, width);
			image.getRaster().setDataElements(0, y, width, 1, buffer);
		}

		return image;
	}
	
}
