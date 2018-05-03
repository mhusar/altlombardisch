package altlombardisch.siglum.text;

import altlombardisch.siglum.Siglum;
import altlombardisch.xml.document.XmlDocumentDefinition;
import altlombardisch.xml.document.XmlDocumentDefinitionDao;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * A panel which displays a preview of a siglum’s text.
 */
public class SiglumTextPreviewPanel extends Panel implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider {
    /**
     * Model of the previewed siglum.
     */
    private IModel<Siglum> model;

    /**
     * Creates a siglum text preview panel.
     *
     * @param model model of the previewed siglum
     */
    public SiglumTextPreviewPanel(IModel<Siglum> model) {
        super("siglumTextPreviewPanel");
        this.model = model;
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer markupContainer, Class<?> containerClass) {
        String transformedText = transformText(model);
        String markup = String.format("<wicket:panel><div class=\"card border-primary mt-3\">\n" +
                "<div class=\"card-header text-white bg-primary\">%s</div>\n" +
                "<div class=\"card-body\">%s</div>\n</div></wicket:panel>",
                getString("SiglumTextPreviewPanel.preview"), transformedText);

        if (transformedText.length() == 0) {
            setVisible(false);
        }

        return new StringResourceStream(markup);
    }

    /**
     * Avoid markup caching for this component.
     *
     * @param markupContainer markup container
     * @param containerClass container class
     * @return A cache key or null.
     */
    @Override
    public String getCacheKey(MarkupContainer markupContainer, Class<?> containerClass) {
        return null;
    }

    /**
     * Transforms siglum text with XSL code.
     *
     * @param model siglum model
     * @return Transformed siglum text.
     */
    private String transformText(IModel<Siglum> model) {
        XmlDocumentDefinition siglumTextMarkupDocumentDefinition = new XmlDocumentDefinitionDao()
                .findByIdentifier("siglumTextMarkup");
        String xslValue = siglumTextMarkupDocumentDefinition.getXsl();

        if (model.getObject().getText() != null && xslValue != null && xslValue.length() > 0) {
            TransformerFactory factory = TransformerFactory.newInstance();
            String xmlValue = String.format("<%s>%s</%s>", siglumTextMarkupDocumentDefinition.getRootElement(),
                    model.getObject().getText(), siglumTextMarkupDocumentDefinition.getRootElement());
            Source xmlSource = new StreamSource(new StringReader(xmlValue));
            Source xslSource = new StreamSource(new StringReader(xslValue));
            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);

            try {
                Transformer transformer = factory.newTransformer(xslSource);
                transformer.transform(xmlSource, result);
                return writer.getBuffer().toString();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
