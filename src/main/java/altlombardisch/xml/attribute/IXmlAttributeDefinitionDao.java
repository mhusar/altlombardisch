package altlombardisch.xml.attribute;

import java.util.List;

import altlombardisch.data.IDao;
import altlombardisch.xml.tag.XmlTagDefinition;

/**
 * Defines an attribute definition DAO by extending interface IDao.
 */
public interface IXmlAttributeDefinitionDao extends IDao<XmlAttributeDefinition> {
    /**
     * Returns the matching attribute definition for a given ID.
     *
     * @param id the ID of a attribute definition
     * @return The matching attribute definition or null.
     */
    XmlAttributeDefinition findById(Integer id);

    /**
     * Returns the matching attribute definition for a given name.
     * 
     * @param parent
     *            the parent tag definition
     * @param name
     *            the name of an attribute definition
     * @return The matching attribute definition or null.
     */
    XmlAttributeDefinition findByName(XmlTagDefinition parent, String name);

    /**
     * Returns the fist attribute definition.
     * 
     * @param parent
     *            the parent tag definition
     * @return An attribute definition or null.
     */
    XmlAttributeDefinition findFirst(XmlTagDefinition parent);

    /**
     * Finds all available attribute definitions.
     * 
     * @param parent
     *            the parent tag definition
     * @return A list of attribute definitions.
     */
    List<XmlAttributeDefinition> findAll(XmlTagDefinition parent);
}
