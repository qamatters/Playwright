package tests;

import org.testng.annotations.Test;

import static DynamicTestCaseExecutor.DynamicTestSuite.executeDynamicTestSuite;

public class RunSingleTestCase {

    @Test
    public static void executeSingleTestCase() {
        executeDynamicTestSuite();
        // mvn clean test -Dtest=RunSingleTestCase -Dsurefire.suiteXmlFiles= -Dbrowser=chrome -Dheadless=true -DlogMode=file -Durl=https://jsonplaceholder.typicode.com -DtestCaseNames=T002_JPH_UI_Test

    }
}
