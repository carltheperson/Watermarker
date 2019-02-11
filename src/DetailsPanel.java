package watermarker;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DetailsPanel extends JPanel {

	private MainFrame mainFrame;
	private DisplayImage displayImage = new DisplayImage();
	
	//components
	private JLabel choosePictureLabel = new JLabel("No file choosen");
	private JButton choosePictureBtn = new JButton("Choose Picture");
	private JLabel chooseWatermarkLabel = new JLabel("No file choosen");
	private JButton chooseWatermarkBtn = new JButton("Choose Watermark");
	private JLabel opacitySliderLabel = new JLabel("Opacity");
	private JSlider opacitySlider = new JSlider(0, 100, 50);
	private JLabel sizeSliderLabel = new JLabel("Size");
	private JSlider sizeSlider = new JSlider(20, 106, 63);
	private JCheckBox centerCheckBox = new JCheckBox("Center", true);
	private JCheckBox cornerCheckBox = new JCheckBox("Corner", false);
	private JButton applyBtn = new JButton("Apply");
	private JButton saveBtn = new JButton("save");
	private JLabel saveLabel = new JLabel();

	static String pictureFileName = "";
	private String pictureFileDirectory;
	
	//----------------------------------------------------------//
	
	//constructor
	public DetailsPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		Watermark.size = 50;
		Watermark.opacity = 50;

		//action listening
		fileListener(choosePictureBtn, choosePictureLabel);
		fileListener(chooseWatermarkBtn, chooseWatermarkLabel);
		checkBoxListener(centerCheckBox, cornerCheckBox);
		sliderListener(sizeSlider);
		sliderListener(opacitySlider);
		applyListener(applyBtn);
		saveListener(saveBtn, saveLabel);

		addComponents();
		
	}

	public void addComponents() {
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		//choose picture
		c.gridy = 1;
		add(choosePictureBtn, c);
		c.gridy = 2;
		add(choosePictureLabel, c);
		//choose watermark
		c.insets = new Insets(20, 0, 0, 0);
		c.gridy = 3;
		add(chooseWatermarkBtn, c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridy = 4;
		add(chooseWatermarkLabel, c);
		//sliders
		c.insets = new Insets(30, 0, 0, 0);
		c.gridy = 5;
		add(opacitySliderLabel, c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridy = 6;
		add(opacitySlider, c);
		c.insets = new Insets(10, 0, 0, 0);
		c.gridy = 7;
		add(sizeSliderLabel, c);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridy = 8;
		add(sizeSlider, c);
		//checkboxes
		c.gridy = 9;
		add(centerCheckBox, c);
		c.gridy = 10;
		c.insets = new Insets(0, 0, 0, 0);
		add(cornerCheckBox, c);
		//apply and save
		c.insets = new Insets(10, 0, 0, 0);
		c.gridy = 11;
		add(applyBtn, c);
		c.gridy = 12;
		c.insets = new Insets(10, 0, 0, 0);
		add(saveBtn, c);
		c.gridy = 13;
		add(saveLabel, c);
		

	}

	public DisplayImage getDisplayImage() {
		return displayImage;
	}
	
	public void pack() {
		this.mainFrame.pack();
	}
	
	//-------------------- ACTION LISTENING -----------------//

	//The "choose file" ande "choose watermark" buttons
	public void fileListener(JButton Btn, JLabel label) {
		Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
					dialog.setMode(FileDialog.LOAD);
					dialog.setVisible(true);

					if (dialog.getFile().endsWith(".png") || dialog.getFile().endsWith(".jpg") || dialog.getFile().endsWith(".PNG") || dialog.getFile().endsWith(".JPG")) {
						label.setText(dialog.getFile());
						label.setVisible(true);

						if (Btn == choosePictureBtn) {
							pictureFileName = "watermarked " + dialog.getFile();
							pictureFileDirectory = dialog.getDirectory();
							displayImage.setPicture(dialog.getDirectory() + dialog.getFile());
							displayImage.draw();
							Watermark.update();
							pack();
						}
						
						if (Btn == chooseWatermarkBtn) {
							displayImage.setWatermark(dialog.getDirectory() + dialog.getFile());
							Watermark.update();
							displayImage.draw();
							pack();
						}

					} else {
						label.setText("File has to be a .png or .jpg");
						label.setVisible(true);
					}
				} catch (NullPointerException a) {}
			}
		});
	}

	//The "center" and "corner" checkboxes
	public void checkBoxListener(JCheckBox centerCheckBox, JCheckBox cornerCheckBox) {
		
		centerCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cornerCheckBox.setSelected(false);
				if (!centerCheckBox.isSelected() && !cornerCheckBox.isSelected()) {
					centerCheckBox.setSelected(true);
				}
				Watermark.center = true;
			}
		});
		
		cornerCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				centerCheckBox.setSelected(false);
				if (!centerCheckBox.isSelected() && !cornerCheckBox.isSelected()) {
					cornerCheckBox.setSelected(true);
				}
				Watermark.center = false;
			}
		});
	}

	//The "size" and "opacity" sliders
	public void sliderListener(JSlider slider) {
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				
				if (slider == sizeSlider) {
					Watermark.size = ((JSlider) ce.getSource()).getValue();

				}
				
				if (slider == opacitySlider) {
					Watermark.opacity = ((JSlider) ce.getSource()).getValue();
				}
			}
		});

	}

	//The "apply" button
	public void applyListener(JButton Btn) {
		Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Watermark.update();
				displayImage.draw();
				pack();
			}
		});
	}

	//The "save" button
	public void saveListener(JButton Btn, JLabel label) {
		Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pictureFileName != "") {
					BufferedImage bi = (BufferedImage) Watermark.getImage();
					File outputfile = new File(pictureFileDirectory + pictureFileName);
					try {
						ImageIO.write(bi, "png", outputfile);
						label.setText("\"" + pictureFileName + "\"" + " saved!");
						label.setVisible(true);
					} catch (IOException e1) {}
					pack();
				}

			}
		});
	}
}
