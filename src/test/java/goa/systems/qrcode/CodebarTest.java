package goa.systems.qrcode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.aztec.AztecReader;

class CodebarTest {

	@Test
	void test() {

		//@formatter:off
		String tr = "01234565";
		//@formatter:on

		Generator generator = new Generator();

		Document d = generator.generateSvgDocument(tr, 10.0, BarcodeFormat.AZTEC);
		Node node = d.getFirstChild();
		assertEquals("svg", node.getNodeName());

		assertDoesNotThrow(() -> {
			Result r = new AztecReader().decode(CodeHelper.toImage(d));
			assertEquals(tr, r.getText());
		});
	}
}
