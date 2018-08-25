/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exercise4;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
        
        static ArrayList<String> authorsList = new ArrayList<String>();
        static ArrayList<String> booksList = new ArrayList<String>();
    
	public static void parse() {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	try {
                
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("exercise-1.xml");
		NodeList authorList = doc.getElementsByTagName("author");
		NodeList bookList = doc.getElementsByTagName("book");
		for(int i=0;i<authorList.getLength();i++) {
			Node a = authorList.item(i);
			if(a.getNodeType()==Node.ELEMENT_NODE) {
				Element person = (Element) a;
				String id = person.getAttribute("id");
				NodeList nameList = person.getChildNodes();
				for(int j=0;j<nameList.getLength();j++) {
					Node n = nameList.item(j);
					if(n.getNodeType()==Node.ELEMENT_NODE) {
						Element name = (Element) n;
						String tmp = "Person "+id+":"+name.getTagName()+"="+name.getTextContent();
                                                authorsList.add(tmp);
					}
				}
			}
		}
		for(int i=0;i<bookList.getLength();i++) {
			Node b = bookList.item(i);
			if(b.getNodeType()==Node.ELEMENT_NODE) {
				Element book = (Element) b;
				String id = book.getAttribute("id");
				NodeList bookInfoList = book.getChildNodes();
				for(int j=0;j<bookInfoList.getLength();j++) {
					Node k = bookInfoList.item(j);
					if(k.getNodeType()==Node.ELEMENT_NODE) {
						Element info = (Element) k;
						String tmp="Book "+id+":"+info.getTagName()+"="+info.getTextContent();
                                                booksList.add(tmp);
                                        }
				}
			}
		}
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

}
