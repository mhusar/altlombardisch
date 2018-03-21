package altlombardisch.xml;

import altlombardisch.xml.attribute.XmlAttributeDefinition;
import altlombardisch.xml.attribute.XmlAttributeDefinitionDao;
import altlombardisch.xml.document.XmlDocumentDefinition;
import altlombardisch.xml.tag.XmlTagDefinition;
import altlombardisch.xml.tag.XmlTagDefinitionDao;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.json.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * A helper class with methods and constants for XML-related tasks.
 */
public class XmlHelper {
    /**
     * A string to assemble an empty schema field with.
     */
    public static final String EMPTY_SCHEMA = "<?xml version=\"1.0\"?>\n"
            + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" "
            + "elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\">\n\n"
            + "</xs:schema>";

    /**
     * Returns the text content of a XML fragment string.
     * 
     * @param xmlText
     *            a XML fragment string
     * @return A text content representation of a XML fragment string or null.
     */
    public static String getTextContent(String xmlText) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        Document document = null;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(
                    "<document>" + xmlText + "</document>")));

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        if (document instanceof Document) {
            return document.getDocumentElement().getTextContent();
        } else {
            return null;
        }
    }

    /**
     * Returns a JsonObject with document data.
     * 
     * @param documentDefinition
     *            document definition used as source for document data
     * @return A JsonObject with document data.
     */
    public static JsonObject getDocumentData(
            XmlDocumentDefinition documentDefinition) {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("identifier", documentDefinition.getIdentifier())
                .add("tags", getTagData(documentDefinition)).build();

        return jsonObject;
    }

    /**
     * Returns a JsonArray with tag data of a document definition.
     * 
     * @param documentDefinition
     *            document definition used as source for tag data
     * @return A JsonArray with tag data.
     */
    private static JsonArray getTagData(XmlDocumentDefinition documentDefinition) {
        JsonArrayBuilder tagArrayBuilder = Json.createArrayBuilder();
        List<XmlTagDefinition> tagDefinitions = new XmlTagDefinitionDao()
                .findAll(documentDefinition);

        for (XmlTagDefinition tagDefinition : tagDefinitions) {
            JsonObjectBuilder tagObjectBuilder = Json.createObjectBuilder();
            JsonArray attributeData = getAttributeData(tagDefinition);

            tagObjectBuilder.add("name", tagDefinition.getName());
            tagObjectBuilder.add("selfClosing", tagDefinition.getSelfClosing());
            tagObjectBuilder.add("attributes", attributeData);
            tagArrayBuilder.add(tagObjectBuilder.build());
        }

        return tagArrayBuilder.build();
    }

    /**
     * Returns a JsonArray with attribute data of a tag definition.
     * 
     * @param tagDefinition
     *            tag definition used as source for attribute data
     * @return A JsonArray with attribute data.
     */
    private static JsonArray getAttributeData(XmlTagDefinition tagDefinition) {
        JsonArrayBuilder attributeArrayBuilder = Json.createArrayBuilder();
        List<XmlAttributeDefinition> attributeDefinitions = new XmlAttributeDefinitionDao()
                .findAll(tagDefinition);

        for (XmlAttributeDefinition attributeDefinition : attributeDefinitions) {
            JsonObjectBuilder attributeObjectBuilder = Json
                    .createObjectBuilder();

            attributeObjectBuilder.add("name", attributeDefinition.getName());
            attributeObjectBuilder.add("required",
                    attributeDefinition.getRequired());
            attributeArrayBuilder.add(attributeObjectBuilder.build());
        }

        return attributeArrayBuilder.build();
    }
}
