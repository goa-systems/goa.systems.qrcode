package goa.systems.qrcode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;

class GeneratorTest {

	/**
	 * Tests if base SVG is valid.
	 */
	@Test
	void testBaseSvg() {
		Generator generator = new Generator();
		assertDoesNotThrow(() -> {
			Document d = generator.getBaseSvg();
			Node node = d.getFirstChild();
			assertEquals("svg", node.getNodeName());
		});
	}

	/**
	 * Generates a SVG QR code into the temporary directory, tests its existence and
	 * deletes the file afterwards.
	 */
	@Test
	void testQRCode() {

		//@formatter:off
		String tr = "BCD\n"
				+ "001\n"
				+ "1\n"
				+ "SCT\n"
				+ "00000000000 \n"
				+ "Prename Surname\n"
				+ "AT000000000000000000\n"
				+ "0000.00\n"
				+ "\n"
				+ "\n"
				+ "TestTransfer";
		//@formatter:on

		Generator generator = new Generator();
		assertDoesNotThrow(() -> {
			Document d = generator.generateSvgDocument(tr, 1.0, BarcodeFormat.QR_CODE);
			Node node = d.getFirstChild();
			assertEquals("svg", node.getNodeName());
		});
	}

	/**
	 * Generates a SVG QR code into the temporary directory, tests its existence and
	 * deletes the file afterwards.
	 */
	@Test
	void testEANCode() {

		//@formatter:off
		String tr = "9031101";
		//@formatter:on

		Generator generator = new Generator();
		assertDoesNotThrow(() -> {
			Document d = generator.generateSvgDocument(tr, 1.0, BarcodeFormat.EAN_8);
			Node node = d.getFirstChild();
			assertEquals("svg", node.getNodeName());
		});
	}
}
