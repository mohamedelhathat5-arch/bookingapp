package com.example.booking_back.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RecentReservationProjection {
    String getReference();
    String getClient();           // Nom complet du client
    LocalDate getArrivee();       // check_in
    LocalDate getDepart();        // check_out
    Integer getNuits();
    String getPlateforme();
    String getStatut();           // "Payé", "Impáyé", etc.
    String getTypePaiement();     // "Virement", "Espèces", "En cours", etc.
    BigDecimal getMontantTotal();
}