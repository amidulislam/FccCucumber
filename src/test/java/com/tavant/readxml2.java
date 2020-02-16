package com.tavant;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

//import com.journaldev.xml.Employee;

public class readxml2 {

	public static void main(String[] args) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			MyHandler handler = new MyHandler();
			saxParser.parse(new File("D:/feb/10thfeb/webservicesample/src/main/resources/testxml.xml"), handler);
			// Get Employees list
			List<Employee> empList = handler.getEmpList();
			// print employee information
			for (Employee emp : empList)
				System.out.println(emp);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
