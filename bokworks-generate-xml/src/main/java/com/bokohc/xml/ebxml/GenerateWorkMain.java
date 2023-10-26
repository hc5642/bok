package com.bokohc.xml.ebxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSFacet;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSInstance.SampleValueGenerator;
import jlibs.xml.xsd.XSParser;
import net.moznion.random.string.RandomStringGenerator;

/**
 * XSD 파일을 읽어들여 최대길이 샘플 XML을 생성하는 프로그램
 * 
 * @date 2023. 9. 7.
 * @author ohhyonchul
 *
 */
public class GenerateWorkMain {

	final static String URL_PATH = "https://docs.oasis-open.org/ebxml-msg/ebms/v3.0/core/os/ebms-header-3_0-200704.xsd";
	final static String INPUT_PATH = "./src/main/resources/static/input/ebxml/ebxml.xsd";
	final static String OUTPUT_PATH = "./src/main/resources/static/output/ebxml/origin";
	final static String CONVERT_PATH = "./src/main/resources/static/output/ebxml/convert_MAX";

	final static int TAP_SPACE_SIZE = 4;
	public static XSInstance INSTANCE = new XSInstance();

	public static void main(String[] args)
			throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {

		if (CONVERT_PATH.endsWith("MIN")) {
			INSTANCE.minimumElementsGenerated = 0; // 최소 엘리먼트 생성 (MIN:0, MAX:1)
			INSTANCE.minimumListItemsGenerated = 0; // 최소 배열 생성 (MIN:0, MAX:1)
			INSTANCE.maximumRecursionDepth = 0; // 최대 재귀 깊이 생략
			INSTANCE.maximumListItemsGenerated = 0; // 최대 배열 생성 고정
			INSTANCE.maximumElementsGenerated = 0; // 최대 엘리먼트 생성 고정
			INSTANCE.generateOptionalElements = false; // 옵셔널 엘리먼트 생성 (MIN:false, MAX:true)
			INSTANCE.generateOptionalAttributes = false; // 옵셔널 애트리뷰트 생성 (MIN:false, MAX:true)
		} else if (CONVERT_PATH.endsWith("MAX")) {
			INSTANCE.minimumElementsGenerated = 1; // 최소 엘리먼트 생성 (MIN:0, MAX:1)
			INSTANCE.minimumListItemsGenerated = 1; // 최소 배열 생성 (MIN:0, MAX:1)
			INSTANCE.maximumRecursionDepth = 1; // 최대 재귀 깊이 (MIN:0, MAX:1)
			INSTANCE.maximumListItemsGenerated = 1; // 최대 배열 생성 고정
			INSTANCE.maximumElementsGenerated = 1; // 최대 엘리먼트 생성 고정
			INSTANCE.generateOptionalElements = true; // 옵셔널 엘리먼트 생성 (MIN:false, MAX:true)
			INSTANCE.generateOptionalAttributes = true; // 옵셔널 애트리뷰트 생성 (MIN:false, MAX:true)
		}
		INSTANCE.generateAllChoices = true; // 모든 선택 생성 고정
		INSTANCE.generateDefaultAttributes = true; // 옵셔널 디폴트 애트리뷰트 생성
		INSTANCE.generateFixedAttributes = true; // 고정 애트리뷰트 생성
		INSTANCE.sampleValueGenerator = new SampleValueGeneratorImpl();

		generateEbXml();

	}

	/**
	 * 한국은행 ISO 20022 XML 생성
	 * 
	 * @throws TransformerConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void generateEbXml() throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
		File resultDir = null;
		resultDir = new File(OUTPUT_PATH);
		File xsd = new File(INPUT_PATH);

		QName root = new QName(getNamesapce(new FileInputStream(xsd)), "Envelope");
		XSModel xsModel = new XSParser().parse(xsd.getAbsolutePath());
		XMLDocument sample = new XMLDocument(new StreamResult(new File(resultDir + "/" + xsd.getName() + ".txt")), true,TAP_SPACE_SIZE, "utf-8");
		try {
			INSTANCE.generate(xsModel, root, sample);
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
	}

	public static String getNamesapce(InputStream input)
			throws SAXException, IOException, ParserConfigurationException {
		// XML Document 객체 생성
		InputSource is = new InputSource(input);
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		Element root = document.getDocumentElement();
		String namespace = root.getAttribute("xmlns");
		return namespace;
	}

}

class SampleValueGeneratorImpl implements SampleValueGenerator {

	@Override
	public String generateSampleValue(XSAttributeDeclaration attribute, XSSimpleTypeDefinition simpleType) {
		return null;
	}

	@Override
	public String generateSampleValue(XSElementDeclaration element, XSSimpleTypeDefinition simpleType) {
		XSFacet lengthFacet = getFacet(simpleType, XSSimpleTypeDefinition.FACET_MAXLENGTH);
		StringBuffer result = null;
		if (lengthFacet != null) {
			result = new StringBuffer();
			int length = Integer.parseInt(lengthFacet.getLexicalFacetValue());
			for (int i = 0; i < length; i++) {
				result.append("0");
			}
		}
		if (result != null) {
			return result.toString();
		} else {
			String pattern = simpleType.getLexicalPattern().toString();
//			System.out.print("--- PATTERN " + pattern + "\t");
			pattern = pattern.substring(1);
			pattern = pattern.substring(0, pattern.length() - 1);
//			System.out.print(pattern + "\t");
			if (simpleType.getPrimitiveType().getBuiltInKind() == 2) {
				if (pattern.trim().length() > 2) {
					String retValue = null;
					try {
						retValue = new RandomStringGenerator().generateByRegex(pattern);
					} catch (Exception e) {

						if (pattern.contains("{1,")) {
							pattern = "[0-9a-zA-Z]{" + pattern.substring(pattern.lastIndexOf("{1,") + 3);
						}
//						System.out.print(pattern + "\t");
						retValue = new RandomStringGenerator().generateByRegex(pattern);
					}
//					System.out.println(retValue);
					return retValue;
				}
			}
			return null;
		}
	}

	public XSFacet getFacet(XSSimpleTypeDefinition simpleType, int kind) {
		XSObjectList facets = simpleType.getFacets();
		for (int i = 0; i < facets.getLength(); i++) {
			XSFacet facet = (XSFacet) facets.item(i);
			if (facet.getFacetKind() == kind) {
				return facet;
			}
		}
		return null;
	}
}
