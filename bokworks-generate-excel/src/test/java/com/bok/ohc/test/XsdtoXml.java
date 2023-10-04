package com.bok.ohc.test;

import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;

public class XsdtoXml {
	
     public static void main(String[] pArgs) {
            try {
                String filename = "out.xsd";
                // instance.

                final Document doc = loadXsdDocument(filename);

                //Find the docs root element and use it to find the targetNamespace
                final Element rootElem = doc.getDocumentElement();
                String targetNamespace = null;
                if (rootElem != null && rootElem.getNodeName().equals("xs:schema")) 
                           {
                    targetNamespace = rootElem.getAttribute("targetNamespace");
                }


                //Parse the file into an XSModel object
                org.apache.xerces.xs.XSModel xsModel = new XSParser().parse(filename);

                //Define defaults for the XML generation
                XSInstance instance = new XSInstance();
                instance.minimumElementsGenerated = 1;
                instance.maximumElementsGenerated = 1;
                instance.generateDefaultAttributes = true;
                instance.generateOptionalAttributes = true;
                instance.maximumRecursionDepth = 0;
                instance.generateAllChoices = true;
                instance.showContentModel = true;
                instance.generateOptionalElements = true;

                //Build the sample xml doc
                //Replace first param to XMLDoc with a file input stream to write to file
                QName rootElement = new QName(targetNamespace, "out");
                XMLDocument sampleXml = new XMLDocument(new StreamResult(System.out), true, 4, null);
                instance.generate(xsModel, rootElement, sampleXml);
            } catch (TransformerConfigurationException e) 
                    {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public static Document loadXsdDocument(String inputName) {
            final String filename = inputName;

            final DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            Document doc = null;

            try {
                final DocumentBuilder builder = factory.newDocumentBuilder();
                final File inputFile = new File(filename);
                doc = builder.parse(inputFile);
            } catch (final Exception e) {
                e.printStackTrace();
                // throw new ContentLoadException(msg);
            }

            return doc;
        }

}