package com.bayzdelivery.service;

import com.bayzdelivery.dtos.DeliveryDTO;
import com.bayzdelivery.dtos.TopDeliveryManDTO;
import com.bayzdelivery.model.Delivery;
import com.bayzdelivery.repositories.DeliveryRepository;
import com.bayzdelivery.validators.DeliveryValidator;
import com.bayzdelivery.validators.PersonValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final PersonValidator personValidator;
    private final DeliveryValidator deliveryValidator;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository,PersonValidator personValidator, DeliveryValidator deliveryValidator) {
        this.deliveryRepository = deliveryRepository;
        this.personValidator = personValidator;
        this.deliveryValidator = deliveryValidator;
    }

    @Override
    @Transactional
    public DeliveryDTO save(DeliveryDTO deliveryDTO) {



        Long deliveryManId = deliveryDTO.getDeliveryMan().getId();
        Long customerId = deliveryDTO.getCustomer().getId();
        LocalDateTime newStartTime = deliveryDTO.getStartTime();

        deliveryValidator.validateDelivery(deliveryDTO);

        personValidator.validateDeliveryManAndCustomer(deliveryManId,customerId);
    
        // Lock the latest delivery for the same delivery man to prevent race conditions
        Optional<Delivery> latestDeliveryOpt = deliveryRepository.findLatestDeliveryForMan(deliveryManId);
    
        if (latestDeliveryOpt.isPresent()) {
            Delivery latestDelivery = latestDeliveryOpt.get();
    
            // Prevent overlapping deliveries
            if (newStartTime.isBefore(latestDelivery.getEndTime())) {
                throw new IllegalStateException("Delivery man has an ongoing delivery.");
            }
        }
    
        // Save the new delivery safely
        Delivery newDelivery = deliveryDTO.toEntity();
        Delivery savedDelivery = deliveryRepository.save(newDelivery);
    
        return DeliveryDTO.fromEntity(savedDelivery);
    }


    @Override
    public DeliveryDTO findById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .map(DeliveryDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with ID " + deliveryId));
    }

    @Override
    public List<TopDeliveryManDTO> getTopDeliveryMenByCommission(LocalDateTime startTime, LocalDateTime endTime) {
        List<Object[]> results = deliveryRepository.findTopDeliveryMenByCommissionRaw(startTime, endTime);

        return results.stream().map(row -> new TopDeliveryManDTO(
                ((Number) row[0]).longValue(),  // deliveryManId
                (String) row[1],                // deliveryManName
                ((Number) row[2]).doubleValue(), // totalCommission
                ((Number) row[3]).doubleValue()  // avgCommission
        )).toList();
    }
}
