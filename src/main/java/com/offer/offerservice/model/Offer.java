package com.offer.offerservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public class Offer {

    @ApiModelProperty(notes = "Id",value = "bf113236-5a50-40d1-9ba5-93d050710ca0" ,position = 1)
    private final UUID id;

    @ApiModelProperty(notes ="Description", value = "some description",position = 2)
    @NotNull
    @Size(min = 3, max = 50)
    private final String description;

    @ApiModelProperty(notes ="Price", value = "110.60",position = 3)
    @NotNull
    private final BigDecimal price;

    @ApiModelProperty(notes ="Currency", value = "GBP/USD",position = 4)
    @NotNull
    private final Currency currency;

    @ApiModelProperty(notes ="Expiry Time", value = "2019-12-01T00:00:00Z",position = 5)
    @NotNull
    private final Instant expiryTime;

    @JsonCreator
    public Offer(
            @JsonProperty("id") final UUID id,
            @JsonProperty("description") final String description,
            @JsonProperty("price") final BigDecimal price,
            @JsonProperty("currency") final Currency currency,
            @JsonProperty("expiryTime") final Instant expiryTime) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.expiryTime = expiryTime;
    }
    public boolean isExpired() {
        return this.expiryTime.isBefore(Instant.now());
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offer)) return false;
        Offer offer = (Offer) o;
        return Objects.equals(getId(), offer.getId()) &&
                Objects.equals(getDescription(), offer.getDescription()) &&
                Objects.equals(getPrice(), offer.getPrice()) &&
                Objects.equals(getCurrency(), offer.getCurrency()) &&
                Objects.equals(getExpiryTime(), offer.getExpiryTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getPrice(), getCurrency(), getExpiryTime());
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", expiryTime=" + expiryTime +
                '}';
    }
}
