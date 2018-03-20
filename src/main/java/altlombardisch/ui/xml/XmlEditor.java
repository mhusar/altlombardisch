package altlombardisch.ui.xml;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import javax.json.JsonObject;

import altlombardisch.xml.XmlHelper;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.validation.validator.StringValidator;

import altlombardisch.xml.TagDataProvider;
import altlombardisch.xml.document.XmlDocumentDefinition;

/**
 * A form component panel with a hidden textarea and a mirror container used to
 * embed the Ace code editor.
 * 
 * @see <a href="http://ace.c9.io">http://ace.c9.io</a>
 */
public class XmlEditor extends FormComponentPanel<String> implements
        TagDataProvider {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Label value made available for validator properties.
     */
    private IModel<String> labelModel;

    /**
     * Defines if the textarea should have autofocus.
     */
    private Boolean autofocus = false;

    /**
     * Maximum allowed length of the XML editor.
     */
    private Integer maximumLength = 1000;

    /**
     * Readonly state of the XML editor.
     */
    private Boolean readOnly = false;

    /**
     * Number of rows of the XML editor.
     */
    private Integer rows = 3;

    /**
     * Document definition of a XML document.
     */
    private XmlDocumentDefinition documentDefinition;

    /**
     * ID of the mirror container.
     */
    private String mirrorId;

    /**
     * ID of the textarea.
     */
    private String textareaId;

    /**
     * A hidden textarea.
     */
    private TextArea<String> textArea;

    /**
     * Creates a XML editor.
     * 
     * @param id
     *            ID of the XML editor
     * @param model
     *            model of the textarea
     * @param labelModel
     *            label model of the textarea
     */
    public XmlEditor(String id, IModel<String> model, IModel<String> labelModel) {
        super(id, model);

        this.labelModel = labelModel;
        SecureRandom random = new SecureRandom();
        String randomSuffix = new BigInteger(130, random).toString(32);
        mirrorId = "mirror-" + randomSuffix;
        textareaId = "textarea-" + randomSuffix;
    }

    /**
     * Creates a XML editor.
     * 
     * @param id
     *            ID of the XML editor
     * @param model
     *            model of the textarea
     * @param labelModel
     *            label model of the textarea
     * @param autofocus
     *            defines if the textarea should have autofocus
     */
    public XmlEditor(String id, IModel<String> model,
            IModel<String> labelModel, Boolean autofocus) {
        super(id, model);

        this.labelModel = labelModel;
        this.autofocus = autofocus;
        SecureRandom random = new SecureRandom();
        String randomSuffix = new BigInteger(130, random).toString(32);
        mirrorId = "mirror-" + randomSuffix;
        textareaId = "textarea-" + randomSuffix;
    }

    /**
     * Sets the maximum length.
     * 
     * @param maximumLength
     *            number of characters allowed
     */
    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

    /**
     * Sets the editor readonly.
     * 
     * @param readonly
     *            state of the editor
     */
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Sets the number of rows to the textarea whose height is applied to the
     * mirror container.
     * 
     * @param rows
     *            number of rows
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * Sets the definition of a XML document.
     * 
     * @param documentDefinition
     *            a document definition of a XML document
     */
    public void setDocumentDefinition(XmlDocumentDefinition documentDefinition) {
        this.documentDefinition = documentDefinition;
    }

    /**
     * Called when a XML editor is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        WebMarkupContainer container = new WebMarkupContainer("container");
        WebMarkupContainer mirror = new WebMarkupContainer("mirror");
        textArea = new TextArea<String>("textarea", getModel());

        if (autofocus) {
            container.add(AttributeModifier.append("data-autofocus",
                    "autofocus"));
        }

        add(container.setOutputMarkupId(true));
        container.add(mirror.setOutputMarkupId(true));
        container.add(textArea.setOutputMarkupId(true));
        mirror.setMarkupId(mirrorId);
        textArea.setMarkupId(textareaId);
        textArea.setLabel(labelModel);
        textArea.add(AttributeModifier.append("rows", rows));
        textArea.add(new XmlEditorBehavior());
        textArea.add(new StringValidator(null, maximumLength));

        if (documentDefinition instanceof XmlDocumentDefinition) {
            textArea.add(new XmlValidator(textArea, documentDefinition));
        } else {
            textArea.add(new XmlValidator(textArea));
        }

        // initialize data for clickable tags
        initializeTagData(container, documentDefinition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeTagData(Component component,
            XmlDocumentDefinition documentDefinition) {
        if (!(documentDefinition instanceof XmlDocumentDefinition)) {
            return;
        }

        JsonObject documentData = XmlHelper.getDocumentData(documentDefinition);

        component.add(AttributeModifier.append("data-document",
                documentData.toString()));
    }

    /**
     * Called when a XML editor is configured.
     */
    @Override
    protected void onConfigure() {
        super.onConfigure();
        textArea.setModelObject(getModelObject());

        if (isRequired()) {
            textArea.add(AttributeModifier.append("required", "required"));
        }
    }

    /**
     * Converts and validates the raw input string.
     */
    @Override
    public void convertInput() {
        textArea.processInput();
        setConvertedInput(textArea.getConvertedInput());
    }

    /**
     * Returns the input string of this component.
     * 
     * @return The value of the request of this component.
     */
    @Override
    public String getInput() {
        return textArea.getInput();
    }

    /**
     * Updates this component’s model.
     */
    @Override
    public void updateModel() {
        setModelObject(getConvertedInput());
    }

    /**
     * A behavior which initializes a XML editor.
     */
    private class XmlEditorBehavior extends AbstractAjaxBehavior {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Renders what the component wants to contribute to the head section.
         * 
         * @param component
         *            the rendered component
         * @param response
         *            the response object
         */
        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            final JavaScriptHeaderItem aceScriptItem = JavaScriptHeaderItem
                    .forUrl("/scripts/ace/src-noconflict/ace.js");
            PackageResourceReference xmlEditorScript = new JavaScriptResourceReference(
                    XmlEditor.class, "scripts/xmleditor.js", getLocale(),
                    getStyle(), "") {
                /**
                 * Determines if a deserialized file is compatible with this
                 * class.
                 */
                private static final long serialVersionUID = 1L;

                /**
                 * Returns a list of dependent references.
                 * 
                 * @return A list of dependent references.
                 */
                @Override
                public List<HeaderItem> getDependencies() {
                    List<HeaderItem> dependencies = super.getDependencies();
                    WebApplication application = altlombardisch.WebApplication.get();
                    ResourceReference jqueryScript = application
                            .getJavaScriptLibrarySettings()
                            .getJQueryReference();

                    dependencies.add(JavaScriptHeaderItem.forReference(jqueryScript));
                    dependencies.add(aceScriptItem);
                    return dependencies;
                }
            };

            JavaScriptHeaderItem xmlEditorScriptItem = JavaScriptHeaderItem
                    .forReference(xmlEditorScript);
            String basePath = "/scripts/ace/src-noconflict";
            String javaScript = "XmlEditor.init(\"" + mirrorId + "\", \""
                    + textareaId + "\", \"" + basePath + "\", "
                    + readOnly.toString() + ");";

            response.render(aceScriptItem);
            response.render(xmlEditorScriptItem);
            response.render(OnDomReadyHeaderItem.forScript(javaScript));
        }

        /**
         * Called when a request to a behavior is received.
         */
        @Override
        public void onRequest() {
        }
    }
}
