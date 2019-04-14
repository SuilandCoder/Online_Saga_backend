package com.liber.sun.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.*;

/**
 * Created by sunlingzhi on 2017/10/19.
 */
public class XmlUtils {
    public static Document parseString(String xmlString) throws DocumentException {
        Document document = DocumentHelper.parseText(xmlString);
        return document;
    }

    public static Document parseFile(File xmlFile) throws DocumentException {
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(xmlFile);
        return document;
    }

    public static String toString(Document doc, String encoding) {
        if (null != doc) {
            OutputFormat outputFormat = new OutputFormat();
            outputFormat.setEncoding(encoding);
            StringWriter stringWriter = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
            try {
                xmlWriter.write(doc);
                return stringWriter.toString();
            } catch (IOException ex) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); //no declaration

            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

    public static boolean saveDocToXMLFile(String SavePath, Document doc)throws IOException {
        if (SavePath.equals("") || doc == null)
            return false;

        FileOutputStream out = new FileOutputStream(SavePath);
        // 指定文本的写出的格式：
        OutputFormat format = OutputFormat.createPrettyPrint(); // 漂亮格式：有空格换行
        format.setEncoding("UTF-8");

        XMLWriter writer = new XMLWriter(out, format);
        writer.write(doc);
        writer.close();

        return true;
    }
}
