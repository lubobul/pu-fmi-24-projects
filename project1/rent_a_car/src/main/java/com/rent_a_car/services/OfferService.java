package com.rent_a_car.services;

import com.rent_a_car.dtos.OfferDTO;
import com.rent_a_car.entities.Offer;
import com.rent_a_car.repositories.OfferRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
    private OfferRepository offerRepository;
    public OfferService(OfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }

    public Offer createOffer(OfferDTO offer) {
        return this.offerRepository.createOffer(offer);
    }

    public Offer getOffer(int id) {
        return this.offerRepository.getOfferById(id);
    }
}
