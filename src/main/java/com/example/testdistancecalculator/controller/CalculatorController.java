package com.example.testdistancecalculator.controller;

import com.example.testdistancecalculator.entity.City;
import com.example.testdistancecalculator.exception_handling.CityIncorrectData;
import com.example.testdistancecalculator.exception_handling.NoSuchCityException;
import com.example.testdistancecalculator.service.CityService;
import com.example.testdistancecalculator.service.DistanceService;
import com.example.testdistancecalculator.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class CalculatorController {
    @Autowired
    private CityService cityService;

    @Autowired
    private DistanceService distanceService;

    @Autowired
    private UploadService uploadService;


    @GetMapping("/cities")
    public String findAll(Model model) {
        List<City> cities = cityService.findAll();
        model.addAttribute("cities", cities);
        return "city-list";
    }

    @GetMapping("/distance")
    public String getDistance(@RequestParam("a") String city1, @RequestParam("b") String city2, @RequestParam("mode") String mode, Model model) {
        String distance = "----";
        String roadDistance = "----";
        String template = "distance";

        switch (mode) {
            case "fly":
                distance = getStraightDistance(city1, city2).toString();
                break;
            case "road":
                distance = distanceService.getDistanceFromMatrix(city1, city2).toString();
                break;
            case "all":
                distance = getStraightDistance(city1, city2).toString();
                roadDistance = distanceService.getDistanceFromMatrix(city1, city2).toString();
                template = "distance-all";
        }

        model.addAttribute("distance", distance);
        model.addAttribute("roadDistance", roadDistance);
        model.addAttribute("city1", city1);
        model.addAttribute("city2", city2);

        return template;
    }

    @GetMapping("upload")
    public String getUploadPage() {
        return "upload";
    }

    @PostMapping("/upload-file")
    @ResponseStatus(HttpStatus.OK)
    public void uploadXMLFile(@RequestParam("file") MultipartFile file) {

        try {
            uploadService.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        distanceService.uploadXMLtoDataBase(file.getOriginalFilename());
    }

    public Double getStraightDistance(String city1, String city2) {
        double latitude1 = cityService.getLatitudeByName(city1);
        double longitude1 = cityService.getLongitudeByName(city1);
        double latitude2 = cityService.getLatitudeByName(city2);
        double longitude2 = cityService.getLongitudeByName(city2);

        if ((latitude1 == 0.0d && longitude1 == 0.0d) || (latitude2 == 0.0d && longitude2 == 0.0d))
            throw new NoSuchCityException("There is no city in database");

        return cityService.getDistanceByLatitudeAndLongitude(latitude1, longitude1, latitude2, longitude2);
    }

    @ExceptionHandler
    public ResponseEntity<CityIncorrectData> handleException(NoSuchCityException exception) {
        CityIncorrectData data = new CityIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

}
