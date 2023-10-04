package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

//        if(parkingLotRepository3.findById(parkingLotId).isEmpty())
//            throw new Exception("Cannot make reservation");
//
//        if(userRepository3.findById(userId).isEmpty())
//            throw new Exception("Cannot make reservation");
//
//
//        List<Spot>spotList=parkingLotRepository3.findById(parkingLotId).get().getSpotList();
//
//        Spot spot=null;
//        int price=Integer.MAX_VALUE;
//        SpotType spotType;
//
//        int wheelCount;
//        if(numberOfWheels<=2)
//        {
//            wheelCount=2;
//            spotType=SpotType.TWO_WHEELER;
//        }
//        else if(numberOfWheels<=4)
//        {
//            wheelCount=4;
//            spotType=SpotType.FOUR_WHEELER;
//        }
//        else
//        {
//            wheelCount=24;
//            spotType=SpotType.OTHERS;
//        }
//
//        for(Spot spot1:spotList)
//        {
//            if( spotType.equals( spot1.getSpotType() ) )
//            {
//                if(spot1.getPricePerHour()*wheelCount<price && !spot1.getOccupied())
//                {
//                    spot=spot1;
//                    price=spot1.getPricePerHour();
//                }
//            }
//        }
//        if(spot==null)
//            throw new Exception("Cannot make reservation");
//
//        spot.setOccupied(true);
//
//        Reservation reservation=new Reservation();
//        reservation.setNumberOfHours(timeInHours);
//        reservation.setSpot(spot);
//        reservation.setUser(userRepository3.findById(userId).get());
//        //reservation.setPayment(new Payment());
//
//        //Reservation savedReservation=reservationRepository3.save(reservation);
//
//        spot.getReservationList().add(reservation);
//        spotRepository3.save(spot);
//
//        User user=userRepository3.findById(userId).get();
//        user.getReservationList().add(reservation);
//        userRepository3.save(user);
//
//
//
//        return reservation;

        //copied

        User user;
        ParkingLot parkingLot;
        try {
            user=userRepository3.findById(userId).get();
            parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        Spot reservedSpot=null;

        int minCost=Integer.MAX_VALUE;

        for(Spot spot: parkingLot.getSpotList()){
            int wheels=0;
            if(spot.getSpotType() == SpotType.TWO_WHEELER)
                wheels=2;
            if(spot.getSpotType() == SpotType.FOUR_WHEELER)
                wheels=4;
            if(spot.getSpotType() == SpotType.OTHERS)
                wheels=24;

            if(!spot.getOccupied() && numberOfWheels <= wheels && spot.getPricePerHour()*timeInHours < minCost) {
                minCost=spot.getPricePerHour()*timeInHours;
                reservedSpot=spot;
            }
        }
        if(reservedSpot == null)
            throw new Exception("Cannot make reservation");

        Reservation reservation=new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(reservedSpot);

        user.getReservationList().add(reservation);
        reservedSpot.getReservationList().add(reservation);

        reservedSpot.setOccupied(true);

        //reservationRepository3.save(reservation);
        spotRepository3.save(reservedSpot);
        userRepository3.save(user);

        return  reservation;
    }
}
