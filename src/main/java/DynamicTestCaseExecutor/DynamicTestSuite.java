package DynamicTestCaseExecutor;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicTestSuite {

    public static void executeDynamicTestSuite() {
        // Collect all system properties passed via maben commands

        Map<String, String> parameters = new LinkedHashMap<>();
        System.getProperties()
                .forEach((key, value) -> parameters.put(key.toString(),value.toString()));

        // Extract Test case names from the parameters
        String testCaseNames = parameters.get("testCaseNames");
        if(testCaseNames == null || testCaseNames.isEmpty()) {
            throw new IllegalArgumentException("The 'testCaseNames' property must be provided");

        }

        // convert the comma-separated test case names into a list
        List<String> testCaseNameList = List.of(testCaseNames.split(","));
        // Execute the test cases
        executeTestCases(parameters, testCaseNameList);

    }

    private static void executeTestCases(Map<String, String> parameters, List<String> testCaseNameList) {
        // Create a new TestNg suite
        XmlSuite suite = new XmlSuite();
        suite.setName("DynamicTestSuite");
        suite.setParallel(XmlSuite.ParallelMode.CLASSES);
        suite.setThreadCount(1);
        suite.setConfigFailurePolicy(XmlSuite.FailurePolicy.CONTINUE);

        // Add listeners
        List<String> listeners = new ArrayList<>();
        listeners.add("listeners.ExtentTestNGReporter");
//        listeners.add("com.reporting.ReportingSuiteListener");
        suite.setListeners(listeners);

        //Create a new TestNg test
        XmlTest test = new XmlTest(suite);
        test.setName("DynamicTest");

        // Add all parameters dynamically
        if(parameters != null) {
            parameters.forEach(test::addParameter);
        }

        // Dynamically resolve the class paths
        List<XmlClass> classes = new ArrayList<>();
        for(String testCasesName: testCaseNameList) {
            String fullClassPath = findClassPath("src/test/java", testCasesName);
            if(fullClassPath != null) {
                classes.add(new XmlClass(fullClassPath));
            } else {
                throw new RuntimeException("Test Case not found: "+ testCasesName);
            }
        }
        test.setXmlClasses(classes);

        // Execute the suite
        TestNG testNG = new TestNG();
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(suite);
        testNG.setXmlSuites(suites);
        testNG.run();
    }

    private static String findClassPath(String baseDir, String testCasesName) {
        File directory = new File(baseDir);
        if(!directory.exists()) {
            return null;
        }

        // Recursively search for the class file
        for(File file : directory.listFiles()) {
            if (file.isDirectory()) {
                String result = findClassPath(file.getAbsolutePath(), testCasesName);
                if (result != null) {
                    return result;
                }
            } else if (file.getName().equals(testCasesName + ".java")) {
                // convert file path to class path
                String filePath = file.getAbsolutePath();
                String classPath = filePath
                        .replace(new File("src/test/java").getAbsolutePath()
                                + File.separator, "")
                        .replace(File.separator, ".")
                        .replace(".java", "");
                return classPath;
            }
        }
        return null;
        }
    }

