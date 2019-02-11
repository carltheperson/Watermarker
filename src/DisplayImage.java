package watermarker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DisplayImage extends JPanel implements ImageObserver {

	private Image picture;
	private Image watermark;

	private int boundWidth = 500;
	private int boundHeight = 300;
	static int newWidth;
	static int newHeight;

	//----------------------------------------------------------//
	
	//constructor
	public DisplayImage() {
		draw();
	}

	public void setPicture(String file) {
		try {
			this.picture = ImageIO.read(new File(file));
			Watermark.picture = this.picture;

			//resize to fit preview
			Dimension resizedDimensions = Watermark.resize(picture.getWidth(null), picture.getHeight(null), boundWidth, boundHeight);
			newWidth = resizedDimensions.width;
			newHeight = resizedDimensions.height;
			
		} catch (IOException e) {}
	}

	public void setWatermark(String file) {
		try {
			this.watermark = ImageIO.read(new File(file));
			Watermark.watermark = this.watermark;
		} catch (IOException e) {}
	}

	
	public void draw() {
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(600, 400));
		revalidate();
		repaint();
		this.setVisible(true);
		
		this.add(new drawG2D());

		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		paintComponent(g);

	}

	private class drawG2D extends JComponent {

		@Override
		protected void paintComponent(Graphics g) {
			
			Graphics2D graph2 = (Graphics2D) g;
			graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graph2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

			//Draw preview background
			graph2.setColor(Color.LIGHT_GRAY);
			graph2.fillRect(40, 35, boundWidth, boundHeight);
			
			//Draw resized preview
			graph2.drawImage(Watermark.getImage(), (boundWidth - newWidth) / 2 + 40, (boundHeight - newHeight) / 2 + 35, newWidth, newHeight, null);

			//Draw file title
			if (picture != null && watermark != null) {
				graph2.setColor(Color.BLACK);
				Font f = new Font("Helvetica", Font.PLAIN, 16);
				graph2.setFont(f);
				graph2.drawString(DetailsPanel.pictureFileName, 40, 30);
			}
		}
	}

}
