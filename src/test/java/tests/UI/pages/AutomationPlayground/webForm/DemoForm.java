package tests.UI.pages.AutomationPlayground.webForm;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static base.fields.AlertHelper.handleSimpleAlert;
import static base.fields.DropDownHelper.selectByValue;

public class DemoForm extends BasePage {

    private Locator fullName;
    private Locator password;
    private Locator comments;
    private Locator age;
    private Locator email;
    private Locator webURL;
    private Locator phoneNumber;
    private Locator gender;
    private Locator Skills;
    private Locator selectLevel;
    private Locator selectCountry;
    private Locator selectState;
    private Locator selectCity;
    private Locator dob;
    private Locator time;

    private Locator weekYear;
    private Locator searchInputField;
    private Locator downloadField;

    private Locator seleniumLink;

    private Locator yesRadioButtonInsideIframe;
    private Locator optionOneCheckBoxInsideIframe;
    private Locator inputTextFieldInsideIframe;

    private Locator submitFormButton;


    public DemoForm(Page page) {
        super(page);
        this.fullName = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Full Name"));
        this.password = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"));
        this.comments = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Comments"));
        this.age = page.getByPlaceholder("Age");
        this.email = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email"));
        this.webURL = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Website URL"));
        this.phoneNumber =page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number"));
        this.gender = page.locator("//input[@type='radio' and @name='gender' and @value='Male']");
        this.Skills = page.locator("//input[@type='checkbox' and @name='skills' and @value='Selenium']");
        this.selectLevel= page.locator("//select[@name='experience']");
        this.selectCountry = page.locator("#country");
        this.selectState = page.locator("#state");
        this.selectCity = page.locator("#city");
        this.dob =  page.locator("input[name=\"dob\"]");
        this.time =  page.locator("input[name=\"time\"]");
    }

    public void fillWebFormDetails() {
        fullName.fill("testqa");
        password.fill("test123");
        comments.fill("This form is filled by automation");
        age.fill(String.valueOf(12));
        email.fill("testqa@gmail.com");
        webURL.fill("https://www.qa.com");
        phoneNumber.fill("+91-1234567890");
        gender.click();
        Skills.click();
        selectByValue(selectLevel, "Beginner");
        handleSimpleAlert(page, selectCountry,2000);
        selectCountry.selectOption("India");
        selectState.selectOption("Maharashtra");
        selectCity.selectOption("Pune");
        dob.fill("2025-09-03");
        page.pause();




    }

}
