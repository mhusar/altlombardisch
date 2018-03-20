package altlombardisch.siglum;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XmlAdapter implementation to convert Siglums to Strings.
 */
public class SiglumAdapter extends XmlAdapter<String, Siglum> {
    /**
     * Converts a User to a String.
     * 
     * @param siglum
     *            the siglum to convert
     * @return A string representation of a siglum.
     * @throws Exception
     */
    @Override
    public String marshal(Siglum siglum) throws Exception {
        return siglum.getName();
    }

    /**
     * Converts a String to null.
     * 
     * @param string
     *            string representation of a siglum
     * @return Null.
     * @throws Exception
     */
    @Override
    public Siglum unmarshal(String string) throws Exception {
        return null;
    }
}
