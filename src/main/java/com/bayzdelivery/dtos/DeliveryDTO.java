package com.bayzdelivery.dtos;

import com.bayzdelivery.model.Delivery;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DeliveryDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long distance;
    private Long price;
    private Long commission;
    private PersonDTO deliveryMan;
    private PersonDTO customer;

    
    public static DeliveryDTO fromEntity(Delivery delivery) {
        return DeliveryDTO.builder()
                .id(delivery.getId())
                .startTime(delivery.getStartTime())
                .endTime(delivery.getEndTime())
                .distance(delivery.getDistance())
                .price(delivery.getPrice())
                .commission(calculateCommission(delivery.getPrice(), delivery.getDistance()))
                .deliveryMan(PersonDTO.fromEntity(delivery.getDeliveryMan()))
                .customer(PersonDTO.fromEntity(delivery.getCustomer()))
                .build();
    }

    
    public Delivery toEntity() {
        Delivery delivery = new Delivery();
        delivery.setId(this.id);
        delivery.setStartTime(this.startTime);
        delivery.setEndTime(this.endTime);
        delivery.setDistance(this.distance);
        delivery.setPrice(this.price);
        delivery.setCommission(calculateCommission(this.price, this.distance));
        delivery.setDeliveryMan(this.deliveryMan.toEntity());
        delivery.setCustomer(this.customer.toEntity());
        return delivery;
    }

    
    private static Long calculateCommission(Long price, Long distance) {
        if (price == null || distance == null) return 0L;
        return Math.round(price * 0.05 + distance * 0.5);
    }
}

