import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static com.jayway.restassured.path.json.JsonPath.from;


public class AppRestTest {
    private static String responseString;
    private static RequestSpecification spec;
    private static ResponseLoggingFilter rspFilter;
    private static RequestLoggingFilter rstFilter;		    
    @BeforeClass(groups = {"PUT", "POST", "GET", "response"})
    public void setUpClass(final ITestContext testContext){
    	Reporter.log(String.format("Test \"%s\" running", testContext.getName()), 1, true);
    	try {
		      File file = new File("D:\\plik.txt");
		      FileOutputStream fout = new FileOutputStream(file);
		      PrintStream out = new PrintStream(fout);
		      rspFilter = new ResponseLoggingFilter(out);
		      rstFilter = new RequestLoggingFilter(out);
	    } catch (IOException ex) {
	      System.out.println("There was a problem creating/writing to the temp file");
	      ex.printStackTrace();
	    }
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("http://jsonplaceholder.typicode.com")
                .addFilter(rspFilter)//log request and response for better debugging. You can also only log if a requests fails.
                .addFilter(rstFilter)
                .build();

    }
    @AfterClass
    public void tearDownClass(){

    }
    @BeforeMethod
    public void setUp(Method method) {
    	Reporter.log("Running test case" + method.getName(), 1, true);
    }
    @AfterMethod
    public void tearDown() {

    }  
    @Test(groups = {"GET.200"})
    public void testStatusCode(){
        given().spec(spec).when().get("/posts").then().statusCode(200);
    }
    
    @Test(groups = {"GET.200", "response"})
    public void testOutputSingleItem() {
        Response response =
                given().spec(spec).when().get("/posts/1").then().statusCode(200).extract().response();
        responseString = response.asString();
        Integer listaUserId = from(responseString).getInt("userId");
        assertThat(listaUserId).isEqualTo(1);
        Map<String, Object> mojaMapa = new HashMap<String, Object>();
        mojaMapa.put("userId", 1);
        mojaMapa.put("id", 1);
        mojaMapa.put("title", "sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
        mojaMapa.put("body", "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
        Map<String, Object> obi = from(responseString).getMap(".");
        assertThat(obi).isEqualTo(mojaMapa);
    }
    
    @Test(groups = {"GET.200", "response"})
    public void testOutput(){
        Response response =
                given().spec(spec).when().get("/posts").then().statusCode(200).extract().response();
        responseString = response.asString();
        List<String> listaUserId = from(responseString).getList("userId");
        assertThat(listaUserId).isNotEmpty();
        List<Integer> lista = from(responseString).get();
        Integer firstId = from(responseString).getInt("[0].userId");
        assertThat(firstId).isEqualTo(1);
        assertThat(firstId == 1);
        assertThat(lista).isNotEmpty();
    }
    
    @Test(groups = {"GET.200", "response"})
    public void testOutputString() {
        Response response =
                given().spec(spec).when().get("/comments?postId=1").then().statusCode(200).extract().response();
        responseString = response.asString();
        String email = from(responseString).getString("[2].email");
        assertThat(email).isEqualTo("Nikita@garfield.biz");
    }
    
    @Test(groups = {"PUT.200", "response"})
    public void testPut() {
        String output = given().spec(spec).when().put("/posts/1").then().statusCode(200).extract().asString();
        assertThat(output).isNotBlank();
        assertThat(output).contains("id");
    }
    
    @Test(groups = {"PUT.200", "response"})
    public void testPut2() {
        Response r = given().spec(spec).when().put("/posts/1").then().statusCode(200).extract().response();
        responseString = r.asString();
        String id = from(responseString).getString("id");
        assertThat(id).isEqualTo("1");
    }
    
    @Test(groups = {"PUT.200"})
    public void testPut3() {
        Response r = given().spec(spec).when().put("/posts/3").then().statusCode(200).extract().response();
        responseString = r.asString();
        String id = from(responseString).getString("id");
        assertThat(id).isEqualTo("3");
    }
    
    @Test(groups = {"POST.201"})
    public void testPostCreated() {
        given().spec(spec).when().post("/posts").then().statusCode(201);
    }
    
    @Test(groups = {"POST.404"})
    public void testPostStatus404() {
        given().spec(spec).when().post("//posts").then().statusCode(404);
    }
    
    @Test(groups = {"GET.404"})
    public void testGetWrongUrlDoubleSlash() {
        given().when().get("http://jsonplaceholder.typicode.com//posts/1").then().statusCode(404);
    }
    
    @Test(groups = {"GET.404"})
    public void testGetWrongUrl404() {
        given().spec(spec).when().get("/posts/1/123").then().statusCode(404);
    }
}
