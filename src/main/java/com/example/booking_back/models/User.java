package com.example.booking_back.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;           // hashed

    private String firstName;
    private String lastName;
    private String phone;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Relations (examples)
    //@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    //private List<Property> ownedProperties = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<Reservation> createdReservations = new ArrayList<>();

    // or: responsible for expenses / reservations
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<Expense> managedExpenses = new ArrayList<>();
}
