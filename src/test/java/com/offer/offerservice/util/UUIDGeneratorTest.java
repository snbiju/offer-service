package com.offer.offerservice.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class UUIDGeneratorTest {
    private UUIDGenerator underTest = new UUIDGenerator();

    @Test
    public void shouldGenerateId() {
        assertNotNull(underTest.generate());
    }

    @Test
    public void shouldGenerateDifferentIdsWithSubsequentInvocations() {
        assertNotEquals(underTest.generate(), underTest.generate());
    }


}