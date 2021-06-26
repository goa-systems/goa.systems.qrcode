package goa.systems.qrcode.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.oned.Code39Reader;

import goa.systems.qrcode.Generator;
import goa.systems.qrcode.testlogic.CodeHelper;

class Code39Test {

	@Test
	void test() {

		//@formatter:off
		String tr = "01234565";
		//@formatter:on

		Generator generator = new Generator();

		Document d = generator.generateSvgDocument(tr, 1.0, BarcodeFormat.CODE_39);
		Node node = d.getFirstChild();
		assertEquals("svg", node.getNodeName());

		assertDoesNotThrow(() -> {
			Result r = new Code39Reader().decode(CodeHelper.toBinaryBitmap(d));
			assertEquals(tr, r.getText());
			CodeHelper.debugOutput(d);
		});
	}
}
