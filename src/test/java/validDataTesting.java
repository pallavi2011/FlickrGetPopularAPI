import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

public class validDataTesting {
        @Test
        public void testValidData()throws IOException{
        dataDriven d = new dataDriven();
        ArrayList<String>  data= new ArrayList<String>();
        data = d.getData("GetPopularAPI","testData");

        //HashMaps to store data in key-value pairs,wherein data from excel is value
        HashMap<String, Object> jsonAsMap = new HashMap<String,Object>();
        jsonAsMap.put("user_id", data.get(1));

        dataDriven d1 = new dataDriven();
        ArrayList paramdata;
        paramdata = d1.getData("QueryParam","QueryParamData");
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

        //Validating Response using JsonPath class to parse through json response
                JsonPath js1=Utility.rawToJson(jsondata);
                String id=js1.getString("photos.photo[0].id");
                String owner=js1.getString("photos.photo[0].owner");
                Assert.assertEquals(owner, paramdata.get(1).toString());
    }}
