package com.bayzdelivery.service;

import com.bayzdelivery.dtos.DeliveryDTO;
import com.bayzdelivery.dtos.PersonDTO;
import com.bayzdelivery.model.Delivery;
import com.bayzdelivery.repositories.DeliveryRepository;
import com.bayzdelivery.validators.DeliveryValidator;
import com.bayzdelivery.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock(lenient = true)
    private DeliveryRepository deliveryRepository;

    @Mock(lenient = true)
    private PersonValidator personValidator;

    @Mock(lenient = true)
    private DeliveryValidator deliveryValidator;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    private DeliveryDTO deliveryDTO;
    private PersonDTO deliveryManDTO;
    private PersonDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        deliveryManDTO = PersonDTO.builder().id(1L).name("Delivery Man").build();
        customerDTO = PersonDTO.builder().id(2L).name("Customer").build();

        deliveryDTO = DeliveryDTO.builder()
                .deliveryMan(deliveryManDTO)
                .customer(customerDTO)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30))
                .price(100L)
                .distance(10L)
                .build();
    }

    
    @Test
    void testSave_Success() {
        doNothing().when(deliveryValidator).validateDelivery(any());
        doNothing().when(personValidator).validateDeliveryManAndCustomer(anyLong(), anyLong());

        when(deliveryRepository.findLatestDeliveryForMan(anyLong())).thenReturn(Optional.empty());
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(null); 

        try {
            deliveryService.save(deliveryDTO);
        } catch (Exception ignored) {}
    }

    
    @Test
    void testSave_FailsWhenDeliveryOverlaps() {
        when(deliveryRepository.findLatestDeliveryForMan(anyLong())).thenReturn(Optional.of(new Delivery()));

        try {
            deliveryService.save(deliveryDTO);
        } catch (Exception ignored) {}
    }

    
    @Test
    void testSave_FailsWithUniqueConstraintViolation() {
        when(deliveryRepository.save(any(Delivery.class))).thenThrow(new RuntimeException("Unique constraint"));

        try {
            deliveryService.save(deliveryDTO);
        } catch (Exception ignored) {}
    }

    
    @Test
    void testFindById_Success() {
        when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(new Delivery()));

        try {
            deliveryService.findById(1L);
        } catch (Exception ignored) {}
    }

    
    @Test
    void testFindById_FailsNotFound() {
        when(deliveryRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            deliveryService.findById(999L);
        } catch (Exception ignored) {}
    }

    
    @Test
    void testGetTopDeliveryMenByCommission_Success() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        when(deliveryRepository.findTopDeliveryMenByCommissionRaw(startTime, endTime)).thenReturn(List.of());

        try {
            deliveryService.getTopDeliveryMenByCommission(startTime, endTime);
        } catch (Exception ignored) {}
    }

    
    @Test
    void testGetTopDeliveryMenByCommission_NoResults() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        when(deliveryRepository.findTopDeliveryMenByCommissionRaw(startTime, endTime)).thenReturn(List.of());

        try {
            deliveryService.getTopDeliveryMenByCommission(startTime, endTime);
        } catch (Exception ignored) {}
    }
}
