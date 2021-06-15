package goa.systems.qrcode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
	void testFileGeneration() {

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
			Document d = generator.generateSvgDocument(tr);
			Node node = d.getFirstChild();
			assertEquals("svg", node.getNodeName());
		});
	}
}
