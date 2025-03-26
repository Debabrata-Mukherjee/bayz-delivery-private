package com.bayzdelivery.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bayzdelivery.model.Delivery;

import java.time.LocalDateTime;
import java.util.List;
// import java.util.Optional;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // Lock only if the same delivery man is involved
    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    // @Query("SELECT d FROM Delivery d WHERE d.deliveryMan.id = :deliveryManId ORDER BY d.endTime DESC")
    // Optional<Delivery> findLatestDeliveryForMan(@Param("deliveryManId") Long deliveryManId);

    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    // @Query("SELECT COUNT(d) FROM Delivery d WHERE d.deliveryMan.id = :deliveryManId AND :startTime < d.endTime")
    // long countOverlappingDeliveries(@Param("deliveryManId") Long deliveryManId, @Param("startTime") LocalDateTime startTime);

    // @Query("SELECT COUNT(d) FROM Delivery d WHERE d.deliveryMan.id = :deliveryManId AND d.endTime > :startTime")
    // long countOverlappingDeliveries(@Param("deliveryManId") Long deliveryManId, @Param("startTime") LocalDateTime startTime);

    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    // @Query(value = "SELECT * FROM delivery WHERE delivery_man_id = :deliveryManId ORDER BY end_time DESC LIMIT 1", nativeQuery = true)
    // Optional<Delivery> findLatestDeliveryForMan(@Param("deliveryManId") Long deliveryManId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Delivery d WHERE d.deliveryMan.id = :deliveryManId ORDER BY d.endTime DESC LIMIT 1")
    Optional<Delivery> findLatestDeliveryForMan(@Param("deliveryManId") Long deliveryManId);    

    // Fetch top 3 delivery men by total earnings in a given time range
    // @Query("""
    //         SELECT new com.bayzdelivery.dtos.TopDeliveryManDTO(
    //             d.deliveryMan.id, 
    //             d.deliveryMan.name, 
    //             SUM(d.commission) AS totalCommission, 
    //             AVG(d.commission) AS avgCommission
    //         ) 
    //         FROM Delivery d
    //         WHERE d.startTime BETWEEN :startTime AND :endTime
    //         GROUP BY d.deliveryMan.id, d.deliveryMan.name
    //         ORDER BY SUM(d.commission) DESC
    //         LIMIT 3
    //         """)
    // List<TopDeliveryManDTO> findTopDeliveryMenByCommission(
    //         @Param("startTime") LocalDateTime startTime,
    //         @Param("endTime") LocalDateTime endTime
    // );

    @Query("""
    SELECT d.deliveryMan.id, 
           d.deliveryMan.name, 
           SUM(d.commission), 
           AVG(d.commission)
    FROM Delivery d
    WHERE d.startTime BETWEEN :startTime AND :endTime
    GROUP BY d.deliveryMan.id, d.deliveryMan.name
    ORDER BY SUM(d.commission) DESC LIMIT 3
    """)
    List<Object[]> findTopDeliveryMenByCommissionRaw(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );


    @Query("""
        SELECT d FROM Delivery d 
        WHERE d.endTime IS NOT NULL 
        AND FUNCTION('TIMESTAMPDIFF', MINUTE, d.startTime, d.endTime) > 45
    """)
    List<Delivery> findDelayedDeliveries();
}