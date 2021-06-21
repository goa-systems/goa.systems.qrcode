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
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.Code93Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.oned.UPCEWriter;
import com.google.zxing.pdf417.PDF417Writer;
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

	private static final String BARCODE_NOT_SUPPORTED = "Barcodeformat {} currently unsupported.";

	/**
	 * Generates a minimal sized QR code that can be scaled to any size
	 * 
	 * @param str String to encode
	 * @param sf  Scaling factor (applied to x and y)
	 * @param bf  Barcode format
	 * @return org.w3c.Document representation of SVG file.
	 */
	public Document generateSvgDocument(String str, double sf, BarcodeFormat bf) {
		return generateSvgDocument(str, sf, sf, bf);
	}

	/**
	 * Generates a minimal sized QR code that can be scaled to any size
	 * 
	 * @param str String to encode
	 * @param xf  Scaling factor width
	 * @param yf  Scaling factor height
	 * @param bf  Barcode format
	 * @return org.w3c.Document representation of SVG file.
	 */
	public Document generateSvgDocument(String str, double xf, double yf, BarcodeFormat bf) {

		Document d = null;
		try {
			BitMatrix bm = generateQRcodeInternal(str, bf, 0, 0);
			int width = bm.getWidth();
			int height = bm.getHeight();

			d = getBaseSvg();
			Node svg = d.getFirstChild();

			svg.getAttributes().getNamedItem("width").setNodeValue(String.format("%fpx", width * xf));
			svg.getAttributes().getNamedItem("height").setNodeValue(String.format("%fpx", height * yf));

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (bm.get(x, y)) {
						svg.appendChild(generateDot(d, x, y, xf, yf, "black"));
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
		} else if (bf == BarcodeFormat.AZTEC) {
			return new AztecWriter().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.CODABAR) {
			return new CodaBarWriter().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.CODE_39) {
			return new Code39Writer().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.CODE_93) {
			return new Code93Writer().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.CODE_128) {
			return new Code128Writer().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.DATA_MATRIX) {
			return new DataMatrixWriter().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.EAN_13) {
			return new EAN13Writer().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.ITF) {
			return new ITFWriter().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.PDF_417) {
			return new PDF417Writer().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.UPC_A) {
			return new UPCAWriter().encode(data, bf, w, h);
		} else if (bf == BarcodeFormat.UPC_E) {
			return new UPCEWriter().encode(data, bf, w, h);
		} else {
			logger.error(BARCODE_NOT_SUPPORTED, bf);
			return new BitMatrix(0);
		}
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

	private Node generateDot(Document d, int x, int y, double xf, double yf, String color) {
		Element node = d.createElement("rect");
		node.setAttribute("x", Double.toString(x * xf));
		node.setAttribute("y", Double.toString(y * yf));
		node.setAttribute("width", Double.toString(xf));
		node.setAttribute("height", Double.toString(yf));
		node.setAttribute("fill", color);
		return node;
	}
}
