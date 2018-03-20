package altlombardisch.siglum;

import java.util.ArrayList;
import java.util.Arrays;

import altlombardisch.ui.NumberTextField;
import altlombardisch.ui.xml.XmlTextField;
import altlombardisch.xml.document.XmlDocumentDefinitionDao;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import altlombardisch.ui.xml.XmlEditor;
import altlombardisch.xml.document.XmlDocumentDefinition;

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
        XmlTextField taggedNameTextField = new XmlTextField("taggedName",
                fontMarkupDocumentDefinition);
        XmlEditor textXmlEditor = new XmlEditor("text",
                new PropertyModel<String>(model, "text"), new Model<String>(
                        new StringResourceModel("Siglum.text").getString()));
        ListChoice<SiglumTypes.Type> typeListChoice = new ListChoice<SiglumTypes.Type>(
                "type", new PropertyModel<SiglumTypes.Type>(getModelObject(),
                        "type"), new ArrayList<SiglumTypes.Type>(
                        Arrays.asList(SiglumTypes.Type.values())),
                new EnumChoiceRenderer<SiglumTypes.Type>(), 1);
        CheckBox gasconCheckbox = new CheckBox("gascon");
        CheckBox occitanCheckbox = new CheckBox("occitan");

        add(taggedNameTextField);
        add(new XmlTextField("localization", fontMarkupDocumentDefinition));
        add(new XmlTextField("language", fontMarkupDocumentDefinition));
        add(new XmlTextField("dating", fontMarkupDocumentDefinition));
        add(new NumberTextField("numericalDating"));
        add(new XmlTextField("singularDate", fontMarkupDocumentDefinition));
        add(new NumberTextField("numericalSingularDate"));
        add(textXmlEditor);
        add(typeListChoice);
        add(new TextField<String>("position"));
        add(gasconCheckbox);
        add(occitanCheckbox);
        add(new CheckBox("unnecessary"));
        add(new ToIndexButton("toIndexButton"));

        taggedNameTextField.setRequired(true);
        textXmlEditor.setDocumentDefinition(documentDefinitionDao
                .findByIdentifier("siglumTextMarkup"));
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
