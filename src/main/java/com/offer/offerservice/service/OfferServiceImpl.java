package com.offer.offerservice.service;

import com.offer.offerservice.model.Offer;
import com.offer.offerservice.repository.OfferRepository;
import com.offer.offerservice.util.IDGenerator;
import com.offer.offerservice.util.exception.InvalidRequestException;
import com.offer.offerservice.util.exception.OfferNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final IDGenerator<UUID> offerIdGenerator;

    @Autowired
    public OfferServiceImpl(
            final OfferRepository offerRepository,
            final IDGenerator<UUID> offerIdGenerator) {
        this.offerRepository = offerRepository;
        this.offerIdGenerator = offerIdGenerator;
    }
    public Offer create(Offer offer) {
       if (offer.getId() != null) {
           System.out.println(offer.getId());
            throw new InvalidRequestException("cannot create an offer with a pre-populated 'id' value");
        }
        return offerRepository.create(
                new Offer(
                        offerIdGenerator.generate(),
                        offer.getDescription(),
                        offer.getPrice(),
                        offer.getCurrency(),
                        offer.getExpiryTime()));
    }

    @Override
    public Offer update(Offer offer) {
        if (offer.getId()==null) {
            throw new InvalidRequestException("id in payload must match id from uri");
        }
        return offerRepository.update(offer);
    }

    @Override
    public Offer getById(final UUID id) {
        return offerRepository.findById(id).orElseThrow(() -> new OfferNotFoundException(id));
    }


    @Override
    public void cancel(UUID id) {
        offerRepository.findById(id).ifPresent(offer -> {
            if (!offer.isExpired()) {
                offerRepository.delete(id);
            } else {
                throw new InvalidRequestException("cannot cancel an offer that has expired");
            }
        });
    }

    @Override
    public List<Offer> query(final Optional<Currency> currency) {
        return offerRepository.findByQuery(currency);
    }

}
