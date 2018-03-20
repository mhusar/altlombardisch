package altlombardisch;

import altlombardisch.auth.WebSession;
import altlombardisch.character.CharacterEditPage;
import altlombardisch.siglum.SiglumIndexPage;
import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.UserBookmarkablePageLink;
import altlombardisch.ui.page.BasePage;
import altlombardisch.user.UserEditPage;
import altlombardisch.xml.document.XmlDocumentDefinitionEditPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * The home or index page of the application.
 */
@AuthorizeInstantiation("SIGNED_IN")
public class HomePage extends BasePage {
    /**
     * Initializes a home page.
     */
    public HomePage() {
        BookmarkablePageLink<Void> siglumIndexLink = new BookmarkablePageLink<>("siglumIndexLink",
                SiglumIndexPage.class);
        BookmarkablePageLink<Void> userEditLink = new BookmarkablePageLink<>("userEditLink", UserEditPage.class);
        UserBookmarkablePageLink characterEditPageLink = new UserBookmarkablePageLink("characterEditPageLink",
                CharacterEditPage.class);
        UserBookmarkablePageLink xmlDocumentDefinitionEditPageLink = new UserBookmarkablePageLink
                ("xmlDocumentDefinitionEditPageLink", XmlDocumentDefinitionEditPage.class);

        // check if the session is expired
        WebSession.get().checkSessionExpired();

        add(siglumIndexLink);
        add(userEditLink);
        add(xmlDocumentDefinitionEditPageLink);
        add(characterEditPageLink);
    }

    /**
     * Called when a home page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new TitleLabel(getString("HomePage.overview")));
    }
}
