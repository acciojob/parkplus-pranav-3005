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

        if(parkingLotRepository3.findById(parkingLotId).isEmpty())
            throw new Exception("Cannot make reservation");

        if(userRepository3.findById(userId).isEmpty())
            throw new Exception("Cannot make reservation");


        List<Spot>spotList=parkingLotRepository3.findById(parkingLotId).get().getSpotList();

        Spot spot=null;
        int price=Integer.MAX_VALUE;
        SpotType spotType;

        int wheelCount;
        if(numberOfWheels<=2)
        {
            wheelCount=2;
            spotType=SpotType.TWO_WHEELER;
        }
        else if(numberOfWheels<=4)
        {
            wheelCount=4;
            spotType=SpotType.FOUR_WHEELER;
        }
        else
        {
            wheelCount=24;
            spotType=SpotType.OTHERS;
        }

        for(Spot spot1:spotList)
        {
            if( spotType.equals( spot1.getSpotType() ) )
            {
                if(spot1.getPricePerHour()*wheelCount<price && !spot1.getOccupied())
                {
                    spot=spot1;
                    price=spot1.getPricePerHour();
                }
            }
        }
        if(spot==null)
            throw new Exception("Cannot make reservation");

        spot.setOccupied(true);

        Reservation reservation=new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spot);
        reservation.setUser(userRepository3.findById(userId).get());
        reservation.setPayment(new Payment());

        Reservation savedReservation=reservationRepository3.save(reservation);

        spot.getReservationList().add(savedReservation);
        spotRepository3.save(spot);

        User user=userRepository3.findById(userId).get();
        user.getReservationList().add(savedReservation);
        userRepository3.save(user);



        return savedReservation;
    }
}
