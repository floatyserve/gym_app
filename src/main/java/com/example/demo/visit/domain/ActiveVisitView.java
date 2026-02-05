package com.example.demo.visit.domain;

import java.time.Instant;

public interface ActiveVisitView {
    Long getVisitId();
    String getCustomerFullName();
    Instant getCheckedInAt();
    Long getLockerId();
    String getLockerNumber();
}