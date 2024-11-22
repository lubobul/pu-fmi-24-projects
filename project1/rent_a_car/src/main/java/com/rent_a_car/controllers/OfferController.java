package com.rent_a_car.controllers;

import com.rent_a_car.dtos.OfferCreateDTO;
import com.rent_a_car.dtos.OfferDTO;
import com.rent_a_car.http.AppResponse;
import com.rent_a_car.services.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    //листване на всички оферти за даден потребител
    @GetMapping
    public ResponseEntity<?> getOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Map<String, String> filters
    ) {
        var offersPagedResponse = this.offerService.getOffers(page, pageSize, filters);
        return AppResponse
                .success()
                .withData(offersPagedResponse)
                .build();
    }

    //листване на конкретна оферта
    @GetMapping("/{id}")
    public ResponseEntity<?> getOffer(@PathVariable int id) {
        var offer = this.offerService.getOffer(id);

        if (offer == null) {
            return AppResponse
                    .error()
                    .withCode(HttpStatus.NOT_FOUND)
                    .build();
        }

        return AppResponse
                .success()
                .withData(offer)
                .build();
    }

    //създаване на нова оферта с данни за потребителя, модела на автомобила и дните за наемане
    @PostMapping
    public ResponseEntity<?> createOffer(@RequestBody OfferCreateDTO offer) {
        var createdOffer = this.offerService.createOffer(offer);

        if (createdOffer == null) {
            return AppResponse.error()
                    .withMessage("Offer not created successfully")
                    .build();
        }

        return AppResponse.success()
                .withData(createdOffer)
                .withMessage("Created offer successfully")
                .build();
    }

    //приемане на оферта - в склучай в които потребителя вземе автомобила
    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptOffer(@PathVariable int id) {

        var isAccepted = this.offerService.acceptOffer(id);

        if (!isAccepted) {
            return AppResponse.error()
                    .withMessage("Offer not accepted successfully")
                    .build();
        }
        return AppResponse
                .success()
                .build();
    }

    //изтриване на оферта
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable int id) {
        boolean isDeleteSuccessful =  this.offerService.deleteOffer(id);

        if(!isDeleteSuccessful) {
            return AppResponse.error()
                    .withMessage("Offer not deleted successfully")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Delete successful")
                .build();
    }
}
