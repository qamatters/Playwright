package base.fields;

import com.microsoft.playwright.Locator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static base.BaseUITest.page;

public class CalenderHelper {
    /**
     * Selects a date from the calendar widget.
     *
     * @param calendarInput   Locator for the input field that opens the calendar.
     * @param monthYearHeader Locator for the month/year text in the calendar popup.
     * @param nextButton      Locator for the next navigation button.
     * @param dateCells       Locator for the clickable date elements.
     * @param dateToSelect    The LocalDate to select.
     */
    public void selectDate(Locator calendarInput,
                           Locator monthYearHeader,
                           Locator nextButton,
                           Locator dateCells,
                           LocalDate dateToSelect) {

        // Open calendar popup
        calendarInput.click();

        // Navigate until the correct month and year is visible
        while (true) {
            String headerText = monthYearHeader.textContent().trim(); // e.g., "September 2025"
            LocalDate visibleDate = parseHeaderDate(headerText);

            if (visibleDate.getMonth() == dateToSelect.getMonth() &&
                    visibleDate.getYear() == dateToSelect.getYear()) {
                break;
            }

            nextButton.click();
            page.waitForTimeout(500); // slight wait for UI update
        }

        // Select the correct day
        for (Locator cell : dateCells.all()) {
            if (cell.textContent().trim().equals(String.valueOf(dateToSelect.getDayOfMonth()))) {
                cell.click();
                break;
            }
        }
    }

    /**
     * Checks if the correct date is set in the input field.
     */
    public boolean isDateSelected(Locator calendarInput, LocalDate expectedDate) {
        String value = calendarInput.inputValue().trim();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expected = expectedDate.format(dateFormatter);
        return value.equals(expected);
    }

    /**
     * Parses the month and year from the calendar header text.
     */
    private LocalDate parseHeaderDate(String headerText) {
        DateTimeFormatter headerFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return LocalDate.parse("01 " + headerText, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }
}