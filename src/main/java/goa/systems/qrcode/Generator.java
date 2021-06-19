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
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import goa.systems.commons.xml.XmlFramework;

public class Generator {

	private static final Logger logger = LoggerFactory.getLogger(Generator.class);

	/**
	 * Generates a minimal sized QR code that can be scaled to any size
	 * 
	 * @param str String to encode
	 * @return File object of svg file on disk
	 */
	public Document generateSvgDocument(String str, double scalingfactor, BarcodeFormat bf) {

		Document d = null;
		try {
			BitMatrix bm = generateQRcodeInternal(str, bf, 50, 0);
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

	private BitMatrix generateQRcodeInternal(String data, BarcodeFormat bf, int h, int w) throws WriterException {

		if (bf == BarcodeFormat.QR_CODE) {
			Map<EncodeHintType, ErrorCorrectionLevel> hints = new EnumMap<>(EncodeHintType.class);
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			return new QRCodeWriter().encode(data, bf, w, h, hints);
		} else if (bf == BarcodeFormat.EAN_8) {
			return new EAN8Writer().encode(data, bf, w, h);
		} else {
			logger.error("Barcodeformat {} currently unsupported.", bf);
			return new BitMatrix(0);
		}
	}

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
