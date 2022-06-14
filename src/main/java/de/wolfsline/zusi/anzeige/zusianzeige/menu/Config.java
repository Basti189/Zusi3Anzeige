package de.wolfsline.zusi.anzeige.zusianzeige.menu;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Config {
	
	private static String mNameApp = "ZusiAnzeige";

	public static void save(String name, double x, double y) {
		File theDir = new File("config");
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		}
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			  DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			  Document doc = docBuilder.newDocument();

			  Element rootElement = doc.createElement(mNameApp);
			  doc.appendChild(rootElement);

			  Element staff = doc.createElement("Config");
			  rootElement.appendChild(staff);

			  Element firstname = doc.createElement("position_x");
			  firstname.appendChild(doc.createTextNode(String.valueOf(x)));
			  staff.appendChild(firstname);

			  Element lastname = doc.createElement("position_y");
			  lastname.appendChild(doc.createTextNode(String.valueOf(y)));
			  staff.appendChild(lastname);
			  
			  TransformerFactory transformerFactory = TransformerFactory.newInstance();
			  Transformer transformer = transformerFactory.newTransformer();
			  transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			  transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			  DOMSource source = new DOMSource(doc);

			  StreamResult result =  new StreamResult(new File("config/config_" + name + ".xml"));
			  transformer.transform(source, result);
			  
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double[] load(String name) {
		try {
			 File inputFile = new File("config/config_" + name + ".xml");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         
	         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	         NodeList nList = doc.getElementsByTagName("Config");
	         for (int temp = 0 ; temp < nList.getLength() ; temp++) {
	             Node nNode = nList.item(temp);
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            	 Element eElement = (Element) nNode;
	            	 String posX = eElement.getElementsByTagName("position_x").item(0).getChildNodes().item(0).getNodeValue();
	            	 String posY = eElement.getElementsByTagName("position_y").item(0).getChildNodes().item(0).getNodeValue();
	            	 System.out.println("X: " + posX);
	            	 System.out.println("Y: " + posY);
	            	 return new double[] {Double.valueOf(posX), Double.valueOf(posY)};
	             }
	         }
		} catch (Exception e) {
			
		}
		return new double[]{0.0D, 0.0D};
	}
	
}
