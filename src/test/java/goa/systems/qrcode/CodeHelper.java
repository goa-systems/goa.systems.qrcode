package goa.systems.qrcode;

import java.awt.image.BufferedImage;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.w3c.dom.Document;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class CodeHelper {

	public static BinaryBitmap toImage(Document d) throws TranscoderException {
		TranscoderInput input = new TranscoderInput(d);
		CustomImageTranscoder t = new CustomImageTranscoder();
		t.transcode(input, null);
		BufferedImage bi = t.getImage();
		LuminanceSource source = new BufferedImageLuminanceSource(bi);
		return new BinaryBitmap(new HybridBinarizer(source));
	}
}
