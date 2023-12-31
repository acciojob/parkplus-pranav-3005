package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        ParkingLot savedParkingLot =parkingLotRepository1.save(parkingLot);

        return savedParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        Spot spot=new Spot();
        if(numberOfWheels<=2)
            spot.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels<=4)
            spot.setSpotType(SpotType.FOUR_WHEELER);
        else
            spot.setSpotType(SpotType.OTHERS);

        spot.setPricePerHour(pricePerHour);

        Optional<ParkingLot> isParkingLot=parkingLotRepository1.findById(parkingLotId);
        ParkingLot parkingLot=isParkingLot.get();
        spot.setParkingLot(parkingLot);

        spot.setOccupied(false);

        //Spot savedSpot=spotRepository1.save(spot);
        parkingLot.getSpotList().add(spot);
        ParkingLot saved=parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Optional<Spot>isSpot=spotRepository1.findById(spotId);
        if(isSpot.isEmpty())
            return;

        Spot spot=isSpot.get();

        spot.getParkingLot().getSpotList().remove(spot);

        spotRepository1.deleteById(spotId);
        parkingLotRepository1.save(spot.getParkingLot());

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot= parkingLotRepository1.findById(parkingLotId).get();

        Spot spot=new Spot();
        for(Spot spot1:parkingLot.getSpotList() )
        {
            if(spot1.getId()==spotId)
            {
                spot1.setPricePerHour(pricePerHour);
                spot=spot1;
                spotRepository1.save(spot1);
                parkingLotRepository1.save(parkingLot);
                break;
            }
        }

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
       /* ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        for(Spot spot:parkingLot.getSpotList())
        {
            int id=spot.getId();
            spotRepository1.deleteById(id);
        }*/

        parkingLotRepository1.deleteById(parkingLotId);

        //or this is alone also ok   ***
        //parkingLotRepository1.deleteById(parkingLotId);
    }
}
