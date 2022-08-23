package project;

import java.util.ArrayList;
import java.util.List;

public class Patient {

    static int count = 0;
    int patientID;
    String name = "";
    String phoneNumber = "";
    String email = "";
    double owedBalance = 0;
    double payedBalance = 0;
    String patientFile;
    List<Appointment> previousAppointemnts = new ArrayList<>();

    public Patient() {

    }

    public Patient(String name, String phone, String email) {
        count++;
        patientID = count;

        setName(name);
        setPhoneNumber(phone);
        if (email.equals("*")) {
            setEmail("not available");
        } else {
            setEmail(email);
        }

    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public void setOwedBalance(Double owedBalance) {
        this.owedBalance = owedBalance;
    }

    public void setPayedBalance(Double PayedBalance) {
        this.payedBalance = PayedBalance;
    }

    public int getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Double getOwedBalance() {
        return owedBalance;
    }

    public Double getPayedBalance() {
        return payedBalance;
    }

    public List<Appointment> getPreviousAppointemnts() {
        return previousAppointemnts;
    }

    public void setPreviousAppointemnt(Appointment previousAppointemnt) {
        previousAppointemnts.add(previousAppointemnt);
    }

    public String getPatientFile() {
        return patientFile;
    }

    public void setPatientFile(String patientFile) {
        this.patientFile = patientFile;
    }

    @Override
    public String toString() {
        return "patient: "
                + "id=" + patientID + "  ,  "
                + "name= " + name + "  , "
                + "phoneNumber= " + phoneNumber + "\n"
                + "Email=  " + email + "  , "
                + "0wedBalance= " + (owedBalance - payedBalance) + "  , "
                + "totalOfPayedBalance= " + payedBalance + "  \n "
                + "appointments : \n " + previousAppointemnts.toString();
    }
}
