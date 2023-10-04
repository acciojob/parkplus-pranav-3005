package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists

        int totalPrice=0;
        totalPrice= reservationRepository2.findById(reservationId).get().getSpot().getPricePerHour();
        totalPrice=totalPrice * reservationRepository2.findById(reservationId).get().getNumberOfHours();

        if(amountSent<totalPrice)
            throw new Exception("Insufficient Amount");

        mode=mode.toUpperCase();

        if(mode.equals(PaymentMode.UPI.toString())) ;
        else if (mode.equals(PaymentMode.CARD.toString())) ;
        else if(mode.equals(PaymentMode.CASH.toString())) ;
        else
            throw new Exception("Payment mode not detected");

        Payment payment=new Payment();

        payment.setPaymentMode(PaymentMode.valueOf(mode));
        payment.setPaymentCompleted(true);
        payment.setReservation(reservationRepository2.findById(reservationId).get());

        Payment savedPayment=paymentRepository2.save(payment);

        Reservation reservation= reservationRepository2.findById(reservationId).get();
        reservation.setPayment(savedPayment);
        reservationRepository2.save(reservation);

        return savedPayment;

    }
}
