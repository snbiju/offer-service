package com.offer.offerservice.web.controller;

import com.offer.offerservice.model.Offer;
import com.offer.offerservice.service.OfferService;
import com.offer.offerservice.util.exception.InvalidRequestException;
import com.offer.offerservice.util.exception.OfferNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfferControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OfferService mockOfferService;

    @Test
    public void shouldReceiveSuccessFromCreate() {
        //Given
        final String description = "description";
        final String price = "100.00";
        final String currency = "GBP";
        final Instant expiryTime = Instant.parse("2018-12-28T22:25:51Z");
        final Offer create = new Offer(null, description, new BigDecimal(price), Currency.getInstance(currency), expiryTime);
        final Offer created = new Offer(UUID.randomUUID(), description, new BigDecimal(price), Currency.getInstance(currency), expiryTime);
        final String requestJson = String.format("{\"description\":\"%s\",\"price\":%s,\"currency\":\"%s\",\"expiryTime\":\"%s\"}", description, price, currency, expiryTime);
        final String expectedResponseJson = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"price\":%s,\"currency\":\"%s\",\"expiryTime\":\"%s\",\"expired\":false}", created.getId().toString(), description, price, currency, expiryTime);
        when(mockOfferService.create(create)).thenReturn(created);

        final ResponseEntity<String> response = restTemplate.exchange("/offers", HttpMethod.POST, requestEntity(requestJson), String.class);
        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponseJson, response.getBody());
    }

    @Test
    public void shouldReceiveSuccessFromUpdate() {

        //Given
        final UUID id = UUID.randomUUID();
        final String description = "description";
        final String price = "100.00";
        final String currency = "GBP";
        final String expiryTime = "2018-12-28T22:25:51Z";
        final Offer offer = new Offer(id, description, new BigDecimal(price), Currency.getInstance(currency), Instant.parse(expiryTime));
        final String offerResponse = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"price\":%s,\"currency\":\"%s\",\"expiryTime\":\"%s\",\"expired\":false}", id, description, price, currency, expiryTime);
        when(mockOfferService.update(offer)).thenReturn(offer);

        final ResponseEntity<String> response = restTemplate.exchange(String.format("/offers"), HttpMethod.PUT, requestEntity(offerResponse), String.class);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offerResponse, response.getBody());
    }

    @Test
    public void shouldReceiveSuccessFromGetById() {

        //Given
        final UUID id = UUID.randomUUID();
        final String description = "description";
        final String price = "100.00";
        final String currency = "GBP";
        final String expiryTime = "2018-12-28T22:25:51Z";
        final Offer offer = new Offer(id, description, new BigDecimal(price), Currency.getInstance(currency), Instant.parse(expiryTime));
        final String offerJson = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"price\":%s,\"currency\":\"%s\",\"expiryTime\":\"%s\",\"expired\":false}", id, description, price, currency, expiryTime);
        when(mockOfferService.getById(id)).thenReturn(offer);

        final ResponseEntity<String> response = restTemplate.getForEntity(String.format("/offers/%s", id), String.class);
        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offerJson, response.getBody());
    }

    @Test
    public void shouldReceiveNotFoundFromGetByIdForUnknownId() {
        //Given
        final UUID id = UUID.randomUUID();
        when(mockOfferService.getById(id)).thenThrow(new OfferNotFoundException(id));

        final ResponseEntity<String> response = restTemplate.getForEntity(String.format("/offers/%s", id), String.class);
        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("\"message\":\"offer does not exist for id: %s\"", id),  messageFormat(response.getBody()));
    }

    @Test
    public void shouldReceiveBadRequestIfDescriptionIsMissing() {
        //Given
        final String price = "100.00";
        final String currency = "GBP";
        final String expiryTime = "2018-12-28T22:25:51Z";
        final String requestJson = String.format("{\"price\":%s,\"currency\":\"%s\",\"expiryTime\":\"%s\"}", price, currency, expiryTime);

        final ResponseEntity<String> response = restTemplate.exchange("/offers", HttpMethod.POST, requestEntity(requestJson), String.class);
        //Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"error\":\"description must not be null\"}", response.getBody());
    }

    @Test
    public void shouldReceiveBadRequestIfTryToCreateOfferWithInvalidRequest() {
        //Given
        final UUID id = UUID.randomUUID();
        final String description = "description";
        final String price = "100.00";
        final String currency = "GBP";
        final String expiryTime = "2018-12-28T22:25:51Z";
        final Offer offerToCreate = new Offer(id, description, new BigDecimal(price), Currency.getInstance(currency), Instant.parse(expiryTime));
        final String requestJson = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"price\":%s,\"currency\":\"%s\",\"expiryTime\":\"%s\"}", id.toString(), description, price, currency, expiryTime);
        when(mockOfferService.create(offerToCreate)).thenThrow(new InvalidRequestException("some error message"));

        final ResponseEntity<String> response = restTemplate.exchange("/offers", HttpMethod.POST, requestEntity(requestJson), String.class);
        //Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("\"message\":\"some error message\"",  messageFormat(response.getBody()));
    }

    @Test
    public void shouldReceiveInternalServerErrorWhenUnexpectedIssueOccurred() {
        //Given
        final String description = "description";
        final String price = "100.00";
        final String currency = "GBP";
        final String expiryTime = "2018-12-28T22:25:51Z";
        final Offer offerToCreate = new Offer(null, description, new BigDecimal(price), Currency.getInstance(currency), Instant.parse(expiryTime));
        final String requestJson = String.format("{\"description\":\"%s\",\"price\":%s,\"currency\":\"%s\",\"expiryTime\":\"%s\"}", description, price, currency, expiryTime);
        when(mockOfferService.create(offerToCreate)).thenThrow(new RuntimeException("Some Issue occurred!"));

        final ResponseEntity<String> response = restTemplate.exchange("/offers", HttpMethod.POST, requestEntity(requestJson), String.class);
        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("\"message\":\"Some Issue occurred!\"",  messageFormat(response.getBody()));
    }

    @Test
    public void shouldReceiveSuccessForCancelRequest() {
        //Given
        final UUID id = UUID.randomUUID();

        final ResponseEntity<String> response = restTemplate.exchange(String.format("/offers/%s", id), HttpMethod.DELETE, null, String.class);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(mockOfferService, times(1)).cancel(id);
    }

    @Test
    public void shouldReceiveBadRequestIfTryToDeleteWithInvalidRequest() {
        //Given
        final UUID id = UUID.randomUUID();
        doThrow(new InvalidRequestException("error message")).when(mockOfferService).cancel(id);

        final ResponseEntity<String> response = restTemplate.exchange(String.format("/offers/%s", id), HttpMethod.DELETE, null, String.class);
        //Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("\"message\":\"error message\"", messageFormat(response.getBody()));
    }

    private String messageFormat(String response){
        String msg ="";
        if(!response.isEmpty()) {
            String [] values= response.split(",");
            if(values.length>=3){
                msg=values[3];
            }
        }
        return msg;
    }

    private HttpEntity<String> requestEntity(final String body) {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        requestHeaders.add(HttpHeaders.ACCEPT, "application/json");
        return new HttpEntity<>(body, requestHeaders);
    }
}
