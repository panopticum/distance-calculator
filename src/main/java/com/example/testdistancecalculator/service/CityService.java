package com.example.testdistancecalculator.service;

import com.example.testdistancecalculator.entity.City;
import com.example.testdistancecalculator.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public double getLatitudeByName(String name) {
        List<City> cities = findAll();
        for (City city : cities) {
            if (city.getName().equalsIgnoreCase(name))
                return city.getLatitude();
        }
        return 0.0d;
    }

    public double getLongitudeByName(String name) {
        List<City> cities = findAll();
        for (City city : cities) {
            if (city.getName().equalsIgnoreCase(name))
                return city.getLongitude();
        }
        return 0.0d;
    }

    public double getDistanceByLatitudeAndLongitude(double latitude1, double longitude1, double latitude2, double longitude2) {
        if ((latitude1 == 0.0d && longitude1 == 0.0d) || (latitude2 == 0.0d && longitude2 == 0.0d))
            return 0.0d;

        int radius = 6371; //Radius of Earth in km

        double dlat = Math.toRadians(latitude2 - latitude1);
        double dlon = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) + Math.cos(Math.toRadians(latitude1))
                * Math.cos(Math.toRadians(latitude2)) * Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radius * c;


        return Math.round(distance * 100.0) / 100.0;
    }
}
