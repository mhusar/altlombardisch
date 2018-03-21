package altlombardisch.xml.tag;

import altlombardisch.data.IDao;
import altlombardisch.xml.document.XmlDocumentDefinition;

import java.util.List;

/**
 * Defines a tag definition DAO by extending interface IDao.
 */
public interface IXmlTagDefinitionDao extends IDao<XmlTagDefinition> {
    /**
     * Returns the matching tag definition for a given ID.
     *
     * @param id the ID of a tag definition
     * @return The matching tag definition or null.
     */
    XmlTagDefinition findById(Integer id);

    /**
     * Returns the matching tag definition for a given name.
     * 
     * @param parent
     *            the parent document definition
     * @param name
     *            the name of a tag definition
     * @return The matching tag definition or null.
     */
    XmlTagDefinition findByName(XmlDocumentDefinition parent, String name);

    /**
     * Returns the fist tag definition.
     * 
     * @param parent
     *            the parent document definition
     * @return A document definition or null.
     */
    XmlTagDefinition findFirst(XmlDocumentDefinition parent);

    /**
     * Finds all available tag definitions.
     * 
     * @param parent
     *            the parent document definition
     * @return A list of tag definitions.
     */
    List<XmlTagDefinition> findAll(XmlDocumentDefinition parent);
}
