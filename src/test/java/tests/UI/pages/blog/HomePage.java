package tests.UI.pages.blog;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

public class HomePage extends BasePage {

    private Locator globalSearchField;
    private Locator popularBlogs;
    private Locator comments;

    public HomePage(Page page) {
        super(page);
        this.globalSearchField = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("search"));
        this.popularBlogs = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Popular"));
        this.comments = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Comments"));

    }

    public void searchBlog(String blogKeyword){
       globalSearchField.waitFor();
       globalSearchField.fill(blogKeyword);
       validateBlogKeywordAfterGlobalSearch(blogKeyword);
    }

    public void validateBlogKeywordAfterGlobalSearch(String blogKeyword) {
       Locator allBlogPostLinks = page.locator("//*[contains(@class,\"post-title entry-title\")]");
        List<Locator> allLocatorsForBlogs = allBlogPostLinks.all();
        int i =1;
        for(Locator blogPost : allLocatorsForBlogs) {
            if(blogPost.allInnerTexts().contains("blogKeyword")) {
                System.out.println(i + ":" + blogPost.allInnerTexts());
            }
        }
    }

    public void validateLinks() {
        assert popularBlogs.isVisible();
        assert comments.isVisible();
    }

}
