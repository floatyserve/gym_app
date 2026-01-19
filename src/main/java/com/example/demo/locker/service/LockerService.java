package com.example.demo.locker.service;

import com.example.demo.locker.domain.Locker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LockerService {
    Page<Locker> findAll(Pageable pageable);
    List<Locker> findAllAvailable();
    Locker findById(Long id);
    Locker create(Integer number);
    void assertAvailable(Locker locker);
    Locker makeUnavailable(Long id);
}
