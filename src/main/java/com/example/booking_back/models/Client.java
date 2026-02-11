package com.example.booking_back.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String fullName;
    private String email;
    private String phone;
    private String cinOrPassport;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();
}
