package com.offer.offerservice.service;

import com.offer.offerservice.model.Offer;
import com.offer.offerservice.repository.OfferRepository;
import com.offer.offerservice.util.IDGenerator;
import com.offer.offerservice.util.exception.InvalidRequestException;
import com.offer.offerservice.util.exception.OfferNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OfferServiceImplTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private OfferServiceImpl offerService;

    @Mock
    private OfferRepository mockOfferRepository;

    @Mock
    private IDGenerator<UUID> mockOfferIdGenerator;

    @Before
    public void setUp() {
        this.offerService = new OfferServiceImpl(mockOfferRepository, mockOfferIdGenerator);
    }

    @Test
    public void shouldCreateValidOffer() {

        // given
        final UUID id = UUID.randomUUID();
        when(mockOfferIdGenerator.generate()).thenReturn(id);
        final Offer offerToCreate = new Offer(null, "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());
        final Offer createdOffer = new Offer(id, "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());
        when(mockOfferRepository.create(createdOffer)).thenReturn(createdOffer);

        final Offer response = offerService.create(offerToCreate);

        assertNotNull(response.getId());
        assertEquals(createdOffer.getId(), response.getId());
        assertEquals(createdOffer.getDescription(), response.getDescription());
        assertEquals(createdOffer.getPrice(), response.getPrice());
        assertEquals(createdOffer.getCurrency(), response.getCurrency());
        assertEquals(createdOffer.getExpiryTime(), response.getExpiryTime());
    }

    @Test(expected = InvalidRequestException.class)
    public void shouldRejectOfferCreationIfIdIsPopulated() {

        // given
        final Offer offer = new Offer(UUID.randomUUID(), "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());

        offerService.create(offer);
    }

    @Test(expected = InvalidRequestException.class)
    public void shouldRejectOfferCreationIfIdInPayloadDoesNotMatchIdInURI() {
        // given
        final Offer offer = new Offer(null, "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());
        offerService.update(offer);
    }

    @Test
    public void shouldUpdateValidOffer() {

        // given
        final UUID id = UUID.randomUUID();
        final Offer offer = new Offer(id, "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());
        when(mockOfferIdGenerator.generate()).thenReturn(id);
        when(mockOfferRepository.update(offer)).thenReturn(offer);

        final Offer result = offerService.update(offer);

        assertEquals(offer, result);
    }

    @Test
    public void shouldGetOfferById() {

        // given
        final UUID id = UUID.randomUUID();
        final Offer offer = new Offer(id, "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());
        when(mockOfferRepository.findById(id)).thenReturn(Optional.of(offer));

        final Offer result = offerService.getById(id);

        assertEquals(offer, result);
    }

    @Test
    public void shouldCancelOfferById() {

        // given
        final UUID id = UUID.randomUUID();
        final Offer offer = new Offer(id, "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now().plus(1, ChronoUnit.HOURS));
        when(mockOfferRepository.findById(id)).thenReturn(Optional.of(offer));


        offerService.cancel(id);

        verify(mockOfferRepository, times(1)).delete(id);
    }

    @Test
    public void shouldQueryForOffers() {

        // given
        final Offer offer1 = new Offer(UUID.randomUUID(), "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());
        final Offer offer2 = new Offer(UUID.randomUUID(), "other description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now());
        final List<Offer> offers = Arrays.asList(offer1, offer2);
        when(mockOfferRepository.findByQuery(Optional.empty())).thenReturn(offers);

        // get
        final List<Offer> result = offerService.query(Optional.empty());

        assertEquals(offers, result);
    }

    @Test
    public void shouldAllowForCancelOfOfferThatDoesntExistInOfferToProvideIdempotency() {

        // given
        final UUID id = UUID.randomUUID();
        when(mockOfferRepository.findById(id)).thenReturn(Optional.empty());

        // get
        offerService.cancel(id);
    }

    @Test(expected = InvalidRequestException.class)
    public void shouldPreventCancellationOfOfferThatIsExpired() {

        // given
        final UUID id = UUID.randomUUID();
        final Offer offer = new Offer(id, "description", new BigDecimal("100.00"), Currency.getInstance("GBP"), Instant.now().minus(1, ChronoUnit.HOURS));
        when(mockOfferRepository.findById(id)).thenReturn(Optional.of(offer));

        // get
        offerService.cancel(id);
    }


    @Test(expected = OfferNotFoundException.class)
    public void shouldThrowOfferNotFoundExceptionIfNoOfferExists() {


        final UUID id = UUID.randomUUID();
        when(mockOfferRepository.findById(id)).thenReturn(Optional.empty());

        offerService.getById(id);
    }
}