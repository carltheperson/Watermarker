package watermarker;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Watermark {

	static Image picture;
	static Image watermark;
	static Image watermarkedPicture;
	static int opacity;
	static int size;
	static boolean center = true;

	//----------------------------------------------------------//
	
	public static Image getImage() {
		return (Image) watermarkedPicture;
	}
	
	public static void update() {
		if (picture != null && watermark != null) {

			BufferedImage image = (BufferedImage) picture;
			BufferedImage overlay = resize((BufferedImage) watermark, picture.getWidth(null), picture.getHeight(null));

			BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) watermarked.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity / 100));

			if (center) {
				g.drawImage(overlay, picture.getWidth(null) / 2 - overlay.getWidth() / 2, picture.getHeight(null) / 2 - overlay.getHeight() / 2, null);
			} else {
				g.drawImage(overlay, 0, 0, null);
			}

			watermarkedPicture = watermarked;
			
		} else if (picture != null) {
			watermarkedPicture = picture;
		}
	}

	//resizes buffered image
	private static BufferedImage resize(BufferedImage img, int width, int height) {
		if (img != null) {
			Dimension resizedDimensions = resize(img.getWidth(), img.getHeight(), width, height);
			width = (resizedDimensions.width * size) / 100;
			height = (resizedDimensions.height * size) / 100;

			Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

			BufferedImage resized;
			resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = resized.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
			
			return resized;
		}
		return img;
	}

	//Gives new resized dimentions while keeping aspect ratio
	public static Dimension resize(int originalWidth, int originalHeight, int boundWidth, int boundHeight) {
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		if (originalWidth > boundWidth) {
			newWidth = boundWidth;
			newHeight = (newWidth * originalHeight) / originalWidth;
		}
		if (newHeight > boundHeight) {
			newHeight = boundHeight;
			newWidth = (newHeight * originalWidth) / originalHeight;
		}
		if (newWidth == originalWidth && newHeight == originalHeight) {
			if ((newWidth * originalHeight) / originalWidth > (newHeight * originalWidth) / originalHeight) {
				newHeight = boundHeight;
				newWidth = (newHeight * originalWidth) / originalHeight;
			} else {
				newWidth = boundWidth;
				newHeight = (newWidth * originalHeight) / originalWidth;
			}

		}
		return new Dimension(newWidth, newHeight);
	}

}
