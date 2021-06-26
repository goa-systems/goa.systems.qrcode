package goa.systems.qrcode.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.pdf417.PDF417Reader;

import goa.systems.qrcode.Generator;
import goa.systems.qrcode.testlogic.CodeHelper;

class Pdf417Test {

	@Test
	void test() {

		//@formatter:off
		String tr = "0123456789012";
		//@formatter:on

		Generator generator = new Generator();

		Document d = generator.generateSvgDocument(tr, 100, BarcodeFormat.PDF_417);
		Node node = d.getFirstChild();
		assertEquals("svg", node.getNodeName());

		assertDoesNotThrow(() -> {
			Result r = new PDF417Reader().decode(CodeHelper.toBinaryBitmap(d));
			assertEquals(tr, r.getText());
			CodeHelper.debugOutput(d);
		});
	}
}
