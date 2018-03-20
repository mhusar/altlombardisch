package altlombardisch.ui.panel;

import altlombardisch.HomePage;
import altlombardisch.siglum.SiglumIndexPage;
import altlombardisch.user.UserDao;
import altlombardisch.user.UserEditPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * A panel that provides shortcuts to main parts of the app.
 */
public class HeaderPanel extends Panel {
    /**
     * Creates a header panel.
     *
     * @param activePageClass class of active page
     */
    public HeaderPanel(Class<? extends Page> activePageClass) {
        super("headerPanel");

        WebMarkupContainer homePageItem = new WebMarkupContainer("homePageItem");
        WebMarkupContainer siglumIndexItem = new WebMarkupContainer("siglumIndexItem");
        WebMarkupContainer userEditItem = new WebMarkupContainer("userEditItem");
        BookmarkablePageLink<Void> homePageLink = new BookmarkablePageLink<>("homePageLink", HomePage.class);
        BookmarkablePageLink<Void> siglumIndexLink = new BookmarkablePageLink<>("siglumIndexLink",
                SiglumIndexPage.class);
        BookmarkablePageLink<Void> userEditLink = new BookmarkablePageLink<>("userEditLink", UserEditPage.class);
        Link<Void> logoutLink = new Link<Void>("logoutLink") {
            public void onClick() {
                new UserDao().logout();
            }
        };

        homePageItem.add(homePageLink);
        siglumIndexItem.add(siglumIndexLink);
        userEditItem.add(userEditLink);

        add(homePageItem);
        add(siglumIndexItem);
        add(userEditItem);
        add(logoutLink);

        if (activePageClass.equals(HomePage.class)) {
            homePageItem.add(AttributeModifier.append("class", "active"));
        } else if (activePageClass.equals(SiglumIndexPage.class)) {
            siglumIndexItem.add(AttributeModifier.append("class", "active"));
        } else if (activePageClass.equals(UserEditPage.class)) {
            userEditItem.add(AttributeModifier.append("class", "active"));
        }
    }
}
