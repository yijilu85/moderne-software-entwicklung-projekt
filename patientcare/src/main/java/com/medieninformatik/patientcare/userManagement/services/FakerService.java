package com.medieninformatik.patientcare.userManagement.services;

import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.PatientRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import com.medieninformatik.patientcare.userManagement.domain.model.valueObjects.MedicalSpeciality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;

import java.util.*;


@Service
public class FakerService {

    private final DoctorRepo doctorRepo;
    /*private final PatientRepo patientRepo;*/


    @Autowired
    public FakerService(DoctorRepo doctorRepo/*, PatientRepo patientRepo*/) {

        this.doctorRepo = doctorRepo;
        /*this.patientRepo = patientRepo;*/
    }

    Map<String, List<Map<String, String>>> cityData = new HashMap<>();

    public void createDoctors(int amount) {
        Faker faker = new Faker();
        Random random = new Random();
        MedicalSpeciality.Speciality[] specialities = MedicalSpeciality.Speciality.values();
        String[] cities = {"Berlin", "Hamburg", "München", "Köln", "Frankfurt", "Stuttgart"};
        populateCityData();
        for (int i = 0; i < amount; i++) {
            Doctor doctor = new Doctor();
            doctor.setFirstName(faker.name().firstName());
            doctor.setLastName(faker.name().lastName());
            doctor.setEmail(faker.internet().emailAddress());
            doctor.setPassword("1234567890");
            doctor.setPhoneNumber(faker.phoneNumber().phoneNumber());
            doctor.setStreet(faker.address().streetAddress());
            doctor.setHouseNumber(faker.address().buildingNumber());
            doctor.setZipCode(faker.number().digits(5));
            doctor.setLicenseId("A" + faker.number().digits(5) + "-D" + faker.number().digits(5));

            String city = cities[random.nextInt(cities.length)];
            doctor.setCity(city);
            doctor.setCity(cities[random.nextInt(cities.length)]);

            List<Map<String, String>> streets = cityData.get(city);

            Map<String, String> streetData = streets.get(random.nextInt(streets.size()));
            String street = streetData.get("street");
            String zipCode = streetData.get("zipCode");

            String houseNumber = (random.nextInt(100) + 1) + "";;

            doctor.setStreet(street);
            doctor.setHouseNumber(houseNumber);
            doctor.setZipCode(zipCode);

            MedicalSpeciality.Speciality speciality = specialities[random.nextInt(specialities.length)];
            doctor.setSpeciality(speciality);
            doctor.setUsertype(Doctor.UserType.DOCTOR);
            doctor.setTitle("Dr. med.");
            doctorRepo.save(doctor);
        }
    }

    /*public void createDoctors(int amount) {
        Faker faker = new Faker();
        for (int i = 0; i < amount; i++) {
            Doctor doctor = new Doctor();
            populateUserFields(doctor, faker);
            doctor.setLicenseId("A" + faker.number().digits(5) + "-D" + faker.number().digits(5));
            doctorRepo.save(doctor);
        }
    }*/

    public void populateCityData(){
        List<Map<String, String>> berlinStreets = new ArrayList<>();
        berlinStreets.add(createStreetData("Unter den Linden", "10115"));
        berlinStreets.add(createStreetData("Friedrichstraße", "10117"));
        berlinStreets.add(createStreetData("Kurfürstendamm", "10719"));
        berlinStreets.add(createStreetData("Alexanderplatz", "10178"));
        berlinStreets.add(createStreetData("Schönhauser Allee", "10435"));
        cityData.put("Berlin", berlinStreets);

        List<Map<String, String>> hamburgStreets = new ArrayList<>();
        hamburgStreets.add(createStreetData("Reeperbahn", "20359"));
        hamburgStreets.add(createStreetData("Mönckebergstraße", "20095"));
        hamburgStreets.add(createStreetData("Spitalerstraße", "20095"));
        hamburgStreets.add(createStreetData("Jungfernstieg", "20354"));
        hamburgStreets.add(createStreetData("HafenCity", "20457"));
        cityData.put("Hamburg", hamburgStreets);

        List<Map<String, String>> muenchenStreets = new ArrayList<>();
        muenchenStreets.add(createStreetData("Maximilianstraße", "80331"));
        muenchenStreets.add(createStreetData("Leopoldstraße", "80802"));
        muenchenStreets.add(createStreetData("Theresienstraße", "80333"));
        muenchenStreets.add(createStreetData("Sendlinger Straße", "80331"));
        muenchenStreets.add(createStreetData("Lindwurmstraße", "80337"));
        cityData.put("München", muenchenStreets);

        List<Map<String, String>> koelnStreets = new ArrayList<>();
        koelnStreets.add(createStreetData("Schildergasse", "50667"));
        koelnStreets.add(createStreetData("Hohestraße", "50667"));
        koelnStreets.add(createStreetData("Ehrenstraße", "50672"));
        koelnStreets.add(createStreetData("Aachener Straße", "50931"));
        koelnStreets.add(createStreetData("Blaubach", "50667"));
        cityData.put("Köln", koelnStreets);

        List<Map<String, String>> frankfurtStreets = new ArrayList<>();
        frankfurtStreets.add(createStreetData("Zeil", "60311"));
        frankfurtStreets.add(createStreetData("Fressgass", "60313"));
        frankfurtStreets.add(createStreetData("Goethestraße", "60313"));
        frankfurtStreets.add(createStreetData("Bockenheimer Landstraße", "60325"));
        frankfurtStreets.add(createStreetData("Berger Straße", "60316"));
        cityData.put("Frankfurt", frankfurtStreets);

        List<Map<String, String>> stuttgartStreets = new ArrayList<>();
        stuttgartStreets.add(createStreetData("Königstraße", "70173"));
        stuttgartStreets.add(createStreetData("Schillerstraße", "70173"));
        stuttgartStreets.add(createStreetData("Rotebühlstraße", "70178"));
        stuttgartStreets.add(createStreetData("Kaiserstraße", "70173"));
        stuttgartStreets.add(createStreetData("Wilhelmsstraße", "70182"));
        cityData.put("Stuttgart", stuttgartStreets);
    }

    private Map<String, String> createStreetData(String streetName, String zipCode) {
        Map<String, String> streetData = new HashMap<>();
        streetData.put("street", streetName);
        streetData.put("zipCode", zipCode);
        return streetData;
    }

   /* public void createPatients(int amount) {
        Faker faker = new Faker();
        for (int i = 0; i < amount; i++) {
            Patient patient = new Patient();
            patient.setFirstName(faker.name().firstName());
            patient.setLastName(faker.name().lastName());
            patient.setEmail(faker.internet().emailAddress());
            patient.setPassword("1234567890"); // Einfaches Passwort für Tests
            patientRepo.save(patient);
        }
    }*/




}
