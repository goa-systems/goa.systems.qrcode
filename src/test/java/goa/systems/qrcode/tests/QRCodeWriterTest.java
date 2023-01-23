package goa.systems.qrcode.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeReader;

import goa.systems.commons.xml.XmlFramework;
import goa.systems.qrcode.Generator;
import goa.systems.qrcode.testlogic.CodeHelper;

class QRCodeWriterTest {

	@Test
	void test() {
		//@formatter:off
		String tr = "BCD\n"
				+ "001\n"
				+ "1\n"
				+ "SCT\n"
				+ "DOSPAT2DXXX\n"
				+ "Andreas Gottardi\n"
				+ "AT962060200000459156\n"
				+ "EUR75.00\n"
				+ "\n"
				+ "\n"
				+ "Rechnung 12/2021";
		//@formatter:on

		Generator generator = new Generator();

		Document d = generator.generateSvgDocument(tr, 1.0, BarcodeFormat.QR_CODE);
		Node node = d.getFirstChild();
		assertEquals("svg", node.getNodeName());

		assertDoesNotThrow(() -> {
			XmlFramework.getTransformer().transform(new DOMSource(d),
					new StreamResult(new File("C:\\Users\\ago\\Desktop\\r12.svg")));
			Result r = new QRCodeReader().decode(CodeHelper.toBinaryBitmap(d));
			assertEquals(tr, r.getText());
			CodeHelper.debugOutput(d);
		});
	}
}
