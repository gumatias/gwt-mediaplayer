/**
 * 
 */
package net.sf.video4j.gwt.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author luc
 *
 */
public class VASTParser {

	
	public void parse(String pXML) {
		try {
		    // parse the XML document into a DOM
		    Document oDoc = XMLParser.parse(pXML);
		    
		    Node n = oDoc.getFirstChild();
		    if (n.getNodeName().indexOf("VAST") >= 0) {
		    	Node oVN = n.getAttributes().getNamedItem("version");
		    	String oVersion = oVN.getNodeValue();
		    	if (oVersion.startsWith("2.")) {
		    		parse2x(n);
		    	}
		    } else {
		    	parse1x(n);
		    }
		    
		    /*
		    // find the sender's display name in an attribute of the <from> tag
		    Node fromNode = messageDom.getElementsByTagName("from").item(0);
		    String from = ((Element)fromNode).getAttribute("displayName");
		    fromLabel.setText(from);

		    // get the subject using Node's getNodeValue() function
		    String subject = messageDom.getElementsByTagName("subject").item(0).getFirstChild().getNodeValue();
		    subjectLabel.setText(subject);

		    // get the message body by explicitly casting to a Text node
		    Text bodyNode = (Text)messageDom.getElementsByTagName("body").item(0).getFirstChild();
		    String body = bodyNode.getData();
		    bodyLabel.setText(body);
			*/
		  } catch (DOMException e) {
		    Window.alert("Could not parse XML document.");
		  }
	}

	private void parse1x(Node pVAST) {
		// TODO Auto-generated method stub
		
	}

	private void parse2x(Node pVAST) {
		NodeList oChildren = pVAST.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			System.out.println("Found node : " + n.getNodeName());
		}
	}
}