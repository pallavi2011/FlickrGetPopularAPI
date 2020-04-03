import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class invalidDataTesting {

    @Test
    public void testInvalidData()throws IOException {
        dataDriven d = new dataDriven();
        ArrayList<String> data= new ArrayList<String>();
        data = d.getData("GetPopularAPI","testData");

        //HashMaps to store data in key-value pairs,wherein data from excel is value
        HashMap<String, Object> jsonAsMap = new HashMap<String,Object>();
        jsonAsMap.put("user_id", data.get(1));

        dataDriven d1 = new dataDriven();
        ArrayList paramdata;
        paramdata = d1.getData("QueryParam","Invalid Data");
        HashMap<String, Object> queryparam = new HashMap();
        queryparam.put("user_id", paramdata.get(1));
        queryparam.put("method", paramdata.get(2));
        queryparam.put("api_key", paramdata.get(3));
        queryparam.put("format", paramdata.get(4));
        queryparam.put("nojsoncallback", paramdata.get(5));


        RestAssured.baseURI = "https://www.flickr.com/";
        String jsondata = given().log().all().queryParams(queryparam).header("Content-Type", "application/json")
                .body(jsonAsMap)
                .when().post("services/rest/")
                .then().assertThat().statusCode(200).extract().asString();
        System.out.println(jsondata);

        JsonPath js1=Utility.rawToJson(jsondata);
        String stat=js1.getString("stat");
        String code=js1.getString("code");
        String msg=js1.getString("message");
        Assert.assertEquals(stat,"fail");
    }}

