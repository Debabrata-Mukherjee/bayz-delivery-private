package com.bayzdelivery.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bayzdelivery.model.Delivery;
import com.bayzdelivery.repositories.DeliveryRepository;

@Component
public class DelayedDeliveryNotifier {

    private static final Logger LOG = LoggerFactory.getLogger(DelayedDeliveryNotifier.class);
    private final DeliveryRepository deliveryRepository;

    public DelayedDeliveryNotifier(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }
    

    @Scheduled(fixedDelay = 30000)
    public void checkDelayedDeliveries() {
        LOG.info("Checking for delayed deliveries...");

        // Find all deliveries where (endTime - startTime) > 45 minutes
        List<Delivery> delayedDeliveries = deliveryRepository.findDelayedDeliveries();

        if (!delayedDeliveries.isEmpty()) {
            LOG.warn("Found {} delayed deliveries. Notifying customer support.", delayedDeliveries.size());
            notifyCustomerSupport();
        } else {
            LOG.info("No delayed deliveries found.");
        }
    }


    
    @Async
    public void notifyCustomerSupport() {
        LOG.info("Customer support team is notified! Delayed deliveries detected.");
    }
}
