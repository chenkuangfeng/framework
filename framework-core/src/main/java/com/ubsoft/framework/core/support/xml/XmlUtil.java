package com.ubsoft.framework.core.support.xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlUtil {

	private static final String ENCODE = "UTF-8";

	@SuppressWarnings("unchecked")
	public static String toXML(Object obj) throws Exception {

		JAXBContext context = JAXBContext.newInstance(obj.getClass());

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xm头声明信息
		StringWriter writer = new StringWriter();
		// XMLOutputFactory xof = XMLOutputFactory.newInstance();
		// XMLStreamWriter streamWriter = xof.createXMLStreamWriter(
		// System.out );
		// CDataXMLStreamWriter cdataStreamWriter = new
		// CDataXMLStreamWriter( streamWriter );
		// marshaller.marshal( test, cdataStreamWriter );
		marshaller.marshal(obj, writer);
		return writer.toString();

	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXML(String xml, Class<T> valueType) throws Exception {

		JAXBContext context = JAXBContext.newInstance(valueType);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		return (T) unmarshaller.unmarshal(new StringReader(xml));

	}

	

	

	@SuppressWarnings("unchecked")
	public static Map xml2map(String xmlStr, boolean needRootKey) throws Exception {
		Document doc = DocumentHelper.parseText(xmlStr);
		Element root = doc.getRootElement();
		Map<String, Object> map = (Map<String, Object>) xml2map(root);
		if (root.elements().size() == 0 && root.attributes().size() == 0) {
			return map;
		}
		if (needRootKey) {
			// 在返回的map里加根节点键（如果需要）
			Map<String, Object> rootMap = new HashMap<String, Object>();
			rootMap.put(root.getName(), map);
			return rootMap;
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map xml2map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = xml2map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!(obj instanceof List)) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj instanceof List) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!(obj instanceof List)) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj instanceof List) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}

	public static String map2xml(Map<String, Object> map, String rootName) throws Exception {
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement(rootName);
		doc.add(root);
		map2xml(map, root);
		// System.out.println(doc.asXML());
		// System.out.println(formatXml(doc));
		return doc.asXML();
	}

	/**
	 * 
	 * map转xml map中含有根节点的键
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String map2xml(Map<String, Object> map) throws Exception {
		Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
		if (entries.hasNext()) { // 获取第一个键创建根节点
			Map.Entry<String, Object> entry = entries.next();
			Document doc = DocumentHelper.createDocument();
			Element root = DocumentHelper.createElement(entry.getKey());
			doc.add(root);
			map2xml((Map) entry.getValue(), root);
			return doc.asXML();
		}
		return null;
	}

	private static Element map2xml(Map<String, Object> map, Element body) {
		Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, Object> entry = entries.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (key.startsWith("@")) { // 属性
				body.addAttribute(key.substring(1, key.length()), value.toString());
			} else if (key.equals("#text")) { // 有属性时的文本
				body.setText(value.toString());
			} else {
				if (value instanceof List) {
					List list = (List) value;
					Object obj;
					for (int i = 0; i < list.size(); i++) {
						obj = list.get(i);
						// list里是map或String，不会存在list里直接是list的，
						if (obj instanceof Map) {
							Element subElement = body.addElement(key);
							map2xml((Map) list.get(i), subElement);
						} else {
							body.addElement(key).setText((String) list.get(i));
						}
					}
				} else if (value instanceof Map) {
					Element subElement = body.addElement(key);
					map2xml((Map) value, subElement);
				} else {
					body.addElement(key).setText(value.toString());
				}
			}

		}
		return body;
	}

	public static String object2XML(Object obj, String outFileName) throws FileNotFoundException {
		// 构造输出XML文件的字节输出流
		File outFile = new File(outFileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
		XMLEncoder xmlEncoder = new XMLEncoder(bos);// 构造一个XML编辑器
		xmlEncoder.writeObject(obj);// 使用XML编码器写对象
		xmlEncoder.close();// 关闭编码器
		return outFile.getAbsolutePath();
	}

	public static Object xml2Object(String inFileName) throws FileNotFoundException {
		// 构造输入的XML文件的字节输入流
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFileName));
		XMLDecoder xmlDecoder = new XMLDecoder(bis);// 构造一个XML解码器
		Object obj = xmlDecoder.readObject();// 使用XML解码器读对象
		xmlDecoder.close();// 关闭解码器
		return obj;
	}

	public static String formatXml(String xmlStr) throws Exception {
		Document document = DocumentHelper.parseText(xmlStr);
		return formatXml(document);
	}

	public static String formatXml(Document document) throws Exception {
		// 格式化输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// format.setEncoding("UTF-8");
		StringWriter writer = new StringWriter();
		// 格式化输出流
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		// 将document写入到输出流
		xmlWriter.write(document);
		xmlWriter.close();
		return writer.toString();
	}
}
