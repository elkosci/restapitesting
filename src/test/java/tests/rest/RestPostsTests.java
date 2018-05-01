package tests.rest;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static com.jayway.restassured.path.json.JsonPath.from;


public class RestPostsTests extends RestApiBaseTest {
    @Test(groups = {"GET.200", "Posts"})
    public void testGetMultipleItemsSuccessStatusCode(){
        given().spec(spec).when().get("/posts").then().statusCode(200);
    }
    
    @Test(groups = {"GET.200", "response123", "Posts"})
    public void testGetSinglePostSuccess() {
        Response response = given().spec(spec)
                .when().get("/posts/1")
                .then().statusCode(200).extract().response();
        responseString = response.asString();
        Integer listaUserId = from(responseString).getInt("userId");
        assertThat(listaUserId).isEqualTo(1);
        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("userId", 1);
        myMap.put("id", 1);
        myMap.put("title", "sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
        myMap.put("body", "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\n" +
                "reprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
        Map<String, Object> obi = from(responseString).getMap(".");
        assertThat(obi).isEqualTo(myMap);
    }
    
    @Test(groups = {"GET.200", "response", "Posts"})
    public void testGetMultiplePostsSuccess(){
        Response response = given().spec(spec)
                .when().get("/posts")
                .then().statusCode(200).extract().response();
        responseString = response.asString();
        List<String> listaUserId = from(responseString).getList("userId");
        assertThat(listaUserId).isNotEmpty();
        List<Integer> lista = from(responseString).get();
        Integer firstId = from(responseString).getInt("[0].userId");
        assertThat(firstId).isEqualTo(1);
        assertThat(firstId == 1);
        assertThat(lista).isNotEmpty();
    }
    
    @Test(groups = {"GET.200", "response", "Posts"} /*, threadPoolSize = 3, invocationCount = 6,  timeOut = 1000*/)
    public void testGetMultipleCommentsCheckThirdEmail() {
        Response response = given().spec(spec)
                .when().get("/comments?postId=1")
                .then().statusCode(200).extract().response();
        responseString = response.asString();
        String email = from(responseString).getString("[2].email");
        assertThat(email).isEqualTo("Nikita@garfield.biz");
    }
    
    @Test(groups = {"PUT.200", "response", "Posts"})
    public void testPutCreateSinglePostSuccessAsString() {
        String output = given().spec(spec)
                .when().put("/posts/1")
                .then().statusCode(200).extract().asString();
        assertThat(output).isNotBlank();
        assertThat(output).contains("id");
    }
    
    @Test(groups = {"PUT.200", "response", "Posts"})
    public void testPutCreateSinglePostSuccessResponse() {
        Response r = given().spec(spec)
                .when().put("/posts/1")
                .then().statusCode(200).extract().response();
        responseString = r.asString();
        String id = from(responseString).getString("id");
        assertThat(id).isEqualTo("1");
    }
    
    @Test(groups = {"PUT.200", "Posts"})
    public void testPutCreateSinglePostWithId3SuccessResponse() {
        Response r = given().spec(spec)
                .when().put("/posts/3")
                .then().statusCode(200).extract().response();
        responseString = r.asString();
        String id = from(responseString).getString("id");
        assertThat(id).isEqualTo("3");
    }
    
    @Test(groups = {"POST.201", "Posts"})
    public void testPostCreateSinglePostSuccessCreated() {
        given().spec(spec).when().post("/posts").then().statusCode(201);
    }
    
    @Test(groups = {"POST.404", "Posts"})
    public void testPostCreateSinglePostWrongUrlDoubleSlash() {
        given().spec(spec).when().post("//posts").then().statusCode(404);
    }
    
    @Test(groups = {"GET.404", "Posts"})
    public void testGetSinglePostWrongUrlDoubleSlash() {
        given().when().get("http://jsonplaceholder.typicode.com//posts/1").then().statusCode(404);
    }
    
    @Test(groups = {"GET.404", "Posts"})
    public void testGetSinglePostJustWrongUrl() {
        given().spec(spec).when().get("/posts/1/123").then().statusCode(404);
    }
    @Test(groups = {"POST.500", "Posts", "text"})
    public void testPostCreateSinglePhotoContentTypeUrlEncStatus500() {
        given().spec(specMalformed).when().post("/posts").then().statusCode(500);
    }
    @Test(groups = { "localhost"})
    public void testPostOnLocalhost() {
        given().spec(specLocalhost).body("[hello...]").when().post("/foo/bar").then().statusCode(200);
        String r = given().spec(specLocalhost).body("[1,2,3]").when().post("/foo/bar")
                .then().statusCode(200).contentType(ContentType.TEXT).extract().response().asString();

        Reporter.log(String.format("Rsp body from localhost cnt-type: \"%s\".", r),true);
//        Reporter.log(String.format("Rsp body from localhost cnt-type: \"%s\".", rsp.getContentType()),true);
//        Reporter.log(String.format("Rsp body from localhost header: \"%s\".", rsp.getHeader("accept:")),true);
//        Reporter.log(String.format("Rsp body from localhost: \"%s\".", rsp.body().toString()),true);
    }
}
