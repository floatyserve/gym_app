package com.example.demo.visit.service;

import com.example.demo.visit.domain.Visit;

public interface VisitService {
    Visit findById(Long id);
    Visit findActiveVisit(Long id);
}
