package com.offer.offerservice.model;

import java.util.Objects;

public class Error {

    private final String error;

    public Error(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Error)) return false;
        Error error1 = (Error) o;
        return Objects.equals(getError(), error1.getError());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getError());
    }

    @Override
    public String toString() {
        return "Error{" +
                "error='" + error.split(",")[2] + '\'' +
                '}';
    }
}
