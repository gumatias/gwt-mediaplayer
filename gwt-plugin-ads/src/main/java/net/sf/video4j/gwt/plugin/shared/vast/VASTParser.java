/**
 * 
 */
package net.sf.video4j.gwt.plugin.shared.vast;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author luc
 */
public class VASTParser implements IVASTParser {

	private Logger	mLogger	= Logger.getLogger(this.getClass().getName());

	@Override
	public VAST parse(String pXML) {
		VAST oVAST = new VAST();
		try {
			// parse the XML document into a DOM
			Document oDoc = XMLParser.parse(pXML);

			Node n = oDoc.getFirstChild();
			if (n.getNodeName().indexOf("VAST") >= 0) {
				Node oVN = n.getAttributes().getNamedItem("version");
				String oVersion = oVN.getNodeValue();
				if (oVersion.startsWith("2.")) return parse2x(n);
			} else {
				return parse1x(n);
			}

			/*
			 * // find the sender's display name in an attribute of the <from>
			 * tag Node fromNode =
			 * messageDom.getElementsByTagName("from").item(0); String from =
			 * ((Element)fromNode).getAttribute("displayName");
			 * fromLabel.setText(from);
			 * // get the subject using Node's getNodeValue() function String
			 * subject =
			 * messageDom.getElementsByTagName("subject").item(0).getFirstChild
			 * ().getNodeValue(); subjectLabel.setText(subject);
			 * // get the message body by explicitly casting to a Text node Text
			 * bodyNode =
			 * (Text)messageDom.getElementsByTagName("body").item(0).getFirstChild
			 * (); String body = bodyNode.getData(); bodyLabel.setText(body);
			 */
		} catch (DOMException e) {
			mLogger.log(Level.SEVERE, "Could not parse XML document.");
		}
		return oVAST;
	}

	private VAST parse1x(Node pVASTNode) {
		// TODO Auto-generated method stub
		mLogger.log(Level.SEVERE, "1x parser not yet implemented");
		return null;
	}

	private VAST parse2x(Node pVASTNode) {
		VAST20Parser oParser = new VAST20Parser();
		return oParser.parse(pVASTNode);
	}
}
