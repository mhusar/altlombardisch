package altlombardisch.frontend;

import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.page.FrontendBasePage;

/**
 * The home or index page of the application.
 */
public class HomePage extends FrontendBasePage {
    /**
     * Initializes a home page.
     */
    public HomePage() {
    }

    /**
     * Called when a home page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new TitleLabel(getString("HomePage.header")));
    }
}
