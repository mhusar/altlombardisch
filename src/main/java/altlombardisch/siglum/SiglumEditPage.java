package altlombardisch.siglum;

import altlombardisch.auth.WebSession;
import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.page.BasePage;
import altlombardisch.ui.panel.IndicatorOverlayPanel;
import altlombardisch.ui.panel.ModalMessagePanel;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A page containing a siglum edit form.
 */
@AuthorizeInstantiation({ "USER", "ADMIN" })
public class SiglumEditPage extends BasePage {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Model of the edited siglum object.
     */
    private CompoundPropertyModel<Siglum> siglumModel;

    /**
     * Class of the next page.
     */
    private Class<? extends Page> nextPageClass;

    /**
     * Id of the next siglum.
     */
    private Integer nextSiglumId;

    /**
     * Creates a new siglum edit page.
     */
    public SiglumEditPage() {
        Siglum siglum = new Siglum();
        siglumModel = new CompoundPropertyModel<Siglum>(siglum);
        nextPageClass = null;
    }

    /**
     * Creates a new siglum edit page.
     * 
     * @param nextPageClass
     *            class of the next page
     */
    public SiglumEditPage(Class<? extends Page> nextPageClass) {
        Siglum siglum = new Siglum();
        siglumModel = new CompoundPropertyModel<Siglum>(siglum);
        this.nextPageClass = nextPageClass;
    }

    /**
     * Creates a new siglum edit page.
     * 
     * @param siglumModel
     *            model of the edited siglum object
     * @param nextPageClass
     *            class of the next page
     */
    public SiglumEditPage(IModel<Siglum> siglumModel,
            Class<? extends Page> nextPageClass) {
        this.nextPageClass = nextPageClass;

        if (siglumModel instanceof IModel) {
            Siglum siglum = siglumModel.getObject();
            this.siglumModel = new CompoundPropertyModel<Siglum>(siglumModel);

            new SiglumDao().refresh(siglum);
        }
    }

    /**
     * Creates a new siglum edit page.
     * 
     * @param siglumModel
     *            model of the edited siglum object
     * @param nextSiglumId
     *            Id of the next siglum
     */
    public SiglumEditPage(IModel<Siglum> siglumModel, Integer nextSiglumId) {
        this.nextPageClass = SiglumIndexPage.class;
        this.nextSiglumId = nextSiglumId;

        if (siglumModel instanceof IModel) {
            new SiglumDao().refresh(siglumModel.getObject());
            this.siglumModel = new CompoundPropertyModel<Siglum>(siglumModel);
        }
    }

    /**
     * Called when a siglum edit page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        // check if the session is expired
        WebSession.get().checkSessionExpired();

        ModalMessagePanel siglumDeleteConfirmPanel;

        if (nextPageClass instanceof Class) {
            if (nextSiglumId instanceof Integer) {
                if (nextSiglumId.equals(new Integer(-1))) {
                    siglumDeleteConfirmPanel = new SiglumDeleteConfirmPanel("siglumDeleteConfirmPanel", nextPageClass);
                } else {
                    Siglum nextSiglum = new SiglumDao().findById(nextSiglumId);
                    Integer afterNextSiglumId = new SiglumDao().getNextSiglumId(nextSiglum);
                    Page nextSiglumPage = new SiglumEditPage(new Model<Siglum>(nextSiglum), afterNextSiglumId);

                    siglumDeleteConfirmPanel = new SiglumDeleteConfirmPanel("siglumDeleteConfirmPanel", nextSiglumPage);
                }
            } else {
                siglumDeleteConfirmPanel = new SiglumDeleteConfirmPanel("siglumDeleteConfirmPanel", nextPageClass);
            }
        } else {
            siglumDeleteConfirmPanel = new SiglumDeleteConfirmPanel("siglumDeleteConfirmPanel", SiglumIndexPage.class);
        }

        add(siglumDeleteConfirmPanel);

        if (new SiglumDao().isTransient(siglumModel.getObject())) {
            siglumDeleteConfirmPanel.setVisible(false);
            add(new TitleLabel(getString("SiglumEditPage.newHeader")));
            add(new Label("header", getString("SiglumEditPage.newHeader")));
        } else if (nextSiglumId instanceof Integer) {
            add(new TitleLabel(getString("SiglumEditPage.correctHeader")));
            add(new Label("header", getString("SiglumEditPage.correctHeader")));
        } else {
            add(new TitleLabel(getString("SiglumEditPage.editHeader")));
            add(new Label("header", getString("SiglumEditPage.editHeader")));
        }

        add(new IndicatorOverlayPanel());

        if (nextSiglumId instanceof Integer) {
            add(new SiglumEditForm("siglumEditForm", siglumModel, nextSiglumId, nextPageClass));
        } else {
            add(new SiglumEditForm("siglumEditForm", siglumModel, nextPageClass));
        }
    }
}
