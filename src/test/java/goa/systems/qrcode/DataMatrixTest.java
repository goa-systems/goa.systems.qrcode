package goa.systems.qrcode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.datamatrix.DataMatrixReader;

import goa.systems.commons.xml.XmlFramework;

class DataMatrixTest {

	@Test
	@Disabled("Not yet possible")
	void test() {

		//@formatter:off
		String tr = "This is a Data Matrix by goa systems";
		//@formatter:on

		Generator generator = new Generator();

		Document d = generator.generateSvgDocument(tr, 50, BarcodeFormat.DATA_MATRIX);
		Node node = d.getFirstChild();
		assertEquals("svg", node.getNodeName());
		assertDoesNotThrow(() -> {
			XmlFramework.getTransformer().transform(new DOMSource(d),
					new StreamResult(new File(System.getProperty("java.io.tmpdir"), "out.svg")));
		});
		assertDoesNotThrow(() -> {

			Result r = new DataMatrixReader().decode(CodeHelper.toImage(d));
			assertEquals(tr, r.getText());
		});
	}
}
