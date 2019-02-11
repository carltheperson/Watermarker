package watermarker;

import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private DisplayImage displayImage;
	private Container c = getContentPane();

	private DetailsPanel detailsPanel;
	
	public MainFrame(String title) {
		super(title);
		setLayout(new FlowLayout());

		detailsPanel = new DetailsPanel(this);
		displayImage = detailsPanel.getDisplayImage();

		c.add(detailsPanel);
		c.add(displayImage);
	}
}
