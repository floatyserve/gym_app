package com.example.demo.visit.service;

import com.example.demo.customer.domain.Customer;
import com.example.demo.staff.domain.Worker;
import com.example.demo.visit.domain.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface VisitService {
    Visit findById(Long id);

    Visit findActiveVisit(Long id);

    Visit findActiveCustomerVisit(Customer customer);

    Page<Visit> getVisitHistory(Customer customer, Pageable pageable);

    Visit checkIn(Customer customer, Worker worker, Instant at);

    Visit checkOut(Long visitId, Instant at);
}
