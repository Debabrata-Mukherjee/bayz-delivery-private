package com.bayzdelivery.validators;

import org.springframework.stereotype.Component;

import com.bayzdelivery.dtos.DeliveryDTO;

@Component
public class DeliveryValidator {

    public void validateDelivery(DeliveryDTO deliveryDTO) {
        if (deliveryDTO == null) {
            throw new IllegalArgumentException("Delivery details cannot be null");
        }
        
        if (deliveryDTO.getDeliveryMan() == null || deliveryDTO.getDeliveryMan().getId() == null) {
            throw new IllegalArgumentException("Delivery must have a valid delivery man");
        }

        if (deliveryDTO.getCustomer() == null || deliveryDTO.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Delivery must have a valid customer");
        }

        if (deliveryDTO.getStartTime() == null || deliveryDTO.getEndTime() == null) {
            throw new IllegalArgumentException("Delivery must have a start and end time");
        }

        if (deliveryDTO.getEndTime().isBefore(deliveryDTO.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        if (deliveryDTO.getPrice() == null) {
            throw new IllegalArgumentException("Delivery must have a valid price");
        }

        if (deliveryDTO.getDistance() == null) {
            throw new IllegalArgumentException("Delivery must have a valid distance");
        }
    }
}