package com.domain.codetest_jake_moritz.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Person extends RealmObject{

    // Annotation denotes whether Realm requires these fields when creating a Person object
    @Required
    private String firstName;

    @Required
    private String lastName;

    @Required
    private String phoneNumber;

    private long dateOfBirth;

    @Required
    private String dateOfBirthFormatted;

    @Required
    private String zipCode;

    @PrimaryKey
    @Required
    private String personID;

    public Person() {
    }

    public Person(String firstName, String lastName, String phoneNumber, long dateOfBirth, String dateOfBirthFormatted, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.dateOfBirthFormatted = dateOfBirthFormatted;
        this.zipCode = zipCode;

        // Generate unique Person ID using UUID
        this.personID = UUID.randomUUID().toString();
    }

    public Person(String firstName, String lastName, String phoneNumber, long dateOfBirth, String dateOfBirthFormatted, String zipCode, String personID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.dateOfBirthFormatted = dateOfBirthFormatted;
        this.zipCode = zipCode;
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirthFormatted() {
        return dateOfBirthFormatted;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPersonID() {
        return personID;
    }
}
