package altlombardisch.siglum;

/**
 * Class defining allowed types of siglums.
 */
public abstract class SiglumTypes {
    /**
     * A type for primary sources.
     */
    public static final String PRIMARY = "PRIMARY";

    /**
     * A type for secondary sources.
     */
    public static final String SECONDARY = "SECONDARY";

    /**
     * A type for tertiary sources.
     */
    public static final String TERTIARY = "TERTIARY";

    /**
     * Allowed types of siglums.
     */
    public static enum Type {
        PRIMARY, SECONDARY, TERTIARY
    };
}
