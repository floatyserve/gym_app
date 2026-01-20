package com.example.demo.membership.domain;

import com.example.demo.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
public class Membership {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private MembershipType type;

    @Enumerated(EnumType.STRING)
    private MembershipDuration duration;

    private Integer visitLimit;

    private Instant startsAt;
    private Instant endsAt;

    private boolean active;

    public boolean isValidAt(Instant time) {
        return active &&
                !time.isBefore(startsAt) &&
                !time.isAfter(endsAt);
    }

    public Membership(
            Customer customer,
            MembershipType type,
            MembershipDuration period,
            Integer visitLimit,
            Instant startsAt,
            Instant endsAt
    ){
        this.customer = customer;
        this.type = type;
        this.duration = period;
        this.visitLimit = visitLimit;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.active = true;
    }

    public boolean isLimited() {
        return type == MembershipType.LIMITED;
    }

    public void reschedule(Instant startsAt, Instant endsAt) {
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

}

