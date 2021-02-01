package com.borjamoll.csvtoxml_heroku.builder;


import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class XMLBuilder {

    private ArrayList<String[]> fileColumns;
    public static final String ROOT = "root";
    public static final String REGISTRY = "registry";

    public XMLBuilder() {

    }


    /**
     * Creates XML structure in a Oldschool way and uses the content stored in ArrayList<String[]>
     *
     * @return an XML document.
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public Document generateRandomProductXML() throws javax.xml.parsers.ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element rootElement = doc.createElement(XMLBuilder.ROOT);
        for (int i = 1; i < this.fileColumns.size(); i++) {
            Element rowElement = doc.createElement(XMLBuilder.REGISTRY);
            for (int j = 0; j < this.fileColumns.get(i).length; j++) {
                Element element = doc.createElement(this.fileColumns.get(0)[j]);
                element.setTextContent(this.fileColumns.get(i)[j]);
                rowElement.appendChild(element);
            }
            rootElement.appendChild(rowElement);
        }
        doc.appendChild(rootElement);
        return doc;
    }

    /**
     * Reads CSV files and extracts the content into String[] and then collect them into an arrayList.
     * CsvParser parses CSV files whether its separators are colons, semicolons or tabs.
     *
     * @param path where the CSV file is located.
     * @throws IOException
     */
    public void readFile(String path) throws IOException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setDelimiterDetectionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        this.fileColumns = (ArrayList<String[]>) parser.parseAll(new FileReader(path));
    }

    /**
     * Save documents with .xml extension.
     *
     * @param path      where the XML file is going to be saved.
     * @param document  target file.
     * @param showOuput if it is true it will show the XML file in Run.
     * @return XML file.
     * @throws javax.xml.transform.TransformerException
     */
    public File saveDocumentAsFile(String path, Document document, boolean showOuput)
            throws javax.xml.transform.TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        File file = new File(path);
        file.getParentFile().mkdirs();
        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);
        if (showOuput) {
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        }
        return file;
    }
}

