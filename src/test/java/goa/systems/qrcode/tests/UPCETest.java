package goa.systems.qrcode.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.oned.UPCEReader;

import goa.systems.qrcode.Generator;
import goa.systems.qrcode.testlogic.CodeHelper;

class UPCETest {

	@Test
	void test() {

		//@formatter:off
		String tr = "04252614";
		//@formatter:on

		Generator generator = new Generator();

		Document d = generator.generateSvgDocument(tr, 10, 600, BarcodeFormat.UPC_E);
		Node node = d.getFirstChild();
		assertEquals("svg", node.getNodeName());

		assertDoesNotThrow(() -> {
			BinaryBitmap bb = CodeHelper.toBinaryBitmap(d);
			Result r = new UPCEReader().decode(bb);
			assertEquals(tr, r.getText());
			CodeHelper.debugOutput(d);
		});
	}
}
