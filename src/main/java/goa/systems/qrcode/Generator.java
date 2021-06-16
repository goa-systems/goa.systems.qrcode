package goa.systems.qrcode;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import goa.systems.commons.xml.XmlFramework;

/**
 * Generator class. Generates SVG graphics.
 * 
 * @author ago
 *
 */
public class Generator {

	private static final Logger logger = LoggerFactory.getLogger(Generator.class);

	/**
	 * Generates a minimal sized QR code that can be scaled to any size
	 * 
	 * @param str String to encode
	 * @return File object of svg file on disk
	 */
	public Document generateSvgDocument(String str) {

		String charset = "UTF-8";
		Document d = null;
		try {
			BitMatrix bm = generateQRcodeInternal(str, charset, 0, 0);
			int width = bm.getWidth();
			int height = bm.getHeight();

			d = getBaseSvg();
			Node svg = d.getFirstChild();

			svg.getAttributes().getNamedItem("width").setNodeValue(String.format("%dpx", width));
			svg.getAttributes().getNamedItem("height").setNodeValue(String.format("%dpx", height));

			for (int x = 0; x < height; x++) {
				for (int y = 0; y < width; y++) {
					if (bm.get(x, y)) {
						svg.appendChild(generateDot(d, x, y, "black"));
					}
				}
			}
		} catch (WriterException | IOException | SAXException | ParserConfigurationException e) {
			logger.error("Error generating SVG document.", e);
		}
		logger.info("QR code document created successfully.");
		return d;
	}

	private BitMatrix generateQRcodeInternal(String data, String charset, int h, int w)
			throws WriterException, IOException {
		Map<EncodeHintType, ErrorCorrectionLevel> hints = new EnumMap<>(EncodeHintType.class);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		return new QRCodeWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h,
				hints);
	}

	/**
	 * Loads base SVG file (empty SVG file).
	 * 
	 * @return org.w3c.Document representation of an empty SVG file.
	 * @throws SAXException                 in case of error.
	 * @throws IOException                  in case of error.
	 * @throws ParserConfigurationException in case of error.
	 */
	public Document getBaseSvg() throws SAXException, IOException, ParserConfigurationException {
		return XmlFramework.getDocumentBuilder().parse(Generator.class.getResourceAsStream("/base.svg"));
	}

	private Node generateDot(Document d, int x, int y, String color) {
		Element node = d.createElement("rect");
		node.setAttribute("x", Integer.toString(x));
		node.setAttribute("y", Integer.toString(y));
		node.setAttribute("width", "1");
		node.setAttribute("height", "1");
		node.setAttribute("fill", color);
		return node;
	}
}
