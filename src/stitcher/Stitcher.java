package stitcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.IOException;

public class Stitcher {

	JFrame frame;
	JTextField inputRootField;
	JTextField outputLocationField;
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
		outputLocationField = new JTextField(40);
		inputRootField.setText("Path for pics go here");
		outputLocationField.setText("Output Path (empty for same as input)");
		stitchButton = new JButton("Stitch");
		stitchButton.setActionCommand("stitch");
		stitchButton.addActionListener(aHandler);

		control = new JPanel();
		control.add(inputRootField);
		control.add(outputLocationField);
		control.add(stitchButton);

		frame.setLayout(new java.awt.BorderLayout());
		frame.add(control, java.awt.BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public void stitchAction() {
		if (inputRootField.getText() != "") {
			rootPath = inputRootField.getText();
			if (outputLocationField.getText() != "") {
				outputPath = rootPath;
				outputLocationField.setText(outputPath);
			}
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
			outputLocationField.setText("Hey, set this first, dumbass!");
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
