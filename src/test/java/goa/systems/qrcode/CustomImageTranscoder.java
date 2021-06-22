package goa.systems.qrcode;

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
		return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public void writeImage(BufferedImage image, TranscoderOutput out) throws TranscoderException {
		bufferedImage = image;
	}

	public BufferedImage getImage() {
		return this.bufferedImage;
	}
}
