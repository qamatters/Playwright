package tests.UI.pages.AutomationPlayground.webForm;

import base.BasePage;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import listeners.ReportUtil;

import java.util.List;

public class DemoForm extends BasePage {
    // ==== Basic Inputs ====
    private Locator fullName;
    private Locator password;
    private Locator comments;
    private Locator age;
    private Locator email;
    private Locator website;
    private Locator phone;

    // ==== Radio & Checkbox ====
    private Locator genderMale;
    private Locator genderFemale;
    private Locator skillSelenium;
    private Locator skillPlaywright;
    private Locator skillApiTesting;

    // ==== Dropdowns ====
    private Locator experience;
    private Locator country;
    private Locator state;
    private Locator city;

    // ==== Date/Time ====
    private Locator dob;
    private Locator time;
    private Locator datetime;
    private Locator month;
    private Locator week;

    // ==== Color, Range, Search ====
    private Locator favColor;
    private Locator volume;
    private Locator search;

    // ==== File Upload & Download ====
    private Locator downloadFile;
    private Locator uploadFile;

    // ==== Iframe ====
    private FrameLocator iframeElement;

    // ==== Tooltip & Toast ====
    private Locator tooltipBtn;
    private Locator tooltipText;
    private Locator toastBtn;
    private Locator toast;

    // ==== Modal ====
    private Locator openModal;
    private Locator closeModal;
    private Locator modal;

    // ==== Buttons ====
    private Locator submitBtn;
    private Locator resetBtn;


    public DemoForm(Page page) {
        super(page);
        // ==== Initialize Locators ====
        fullName = page.locator("input[name='fullname']");
        password = page.locator("input[name='password']");
        comments = page.locator("textarea[name='comments']");
        age = page.locator("input[name='age']");
        email = page.locator("input[name='email']");
        website = page.locator("input[name='website']");
        phone = page.locator("input[name='phone']");

        genderMale = page.locator("input[type='radio'][value='Male']");
        genderFemale = page.locator("input[type='radio'][value='Female']");
        skillSelenium = page.locator("input[type='checkbox'][value='Selenium']");
        skillPlaywright = page.locator("input[type='checkbox'][value='Playwright']");
        skillApiTesting = page.locator("input[type='checkbox'][value='API Testing']");

        experience = page.locator("select[name='experience']");
        country = page.locator("#country");
        state = page.locator("#state");
        city = page.locator("#city");

        dob = page.locator("input[name='dob']");
        time = page.locator("input[name='time']");
        datetime = page.locator("input[name='datetime']");
        month = page.locator("input[name='month']");
        week = page.locator("input[name='week']");

        favColor = page.locator("input[name='favcolor']");
        volume = page.locator("input[name='volume']");
        search = page.locator("input[name='search']");

        downloadFile = page.locator("#downloadFile");
        uploadFile = page.locator("input[name='uploadfile']");

        iframeElement = page.frameLocator("iframe");

        tooltipBtn = page.locator("#tooltipBtn");
        tooltipText = page.locator("#tooltipText");
        toastBtn = page.locator("#toastBtn");
        toast = page.locator("#toast");

        openModal = page.locator("#openModal");
        closeModal = page.locator("#closeModal");
        modal = page.locator("#modal");

        submitBtn = page.locator("button[type='submit']");
        resetBtn = page.locator("button[type='reset']");
    }

    public void fillForm(FormData data) {
        fullName.fill(data.getFullName());
        password.fill(data.getPassword());
        comments.fill(data.getComments());
        age.fill(data.getAge());
        email.fill(data.getEmail());
        website.fill(data.getWebsite());
        phone.fill(data.getPhone());
        // gender
        if ("Male".equalsIgnoreCase(data.getGender())) genderMale.click();
        else genderFemale.click();
        // skills
        for (String skill : data.getSkills()) {
            switch (skill) {
                case "Selenium": skillSelenium.click(); break;
                case "Playwright": skillPlaywright.click(); break;
                case "API Testing": skillApiTesting.click(); break;
            }
        }
        experience.selectOption(data.getExperience());
        country.selectOption(data.getCountry());
        state.selectOption(data.getState());
        city.selectOption(data.getCity());
        dob.fill(data.getDob());
        time.fill(data.getTime());
        datetime.fill(data.getDatetime());
        month.fill(data.getMonth());
        week.fill(data.getWeek());
        favColor.fill(data.getFavColor());
        volume.fill(data.getVolume());
        search.fill(data.getSearch());
//        uploadFile.setInputFiles(data.getUploadFile());
        // iframe
        iframeElement.locator("input[type='radio'][value='" + data.getIframeRadio() + "']").click();
        for (String cb : data.getIframeCheckboxes())
            iframeElement.locator("input[type='checkbox'][value='" + cb + "']").click();
        iframeElement.locator("input[name='iframeText']").fill(data.getIframeText());
        submitBtn.click();
    }

    public void validateSubmittedData(FormData data) {
        Locator tableRow = page.locator("#recordsTable tbody tr").nth(1);
        tableRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        // Check if table has data
        if (tableRow.locator("td").count() == 1 && tableRow.locator("td").textContent().contains("No data available")) {
            ReportUtil.logFail("No data available in the submitted table.");
            return;
        }

        List<Locator> cells = tableRow.locator("td").all();
        ReportUtil.verifyText(cells.get(0).textContent(), data.getFullName(), "Full Name");
        ReportUtil.verifyText(cells.get(1).textContent(), data.getEmail(), "Email");
        ReportUtil.verifyText(cells.get(2).textContent(), data.getPhone(), "Phone");
        ReportUtil.verifyText(cells.get(3).textContent(), data.getCountry(), "Country");
        ReportUtil.verifyText(cells.get(4).textContent(), data.getState(), "State");
        ReportUtil.verifyText(cells.get(5).textContent(), data.getCity(), "City");
        ReportUtil.verifyText(cells.get(6).textContent(), String.join(", ", data.getSkills()), "Skills");
        ReportUtil.verifyText(cells.get(7).textContent(), data.getExperience(), "Experience");
        ReportUtil.verifyText(cells.get(8).textContent(), data.getDob(), "DOB");
        ReportUtil.verifyText(cells.get(9).textContent(), data.getTime(), "Time");
        ReportUtil.verifyText(cells.get(10).textContent(), data.getDatetime(), "Datetime");
        ReportUtil.verifyText(cells.get(11).textContent(), data.getMonth(), "Month");
        ReportUtil.verifyText(cells.get(12).textContent(), data.getWeek(), "Week");
        ReportUtil.verifyText(cells.get(13).textContent(), data.getFavColor(), "Fav Color");
        ReportUtil.verifyText(cells.get(14).textContent(), data.getVolume(), "Volume");
        ReportUtil.verifyText(cells.get(15).textContent(), data.getSearch(), "Search");
        ReportUtil.logPass("Form submitted and validated successfully.");
    }

}