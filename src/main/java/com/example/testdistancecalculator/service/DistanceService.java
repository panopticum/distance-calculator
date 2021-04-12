package com.example.testdistancecalculator.service;

import com.example.testdistancecalculator.entity.Distance;
import com.example.testdistancecalculator.exception_handling.NoSuchCityException;
import com.example.testdistancecalculator.jaxb.JaxbReader;
import com.example.testdistancecalculator.repository.DistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.List;

@Service
public class DistanceService {
    @Autowired
    private DistanceRepository distanceRepository;

    public List<Distance> findAll() {
        return distanceRepository.findAll();
    }

    public Double getDistanceFromMatrix(String city1, String city2) {
        List<Distance> distances = findAll();

        for (Distance distance : distances) {
            if (distance.getFromCity().equalsIgnoreCase(city1) && distance.getToCity().equalsIgnoreCase(city2)
            || distance.getFromCity().equalsIgnoreCase(city2) && distance.getToCity().equalsIgnoreCase(city1)) {
                return distance.getDistance();
            }
        }
        throw new NoSuchCityException("There is no city in database");
    }

    public void uploadXMLtoDataBase (String fileName) {
        List<Distance> distances = null;
        try {
            distances = JaxbReader.unMarshallXML(fileName);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        for (Distance distance : distances) {
            distanceRepository.save(distance);
        }
    }
}
