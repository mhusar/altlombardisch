package altlombardisch.xml.document;

import altlombardisch.xml.tag.XmlTagDefinition;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Represents the definition of a XML document.
 */
@BatchSize(size = 20)
@DynamicUpdate
@Entity
@OptimisticLocking(type = OptimisticLockType.VERSION)
@SelectBeforeUpdate
@Table(name = "xml_document_definition", indexes = {
        @Index(columnList = "uuid", unique = true),
        @Index(columnList = "identifier", unique = true) })
public class XmlDocumentDefinition implements Serializable {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID associated with a document definition.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * A UUID used to distinguish document definitions.
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
     * Identifier of a document definition.
     */
    @Column(name = "identifier", nullable = false)
    private String identifier;

    /**
     * Root element of a defined document.
     */
    @Column(name = "root_element", nullable = false)
    private String rootElement;

    /**
     * Schema definition which describes the structure of a XML document.
     */
    @Column(name = "\"schema\"", columnDefinition = "TEXT", length = 65535)
    private String schema;

    /**
     * A list of definitions for child tags.
     */
    @OneToMany(mappedBy = "documentDefinition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("name")
    private List<XmlTagDefinition> tagDefinitions;

    /**
     * Creates an instance of a XML document definition.
     */
    public XmlDocumentDefinition() {
    }

    /**
     * Returns the ID associated with a document definition.
     * 
     * @return Primary key of a document definition.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of a document definition.
     * 
     * @param id
     *            the ID of a document definition
     */
    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the UUID of a document definition.
     * 
     * @return UUID of a document definition.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of a document definition.
     * 
     * @param uuid
     *            the UUID of a document definition
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns the version of a document definition.
     * 
     * @return Version number of a document definition.
     */
    @SuppressWarnings("unused")
    private Long getVersion() {
        return version;
    }

    /**
     * Sets the version number of a document definition.
     * 
     * @param version
     *            version number of a document definition
     */
    @SuppressWarnings("unused")
    private void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Returns the identifier of a document definition.
     * 
     * @return Identifier of a document definition.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier of a document definition.
     * 
     * @param identifier
     *            the identifier of a document definition
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Return the root element of a defined document.
     * 
     * @return Root element of a defined document.
     */
    public String getRootElement() {
        return rootElement;
    }

    /**
     * Sets the root element of a defined document.
     * 
     * @param rootElement
     *            the root element of a defined document
     */
    public void setRootElement(String rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * Returns the schema of a document definition.
     * 
     * @return Schema of a document definition.
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the schema of a document definition.
     * 
     * @param schema
     *            the schema of a document definition
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Returns the definitions for child tags.
     * 
     * @return A list of tag definitions.
     */
    public List<XmlTagDefinition> getTagDefinitions() {
        return tagDefinitions;
    }

    /**
     * Sets the definitions for child tags.
     * 
     * @param tagDefinitions
     *            a list of tag definitions
     */
    public void setTagDefinitions(List<XmlTagDefinition> tagDefinitions) {
        this.tagDefinitions = tagDefinitions;
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
        if (object == null || !(object instanceof XmlDocumentDefinition))
            return false;

        XmlDocumentDefinition definition = (XmlDocumentDefinition) object;

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
