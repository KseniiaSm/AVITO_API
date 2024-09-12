package test.tests;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import test.lib.Assertions;
import test.lib.BaseTestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.lib.DataGenerator.getRandomInt;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublicationTests extends BaseTestCase {

    final int sellerId = 956148;
    Map<String, Object> mainParamsReq = new HashMap<>();
    Map<String, Object> statisticsReq = new HashMap<>();

    @Test
    @Order(1)
    public void postPositivePublicationTest() {
        statisticsReq.put("contacts", getRandomInt(1, 100));
        statisticsReq.put("likes", getRandomInt(1, 100));
        statisticsReq.put("viewCount", getRandomInt(1, 100));

        mainParamsReq.put("name", "Телефон");
        mainParamsReq.put("price", getRandomInt(1000, 10000));
        mainParamsReq.put("sellerId", sellerId);
        mainParamsReq.put("statistics", statisticsReq);

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(mainParamsReq)
                .post("https://qa-internship.avito.com/api/1/item")
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 200);
        Assertions.assertNoNullValues(response);

        String publicationId = parsePublicationIdFromResponse(response, "status");
        mainParamsReq.put("id", publicationId);
    }

    @Test
    public void getPublicationByIdTest() {
        Response response = RestAssured
                .given()
                .get("https://qa-internship.avito.com/api/1/item/" + mainParamsReq.get("id"))
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 200);

        Assertions.assertNoNullValuesInMainNode(response);
        Assertions.assertNoNullValuesInChildNode(response, "statistics");

        Map<String, Object> mainParamsRes = response.path("[0]");

        assertThat(mainParamsRes.get("id"), equalTo(mainParamsReq.get("id")));
        assertThat(mainParamsRes.get("name"), equalTo("Телефон"));
        assertThat(mainParamsRes.get("price"), equalTo(mainParamsReq.get("price")));
        assertThat(mainParamsRes.get("sellerId"), equalTo(sellerId));

        Map<String, Object> statisticsRes = response.path("[0].statistics");

        assertThat(statisticsRes.get("contacts"), equalTo(statisticsReq.get("contacts")));
        assertThat(statisticsRes.get("likes"), equalTo(statisticsReq.get("likes")));
        assertThat(statisticsRes.get("viewCount"), equalTo(statisticsReq.get("viewCount")));
    }

    @Test
    public void getAllSellerPublicationsByIdTest() {

        Response response = RestAssured
                .given()
                .get("https://qa-internship.avito.com/api/1/" + sellerId + "/item")
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 200);

        Assertions.assertNoNullValuesInMainNode(response);
        Assertions.assertNoNullValuesInChildNode(response, "statistics");

        Assertions.assertJsonElementFieldsInMainNode(response, "createdAt", "id", "name", "price", "sellerId", "statistics");
        Assertions.assertJsonElementsFieldsInChildNode(response, "statistics", "contacts", "likes", "viewCount");

        List<Integer> sellerIds = response.path("sellerId");
        for(int id : sellerIds) {
            org.junit.jupiter.api.Assertions.assertEquals(id, sellerId, sellerId + " is wrong for publication");
        }
    }
}
