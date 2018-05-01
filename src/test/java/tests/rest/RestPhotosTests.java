package tests.rest;

import io.restassured.response.Response;
import org.testng.Reporter;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RestPhotosTests extends RestApiBaseTest {
    private Map<String, Object> photoMap;


    @Test(groups = {"GET.200", "Photos"})
    public void testGetMultipleItemsSuccessStatusCode(){
        given().spec(spec)
                .when().get("/photos")
                .then().statusCode(200);
    }
    
    @Test(groups = {"GET.200", "response", "Photos"})
    public void testGetSinglePhotoSuccess() {
        Response response =
                given().spec(spec)
                .when().get("/photos/1")
                .then().statusCode(200).extract().response();
        responseString = response.asString();
        Integer albumId = from(responseString).getInt("albumId");
        assertThat(albumId).isEqualTo(1);
        photoMap = new HashMap<String, Object>();
        photoMap.put("albumId", 1);
        photoMap.put("id", 1);
        photoMap.put("title", "accusamus beatae ad facilis cum similique qui sunt");
        photoMap.put("url", "http://placehold.it/600/92c952");
        photoMap.put("thumbnailUrl", "http://placehold.it/150/92c952");
        Map<String, Object> obi = from(responseString).getMap(".");
        assertThat(obi).isEqualTo(photoMap);
    }
    
    @Test(groups = {"GET.200", "response", "Photos"})
    public void testGetMultiplePhotosSuccess(){
        Response response =
                given().spec(spec)
                .when().get("/photos")
                .then().statusCode(200).extract().response();
        responseString = response.asString();
        List<String> albumIds = from(responseString).getList("albumId");
        assertThat(albumIds).isNotEmpty();
        List<Integer> lista = from(responseString).get();
        Integer firstId = from(responseString).getInt("[0].albumId");
        assertThat(firstId).isEqualTo(1);
        assertThat(firstId == 1);
        assertThat(lista).isNotEmpty();
    }

    @Test(groups = {"PUT.200", "response", "Photos"})
    public void testPutCreateSinglePhotoSuccessAsString() {
        String output =
                given().spec(spec)
                .when().put("/photos/1")
                .then().statusCode(200).extract().asString();
        assertThat(output).isNotBlank();
        assertThat(output).contains("id");
    }
    
    @Test(groups = {"PUT.200", "response", "Photos"})
    public void testPutCreateSinglePhotoSuccessResponse() {
        Response response =
                given().spec(spec)
                .when().put("/photos/1")
                .then().statusCode(200).extract().response();
        responseString = response.asString();
        String id = from(responseString).getString("id");
        assertThat(id).isEqualTo("1");
    }
    
    @Test(groups = {"PUT.200", "Photos"})
    public void testPutCreateSinglePhotoWithId3SuccessResponse() {
        Response response =
                given().spec(spec)
                .when().put("/photos/3/")
                .then().statusCode(200).extract().response();
        responseString = response.asString();
        String id = from(responseString).getString("id");
        Reporter.log("This is message originated from test method", true);
        assertThat(id).isEqualTo("3");
    }
    
    @Test(groups = {"POST.201", "Photos"})
    public void testPostCreateSinglePhotoSuccessCreated() {
        given().spec(spec)
                .when().post("/photos")
                .then().statusCode(201);
    }
    
    @Test(groups = {"POST.404", "Photos"})
    public void testPostCreateSinglePhotoWrongUrlDoubleSlash() {
        given().spec(spec)
                .when().post("//photos")
                .then().statusCode(404);
    }
    
    @Test(groups = {"GET.404", "Photos"})
    public void testGetSinglePhotoWrongUrlDoubleSlash() {
        given()
                .when().get("http://jsonplaceholder.typicode.com//photos/1")
                .then().statusCode(404);
    }
    
    @Test(groups = {"GET.404", "Photos"})
    public void testGetSinglePhotoJustWrongUrl() {
        given().spec(spec)
                .when().get("/photos/1/123")
                .then().statusCode(404);
    }
    @Test(groups = {"POST.500", "Photos", "text"})
    public void testPostCreateSinglePhotoContentTypeUrlEncStatus500() {
        given().spec(specMalformed)
                .when().post("/photos")
                .then().statusCode(500);
    }
}
