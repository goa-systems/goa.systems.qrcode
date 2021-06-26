package goa.systems.qrcode.testlogic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

public class CustomImageTranscoder extends ImageTranscoder {

	BufferedImage bufferedImage;

	public CustomImageTranscoder() {
		bufferedImage = null;
	}

	@Override
	public BufferedImage createImage(int w, int h) {
		BufferedImage b_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = b_img.createGraphics();
		graphics.setPaint(Color.white);
		graphics.fillRect(0, 0, b_img.getWidth(), b_img.getHeight());
		return b_img;
	}

	@Override
	public void writeImage(BufferedImage image, TranscoderOutput out) throws TranscoderException {
		bufferedImage = image;
	}

	public BufferedImage getImage() {
		return this.bufferedImage;
	}
}
