package stitcher;


import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;

public class Stitcher {

	JFrame frame;
	JTextField inputRootField;
	JButton stitchButton;
	JPanel control;
	JTextArea logBox;
	JTextAreaOutputStream logBoxStream;
	JComboBox<ImageStitcher.Direction> dirComboBox;
	JComboBox<FilePile.sortBy> sortComboBox;
	String rootPath;
	String outputPath;
	FilePile fp = null;
	ImageStitcher sch = null;

	private void UIInit() {
		ActionHandler aHandler = new ActionHandler();
		frame = new JFrame("Glue dem screencaps together dawg");
		Toolkit tk = frame.getToolkit();
		//File LogoFile = new File("/logo.png");
		Image LogoImage = new BufferedImage(256, 256, 1);
		LogoImage = tk.getImage(this.getClass().getResource("logo.png"));
		frame.setIconImage(LogoImage);
		inputRootField = new JTextField(40);
		inputRootField.setText("Path for pics go here");
		stitchButton = new JButton("Stitch");
		stitchButton.setActionCommand("stitch");
		stitchButton.addActionListener(aHandler);
		logBox = new JTextArea(20, 40);
		logBox.setEditable(false);
		dirComboBox = new JComboBox<ImageStitcher.Direction>();
		dirComboBox.addItem(ImageStitcher.Direction.DOWN);
		dirComboBox.addItem(ImageStitcher.Direction.RIGHT);
		dirComboBox.addActionListener(aHandler);
		sortComboBox = new JComboBox<FilePile.sortBy>();
		sortComboBox.addItem(FilePile.sortBy.DATE);
		sortComboBox.addItem(FilePile.sortBy.NAME);
		sortComboBox.addActionListener(aHandler);
		
		logBoxStream = new JTextAreaOutputStream(logBox);
		System.setOut(new PrintStream(logBoxStream));

		control = new JPanel();
		control.add(inputRootField);
		control.add(new JLabel("Sticth Direction"));
		control.add(dirComboBox);
		control.add(new JLabel("Sort by "));
		control.add(sortComboBox);
		control.add(stitchButton);

		frame.setLayout(new java.awt.BorderLayout());
		frame.add(control, java.awt.BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JScrollPane(logBox, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), 
		java.awt.BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public void stitchAction() {
		logBox.setText("");
		if (inputRootField.getText() != "Path for pics go here") {
			rootPath = inputRootField.getText();
			outputPath = rootPath;
			if (fp == null) {
				try {
					fp = new FilePile(rootPath,(FilePile.sortBy)sortComboBox.getSelectedItem());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				try {
					fp.setSort((FilePile.sortBy)sortComboBox.getSelectedItem());
					fp.setRootPath(rootPath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (fp.getImagePaths().size() != 0) {
				if (sch == null) {
					sch = new ImageStitcher(fp.getImagePaths(), outputPath, (ImageStitcher.Direction)dirComboBox.getSelectedItem());
				}
				else {
					sch.setDir((ImageStitcher.Direction)dirComboBox.getSelectedItem());
					sch.setImagePaths(fp.getImagePaths());
					sch.setEndPath(outputPath);
				}
				sch.combineAll();
			}
			else logBox.setText("No pictures where found in folder specified");
		} else {
			inputRootField.setText("Hey, set this first!");
		}
	}

	public class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "stitch") {
				stitchAction();
			}
		}
	}

	public static void main(String[] args) {
		Stitcher st = new Stitcher();
		st.UIInit();
	}

}
