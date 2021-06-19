package goa.systems.qrcode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.UUID;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;

import goa.systems.commons.xml.XmlFramework;

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
	 * Generates a SVG EAN8 code.
	 */
	@Test
	void testEANCodeAsFile1() {

		//@formatter:off
		String tr = "9031101";
		//@formatter:on

		Generator generator = new Generator();
		assertDoesNotThrow(() -> {
			Document d = generator.generateSvgDocument(tr, 1.0, BarcodeFormat.EAN_8);
			File output = new File(System.getProperty("java.io.tmpdir"),
					String.format("%s.svg", UUID.randomUUID().toString()));
			XmlFramework.getTransformer().transform(new DOMSource(d), new StreamResult(output));
			assertTrue(output.exists());
			output.delete();
			assertFalse(output.exists());
		});
	}

	/**
	 * Generates a SVG EAN8 code.
	 */
	@Test
	void testEANCodeAsFile2() {

		//@formatter:off
		String tr = "9031101";
		//@formatter:on

		Generator generator = new Generator();
		assertDoesNotThrow(() -> {
			Document d = generator.generateSvgDocument(tr, 6.0864521, 67.987654321, BarcodeFormat.EAN_8);
			File output = new File(System.getProperty("java.io.tmpdir"),
					String.format("%s.svg", UUID.randomUUID().toString()));
			XmlFramework.getTransformer().transform(new DOMSource(d), new StreamResult(output));
			assertTrue(output.exists());
			output.delete();
			assertFalse(output.exists());
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
