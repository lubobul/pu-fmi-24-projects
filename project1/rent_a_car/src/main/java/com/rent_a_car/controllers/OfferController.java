package com.rent_a_car.controllers;

import com.rent_a_car.http.AppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OfferController {


    @GetMapping("/offers")
    public ResponseEntity<?> fetchAllCustomers() {
        return AppResponse
                .success()
                .withData(new ArrayList<Integer>(2) {
                })
                .build();
    }
}
