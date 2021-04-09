package stitcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.IOException;

public class Stitcher {

	JFrame frame;
	JTextField inputRootField;
	JButton stitchButton;
	JPanel control;
	String rootPath;
	String outputPath;
	FilePile fp = null;
	ImageStitcher sch = null;

	private void UIInit() {
		ActionHandler aHandler = new ActionHandler();
		frame = new JFrame("Glue dem screencaps together dawg");
		inputRootField = new JTextField(40);
		inputRootField.setText("Path for pics go here");
		stitchButton = new JButton("Stitch");
		stitchButton.setActionCommand("stitch");
		stitchButton.addActionListener(aHandler);

		control = new JPanel();
		control.add(inputRootField);
		control.add(stitchButton);

		frame.setLayout(new java.awt.BorderLayout());
		frame.add(control, java.awt.BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public void stitchAction() {
		if (inputRootField.getText() != "Path for pics go here") {
			rootPath = inputRootField.getText();
			outputPath = rootPath;
			if (fp == null) {
				try {
					fp = new FilePile(rootPath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				try {
					fp.setRootPath(rootPath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			sch = new ImageStitcher(fp.getImagePaths(), outputPath, stitcher.ImageStitcher.Direction.DOWN);
			sch.combineAll();
		} else {
			inputRootField.setText("Hey, set this first, dumbass!");
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
