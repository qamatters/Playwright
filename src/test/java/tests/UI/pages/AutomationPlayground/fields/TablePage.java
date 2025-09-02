package tests.UI.pages.AutomationPlayground.fields;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

import static base.fields.TableHelper.*;

public class TablePage  extends BasePage {
    public Locator tableId;
    public TablePage(Page page) {
        super(page);
        this.tableId = page.locator("#automationTable");
    }

    public int totalRows() {
        return getRowCount(tableId);
    }

    public int totalColumns() {
        return getColumnCount(tableId);
    }

    public String getCellValueBasesOnColumnRowIndex(int rowIndex, int columnIndex) {
        return getCellValue(tableId, rowIndex,columnIndex);
    }

    public List<String> getParticularRowValues(int rowIndex) {
        return getRowValues(tableId, rowIndex);
    }

    public List<String> getParticularColumnValues(int columnIndex) {
        return getColumnValues(tableId, columnIndex);
    }

    public List<List<String>> getAllTableData() {
        return getAllTableValues(tableId);
    }

}
