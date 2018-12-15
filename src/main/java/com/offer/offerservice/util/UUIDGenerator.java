package com.offer.offerservice.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator implements IDGenerator<UUID> {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
