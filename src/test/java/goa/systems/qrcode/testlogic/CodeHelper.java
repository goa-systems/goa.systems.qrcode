package goa.systems.qrcode.testlogic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class CodeHelper {

	private static final Logger logger = LoggerFactory.getLogger(CodeHelper.class);

	/* Width of silent area around the codes. */
	private static final int silent = 64;

	/* Set this to true to persist generated images in the temporary directory. */
	private static final boolean doDebug = false;

	/**
	 * Converts the given SVG document to a BinaryBitmap object.
	 * 
	 * @param d Document to transcode and convert.
	 * @return BinaryBitmap
	 * @throws TranscoderException in case the document could not be transcoded.
	 */
	public static BinaryBitmap toBinaryBitmap(Document d) throws TranscoderException {
		return toBinaryBitmap(addSilent(toBufferedImage(d)));
	}

	/**
	 * Converts the given SVG document to a BinaryBitmap object.
	 * 
	 * @param bi BufferedImage to convert
	 * @return BinaryBitmap
	 * @throws TranscoderException in case the document could not be transcoded.
	 */
	public static BinaryBitmap toBinaryBitmap(BufferedImage bi) throws TranscoderException {
		LuminanceSource source = new BufferedImageLuminanceSource(bi);
		return new BinaryBitmap(new HybridBinarizer(source));
	}

	/**
	 * Persists the given document as PNG in the defined file.
	 * 
	 * @param target Destination file object.
	 * @param d      Document to transcode and save.
	 * @throws TranscoderException is thrown in case the document can not be
	 *                             transcoded.
	 * @throws IOException         is thrown in case the file could not be created
	 *                             or saved.
	 */
	public static void persistImage(File target, Document d) throws TranscoderException, IOException {
		ImageIO.write(addSilent(toBufferedImage(d)), "png", target);
	}

	/**
	 * Loads a PNG from the given input stream and converts it into a BinaryBitmap.
	 * 
	 * @param is
	 * @return BinaryBitmap in case of success.
	 * @throws IOException         is thrown if image can not be loaded.
	 * @throws TranscoderException is thrown if image can not be transcoded.
	 */
	public static BinaryBitmap loadTestImage(InputStream is) throws IOException, TranscoderException {
		return toBinaryBitmap(ImageIO.read(is));
	}

	private static BufferedImage getImageWithSilentArea(BufferedImage original) {
		BufferedImage withsilent = new BufferedImage(original.getWidth() + silent, original.getHeight() + silent,
				original.getType());
		Graphics2D graphics = withsilent.createGraphics();
		graphics.setPaint(Color.white);
		graphics.fillRect(0, 0, withsilent.getWidth(), withsilent.getHeight());
		return withsilent;
	}

	/**
	 * Adds a silent area around the image and returns a new buffered image.
	 * 
	 * @param original Original image without silent area.
	 * @return resized image with silent area added.
	 */
	private static BufferedImage addSilent(BufferedImage original) {
		BufferedImage withsilent = getImageWithSilentArea(original);
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				withsilent.setRGB((silent / 2) + x, (silent / 2) + y, original.getRGB(x, y));
			}
		}
		return withsilent;
	}

	private static BufferedImage toBufferedImage(Document d) throws TranscoderException {
		TranscoderInput input = new TranscoderInput(d);
		CustomImageTranscoder t = new CustomImageTranscoder();
		t.transcode(input, null);
		return t.getImage();
	}

	/**
	 * Writes the given document as PNG into the temporary directory with a UUID as
	 * filename.
	 * 
	 * @param d
	 * @throws TranscoderException
	 * @throws IOException
	 */
	public static File debugOutput(Document d) throws TranscoderException, IOException {
		return debugOutput(System.getProperty("java.io.tmpdir"), d);
	}

	/**
	 * Writes the given
	 * 
	 * @param base Path to the output directory.
	 * @param d    Document to convert and save.
	 * @throws TranscoderException in case the document can not be transcoded.
	 * @throws IOException         in case the image can not be saved.
	 * 
	 * @return Returns the file object the image is written to.
	 */
	public static File debugOutput(String base, Document d) throws TranscoderException, IOException {
		if (!doDebug) {
			return null;
		}
		File file = new File(System.getProperty("java.io.tmpdir"),
				String.format("%s.png", UUID.randomUUID().toString()));
		persistImage(file, d);
		logger.debug("Image persisted to {}", file.getAbsolutePath());
		return file;
	}
}
