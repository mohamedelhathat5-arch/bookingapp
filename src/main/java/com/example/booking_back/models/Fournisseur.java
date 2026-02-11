package com.example.booking_back.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fournisseurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nom;

    private String raisonSociale;
    private String telephone;
    private String email;
    private String adresse;

    private String categorie; // ex: "Ménage", "Plomberie", "Électricité", "Assurance"...


    @OneToMany(mappedBy = "fournisseur", fetch = FetchType.LAZY)
    private List<Expense> expenses = new ArrayList<>();
} 