package goa.systems.qrcode;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

public class Generator {

	private static final Logger logger = LoggerFactory.getLogger(Generator.class);

	/**
	 * Generates a minimal sized QR code that can be scaled to any size
	 * 
	 * @param tr Transfer data
	 * @return File object of svg file on disk
	 */
	public File generateQrCode(Transfer tr) {

		//@formatter:off
		String str = String.format(""
				+ "BCD\n"
				+ "001\n"
				+ "1\n"
				+ "SCT\n"
				+ "%s\n"
				+ "%s\n"
				+ "%s\n"
				+ "EUR%s\n"
				+ "\n"
				+ "\n"
				+ "%s", tr.getBic(), tr.getRecipientname(), tr.getIban(), tr.getSum(), tr.getUsage());
		//@formatter:on

		File path = new File(System.getProperty("java.io.tmpdir"), String.format("%s.svg", UUID.randomUUID()));

		String charset = "UTF-8";

		try {
			BitMatrix bm = generateQRcodeInternal(str, charset, 0, 0);
			int width = bm.getWidth();
			int height = bm.getHeight();

			Document d = getBaseSvg();
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
			Transformer t = XmlFramework.getTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			t.transform(new DOMSource(d), new StreamResult(path));
		} catch (WriterException | IOException | SAXException | ParserConfigurationException | TransformerException e) {
			logger.error("General error template.", e);
		}
		logger.info("QR Code created successfully in {}", path.getAbsolutePath());
		logger.info("URL {}", path.toURI());
		return path;
	}

	public BitMatrix generateQRcodeInternal(String data, String charset, int h, int w)
			throws WriterException, IOException {
		Map<EncodeHintType, ErrorCorrectionLevel> hints = new EnumMap<>(EncodeHintType.class);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		return new QRCodeWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h,
				hints);
	}

	public Document getBaseSvg() throws SAXException, IOException, ParserConfigurationException {
		return XmlFramework.getDocumentBuilder().parse(Generator.class.getResourceAsStream("/base.svg"));
	}

	public Node generateDot(Document d, int x, int y, String color) {
		Element node = d.createElement("rect");
		node.setAttribute("x", Integer.toString(x));
		node.setAttribute("y", Integer.toString(y));
		node.setAttribute("width", "1");
		node.setAttribute("height", "1");
		node.setAttribute("fill", color);
		return node;
	}
}
