package stitcher;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ImageStitcher {

	public enum Direction {
		DOWN, LEFT
	}

	private Vector<String> imagePaths;
	private String endPath;
	Direction dir;
	BufferedImage combined;

	public ImageStitcher(Vector<String> imagePaths, String end, Direction d) {
		this.imagePaths = imagePaths;
		endPath = end;
		dir = d;
	}

	public Vector<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(Vector<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public void combineAll() {
		combined = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage temp;
		BufferedImage curImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		for (String path : imagePaths) {
			try {
				curImage = ImageIO.read(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
			int w = Math.max(getCombined().getWidth(), curImage.getWidth());
			int h = getCombined().getHeight() + curImage.getHeight();
			temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g = temp.getGraphics();
			g.drawImage(getCombined(), 0, 0, null);
			switch (dir) {
			case DOWN:
				g.drawImage(curImage, 0, getCombined().getHeight(), null);
				break;
			case LEFT:
				g.drawImage(curImage, getCombined().getWidth(), 0, null);
				break;
			default:
			}
			combined = temp;
			g.dispose();
		}
		try {
			ImageIO.write(combined, "PNG", new File(endPath, "combined.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		combined.flush();
	}

	public BufferedImage getCombined() {
		return combined;
	}

}
