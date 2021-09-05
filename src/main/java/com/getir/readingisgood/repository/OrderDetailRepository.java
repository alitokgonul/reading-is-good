package com.getir.readingisgood.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.getir.readingisgood.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("SELECT o FROM OrderDetail o WHERE (:customerId is null or :customerId = o.customer.id)")
    Page<OrderDetail> filterOrders(Long customerId, Pageable pageable);

    List<OrderDetail> findAllByCreatedDateTimeAfterAndCreatedDateTimeBefore(LocalDateTime startDate, LocalDateTime endDate);

    List<OrderDetail> findAllByCustomer_Id(Long customerId);
}
