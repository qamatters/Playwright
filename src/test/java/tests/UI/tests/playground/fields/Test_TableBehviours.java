package tests.UI.tests.playground.fields;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.fields.AlertPage;
import tests.UI.pages.AutomationPlayground.fields.TablePage;

import java.util.List;

import static base.fields.AlertHelper.*;

public class Test_TableBehviours extends BaseUITest {
    @Test
    public void TestAlertBehaviourInPlaywright() {
        TablePage tablePage = new TablePage(page);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/Fields/table.html");
        ReportUtil.logInfo("Basic Table Handling");
        ReportUtil.verifyEquals(String.valueOf(tablePage.totalRows()), "11", "Validate total rows in table");
        ReportUtil.verifyEquals(String.valueOf(tablePage.totalColumns()), "7", "Validate total columns in table");
        ReportUtil.logInfo("First column value is " + tablePage.getCellValueBasesOnColumnRowIndex(1,1));
        List<String> rowValues = tablePage.getParticularRowValues(1);
        int i =1;
        for(String s: rowValues) {
            ReportUtil.logInfo(i+ "Column data is " + s);
            i =i+1;
        }
        List<String> columnValues = tablePage.getParticularColumnValues(1);
        int index =1;
        for(String columnData: columnValues) {
            ReportUtil.logInfo(i+ "Rows data is " + columnData);
            index =index+1;
        }
        List<List<String>> allTableData = tablePage.getAllTableData();
        for(List<String> rowsData : allTableData){
            for(String s: rowsData) {
                ReportUtil.logInfo(s);
            }
        }


    }
}
