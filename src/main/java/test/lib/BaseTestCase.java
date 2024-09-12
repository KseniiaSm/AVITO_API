package test.lib;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;


public class BaseTestCase {
    protected String getStringFromJson(Response response, String name) {
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().getString(name);
    }

    protected String parsePublicationIdFromResponse(Response response, String fieldName) {
        return getStringFromJson(response, fieldName).split(" - ")[1].trim();
    }
}
