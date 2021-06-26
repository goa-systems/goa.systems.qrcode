package goa.systems.qrcode.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.oned.UPCAReader;

import goa.systems.qrcode.Generator;
import goa.systems.qrcode.testlogic.CodeHelper;

class UPCATest {

	@Test
	void test() {

		//@formatter:off
		String tr = "012345678905";
		//@formatter:on

		Generator generator = new Generator();

		Document d = generator.generateSvgDocument(tr, 1.0, BarcodeFormat.UPC_A);
		Node node = d.getFirstChild();
		assertEquals("svg", node.getNodeName());

		assertDoesNotThrow(() -> {
			Result r = new UPCAReader().decode(CodeHelper.toBinaryBitmap(d));
			assertEquals(tr, r.getText());
			CodeHelper.debugOutput(d);
		});
	}
}
