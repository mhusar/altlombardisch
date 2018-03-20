package altlombardisch.xml.tag;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import altlombardisch.xml.attribute.XmlAttributeDefinition;
import altlombardisch.xml.document.XmlDocumentDefinition;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * Represents the definition of a XML tag.
 */
@BatchSize(size = 20)
@DynamicUpdate
@Entity
@OptimisticLocking(type = OptimisticLockType.VERSION)
@SelectBeforeUpdate
@Table(name = "xml_tag_definition", indexes = {
        @Index(columnList = "uuid", unique = true),
        @Index(columnList = "document_definition_id, name", unique = true) })
public class XmlTagDefinition implements Serializable {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID associated with a tag definition.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * A UUID used to distinguish tag definitions.
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
     * Document definition a tag definition belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "document_definition_id")
    private XmlDocumentDefinition documentDefinition;

    /**
     * Name of a defined tag.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Defines if a defined tag is self-closing.
     */
    @Column(name = "is_self_closing")
    private Boolean selfClosing;

    /**
     * A list of definitions for child attributes.
     */
    @OneToMany(mappedBy = "tagDefinition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("name")
    private List<XmlAttributeDefinition> attributeDefinitions;

    /**
     * Creates an instance of a XML tag definition.
     */
    public XmlTagDefinition() {
    }

    /**
     * Returns the ID associated with a tag definition.
     * 
     * @return Primary key of a tag definition.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of a tag definition.
     * 
     * @param id
     *            the ID of a tag definition
     */
    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the UUID of a tag definition.
     * 
     * @return UUID of a tag definition.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of a tag definition.
     * 
     * @param uuid
     *            the UUID of a tag definition
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns the version of a tag definition.
     * 
     * @return Version number of a tag definition.
     */
    @SuppressWarnings("unused")
    private Long getVersion() {
        return version;
    }

    /**
     * Sets the version number of a tag definition.
     * 
     * @param version
     *            version number of a tag definition
     */
    @SuppressWarnings("unused")
    private void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Returns the definition of a parent XML document.
     * 
     * @return Definition of a XML document.
     */
    public XmlDocumentDefinition getDocumentDefinition() {
        return documentDefinition;
    }

    /**
     * Sets the definition of a parent XML document.
     * 
     * @param documentDefinition
     *            definition of a XML document
     */
    public void setDocumentDefinition(XmlDocumentDefinition documentDefinition) {
        this.documentDefinition = documentDefinition;
    }

    /**
     * Returns the name of a defined tag.
     * 
     * @return Name of a defined tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a defined tag.
     * 
     * @param name
     *            the name of a defined tag
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the self-closing state of a defined tag.
     * 
     * @return Self-closing state of a defined tag.
     */
    public Boolean getSelfClosing() {
        return selfClosing;
    }

    /**
     * Sets a defined tag as self-closing or not self-closing.
     * 
     * @param selfClosing
     *            self-closing state of a defined tag
     */
    public void setSelfClosing(Boolean selfClosing) {
        this.selfClosing = selfClosing;
    }

    /**
     * Returns the definitions for child attributes.
     * 
     * @return A list of attribute definitions.
     */
    public List<XmlAttributeDefinition> getAttributeDefinitions() {
        return attributeDefinitions;
    }

    /**
     * Sets the definitions for child attributes.
     * 
     * @param attributeDefinitions
     *            a list of attribute definitions
     */
    public void setAttributeDefinitions(
            List<XmlAttributeDefinition> attributeDefinitions) {
        this.attributeDefinitions = attributeDefinitions;
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
        if (object == null || !(object instanceof XmlTagDefinition))
            return false;

        XmlTagDefinition definition = (XmlTagDefinition) object;

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
