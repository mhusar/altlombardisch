package altlombardisch.siglum;

import java.util.Locale;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.hibernate.LazyInitializationException;

/**
 * A text field converting siglum names to siglums.
 */
public class SiglumTextField extends TextField<Siglum> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a siglum text field.
     * 
     * @param id
     *            ID of the text field
     */
    public SiglumTextField(String id) {
        super(id);
    }

    /**
     * Creates a siglum text field.
     * 
     * @param id
     *            ID of the text field
     * @param model
     *            data model of the text field
     */
    public SiglumTextField(String id, IModel<Siglum> model) {
        super(id, model);
    }

    /**
     * Returns a converter for siglum objects.
     * 
     * @param type
     *            class type of converted object
     * @return A converter for siglum objects.
     */
    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return new IConverter<C>() {
            /**
             * Determines if a deserialized file is compatible with this class.
             */
            private static final long serialVersionUID = 1L;

            /**
             * Converts a name string to a siglum object.
             * 
             * @param name
             *            name that is converted
             * @param locale
             *            locale used to convert the name
             * @return The converted object.
             * @throws ConversionException
             */
            @Override
            @SuppressWarnings("unchecked")
            public C convertToObject(String name, Locale locale)
                    throws ConversionException {
                Siglum siglum = new SiglumDao().findByName(name);

                if (siglum instanceof Siglum) {
                    return (C) siglum;
                } else {
                    return null;
                }
            }

            /**
             * Converts a siglum object to a name string.
             * 
             * @param siglum
             *            siglum that is converted
             * @param locale
             *            locale used to convert the siglum
             * @return The converted siglum.
             */
            @Override
            public String convertToString(C siglum, Locale locale) {
                try {
                    return ((Siglum) siglum).getName();
                } catch (LazyInitializationException e) {
                    Siglum castedSiglum = (Siglum) siglum;

                    new SiglumDao().refresh(castedSiglum);
                    return castedSiglum.getName();
                }
            }
        };
    }
}
