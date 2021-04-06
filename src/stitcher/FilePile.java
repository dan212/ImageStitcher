package stitcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.Vector;
import java.util.stream.Stream;

public class FilePile {

	private String rootPath;
	private Vector<String> imagePaths;

	public FilePile(String root) throws IOException {
		rootPath = root;
		imagePaths = new Vector<String>();

		class CreationDateComparator implements Comparator<Path> {
			@Override
			public int compare(Path o1, Path o2) {
				try {
					FileTime temp1 = Files.readAttributes(o1, BasicFileAttributes.class).creationTime();
					FileTime temp2 = Files.readAttributes(o2, BasicFileAttributes.class).creationTime();
					return temp1.compareTo(temp2);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return -1;
			}
		}

		try (Stream<Path> paths = Files.walk(Paths.get(rootPath), 1)) {
			paths.filter(p -> p.toString().contains(".png") || p.toString().contains(".jpeg")
					|| p.toString().contains(".PNG") || p.toString().contains(".JPEG"))
					.sorted((o1, o2) -> new CreationDateComparator().compare(o1, o2))
					.forEach(p -> getImagePaths().add(p.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setRootPath(String root) throws IOException {
		rootPath = root;
		imagePaths.clear();
		try (Stream<Path> paths = Files.walk(Paths.get(rootPath), 1)) {
			paths.filter(p -> p.toString().contains(".png") || p.toString().contains(".jpeg")
					|| p.toString().contains(".PNG") || p.toString().contains(".JPEG"))
					.forEachOrdered(p -> getImagePaths().add(p.toString()));
		}
	}

	public Vector<String> getImagePaths() {
		return imagePaths;
	}

}
