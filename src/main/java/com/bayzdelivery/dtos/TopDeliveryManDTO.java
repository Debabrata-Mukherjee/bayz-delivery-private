package com.bayzdelivery.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopDeliveryManDTO {
    private Long deliveryManId;
    private String deliveryManName;
    private Double totalCommission;  
    private Double avgCommission;   

    public TopDeliveryManDTO(Long deliveryManId, String deliveryManName, Double totalCommission, Double avgCommission) {
        this.deliveryManId = deliveryManId;
        this.deliveryManName = deliveryManName;
        this.totalCommission = totalCommission;
        this.avgCommission = avgCommission;
    }
}
