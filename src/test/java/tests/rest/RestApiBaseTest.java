package tests.rest;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import tests.config.ReadConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Map;


public class RestApiBaseTest {
    protected static String responseString;
    protected static RequestSpecification spec;
    protected static RequestSpecification specMalformed;
    protected static RequestSpecification specLocalhost;
    protected static ResponseLoggingFilter rspFilter;
    protected static RequestLoggingFilter rstFilter;


    @BeforeClass(alwaysRun = true)
    public void setUpClass(final ITestContext testContext){


        Reporter.log(String.format("Test \"%s\" running", testContext.getName()), 1, true);
        try {
            Map<String, String> config = ReadConfiguration.getPropValues();
            File file = new File(config.get("logFile"));
            FileOutputStream fout = new FileOutputStream(file);
            PrintStream out = new PrintStream(fout);
            rspFilter = new ResponseLoggingFilter(out);
            rstFilter = new RequestLoggingFilter(out);
            spec = new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .setBaseUri(config.get("baseUrl"))
                    .addFilter(rspFilter)//log request and response for better debugging. You can also only log if a requests fails.
                    .addFilter(rstFilter)
                    .build();
            specMalformed = new RequestSpecBuilder()
                    .setContentType(ContentType.URLENC)
                    .setBaseUri("http://jsonplaceholder.typicode.com")
                    .addFilter(rspFilter)//log request and response for better debugging. You can also only log if a requests fails.
                    .addFilter(rstFilter)
                    .build();
            specLocalhost = new RequestSpecBuilder()
                    .setContentType(ContentType.URLENC)
                    .setBaseUri("http://localhost:3000")
                    .addFilter(rspFilter)//log request and response for better debugging. You can also only log if a requests fails.
                    .addFilter(rstFilter)
                    .build();
        } catch (IOException ex) {
            System.out.println("There was a problem creating/writing to the temp file");
            ex.printStackTrace();
        }

    }
    @AfterClass
    public void tearDownClass(){

    }
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
    //        Reporter.log("Running test case: " + method.getName(), 1, true);
        Reporter.log(String.format("Running test case: \"%s\".", method.getName()),true);
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method) {
        Reporter.log(String.format("This is clean up after test case: \"%s\".", method.getName()),true);
    }
}
