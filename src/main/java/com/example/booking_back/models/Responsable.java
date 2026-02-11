package com.example.booking_back.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "responsables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Responsable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nomComplet;

    private String telephone;
    private String email;

    private String fonction; // ex: "Gérant", "Agent d'entretien", "Comptable"...

    @OneToMany(mappedBy = "responsable")
    private List<Reservation> reservationsResponsables = new ArrayList<>();
}
