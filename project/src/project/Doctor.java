package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doctor {

    //fields
    static int id = 0;
    int doctorID;
    String doctorName = "";
    Map<Days, List<Double>> workingSchedule;
    List<Appointment> appointments = new ArrayList<Appointment>();

    /*
	 * server will iterate a loop to take the working schedule as an input from client,
	 * such as:
	 * insert a day:
	 * Monday //inserted to a list of days
	 * insert a start time:
	 * 9.00 //inserted to a list of times
	 * insert an end time:
	 * 15.00 /inserted to the same list of times
	 * then it will be passed to the constructor or to the set function in case of updating the schedule
     */
    //constructor
    public Doctor(String doctorName) {
        this.doctorID = ++id;
        this.doctorName = doctorName;
        workingSchedule = new HashMap<Days, List<Double>>();
    }

    public Doctor(String doctorName, List<Days> days, double from, double to) {
        this.doctorID = ++id;
        this.doctorName = doctorName;
        workingSchedule = new HashMap<Days, List<Double>>();
        setSchedule(days, from, to);
    }

    //getters
    public int getDoctorID() {
        return doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Map<Days, List<Double>> getWorkingSchedule() {
        return workingSchedule;
    }

    public String cancelDay(Days days) {
        if (!workingSchedule.containsKey(days)) {
            return "Day is not contained in the schedual";
        }
        workingSchedule.remove(days);
        return "Day is canceled successfully";
    }

    //set schedule
    public void setSchedule(List<Days> days, double from, double to) {
        List<Double> time = new ArrayList<Double>();
        time.add(from);
        time.add(to);
        for (Days day : days) {
            workingSchedule.put(day, time);
        }
    }

    //printing
    @Override
    public String toString() {
        return " Doctor ID : " + doctorID + "\n Doctor Name : " + doctorName + "\n Working Schedule :\n" + print();
    }

    public String print() {
        String str = "";
        for (Map.Entry<Days, List<Double>> entry : workingSchedule.entrySet()) {
            str += ("\t" + entry.getKey() + " = " + entry.getValue() + "\n");
        }
        return str;
    }

    public boolean IsAvailable(OurDate date, String time) {
        for (Appointment appointment : appointments) {
            if (appointment.date.month == date.month && appointment.date.day == date.day) {
                if (time.equals(appointment.time)) {
                    return false;
                }
            }
        }
        return true;
    }

}
