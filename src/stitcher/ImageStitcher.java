package stitcher;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ImageStitcher {

	public enum Direction {
		DOWN, RIGHT
	}

	private Vector<String> imagePaths;
	private String endPath;
	private Direction dir;
	BufferedImage combined;

	public ImageStitcher(Vector<String> imagePaths, String end, Direction d) {
		this.imagePaths = imagePaths;
		setEndPath(end);
		setDir(d);
	}

	public Vector<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(Vector<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public void combineAll() {
		System.out.println("Began work");
		combined = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		BufferedImage temp;
		Image curImage = null;
		for (String path : imagePaths) {
			if (!path.contains("combined.png")) {
				try {
					curImage = ImageIO.read(new File(path));
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.print(path);
				int w = 0;
				int h = 0;
				switch (getDir()) {
				case DOWN:
					w = Math.max(getCombined().getWidth(), curImage.getWidth(null));
					h = getCombined().getHeight() + curImage.getHeight(null);
					break;
				case RIGHT:
					w = getCombined().getWidth()+ curImage.getWidth(null);
					h = Math.max(getCombined().getHeight(), curImage.getHeight(null));
					break;
				default:
				}
				temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = temp.createGraphics();
				g.drawImage(getCombined(), 0, 0, null);
				switch (getDir()) {
				case DOWN:
					g.drawImage(curImage, 0, getCombined().getHeight(), null);
					break;
				case RIGHT:
					g.drawImage(curImage, getCombined().getWidth(), 0, null);
					break;
				default:
				}
				combined = temp;
				curImage.flush();
				temp.flush();
				g.dispose();
				System.out.println("... Done!");
			} else System.out.println("Pre-existing output file found, skipping and replasing");
		}
		try {
			ImageIO.write(combined, "PNG", new File(getEndPath(), "combined.png"));
			System.out.println("Job's done! End file is " + getEndPath() + "\\combined.png");
			switch (getDir()) {
			case DOWN:
				System.out.print("Tall boy's size is " + combined.getHeight() + " pixels");
				break;
			case RIGHT:
				System.out.print("Wide boy's size is " + combined.getWidth() + " pixels");
				break;
			default:
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		combined.flush();
	}

	public BufferedImage getCombined() {
		return combined;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public String getEndPath() {
		return endPath;
	}

	public void setEndPath(String endPath) {
		this.endPath = endPath;
	}

}
