package altlombardisch.siglum;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a siglum of a bibliography.
 */
@BatchSize(size = 20)
@DynamicUpdate
@Entity
@OptimisticLocking(type = OptimisticLockType.VERSION)
@SelectBeforeUpdate
@Table(name = "siglum", indexes = { @Index(columnList = "uuid", unique = true),
        @Index(columnList = "name", unique = true) })
public class Siglum implements Serializable {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID associated with a siglum.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * A UUID used to distinguish siglums.
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
     * Name of a siglum.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Name of a siglum as tagged variant.
     */
    @Column(name = "tagged_name", nullable = false)
    private String taggedName;

    /**
     * Description text of a siglum.
     */
    @Column(name = "text", columnDefinition = "TEXT", length = 65535)
    private String text;

    /**
     * Type of a siglum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SiglumType.Type type;

    /**
     * Creates an instance of a siglum.
     */
    public Siglum() {
    }

    /**
     * Returns the ID associated with a siglum.
     * 
     * @return Primary key of a siglum.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of a siglum.
     * 
     * @param id
     *            the ID of a siglum
     */
    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the UUID of a siglum.
     * 
     * @return UUID of a siglum.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of a siglum.
     * 
     * @param uuid
     *            the UUID of a siglum
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns the version of a siglum.
     * 
     * @return Version number of a siglum.
     */
    @SuppressWarnings("unused")
    private Long getVersion() {
        return version;
    }

    /**
     * Sets the version number of a siglum.
     * 
     * @param version
     *            version number of a siglum
     */
    @SuppressWarnings("unused")
    private void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Returns the name of a siglum.
     * 
     * @return Name of a siglum.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a siglum.
     * 
     * @param name
     *            the name of a siglum
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of a siglum as tagged variant.
     * 
     * @return Name of a siglum as tagged variant.
     */
    public String getTaggedName() {
        return taggedName;
    }

    /**
     * Sets the name of a siglum as tagged variant.
     * 
     * @param taggedName
     *            the name of a siglum as tagged variant
     */
    public void setTaggedName(String taggedName) {
        this.taggedName = taggedName;
    }

    /**
     * Returns the description text of a siglum.
     * 
     * @return Description text of a siglum.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the description text of a siglum.
     * 
     * @param text
     *            the description text of a siglum
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the type of a siglum.
     * 
     * @return Type of a siglum.
     */
    public SiglumType.Type getType() {
        return type;
    }

    /**
     * Sets the type of a siglum.
     * 
     * @param type type of a siglum
     */
    public void setType(SiglumType.Type type) {
        this.type = type;
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
        if (object == null || !(object instanceof Siglum))
            return false;

        Siglum siglum = (Siglum) object;

        if (!(uuid instanceof String)) {
            uuid = UUID.randomUUID().toString();
        }

        return uuid.equals(siglum.getUuid());
    }

    /**
     * Returns a hash code value for a siglum.
     * 
     * @return A hash code value for a siglum.
     */
    @Override
    public int hashCode() {
        if (!(uuid instanceof String)) {
            uuid = UUID.randomUUID().toString();
        }

        return uuid.hashCode();
    }
}
