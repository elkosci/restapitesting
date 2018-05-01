package tests.rest;
import tests.config.GetConfiguration;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.*;
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
    protected static RequestSpecification specLocalhostText;
    protected static ResponseLoggingFilter rspFilter;
    protected static RequestLoggingFilter rstFilter;
    protected static PrintStream outFileLog;
    protected static File logFileHandle;
    protected static Map<String, String> config;

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite(){
        try {
            config = GetConfiguration.getProperites();
            logFileHandle =
                    new File(config.get("logFile"));
            FileOutputStream fout =
                    new FileOutputStream(logFileHandle);
            outFileLog =
                    new PrintStream(fout);
            if (outFileLog != null){
                Reporter.log(
                        "Set up a log file: " + config.get("logFile"),
                        1,
                        true);
            }
        } catch (IOException ex) {
            String msg =
                    String.format("Problem occurred during creation of log file \"%s\"",
                            config.get("logFile"));
            System.out.println(msg);
            ex.printStackTrace();
        }
    }
    @AfterSuite(alwaysRun = true)
    public void tearDownSuite(){
        if (outFileLog != null){
            outFileLog.close();
            logFileHandle.renameTo(
                    new File(config.get("logBackupPath")));
            Reporter.log(
                    "Move log file: " + config.get("logFile")
                            + " to: " + config.get("logBackupPath"),
                    1,
                    true);
            File f =
                    new File(config.get("logBackupPath"));
            if(f.exists()) {
                System.out.println(
                        String.format("File \"%s\" successfully created.",
                                config.get("logBackupPath")));
            }

        }
    }
    @BeforeClass(alwaysRun = true)
    public void setUpClass(final ITestContext testContext){
        String msg =
                String.format("Test \"%s\" running", testContext.getName());
        Reporter.log(msg,true);
        outFileLog.println(msg);
        rspFilter =
                new ResponseLoggingFilter(outFileLog);
        rstFilter =
                new RequestLoggingFilter(outFileLog);
        spec =
                new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(config.get("baseUrl"))
                .addFilter(rspFilter)
                .addFilter(rstFilter)
                .build();
        specMalformed =
                new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .setBaseUri(config.get("baseUrl"))
                .addFilter(rspFilter)
                .addFilter(rstFilter)
                .build();
        specLocalhost =
                new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost:3000")
                .addFilter(rspFilter)
                .addFilter(rstFilter)
                .build();
        specLocalhostText =
                new RequestSpecBuilder()
                .setContentType(ContentType.TEXT)
                .setBaseUri("http://localhost:3000")
                .addFilter(rspFilter)
                .addFilter(rstFilter)
                .build();
    }
    @AfterClass
    public void tearDownClass(){

    }
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        String msg =
                String.format("Running test case: \"%s\".", method.getName());
        outFileLog.println(msg);
        Reporter.log(msg,true);
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method) {
        String msg =
                String.format("This is clean up after test case: \"%s\".", method.getName());
        outFileLog.println(msg);
        Reporter.log(msg,true);
    }
}
