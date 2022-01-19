package com.example.flights;

import java.io.Serializable;

public class Flights implements Serializable {

    private String FlightDepartureCountry, FlightDepartureCity, FlightForCountry, FlightForCity, FlightDepartureDateAndTime, FlightForDateAndTime;
    private int _id, FlightBusinessTicketNumber, FlightFirstClassTicketNumber;
    private double FlightBusinessTicket, FlightFirstClassTicket;

    public Flights() {
    }

    public Flights(int id, String flightDepartureCountry, String flightDepartureCity, String flightForCountry, String flightForCity, String flightDepartureDateAndTime, String flightForDateAndTime, double flightBusinessTicket, double flightFirstClassTicket, int businessTicketNumber, int firstClassTicketNumber) {
        _id = id;
        FlightDepartureCountry = flightDepartureCountry;
        FlightDepartureCity = flightDepartureCity;
        FlightForCountry = flightForCountry;
        FlightForCity = flightForCity;
        FlightDepartureDateAndTime = flightDepartureDateAndTime;
        FlightForDateAndTime = flightForDateAndTime;
        FlightBusinessTicket = flightBusinessTicket;
        FlightFirstClassTicket = flightFirstClassTicket;
        FlightBusinessTicketNumber = businessTicketNumber;
        FlightFirstClassTicketNumber = firstClassTicketNumber;
    }

    public String getFlightDepartureCountry() {
        return FlightDepartureCountry;
    }

    public void setFlightDepartureCountry(String flightDepartureCountry) {
        this.FlightDepartureCountry = flightDepartureCountry;
    }

    public String getFlightDepartureCity() {
        return FlightDepartureCity;
    }

    public void setFlightDepartureCity(String flightDepartureCity) {
        this.FlightDepartureCity = flightDepartureCity;
    }

    public String getFlightForCountry() {
        return FlightForCountry;
    }

    public void setFlightForCountry(String flightForCountry) {
        this.FlightForCountry = flightForCountry;
    }

    public String getFlightForCity() {
        return FlightForCity;
    }

    public void setFlightForCity(String flightForCity) {
        this.FlightForCity = flightForCity;
    }

    public String getFlightDepartureDateAndTime() {
        return FlightDepartureDateAndTime;
    }

    public void setFlightDepartureDateAndTime(String flightDepartureDateAndTime) {
        this.FlightDepartureDateAndTime = flightDepartureDateAndTime;
    }

    public String getFlightForDateAndTime() {
        return FlightForDateAndTime;
    }

    public void setFlightForDateAndTime(String flightForDateAndTime) {
        this.FlightForDateAndTime = flightForDateAndTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double getFlightBusinessTicket() {
        return FlightBusinessTicket;
    }

    public void setFlightBusinessTicket(double flightBusinessTicket) {
        this.FlightBusinessTicket = flightBusinessTicket;
    }

    public double getFlightFirstClassTicket() {
        return FlightFirstClassTicket;
    }

    public void setFlightFirstClassTicket(double flightFirstClassTicket) {
        this.FlightFirstClassTicket = flightFirstClassTicket;
    }

    public int getFlightFirstClassTicketNumber() {
        return FlightFirstClassTicketNumber;
    }

    public void setFlightFirstClassTicketNumber(int flightFirstClassTicketNumber) {
        this.FlightFirstClassTicketNumber = flightFirstClassTicketNumber;
    }

    public int getFlightBusinessTicketNumber() {
        return FlightBusinessTicketNumber;
    }

    public void setFlightBusinessTicketNumber(int businessTicketNumber) {
        this.FlightBusinessTicketNumber = businessTicketNumber;
    }
}
