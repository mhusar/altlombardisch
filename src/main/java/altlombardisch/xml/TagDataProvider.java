package altlombardisch.xml;

import altlombardisch.xml.document.XmlDocumentDefinition;
import org.apache.wicket.Component;

/**
 * A provider for tag data. A user of this interface should provide data to
 * initialize tag data.
 * 
 * @see XmlHelper#getDocumentData(XmlDocumentDefinition)
 */
public interface TagDataProvider {
    /**
     * Initializes data that is needed to create clickable tags with attributes.
     * 
     * @param component
     *            component the tag data is attached to
     * @param documentDefinition
     *            document definition used as source for tag data
     */
    public void initializeTagData(Component component, XmlDocumentDefinition documentDefinition);
}
