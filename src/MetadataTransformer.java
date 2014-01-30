package com.salesforce;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MetadataTransformer {
	
	String filePath;
	String packageName;
	String versionType;
	String newVersionNumber;
	
	//constructor
	public MetadataTransformer(String filepath, String packageName, String versionType, String newVersionNumber){
		this.filePath 			= filepath;
		this.packageName 		= packageName;
		this.versionType 		= versionType;
		this.newVersionNumber 	= newVersionNumber;
	}
	
	private void modifyVersion(String filePath, DocumentBuilder docBuilder){
		try {
			
			//load document
			Document doc = docBuilder.parse(filePath);
			Node elementNode = doc.getFirstChild();
			
			//get all packages
			NodeList packages = doc.getElementsByTagName("package");
			
			//for each package
			for(int i=0;i<packages.getLength();i++){
				
				//get package name
				Node aPackage = packages.item(i);
				NodeList packageDefinition = aPackage.getChildNodes();
				
				//check if its the required package
				boolean nameMatches = false;
				for(Integer j=0;j<packageDefinition.getLength();j++){
					Node packageDefNode = packageDefinition.item(j);
					if("name".equalsIgnoreCase(packageDefNode.getNodeName()) && packageName.equals(packageDefNode.getTextContent())){
						nameMatches = true;
						break;
					}
				}
				
				//if yes, replace the version number
				if(nameMatches){
					for(Integer j=0;j<packageDefinition.getLength();j++){
						Node packageDefNode = packageDefinition.item(j);
						if(versionType.equalsIgnoreCase(packageDefNode.getNodeName())){
							packageDefNode.setTextContent(newVersionNumber);
						}
					}
				}
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
		
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
			
			System.out.println("Done..."+ filePath);
			
		} 
		catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processMetaFiles(){
		File folder = new File(filePath);
		File[] folderList = folder.listFiles();
		
		DocumentBuilder docBuilder = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(File f : folderList){
			if(f.isFile() && f.getName().contains("meta.xml")){
				modifyVersion(filePath+"\\"+f.getName(),docBuilder );
			}
		}
	}
}
