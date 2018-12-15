package com.offer.offerservice.repository;

import com.offer.offerservice.model.Offer;
import com.offer.offerservice.util.exception.InvalidRequestException;
import com.offer.offerservice.util.exception.OfferNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OfferRepositoryImpl implements OfferRepository {
    private final Map<UUID,Offer> offers = new LinkedHashMap<>();

    @Override
    public Offer create(final Offer offer) {
        offers.put(offer.getId(), offer);
        return offer;
    }

    @Override
    public Offer update(Offer offer) {
        if (offers.putIfAbsent(offer.getId(), offer) == null) {
            throw new OfferNotFoundException(offer.getId());
        }else {
            offers.replace(offer.getId(),offer);
        }
        return offer;
    }

    @Override
    public Optional<Offer> findById(final UUID id) {
        return Optional.ofNullable(offers.get(id));
    }

    @Override
    public void delete(final UUID id) {
        offers.remove(id);
    }

    @Override
    public List<Offer> findByQuery(final Optional<Currency> currency) {
        return offers
                .values()
                .stream()
                .filter(offer -> !currency.isPresent() || currency.get().equals(offer.getCurrency()))
                .collect(Collectors.toList());
    }
}
