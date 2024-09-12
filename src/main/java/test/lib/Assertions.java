package test.lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static void assertResponseCodeEquals(Response response, int expectedCode) {
        assertEquals(expectedCode, response.getStatusCode(), "response status code is not as expected");
    }

    public static void assertJsonElementFieldInMainNode(Response response, String expectedFieldName) {
        int size = response.jsonPath().getList("$").size();

        for (int i = 0; i < size; i++) {
            response.then().assertThat().body("[" + i + "]", hasKey(expectedFieldName));
        }
    }

    public static void assertJsonElementFieldsInMainNode(Response response, String...expectedFieldNames) {
        for(String expectedFieldName : expectedFieldNames) {
            assertJsonElementFieldInMainNode(response, expectedFieldName);
        }
    }

    public static void assertJsonElementFieldInChildNode(Response response, String arrayName, String expectedFieldName) {
        int size = response.jsonPath().getList(arrayName).size();

        for (int i = 0; i < size; i++) {
            response.then().assertThat().body(arrayName + "[" + i + "]", hasKey(expectedFieldName));
        }
    }

    public static void assertJsonElementsFieldsInChildNode(Response response, String arrayName, String...expectedFieldNames) {
        for(String expectedFieldName : expectedFieldNames) {
            assertJsonElementFieldInChildNode(response, arrayName, expectedFieldName);
        }
    }

    public static void assertNoNullValues(Response response) {
        response.then().assertThat().body("$", not(hasValue(nullValue())));
    }

    public static void assertNoNullValuesInMainNode(Response response) {
        int size = response.jsonPath().getList("$").size();

        for (int i = 0; i < size; i++) {
            response.then().assertThat().body("[" + i + "]", not(hasValue(nullValue())));
        }
    }

    public static void assertNoNullValuesInChildNode(Response response, String arrayName) {
        int size = response.jsonPath().getList(arrayName).size();

        for (int i = 0; i < size; i++) {
            response.then().assertThat().body(arrayName + "[" + i + "]", not(hasValue(nullValue())));
        }
    }
}
