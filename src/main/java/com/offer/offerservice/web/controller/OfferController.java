package com.offer.offerservice.web.controller;

import com.offer.offerservice.model.Offer;
import com.offer.offerservice.service.OfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(value = "http://localhost:8080/offers",description = "Offer Service Endpoint")
@RestController
@RequestMapping(path = "/offers")
public class OfferController {

    public static final Logger logger = LoggerFactory.getLogger(OfferController.class);
    private final OfferService offerService;


    @Autowired
    public OfferController(final OfferService offerService) {
        this.offerService = offerService;
    }

    @ApiOperation(value ="Create new Offer" ,position = 1)
    @ApiResponses(value = {@ApiResponse(code = 201,message = "Offer successfully created."),@ApiResponse(code =400,message = "Invalid Request")})
    @RequestMapping( method = POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Offer create(@Valid @RequestBody final Offer offer) {
        logger.info(String.format("creating offer: %s", offer));
        return offerService.create(offer);
    }

    @ApiOperation(value ="Update Offer (UUID is mandatory)" ,position = 2)
    @ApiResponses(value = {@ApiResponse(code = 200,message = "Offer has been updated."),@ApiResponse(code =404,message = "Offer Not Found!")})
    @RequestMapping(method = PUT, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Offer update(@Valid @RequestBody final Offer offer) {
        logger.info(String.format("updating offer for to: %s", offer));
        return offerService.update(offer);
    }

    @ApiOperation(value ="Offer Fetch" ,position = 3)
    @ApiResponses(value = {@ApiResponse(code = 200,message = "Offer has been fetched."),@ApiResponse(code =404,message = "Offer Not Found!")})
    @RequestMapping(path = "/{id}", method = GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Offer getById(@PathVariable("id") final UUID id) {
        logger.info(String.format("fetching offer for id: %s", id));
        return offerService.getById(id);
    }
    @ApiOperation(value ="Offer Delete" ,position = 4)
    @ApiResponses(value = {@ApiResponse(code = 204,message = "Offer has been deleted."),@ApiResponse(code =404,message = "Offer Not Found!")})
    @RequestMapping(path = "/{id}", method = DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void cancel(@PathVariable("id") final UUID id) {
        logger.info(String.format("cancelling offer for id: %s", id));
        offerService.cancel(id);
    }

    @ApiOperation(value ="Offer fetch with given currency" ,position = 5)
    @ApiResponses(value = {@ApiResponse(code = 200,message = "Offer has been fetched."),@ApiResponse(code =404,message = "Offer Not Found!")})
    @RequestMapping(method = GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Offer> query(@RequestParam(value = "currency", required = false) final Optional<Currency> currency) {
        logger.info(String.format("querying for offers (filtered by currency: %s)", currency));
        return offerService.query(currency);
    }


}
