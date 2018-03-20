package altlombardisch.siglum;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XmlAdapter implementation to convert siglum types to Strings.
 */
public class SiglumTypeAdapter extends XmlAdapter<String, SiglumTypes.Type> {
    /**
     * Converts a User to a String.
     * 
     * @param siglumType
     *            the siglum type to convert
     * @return A string representation of a siglum type.
     * @throws Exception
     */
    @Override
    public String marshal(SiglumTypes.Type siglumType) throws Exception {
        return siglumType.toString();
    }

    /**
     * Converts a String to null.
     * 
     * @param string
     *            string representation of a siglum type
     * @return Null.
     * @throws Exception
     */
    @Override
    public SiglumTypes.Type unmarshal(String string) throws Exception {
        return null;
    }
}
