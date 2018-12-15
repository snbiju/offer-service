package com.offer.offerservice.service;

import com.offer.offerservice.model.Offer;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferService {

    Offer create(Offer offer);

    Offer update(Offer offer);

    Offer getById(UUID id);

    void cancel(UUID id);

    List<Offer> query(Optional<Currency> currency);
}
