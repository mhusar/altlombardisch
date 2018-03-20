package altlombardisch.siglum;

import altlombardisch.table.GenericDataTable;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.model.StringResourceModel;

import altlombardisch.ui.panel.ModalMessagePanel;

/**
 * A panel containing a modal window dialog asking if a siglum shall be deleted.
 */
@AuthorizeAction(action = Action.RENDER, roles = { "USER", "ADMIN" })
public class SiglumDeleteConfirmPanel extends ModalMessagePanel {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a panel.
     * 
     * @param id
     *            ID of the panel
     * @param responsePage
     *            page loaded on confirmation
     */
    public SiglumDeleteConfirmPanel(String id, Page responsePage) {
        super(id, DialogType.YES_NO, responsePage);
    }

    /**
     * Creates a panel.
     * 
     * @param id
     *            ID of the panel
     * @param responsePageClass
     *            class of page loaded on confirmation
     */
    public SiglumDeleteConfirmPanel(String id,
            Class<? extends Page> responsePageClass) {
        super(id, DialogType.YES_NO, responsePageClass);
    }

    /**
     * Creates a panel.
     * 
     * @param id
     *            ID of the panel
     * @param dataTable
     *            data table that is refreshed
     */
    public SiglumDeleteConfirmPanel(String id,
            GenericDataTable<Siglum> dataTable) {
        super(id, DialogType.YES_NO, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitleString() {
        return getString("SiglumDeleteConfirmPanel.title");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringResourceModel getMessageModel() {
        Siglum siglum = (Siglum) getDefaultModelObject();

        new SiglumDao().refresh(siglum);
        return new StringResourceModel("SiglumDeleteConfirmPanel.message",
                (Component) this, getDefaultModel()).setParameters("<b>" + siglum.getName() + "</b>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfirmationString() {
        return getString("SiglumDeleteConfirmPanel.confirm");
    }

    /**
     * Does nothing.
     */
    @Override
    public void onCancel() {
    }

    /**
     * Removes the package of the default model.
     *
     * @param target target that produces an Ajax response
     */
    @Override
    public void onConfirm(AjaxRequestTarget target) {
        new SiglumDao().remove((Siglum) getDefaultModelObject());
    }
}
