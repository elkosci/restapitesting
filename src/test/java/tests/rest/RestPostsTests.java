package tests.rest;
import org.json.simple.JSONValue;
import tests.DTO.CommentDTO;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;
import org.testng.annotations.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsCollectionContaining.hasItems;


public class RestPostsTests extends RestApiBaseTest {
    @Test(groups = {"GET.200", "Posts"})
    public void testGetMultipleItemsSuccessStatusCode() {
        given().spec(spec).when().get("/posts").then().statusCode(200);
    }

    @Test(groups = {"GET.200", "response", "Posts"})
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
    public void testGetMultiplePostsSuccess() {
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

    @Parameters(value="postId")
    @Test(groups = {"GET.200", "Posts"})
    public void testGetSinglePostViaDtoClassWithParameter(int postId) {
        CommentDTO cdto = given().spec(spec)
                .when().get(String.format("/posts/%d", postId))
                .then().statusCode(200).extract().as(CommentDTO.class);

        assertEqualBlog(cdto_test, cdto);
    }
    private static CommentDTO cdto_test =
            new CommentDTO()
                    .setId("1")
                    .setUserId(1)
                    .setTitle("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
                    .setBody("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");

    private void assertEqualBlog(CommentDTO testObject, CommentDTO retrievedObject){
        assertThat(testObject.getBody()).isEqualTo(retrievedObject.getBody());
        assertThat(testObject.getId()).isEqualTo(retrievedObject.getId());
        assertThat(testObject.getUserId()).isEqualTo(retrievedObject.getUserId());
        assertThat(testObject.getTitle()).isEqualTo(retrievedObject.getTitle());
    }

    @Test(groups = {"GET.200", "localhost44", "Posts"} /*, threadPoolSize = 3, invocationCount = 6,  timeOut = 1000*/)
    public void testGetMultiplePostsCheckBody() {
        given().spec(specLocalhost).body("[1,2,3]")
                .when().get("/")
                .then().statusCode(200).body("headers.content-length", equalTo("7"));
        given().spec(specLocalhost).body("[1,2,3]")
                .when().get("/")
                .then().statusCode(200).body("body", containsString("_server_addon"));
        given().spec(specLocalhost).body("[1,2,3]")
                .when().get("/")
                .then().statusCode(200).body("body".split(",")[0], containsString("1"));
        JsonPath jspath = given().spec(specLocalhost).body("[1,2,3]")
                .when().get("/")
                .then().statusCode(200).extract().response().getBody().jsonPath();

        String clearBody = jspath.get("body").toString().replace("_server_addon", "");
        System.out.println("body: "+ clearBody);
    }

    @Test(groups = {"localhost"})
    public void testPostOnLocalhostStringBodyStatusCode() {
        given().spec(specLocalhost).body("Stringus").when().post("/foo/bar").then().statusCode(200);
    }
    @Test(groups = {"localhost"})
    public void testPostOnLocalhostJsonBodyStatusCode() {
        given().spec(specLocalhost).body("{\"id\": \"hello...\"}")
                .when().post("/foo/bar")
                .then().statusCode(200).contentType(ContentType.JSON);

    }

    @Test(groups = {"localhost"})
    public void testPostOnLocalhostResponseAsString() {
        try {
            String responseString =
                    given().spec(specLocalhost).body("{\"id\":\"slowo\"}")
                            .when().post("/")
                            .then().statusCode(200)
                            .contentType(ContentType.JSON)
                            .header("Content-Type", "application/json")
                            .extract().response().asString();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
            String body = (String) jsonObject.get("body");
            assertThat(body).containsIgnoringCase("SLOWO");
            Reporter.log(String.format("Rsp body from localhost resp: \"%s\".", responseString), true);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
    @Test(groups = {"localhost"})
    public void testPostOnLocalhostResponseAsJsonPath() {
        JSONObject o = new JSONObject();
        o.put("id", "some text to manipulate");
        String textToSend = JSONValue.toJSONString(o);
        JsonPath jsonPath =
                given().spec(specLocalhost).body(textToSend)
                .when().get("/")
                .then().statusCode(200).extract().response().jsonPath();
        Map<String, String> list = jsonPath.getMap("headers");
        String body = jsonPath.getString("body");
        Reporter.log("Version with json: ", true);
        Reporter.log("body: " + body, true);
        assertThat(body.replace("_server_addon", "")).isEqualToIgnoringCase(textToSend);
        for (String s : list.keySet()) {
            Reporter.log("val: " + s + " " + list.get(s), true);
        }
        assertThat(list.get("connection")).isEqualTo("Keep-Alive");
        assertThat(list.get("content-type")).contains("application/json; charset=");
        assertThat(list.size()).isNotEqualTo(0);
    }

    @Test(groups = {"localhost"})
    public void testPostOnLocalhostTextParsingOutput() {
        /*
        * @author: elkosci
        * @purpose: parsing output
        *
        * */
        JSONObject o =
                new JSONObject();
        o.put("id", "some text to manipulate");
        String textToSend =
                JSONValue.toJSONString(o);
        Response response =
                given().spec(specLocalhostText).body(textToSend)
                .when().get("/")
                .then().statusCode(200)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        Map<String, String> list = jsonPath.getMap("headers");
        String body1 = jsonPath.getString("body");
        String new_body = body1;
        Reporter.log("body1: " + body1,  true);
        assertThat(body1).isEqualToIgnoringCase(textToSend + "_server_addon");
        for (String s : list.keySet()) {
            Reporter.log("val: " + s + " " + list.get(s), true);
        }
        assertThat(list.get("connection")).isEqualTo("Keep-Alive");
        assertThat(list.get("content-type")).contains("text/plain; charset=");
        Reporter.log("new_body: " + new_body, true);
        Response response2 =
                given().spec(specLocalhostText).body(new_body)
                .when().get("/")
                .then().statusCode(200)
                .extract().response();
        JsonPath jsonPath2 = response2.jsonPath();
        String body2 = jsonPath2.getString("body");
        Reporter.log("body2: " + body2, true);
        assertThat(body2).isEqualToIgnoringCase("{\"id\":\"some text to manipulate\"}_server_addon_server_addon");
        assertThat(body2).isEqualToIgnoringCase(textToSend + "_server_addon_server_addon");
    }
    @Test(groups = {"localhost"})
    public void testGetOnLocalhostSendParams() {
        String responseString =
                given().spec(specLocalhost)
                        .when().get("/?username=Kostek&fullname=elkosci")
                        .then().statusCode(200)
                        .contentType(ContentType.JSON)
                        .header("Content-Type", "application/json")
                        .extract().response().asString();
        Reporter.log(String.format("Rsp body from localhost resp: \"%s\".", responseString), true);
        String[] user = {"kostek", "elkosci"};
            given().spec(specLocalhost)
                    .when().get(String.format("/?username=%s&fullname=%s",user[0], user[1]))
                    .then().statusCode(200)
                    .contentType(ContentType.JSON)
//                    .header("Content-Type", "application/json")
                    .body("body", containsString(String.format("Hello %s %s",user[0], user[1])));
    }
    @Test(groups = {"localhost"})
    public void testPostOnLocalhostSendParams() {
        JSONObject o =
                new JSONObject();
        o.put("username", "Kostek123123");
        o.put("fullname", "Lukasz");
        String textToSend =
                JSONValue.toJSONString(o);
        given().spec(specLocalhost).body(textToSend)
                .when().post("/")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .body("body", containsString(String.format("Hello %s %s", o.get("username"), o.get("fullname"))));

        String responseHello = given().spec(specLocalhost).body(textToSend)
                .when().post("/")
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .header("Content-Type", "application/json").extract().body().asString();
        String bodyPart = from(responseHello).get("body");
        assertThat(bodyPart).isEqualTo(String.format("Hello %s %s", o.get("username"), o.get("fullname")));
        Reporter.log(String.format("Rsp body from localhost via POST resp: \"%s\".", responseHello), true);
        Reporter.log(String.format("Rsp body from localhost via POST body: \"%s\".", bodyPart), true);
    }

}
