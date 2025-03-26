package com.bayzdelivery.controller;

import com.bayzdelivery.dtos.DeliveryDTO;
import com.bayzdelivery.dtos.TopDeliveryManDTO;
import com.bayzdelivery.service.DeliveryService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<?> createNewDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        try {
            DeliveryDTO savedDelivery = deliveryService.save(deliveryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDelivery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving delivery: " + e.getMessage());
        }
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<?> getDeliveryById(@PathVariable Long deliveryId) {
        try {
            DeliveryDTO delivery = deliveryService.findById(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching delivery details.");
        }
    }

    @GetMapping("/top-earners")
    public ResponseEntity<?> getTopDeliveryMen(
        @RequestParam("start") String startTime,
        @RequestParam("end") String endTime) {
    try {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);

        List<TopDeliveryManDTO> topDeliveryMen = deliveryService.getTopDeliveryMenByCommission(start, end);

        if (topDeliveryMen.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No top delivery men found for the given time range.");
        }

        return ResponseEntity.ok(topDeliveryMen);
    } catch (DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid date format. Use ISO-8601 format (e.g., 2022-03-01T00:00:00). Error: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing the request: " + e.getMessage());
    }
}
}
