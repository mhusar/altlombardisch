package altlombardisch.user;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XmlAdapter implementation to convert Users to Strings.
 */
public class UserAdapter extends XmlAdapter<String, User> {
    /**
     * Converts a User to a String.
     * 
     * @param user
     *            the user to convert
     * @return A string representation of a user.
     * @throws Exception
     */
    @Override
    public String marshal(User user) throws Exception {
        return user.getRealName();
    }

    /**
     * Converts a String to null.
     * 
     * @param string
     *            string representation of a user
     * @return Null.
     * @throws Exception
     */
    @Override
    public User unmarshal(String string) throws Exception {
        return null;
    }
}
