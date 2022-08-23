package project;

public class Appointment {

    int appointmentID;
    int doctorID;
    String time = "";
    OurDate date;
    double cost = 20;
    String doctorName = "";
    static int count = 0;

    public Appointment() {
        count++;
        appointmentID = count;
    }

    public Appointment(int doctorID, String doctorName, String time, OurDate date) {
        count++;
        appointmentID = count;
        this.doctorID = doctorID;
        this.time = time;
        this.date = date;
        this.doctorName = doctorName;

    }

    public void setAppointmentID(int appointmentID) {

        this.appointmentID = appointmentID;

    }

    public void setDoctorID(int doctorID) {

        this.doctorID = doctorID;
    }

    public void setTime(int hour, int minute) {

        this.time += hour;
        this.time += ":";
        this.time += minute;

    }

    public void setDate(int day, int month, Days dayName) {
        this.date.day += day;
        this.date.month += month;
        this.date.dayName = dayName;

    }

    public void setCost(double cost) {

        this.cost = cost;
    }

    public void setDoctorName(String doctorName) {

        this.doctorName = doctorName;
    }

    public int getAppointmentID() {

        return appointmentID;
    }

    public int getDoctorID() {

        return doctorID;
    }

    public double getCost() {

        return cost;
    }

    public String getDoctorName() {

        return doctorName;
    }

    public String getTime() {

        return time;
    }

    public OurDate getDate() {

        return date;
    }

    @Override
    public String toString() {

        return "Appointment ID : " + appointmentID + " \n Doctor ID : " + doctorID
                + " \n Time : " + time + "  Date : " + date.day + "/" + date.month + " \n Name of Day:" + date.dayName + " \n Cost : " + cost + "  DoctorName : " + doctorName + "\n";

    }

}
