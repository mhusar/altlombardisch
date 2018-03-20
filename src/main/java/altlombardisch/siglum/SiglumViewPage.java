package altlombardisch.siglum;

import altlombardisch.auth.WebSession;
import altlombardisch.ui.page.ReadOnlyBasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * A page containing a read-only siglum view form.
 */
@AuthorizeInstantiation({ "SIGNED_IN" })
public class SiglumViewPage extends ReadOnlyBasePage {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Model of the viewed siglum object.
     */
    private CompoundPropertyModel<Siglum> siglumModel;

    /**
     * Creates a siglum view page.
     * 
     * @param siglumModel
     *            model of the viewed siglum object
     */
    public SiglumViewPage(IModel<Siglum> siglumModel) {
        // check if the session is expired
        WebSession.get().checkSessionExpired();

        if (siglumModel instanceof IModel) {
            Siglum siglum = siglumModel.getObject();
            this.siglumModel = new CompoundPropertyModel<Siglum>(siglumModel);

            new SiglumDao().refresh(siglum);
        }
    }

    /**
     * Called when a siglum view page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("header", getString("SiglumViewPage.header")));
        add(new SiglumViewForm("siglumViewForm", siglumModel));
    }
}
