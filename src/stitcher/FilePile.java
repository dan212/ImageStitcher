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
	public enum sortBy{
		DATE, NAME;
	}

	private String rootPath;
	private Vector<String> imagePaths;
	private sortBy sort;
	
	class CreationDateComparator implements Comparator<Path> {
		@Override
		public int compare(Path o1, Path o2) {
			if (getSort() == sortBy.DATE) {
				try {
					FileTime temp1 = Files.readAttributes(o1, BasicFileAttributes.class).creationTime();
					FileTime temp2 = Files.readAttributes(o2, BasicFileAttributes.class).creationTime();
					return temp1.compareTo(temp2);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return -1;
			} else if (getSort() == sortBy.NAME) return o1.compareTo(o2);
			else return -1;
		}
	}
	
	public FilePile(String root) throws IOException {
		setSort(FilePile.sortBy.DATE);
		rootPath = root;
		imagePaths = new Vector<String>();
		try (Stream<Path> paths = Files.walk(Paths.get(rootPath), 1)) {
			paths.filter(p -> p.toString().contains(".png") || p.toString().contains(".jpeg")
					|| p.toString().contains(".PNG") || p.toString().contains(".JPEG"))
					.sorted((o1, o2) -> new CreationDateComparator().compare(o1, o2))
					.forEach(p -> getImagePaths().add(p.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FilePile(String root, sortBy sorting) throws IOException {
		setSort(sorting);
		rootPath = root;
		imagePaths = new Vector<String>();
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
					.sorted((o1, o2) -> new CreationDateComparator().compare(o1, o2))
					.forEach(p -> getImagePaths().add(p.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Vector<String> getImagePaths() {
		return imagePaths;
	}

	public sortBy getSort() {
		return sort;
	}

	public void setSort(sortBy sort) {
		this.sort = sort;
	}

}
