package org.example.sftpclient.runner;

import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) throws IOException {
        try (InputStream xmlStream = TestRunner.class.getClassLoader().getResourceAsStream("testng.xml")) {
            if (xmlStream == null) {
                throw new IllegalStateException("testng.xml not found in classpath");
            }

            Parser parser = new Parser(xmlStream);
            List<XmlSuite> suites = parser.parseToList();

            TestNG testng = new TestNG();
            testng.setXmlSuites(suites);
            testng.run();

            if (testng.hasFailure()) {
                System.exit(1);
            }
        }
    }
}
