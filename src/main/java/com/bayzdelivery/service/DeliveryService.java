package com.bayzdelivery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.bayzdelivery.dtos.DeliveryDTO;
import com.bayzdelivery.dtos.TopDeliveryManDTO;

public interface DeliveryService {

    /**
     * Saves a new delivery, ensuring no overlapping deliveries for the same delivery man.
     * @param deliveryDTO The delivery details.
     * @return The saved DeliveryDTO.
     */
    DeliveryDTO save(DeliveryDTO deliveryDTO);

    /**
     * Retrieves a delivery by its ID.
     * @param deliveryId The ID of the delivery.
     * @return The DeliveryDTO if found.
     * @throws ResourceNotFoundException if the delivery is not found.
     */
    DeliveryDTO findById(Long deliveryId);

    List<TopDeliveryManDTO> getTopDeliveryMenByCommission(LocalDateTime startTime, LocalDateTime endTime);
}
