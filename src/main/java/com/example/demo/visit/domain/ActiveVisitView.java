package com.example.demo.visit.domain;

import java.time.Instant;

public interface ActiveVisitView {
    Long getVisitId();
    String getCustomerFullName();
    String getCustomerEmail();
    Instant getCheckedInAt();
    Long getLockerId();
    String getLockerNumber();
}