package base.fields;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;

public class TableHelper {

    private static final int DEFAULT_TIMEOUT = 2000;

    // ================= GET ROW COUNT =================
    public static int getRowCount(Locator table) {
        List<ElementHandle> rows = table.locator("tr").elementHandles();
        return rows.size();
    }

    // ================= GET COLUMN COUNT =================
    public static int getColumnCount(Locator table) {
        List<ElementHandle> headerCells = table.locator("tr").first().locator("th, td").elementHandles();
        return headerCells.size();
    }

    // ================= GET CELL VALUE =================
    public static String getCellValue(Locator table, int rowIndex, int colIndex) {
        Locator cell = table.locator("tr").nth(rowIndex).locator("td, th").nth(colIndex);
        return cell.innerText().trim();
    }

    // ================= GET ENTIRE ROW =================
    public static List<String> getRowValues(Locator table, int rowIndex) {
        List<String> values = new ArrayList<>();
        List<ElementHandle> cells = table.locator("tr").nth(rowIndex).locator("td, th").elementHandles();
        for (ElementHandle cell : cells) {
            values.add(cell.innerText().trim());
        }
        return values;
    }

    // ================= GET ENTIRE COLUMN =================
    public static List<String> getColumnValues(Locator table, int colIndex) {
        List<String> values = new ArrayList<>();
        List<ElementHandle> rows = table.locator("tr").elementHandles();
        for (ElementHandle row : rows) {
            ElementHandle cell = row.querySelector("td:nth-child(" + (colIndex + 1) + "), th:nth-child(" + (colIndex + 1) + ")");
            if (cell != null) {
                values.add(cell.innerText().trim());
            }
        }
        return values;
    }

    // ================= FIND ROW INDEX BY CELL VALUE =================
    public static int findRowIndexByCellValue(Locator table, int colIndex, String value) {
        List<ElementHandle> rows = table.locator("tr").elementHandles();
        for (int i = 0; i < rows.size(); i++) {
            ElementHandle cell = rows.get(i).querySelector("td:nth-child(" + (colIndex + 1) + "), th:nth-child(" + (colIndex + 1) + ")");
            if (cell != null && cell.innerText().trim().equals(value)) {
                return i;
            }
        }
        return -1; // not found
    }

    // ================= CLICK CELL =================
    public static void clickCell(Locator table, int rowIndex, int colIndex) {
        Locator cell = table.locator("tr").nth(rowIndex).locator("td, th").nth(colIndex);
        cell.click();
    }

    // ================= SEARCH AND CLICK IN TABLE =================
    public static boolean clickCellByValue(Locator table, int searchColIndex, String value, int targetColIndex) {
        int rowIndex = findRowIndexByCellValue(table, searchColIndex, value);
        if (rowIndex != -1) {
            clickCell(table, rowIndex, targetColIndex);
            return true;
        }
        return false;
    }

    // ================= GET ALL TABLE VALUES =================
    public static List<List<String>> getAllTableValues(Locator table) {
        List<List<String>> tableData = new ArrayList<>();
        List<ElementHandle> rows = table.locator("tr").elementHandles();
        for (ElementHandle row : rows) {
            List<String> rowData = new ArrayList<>();
            List<ElementHandle> cells = row.querySelectorAll("td, th");
            for (ElementHandle cell : cells) {
                rowData.add(cell.innerText().trim());
            }
            tableData.add(rowData);
        }
        return tableData;
    }

    // ================= WAIT FOR TABLE ROW =================
    public static void waitForRowCount(Page page, Locator table, int expectedRows, int timeoutMs) {
        int waited = 0;
        while (getRowCount(table) < expectedRows && waited < timeoutMs) {
            page.waitForTimeout(200);
            waited += 200;
        }
    }

    public static void waitForRowCount(Page page, Locator table, int expectedRows) {
        waitForRowCount(page, table, expectedRows, DEFAULT_TIMEOUT);
    }
}

