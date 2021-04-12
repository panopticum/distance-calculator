package com.example.testdistancecalculator.jaxb;

import com.example.testdistancecalculator.entity.Distance;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class JaxbReader {

    public static List<Distance> unMarshallXML(String fileName) throws JAXBException {
        String uploadDirectory = "c:/temp/";
        JAXBContext context = JAXBContext.newInstance(Distances.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Distances distances = (Distances) unmarshaller.unmarshal(new File(uploadDirectory + fileName));

        return distances.getDistances();
    }
}
