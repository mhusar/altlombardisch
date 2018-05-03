package altlombardisch.siglum;

import altlombardisch.siglum.text.SiglumTextPreviewPanel;
import altlombardisch.ui.xml.XmlEditor;
import altlombardisch.ui.xml.XmlTextField;
import altlombardisch.xml.document.XmlDocumentDefinition;
import altlombardisch.xml.document.XmlDocumentDefinitionDao;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A form for viewing siglums.
 */
public class SiglumViewForm extends Form<Siglum> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a siglum view form.
     * 
     * @param id
     *            ID of the view form
     * @param model
     *            siglum model that is viewed
     */
    public SiglumViewForm(String id, IModel<Siglum> model) {
        super(id, model);

        XmlDocumentDefinitionDao documentDefinitionDao = new XmlDocumentDefinitionDao();
        XmlDocumentDefinition fontMarkupDocumentDefinition = documentDefinitionDao
                .findByIdentifier("fontMarkup");
        XmlDocumentDefinition siglumTextMarkupDocumentDefinition = documentDefinitionDao
                .findByIdentifier("siglumTextMarkup");
        XmlTextField taggedNameTextField = new XmlTextField("taggedName",
                fontMarkupDocumentDefinition);
        XmlEditor textXmlEditor = new XmlEditor("text", new PropertyModel<String>(model, "text"),
                new Model<String>(new StringResourceModel("Siglum.text").getString()),
                siglumTextMarkupDocumentDefinition);
        ListChoice<SiglumType.Type> typeListChoice = new ListChoice<SiglumType.Type>(
                "type", new PropertyModel<SiglumType.Type>(getModelObject(),
                        "type"), new ArrayList<SiglumType.Type>(
                        Arrays.asList(SiglumType.Type.values())),
                new EnumChoiceRenderer<SiglumType.Type>(), 1);

        add(taggedNameTextField);
        add(textXmlEditor);
        add(typeListChoice);
        add(new ToIndexButton("toIndexButton"));
        add(new SiglumTextPreviewPanel(getModel()));

        taggedNameTextField.setRequired(true);
        textXmlEditor.setMaximumLength(5000);
        textXmlEditor.setReadOnly(true);
        textXmlEditor.setRows(5);
    }

    /**
     * A button which redirects to the siglum index page.
     */
    private final class ToIndexButton extends AjaxLink<Siglum> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a redirect button.
         * 
         * @param id
         *            ID of the button
         */
        public ToIndexButton(String id) {
            super(id);
        }

        /**
         * Called on button click.
         * 
         * @param target
         *            target that produces an Ajax response
         */
        @Override
        public void onClick(AjaxRequestTarget target) {
            setResponsePage(SiglumIndexPage.class);
        }
    }
}
