package altlombardisch.xml.document;

import java.util.List;

import altlombardisch.data.IDao;

/**
 * Defines a document definition DAO by extending interface IDao.
 */
public interface IXmlDocumentDefinitionDao extends IDao<XmlDocumentDefinition> {
    /**
     * Initializes needed document definitions.
     */
    void initialize();

    /**
     * Returns the matching document definition for a given ID.
     *
     * @param id the ID of a document definition
     * @return The matching document definition or null.
     */
    XmlDocumentDefinition findById(Integer id);

    /**
     * Returns the matching document definition for a given identifier.
     * 
     * @param identifier
     *            the identifier of a document definition
     * @return The matching document definition or null.
     */
    XmlDocumentDefinition findByIdentifier(String identifier);

    /**
     * Returns the fist document definition.
     * 
     * @return A document definition or null.
     */
    XmlDocumentDefinition findFirst();

    /**
     * Finds all available document definitions.
     * 
     * @return A list of document definitions.
     */
    List<XmlDocumentDefinition> findAll();
}
