package com.example.booking_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.booking_back.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    @Query("SELECT c FROM Client c WHERE c.cinOrPassport = :cinOrPassport")
    Client findByCinOrPassport(@Param("cinOrPassport") String cinOrPassport);
}
