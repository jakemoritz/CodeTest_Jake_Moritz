package com.domain.codetest_jake_moritz.models;

import org.joda.time.LocalDate;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Person extends RealmObject{

    @Required
    private String firstName;

    @Required
    private String lastName;

    @Required
    private String phoneNumber;

    private long dateOfBirth;

    private String dateOfBirthFormatted;

    @Required
    private String zipCode;

    @PrimaryKey
    @Required
    private String personID;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirthFormatted() {
        return dateOfBirthFormatted;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;

        LocalDate date = new LocalDate(dateOfBirth);
        this.dateOfBirthFormatted = date.toString();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
