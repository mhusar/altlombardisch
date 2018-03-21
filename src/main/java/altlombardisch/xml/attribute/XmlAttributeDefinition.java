package altlombardisch.xml.attribute;

import altlombardisch.xml.tag.XmlTagDefinition;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * Represents the definition of a XML attribute.
 */
@BatchSize(size = 20)
@DynamicUpdate
@Entity
@OptimisticLocking(type = OptimisticLockType.VERSION)
@SelectBeforeUpdate
@Table(name = "xml_attribute_definition", indexes = {
        @Index(columnList = "uuid", unique = true),
        @Index(columnList = "tag_definition_id, name", unique = true) })
public class XmlAttributeDefinition implements Serializable {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID associated with an attribute definition.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * A UUID used to distinguish attribute definitions.
     */
    @Column(name = "uuid")
    private String uuid;

    /**
     * Version number field used for optimistic locking.
     */
    @Column(name = "version")
    @Version
    private Long version;

    /**
     * Tag definition an attribute definition belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "tag_definition_id")
    private XmlTagDefinition tagDefinition;

    /**
     * Name of a defined attribute.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Defines if a defined attribute is required.
     */
    @Column(name = "is_required")
    private Boolean required;

    /**
     * Creates an instance of a XML attribute definition.
     */
    public XmlAttributeDefinition() {
    }

    /**
     * Returns the ID associated with an attribute definition.
     * 
     * @return Primary key of an attribute definition.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of an attribute definition.
     * 
     * @param id
     *            the ID of an attribute definition
     */
    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the UUID of an attribute definition.
     * 
     * @return UUID of an attribute definition.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of an attribute definition.
     * 
     * @param uuid
     *            the UUID of an attribute definition
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns the version of an attribute definition.
     * 
     * @return Version number of an attribute definition.
     */
    @SuppressWarnings("unused")
    private Long getVersion() {
        return version;
    }

    /**
     * Sets the version number of an attribute definition.
     * 
     * @param version
     *            version number of an attribute definition
     */
    @SuppressWarnings("unused")
    private void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Returns the definition of a parent tag.
     * 
     * @return Definition of a tag.
     */
    public XmlTagDefinition getTagDefinition() {
        return tagDefinition;
    }

    /**
     * Sets the definition of a parent tag.
     * 
     * @param tagDefinition
     *            definition of a tag
     */
    public void setTagDefinition(XmlTagDefinition tagDefinition) {
        this.tagDefinition = tagDefinition;
    }

    /**
     * Returns the name of a defined attribute.
     * 
     * @return Name of a defined attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a defined attribute.
     * 
     * @param name
     *            the name of a defined attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the required state of a defined attribute.
     * 
     * @return Required state of a defined attribute.
     */
    public Boolean getRequired() {
        return required;
    }

    /**
     * Sets a defined attribute as required or not required.
     * 
     * @param required
     *            required state of a defined attribute
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * Indicates if some other object is equal to this one.
     * 
     * @param object
     *            the reference object with which to compare
     * @return True if this object is the same as the object argument; false
     *         otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || !(object instanceof XmlAttributeDefinition))
            return false;

        XmlAttributeDefinition definition = (XmlAttributeDefinition) object;

        if (!(uuid instanceof String)) {
            uuid = UUID.randomUUID().toString();
        }

        return uuid.equals(definition.getUuid());
    }

    /**
     * Returns a hash code value for a document definition.
     * 
     * @return A hash code value for a document definition.
     */
    @Override
    public int hashCode() {
        if (!(uuid instanceof String)) {
            uuid = UUID.randomUUID().toString();
        }

        return uuid.hashCode();
    }
}
