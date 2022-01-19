package com.example.tickets;

import com.example.flights.Flights;

public class TicketReservation {

    private int _id, Id_user, Id_flight;
    private String Ticket_class, Check_in_user_name, Check_in_user_surname, Check_in_user_nationality, Departure_from, Flight_for, Date_and_time_departure, Check_in_user_passport_number;
    private boolean CheckIn;
    private double TicketPrice;

    public TicketReservation() {
    }

    public TicketReservation(int id, int id_user, int id_flight, String ticket_class, String departure_from, String flight_for, String date_and_time_departure, String check_in_user_name, String check_in_user_surname, String check_in_user_passport_number, String check_in_user_nationality, boolean checkIn, double ticketPrice) {
        _id = id;
        Id_user = id_user;
        Id_flight = id_flight;
        Ticket_class = ticket_class;
        Departure_from = departure_from;
        Flight_for = flight_for;
        Date_and_time_departure = date_and_time_departure;
        Check_in_user_name = check_in_user_name;
        Check_in_user_surname = check_in_user_surname;
        Check_in_user_passport_number = check_in_user_passport_number;
        Check_in_user_nationality = check_in_user_nationality;
        CheckIn = checkIn;
        TicketPrice = ticketPrice;
    }

    public int getTicketReservationID() {
        return _id;
    }

    public void setTicketReservationID(int id) {
        this._id = id;
    }

    public int getTicketReservationIDUser() {
        return Id_user;
    }

    public void setTicketReservationIDUser(int id_user) {
        this.Id_user = id_user;
    }

    public int getTicketReservationIDFlight() {
        return Id_flight;
    }

    public void setTicketReservationIDFlight(int id_flight) {
        this.Id_flight = id_flight;
    }

    public String getTicketReservationTicketClass() {
        return Ticket_class;
    }

    public void setTicketReservationTicketClass(String ticket_class) { this.Ticket_class = ticket_class; }

    public String getTicketReservationCheckInUserName() {
        return Check_in_user_name;
    }

    public void setTicketReservationCheckInUserName(String check_in_user_name) { this.Check_in_user_name = check_in_user_name; }

    public String getTicketReservationCheckInUserSurname() {
        return Check_in_user_surname;
    }

    public void setTicketReservationCheckInUserSurname(String check_in_user_surname) { this.Check_in_user_surname = check_in_user_surname; }

    public String getTicketReservationCheckInUserNationality() {
        return Check_in_user_nationality;
    }

    public void setTicketReservationCheckInUserNationality(String check_in_user_nationality) { this.Check_in_user_nationality = check_in_user_nationality; }

    public String getTicketReservationCheckInUserPassportNumber() { return Check_in_user_passport_number; }

    public void setTicketReservationCheckInUserPassportNumber(String check_in_user_passport_number) { this.Check_in_user_passport_number = check_in_user_passport_number; }

    public String getTicketReservationDepartureFrom() { return Departure_from; }

    public void setTicketReservationDepartureFrom(String departure_from) { this.Departure_from = departure_from; }

    public String getTicketReservationFlightFor() { return Flight_for; }

    public void setTicketReservationFlightFor(String flight_for) { this.Flight_for = flight_for; }

    public String getTicketReservationDepartureDateAndTime() { return Date_and_time_departure; }

    public void setTicketReservationDepartureDateAndTime(String date_and_time_departure) { this.Date_and_time_departure = date_and_time_departure; }

    public boolean getTicketReservationCheckIn() { return CheckIn; }

    public void setTicketReservationCheckIn(boolean checkIn) { this.CheckIn = checkIn; }

    public double getTicketPrice() { return TicketPrice; }

    public void setTicketPrice(double ticketPrice) { this.TicketPrice = ticketPrice; }
}
