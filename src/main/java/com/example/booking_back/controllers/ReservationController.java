package com.example.booking_back.controllers;

import com.example.booking_back.models.Client;
import com.example.booking_back.models.Payment;
import com.example.booking_back.models.Property;
import com.example.booking_back.models.Reservation;
import com.example.booking_back.models.Responsable;
import com.example.booking_back.models.User;
import com.example.booking_back.models.enums.PaymentMethod;
import com.example.booking_back.models.enums.PaymentStatus;
import com.example.booking_back.models.enums.Platform;
import com.example.booking_back.models.enums.PropertyStatus;
import com.example.booking_back.models.enums.ReservationStatus;
import com.example.booking_back.projection.*;
import com.example.booking_back.repositories.ClientRepository;
import com.example.booking_back.repositories.PaymentRepository;
import com.example.booking_back.repositories.PropertyRepository;
import com.example.booking_back.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.booking_back.dtos.ReservationDTO;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/reservations")
public class ReservationController {
    ReservationService resService;
    PropertyRepository propertyRepository;
    ClientRepository clientRepository;
    PaymentRepository paymentRepository;
    public ReservationController(ReservationService resService,PropertyRepository propertyRepository,ClientRepository clientRepository,PaymentRepository paymentRepository) {
        this.resService=resService;
        this.propertyRepository=propertyRepository;
        this.clientRepository=clientRepository;
        this.paymentRepository=paymentRepository;
    }


    @GetMapping("/byMonth")
    public List<DailyStatsProjection> allReservationperMonth(
            @RequestParam Integer month,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) String platform
    ) {
        return resService.getAllReservationsByMonth(month, city, propertyId, platform);
    }


    @GetMapping("/dashboard")
    public DashboardStatsProjection getDashboard(
            @RequestParam Integer month,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) String platform
    ) {
        return resService.getDashboardStats(month, city, propertyId, platform);
    }



    @GetMapping("/trends")
    public ResponseEntity<List<MonthlyTrendProjection>> getTrendsCurrentYear() {
        return ResponseEntity.ok(resService.getMonthlyTrendsCurrentYear());
    }

    @GetMapping("/platform-distribution")
    public ResponseEntity<List<PlatformDistributionProjection>> getPlatformDistribution() {
        return ResponseEntity.ok(resService.getPlatformDistribution());
    }


    @GetMapping("/recent")
    public ResponseEntity<List<RecentReservationProjection>> getRecentReservations() {
        List<RecentReservationProjection> reservations = resService.getRecentReservations();
        return ResponseEntity.ok(reservations);
    }
  @PostMapping("/add")
public String createReservation(@RequestBody ReservationDTO dto) {
System.out.println(dto.toString());
    Client client = new Client();

    if(clientRepository.findByCinOrPassport(dto.getClientCin())==null){
    client.setFullName(dto.getClientName());
    client.setCinOrPassport(dto.getClientCin());
    client.setPhone(dto.getClientPhone());
    client.setEmail(dto.getClientEmail());
    }else{
        client=clientRepository.findByCinOrPassport(dto.getClientCin());
    }
    
    Property property = new Property(dto.getPropertyId());

    User user = new User(dto.getUserId());

    Responsable responsable = new Responsable();
    responsable.setId(dto.getResponsable());

    Reservation reservation = new Reservation();    
    reservation.setCheckIn(dto.getCheckIn());
    reservation.setCheckOut(dto.getCheckOut());
    reservation.setNights(dto.getNights());
    reservation.setMonth(dto.getCheckIn().getMonthValue());
    reservation.setStatus(ReservationStatus.CONFIRMED);
    reservation.setPlatform(Platform.valueOf(dto.getPlatform()));
    reservation.setTotalAmount(dto.getTotalAmount());
    reservation.setNightlyRate(dto.getNightlyRate());
    reservation.setProperty(property);
    reservation.setClient(client);
    reservation.setCreatedBy(user);
    reservation.setResponsable(responsable);
    reservation.setReference("res-"+ System.currentTimeMillis());
    

    Payment payment = new Payment();
    payment.setAmount(dto.getTotalAmount());
    payment.setMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));
    payment.setStatus(PaymentStatus.valueOf(dto.getPaymentStatus()));
    payment.setClient(client);
    payment.setPaymentDate(java.time.LocalDateTime.now());
    payment.setNotes("pas de notes pour l'instant");
    payment.setTransactionReference("txn_" + System.currentTimeMillis());
    payment.setReservation(reservation); // à lier après la création de la réservation


System.out.println(dto.toString());
clientRepository.save(client);

// Save reservation 
Reservation savedReservation = resService.addReservation(reservation);


payment.setReservation(savedReservation);


paymentRepository.save(payment);
if(savedReservation!=null)
{
    return "ajouter avec succes";
}
else {
    return "reservation non-ajouter";
}

}
}
