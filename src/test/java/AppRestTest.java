import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import javax.swing.text.AbstractDocument;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static com.jayway.restassured.path.json.JsonPath.from;


public class AppRestTest {
    private static String responseString;
    private static RequestSpecification spec;
    @BeforeClass
    public void setUpClass(){
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("http://jsonplaceholder.typicode.com")
                .addFilter(new ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
                .addFilter(new RequestLoggingFilter())
                .build();

    }
    @AfterClass
    public void tearDownClass(){

    }
    @BeforeMethod
    public void setUp() {

    }
    @AfterMethod
    public void tearDown() {

    }
    @Test
    public void testStatusCode(){
        given().spec(spec).when().get("/posts").then().statusCode(200);
    }
    @Test
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
    @Test
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
    @Test
    public void testOutputString() {
        Response response =
                given().spec(spec).when().get("/comments?postId=1").then().statusCode(200).extract().response();
        responseString = response.asString();
        String email = from(responseString).getString("[2].email");
        assertThat(email).isEqualTo("Nikita@garfield.biz");
    }
    @Test
    public void testPut() {
        String output = given().spec(spec).when().put("/posts/1").then().statusCode(200).extract().asString();
        assertThat(output).isNotBlank();
        assertThat(output).contains("id");
    }
    @Test
    public void testPut2() {
        Response r = given().spec(spec).when().put("/posts/1").then().statusCode(200).extract().response();
        responseString = r.asString();
        String id = from(responseString).getString("id");
        assertThat(id).isEqualTo("1");
    }
    @Test
    public void testGetWrongUrlDoubleSlash() {
        given().when().get("http://jsonplaceholder.typicode.com//posts/1").then().statusCode(404);
    }
    @Test
    public void testGetWrongUrl404() {
        given().spec(spec).when().get("/posts/1/123").then().statusCode(404);
    }
}