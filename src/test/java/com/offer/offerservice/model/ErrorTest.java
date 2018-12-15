package com.offer.offerservice.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ErrorTest {
    @Test
    public void shouldCompareTwoErrorObjectWithSameValue(){
        Error error = new Error("some value");
        Error error1 = new Error("some value");
        Assert.assertEquals(error,error1);
    }
    @Test
    public void shouldCompareTwoErrorObjectWithDiffValue(){
        Error error = new Error("some value");
        Error error1 = new Error("other value");
        Assert.assertNotEquals(error,error1);
    }

    @Test
    public void shouldCompareHashCodeValue(){
        Error error = new Error("some value");
        Error error1 = new Error("some value");
        assertTrue(error1.hashCode()==error.hashCode());
    }

    @Test
    public void shouldCompareEqualsValue(){
        Error error = new Error("some, error,information");
        Error error1 = new Error("some, error,information");
        assertTrue(error1.toString().equals(error.toString()));
    }
}