package cz.softinel.retra.jiraintegration.soap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DocumentType;

/**
 * Helping the marshaller with utility methods.
 * @author SzalaiErik
 */
@Deprecated
public class MarshallerHelper {
	
	/**
	 * Get the node with the given name from the child nodelist.
	 * @param children
	 * @param nodeName
	 * @return
	 */
	public static Node getNodeByNodeName(NodeList children, String nodeName) {
		for (int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals(nodeName)) {
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Return the sibling with the given name relative to the given root.
	 * @param root
	 * @param nodeName
	 * @return
	 */
	public static Node getSiblingByName(Node root, String nodeName) {
		Node sibling = root.getNextSibling();
		while(!sibling.getNodeName().equals(nodeName)) {
			if (sibling.getNextSibling() != null) {
				sibling = sibling.getNextSibling();
			} else {
				return null;
			}
		}
		return sibling;
	}
	
	/**
	 * This will print the node contents to the System.out. Used for test purposes.
	 * @param root
	 */
	public static void printNode(Node node, String indent) {
		switch (node.getNodeType()) {
		case Node.DOCUMENT_NODE:
			System.out.println(indent + "<?xml version=\"1.0\"?>");

			NodeList nodes = node.getChildNodes();

			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					printNode(nodes.item(i), "");
				}
			}

			break;

		case Node.ELEMENT_NODE:

			String name = node.getNodeName();
			System.out.print(indent + "<" + name);

			NamedNodeMap attributes = node.getAttributes();

			for (int i = 0; i < attributes.getLength(); i++) {
				Node current = attributes.item(i);
				System.out.print(" " + current.getNodeName() +
				        "=\"" + current.getNodeValue() + "\"");
			}

			System.out.println(">");

			NodeList children = node.getChildNodes();

			if (children != null) {
				for (int i = 0; i < children.getLength();
					        i++) {
					printNode(children.item(i),
					        indent + "  ");
				}
			}

			System.out.println(indent + "</" + name + ">");

			break;

		case Node.TEXT_NODE:
		case Node.CDATA_SECTION_NODE:
			System.out.println(indent + node.getNodeValue());

			break;

		case Node.PROCESSING_INSTRUCTION_NODE:
			System.out.println(indent + "<?" + node.getNodeName() +
			        " " + node.getNodeValue() + " ?>");

			break;

		case Node.ENTITY_REFERENCE_NODE:
			System.out.println("&" + node.getNodeName() + ";");

			break;

		case Node.DOCUMENT_TYPE_NODE:

			DocumentType docType = (DocumentType) node;
			System.out.print("<!DOCTYPE " + docType.getName());

			if (docType.getPublicId() != null) {
				System.out.print("PUBLIC \"" +
				        docType.getPublicId() + "\"");
			} else {
				System.out.print(" SYSTEM ");
			}

			System.out.println("\"" + docType.getSystemId() +
			        "\" >");

			break;
		}

	}

}
