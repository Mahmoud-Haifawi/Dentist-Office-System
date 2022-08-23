package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.Object;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Server {

    public static boolean tryAgain = true;
    public static List<Doctor> doctors = new ArrayList<Doctor>();
    public static List<Patient> patients = new ArrayList<Patient>();
    static DataOutputStream outPublic;

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket socket = new ServerSocket(3030);
        Socket client = socket.accept();
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        Scanner input = new Scanner(System.in);
        outPublic = out;

        Doctor d1 = new Doctor("ahmad");
        Doctor d2 = new Doctor("ali");
        Doctor d3 = new Doctor("Joud");
        Doctor d4 = new Doctor("Tasneem");

        doctors.add(d1);
        doctors.add(d2);
        doctors.add(d3);
        doctors.add(d4);

        while (tryAgain) {
            boolean loggedIn = LogIn(in, out);

            if (!loggedIn) {
                out.writeUTF("-1"); //
                tryAgain = in.readBoolean();
            } else {
                out.writeUTF("");//
                String option = "";
                while (!option.equals("0")) {
                    out.writeUTF(ShowOptions());//
                    option = in.readUTF();//option
                    CheckOption(option, in, out, client);
                    Date sysDate = new Date();
                    //System.out.println(sysDate);
                    SendReminders(patients, sysDate);

                }
            }
        }

    }

    public static boolean LogIn(DataInputStream in, DataOutputStream out) throws IOException {
        String msg = "~~~~~~~~~~~~~~~~~~\n"
                + "Dental House \n"
                + "Sign in\n"
                + "-------------------\n"
                + "Reciptionest name:";
        out.writeUTF(msg); //
        String name = in.readUTF(); //receptionest name
        out.writeUTF("Password:"); //
        String password = in.readUTF();//pass

        if (name.equalsIgnoreCase("talia") && password.equals("1234")) {
            return true;
        }
        return false;

    }

    public static void SendReminders(List<Patient> patients, Date d) {

        for (int i = 0; i < patients.size(); i++) {
            for (int j = 0; j < patients.get(i).getPreviousAppointemnts().size(); j++) {
                int day = patients.get(i).getPreviousAppointemnts().get(j).getDate().day;
                int month = patients.get(i).getPreviousAppointemnts().get(j).getDate().month;
                if (month == ((int) d.getMonth() + 1) && d.getDate() == day) {
                    String[] arrOfTime = patients.get(i).getPreviousAppointemnts().get(j).getTime().split(":", 2);
                    if ((d.getHours() + 1) == Integer.parseInt(arrOfTime[0]) && d.getMinutes() == Integer.parseInt(arrOfTime[1])) {
                        System.out.println("Email sent for SendReminders" + patients.get(i).getEmail());
                    }
                }
            }
        }
    }

    public static void SendCancellation(int doctorID, Days day) {

        for (int i = 0; i < patients.size(); i++) {
            List<Appointment> appointments = patients.get(i).getPreviousAppointemnts();

            for (int j = 0; j < appointments.size(); j++) {
                Appointment appoint = patients.get(i).getPreviousAppointemnts().get(j);

                if (day != null) {

                    if (appoint.getDate().dayName.equals(day) && appoint.doctorID == doctorID) {
                        appointments.remove(j);
                        System.out.println("Email sent for " + patients.get(i).getPhoneNumber());
                        System.out.println("your appointment canceled");
                    }
                } else if (day == null && appoint.doctorID == doctorID) {
                    appointments.remove(j);
                    System.out.println("Email sent for " + patients.get(i).getPhoneNumber());
                    System.out.println("your appointment canceled");
                }
            }
        }

    }

    public static void CreateFile(int patientID) throws IOException {
        boolean flag = true;
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientFile() != null) {
                continue;
            }
            if (patients.get(i).getPatientID() == patientID) {
                flag = false;
                String fileName = patients.get(i).getName();
                PrintWriter writer = new PrintWriter(fileName + ".txt", StandardCharsets.UTF_8);
                writer.println(patients.get(i).toString());
                writer.close();
                patients.get(i).setPatientFile("F:\\Desktop\\" + fileName + ".txt");
            }
        }
        if (flag) {
            System.out.println("patientID not found");
        }

    }

    public static String Print(int patientID, int appointmentID) {
        boolean found = false;
        String info = "";
        for (Patient patient : patients) {
            if (patient.getPatientID() == patientID) {
                for (Appointment appointment : patient.getPreviousAppointemnts()) {
                    if (appointment.getAppointmentID() == appointmentID) {
                        info = appointment.toString();
                        found = true;
                    }
                }
            }
        }
        if (found) {
            return info;
        }
        return "-2";
    }

    public static void CancelDay(Doctor doctorID, Days day) throws IOException {
        canceling(1, doctorID, null, day);

    }

    public static void CancelDays(Doctor doctorID, List<Days> days) throws IOException {
        canceling(2, doctorID, days, null);

    }

    public static void CancelSchedule(Doctor doctorID) throws IOException {
        canceling(3, doctorID, null, null);
    }

    public static void canceling(int what, Doctor doctor, List<Days> days, Days day) throws IOException {
        boolean exist = false;

        switch (what) {
            case 1:
                String str = doctor.cancelDay(day);

                outPublic.writeUTF(str + "\n" + doctor.toString());
                SendCancellation(doctor.getDoctorID(), day);
                break;
            case 2:
                String strr = "";
                for (Days dai : days) {
                    strr = doctor.cancelDay(dai);
                    SendCancellation(doctor.getDoctorID(), dai);

                }
                outPublic.writeUTF(strr + "\n" + doctor.toString());
                break;
            case 3:
                int x = doctor.workingSchedule.size();
                doctor.workingSchedule.clear();
                outPublic.writeUTF("Schedule deleted\n" + doctor.toString());

                for (int i = 0; i < x; i++) {

                    SendCancellation(doctor.getDoctorID(), null);
                }
                break;
        }

    }

    public static void SetSchedule(int doctorID, List<Days> days, String from, String to) {
        double finalFrom = StringtoDouble(from);
        double finalTo = StringtoDouble(to);
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorID() == doctorID) {
                doctor.setSchedule(days, finalFrom, finalTo);
            }
        }

    }

    private static double StringtoDouble(String str) {
        try {
            String[] Form = str.split(":");
            double finalForm = Double.parseDouble(Form[0] + "." + Form[1]);
            return finalForm;
        } catch (Exception e) {
            double finalForm = Double.parseDouble(str + ".00");
            return finalForm;
        }

    }

    private static String[] Splitting(String str) {
        String[] Form = str.split(" ");
        return Form;

    }

    public static boolean SetAppointment(Patient patient, int doctorID, OurDate date, String time) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorID() == doctorID) {
                if (doctor.IsAvailable(date, time)) {
                    Appointment appointment = new Appointment(doctorID, doctor.getDoctorName(), time, date);
                    doctor.appointments.add(appointment);
                    patient.setPreviousAppointemnt(appointment);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private static void CheckOption(String option, DataInputStream in, DataOutputStream out, Socket client) throws IOException {
        switch (option) {
            case "1":
                out.writeUTF("Enter patient Name:");//
                String name = in.readUTF();//recieve name
                out.writeUTF("Enter patient PhoneNumber:");//
                String phone = in.readUTF();//recieve phone
                out.writeUTF("Enter patient E-Mail(or enter *):");//
                String email = in.readUTF();//recieve email
                Patient patient = new Patient(name, phone, email);
                patients.add(patient);
                CreateFile(patient.patientID);
                out.writeUTF("File created.");
                break;
            case "2":
                out.writeUTF("Enter doctor ID:");
                int doctorID = in.readInt();
                out.writeUTF("Enter Number of prefered set of days:\n"
                        + "1- sunday - tuesday - thursday\n"
                        + "2- monday - wednesday - thursday\n"
                        + "3- friday - saturday");
                int daysOption = in.readInt();
                List<Days> daysList = new ArrayList<Days>();
                if (daysOption == 1) {
                    daysList.add(Days.sunday);
                    daysList.add(Days.tuesday);
                    daysList.add(Days.thursday);
                } else if (daysOption == 2) {
                    daysList.add(Days.monday);
                    daysList.add(Days.wednesday);
                    daysList.add(Days.thursday);
                } else {
                    daysList.add(Days.friday);
                    daysList.add(Days.saturday);
                }
                out.writeUTF("Enter hours range (24 hour format(hh:mm)):\nFrom: ");
                String from = in.readUTF();
                out.writeUTF("To: ");
                String To = in.readUTF();
                SetSchedule(doctorID, daysList, from, To);
                out.writeUTF("Schedule is set successfully");
                break;
            case "3":
                out.writeUTF("Enter patient ID:");//
                int patientID = in.readInt();//recieve patient id
                out.writeUTF("Enter appointment ID:");//
                int appointmentID = in.readInt();//recieve appointment id
                String info = Print(patientID, appointmentID);
                if (info.equals("-2")) {
                    out.writeUTF("-2");
                } else {
                    out.writeUTF(info);
                }
                break;

            case "4":
                out.writeUTF("Enter doctor ID:");
                int doctorID_case4 = in.readInt();
                Object[] whichOne = doctorInfo(doctorID_case4);
                if (whichOne[0] == null) {
                    out.writeUTF(whichOne[1].toString());
                } else {
                    out.writeUTF(whichOne[1].toString());
                    out.writeUTF("Enter day you want to cancel:");
                    String day = in.readUTF();
                    CancelDay((Doctor) whichOne[0], StringToDay(day));
                }

                break;
            case "5":
                out.writeUTF("Enter doctor ID:");
                int doctorID_case5 = in.readInt();
                Object[] whichOneC5 = doctorInfo(doctorID_case5);
                if (whichOneC5[0] == null) {
                    out.writeUTF(whichOneC5[1].toString());
                } else {
                    out.writeUTF(whichOneC5[1].toString() + "\nEnter days you want to cancel separeted by spaces:");
                    String str = in.readUTF();
                    String[] days = Splitting(str);
                    List<Days> dais = new ArrayList<Days>();
                    for (String day : days) {
                        dais.add(StringToDay(day));
                    }
                    CancelDays((Doctor) whichOneC5[0], dais);
                }
                break;

            case "6":
                out.writeUTF("Enter doctor ID:");//
                int doctorID_case6 = in.readInt();//recievd D id
                Object[] whichOneC6 = doctorInfo(doctorID_case6);
                if (whichOneC6[0] == null) {
                    out.writeUTF(whichOneC6[1].toString());
                } else {
                    out.writeUTF(whichOneC6[1].toString());
                    CancelSchedule((Doctor) whichOneC6[0]);
                }
                break;
            case "7":
                out.writeUTF("Enter patient ID:");//
                int patientID_C7 = in.readInt();//recieve id
                Patient patientC7 = new Patient();
                for (Patient p : patients) {
                    if (p.patientID == patientID_C7) {
                        patientC7 = p;
                        out.writeUTF("found");//
                    } else {
                        out.writeUTF("notfound");//
                        out.writeUTF("Enter patient Name:");//
                        String nameC7 = in.readUTF();//recieve name
                        out.writeUTF("Enter patient PhoneNumber:");//
                        String phoneC7 = in.readUTF();//recieve phone
                        out.writeUTF("Enter patient E-Mail(or enter *):");//
                        String emailC7 = in.readUTF();//recieve email
                        Patient patientC7_2 = new Patient(nameC7, phoneC7, emailC7);
                        CreateFile(patientC7_2.patientID);
                        patientC7 = p;
                    }

                }
                out.writeUTF("Enter doctor ID:");//
                int doctorIDC7 = in.readInt();//
                boolean foundC7 = false;
                for (Doctor doctor : doctors) {
                    if (doctor.getDoctorID() == doctorIDC7) {
                        foundC7 = true;
                        break;
                    }
                }
                if (foundC7) {
                    out.writeUTF("found");//
                    boolean iterate = false;

                    while (!iterate) {
                        out.writeUTF("Enter appointment date(format(dd/mm)):");//
                        String dateC7 = in.readUTF();//
                        String[] arrOfDate = dateC7.split("/", 2);
                        OurDate dateC7_2 = new OurDate(Integer.parseInt(arrOfDate[0]), Integer.parseInt(arrOfDate[1]));//
                        out.writeUTF("Enter appointmet time (24 hour format(hh:mm)):\n");
                        String fromC7 = in.readUTF();//
                        iterate = SetAppointment(patientC7, doctorIDC7, dateC7_2, fromC7);
                        if (!iterate) {
                            out.writeUTF("Date and time is not available, try again :)");//
                        } else {
                            out.writeUTF("done");//
                        }
                    }
                } else {
                    out.writeUTF("Doctor ID not found");////
                }
                break;

            case "8":
                out.writeUTF("Patients List:");//
                out.writeUTF(ShowPatients());//
                break;
            case "9":
                out.writeUTF("Doctors List:");//
                out.writeUTF(ShowDoctors());//
                break;

            case "0":
                out.writeUTF("Good bye, talia");
                tryAgain = false;
                in.close();
                out.close();
                client.close();
                break;
            default:
                out.writeUTF("please choose between the range 0-6");
        }
    }

    private static Object[] doctorInfo(int id) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorID() == id) {

                return new Object[]{doctor, doctor.toString()};
            }
        }
        return new Object[]{null, "The doctor does not exist"};
    }

    private static Days StringToDay(String day) {
        switch (day) {
            case "saturday":
                return Days.saturday;
            case "sunday":
                return Days.sunday;
            case "monday":
                return Days.monday;
            case "tuesday":
                return Days.tuesday;
            case "wednesday":
                return Days.wednesday;
            case "thursday":
                return Days.thursday;
            default:
                return Days.friday;
        }
    }

    private static String ShowOptions() {
        return "Select one of the following options:\n"
                + "--------------------------------------\n"
                + "1- Create patient file\n"
                + "2- Set doctor schedule\n"
                + "3- Print specific appointment details\n"
                + "4- Cancel day\n"
                + "5- Cancel days\n"
                + "6- Cancel schedule\n"
                + "7- Set appointment\n"
                + "8- Show patients\n"
                + "9- Show doctors\n"
                + "0- Logout and Exit\n"
                + "--------------------------------------\n";
    }

    private static String ShowPatients() {
        String patientsListAsString = "";
        for (Patient patient : patients) {
            patientsListAsString += patient.toString();
        }
        return patientsListAsString;
    }

    private static String ShowDoctors() {
        String doctorsListAsString = "";
        for (Doctor doctor : doctors) {
            doctorsListAsString += doctor.toString();
        }
        return doctorsListAsString;
    }
}
