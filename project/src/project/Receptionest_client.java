package project;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Receptionest_client {

    public static void main(String[] args) throws IOException {
        String receptionistName;
        String password;
        Socket client = new Socket("localhost", 3030);
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        Scanner input = new Scanner(System.in);
        BufferedReader bi = new BufferedReader(
                new InputStreamReader(System.in));
        while (Server.tryAgain) {
            System.out.println(in.readUTF()); //signup msg
            receptionistName = input.next();
            out.writeUTF(receptionistName); //
            System.out.print(in.readUTF()); //pass msg
            password = input.next();
            out.writeUTF(password);//

            if (in.readUTF().equals("-1")) {
                System.out.println("Incorrect Name or Password");
                System.out.println("Try Again? Y/N");
                String trying = input.next();
                out.writeBoolean(trying.equalsIgnoreCase("Y"));
                if (!trying.equalsIgnoreCase("Y")) {
                    break;
                }

            } else {
                String option = "";
                while (!option.equals("0")) {
                    System.out.println(in.readUTF());//menu
                    option = input.next();
                    out.writeUTF(option);//
                    switch (option) {
                        case "1":
                            System.out.println(in.readUTF());//Enter patient Name:
                            input.nextLine();
                            String name = input.nextLine();
                            out.writeUTF(name); //
                            System.out.println(in.readUTF());//Enter patient PhoneNumber:
                            String phone = input.next();
                            out.writeUTF(phone);//
                            System.out.println(in.readUTF());//Enter patient E-Mail(or enter *)
                            String email = input.next();
                            out.writeUTF(email);//
                            System.out.println(in.readUTF());
                            break;
                        case "2":
                            System.out.println(in.readUTF());
                            int doctorID = input.nextInt();
                            out.writeInt(doctorID);
                            System.out.println(in.readUTF());
                            int choice = input.nextInt();
                            out.writeInt(choice);
                            if (choice > 3) {
                                System.out.println(in.readUTF());
                                break;
                            }
                            System.out.println(in.readUTF());
                            String from = input.next();
                            out.writeUTF(from);
                            System.out.println(in.readUTF());
                            String To = input.next();
                            out.writeUTF(To);
                            System.out.println(in.readUTF());
                            break;
                        case "3":
                            System.out.println(in.readUTF());//Enter patient ID:
                            int patientID = input.nextInt();
                            out.writeInt(patientID);//
                            System.out.println(in.readUTF());//Enter patient ID:
                            int appointmentID = input.nextInt();
                            out.writeInt(appointmentID);//
                            String info = in.readUTF();
                            if (info.equals("-2")) {
                                System.out.println("The appointmet ID you entered is not correct");
                            } else {
                                System.out.println(info);
                            }
                            break;

                        case "4":
                            System.out.println(in.readUTF());
                            int doctorID_C4 = input.nextInt();
                            out.writeInt(doctorID_C4);
                            String response = in.readUTF();
                            System.out.println(response);
                            if (response.equalsIgnoreCase("The doctor does not exist")) {
                                break;
                            }
                            System.out.println(in.readUTF());
                            String day = input.next();
                            out.writeUTF(day);
                            System.out.println(in.readUTF());
                            break;
                        case "5":
                            System.out.println(in.readUTF());
                            int doctorID_C5 = input.nextInt();
                            out.writeInt(doctorID_C5);
                            String responseC5 = in.readUTF();
                            System.out.println(responseC5);
                            if (responseC5.equalsIgnoreCase("The doctor does not exist")) {
                                break;
                            }
                            String days = bi.readLine();
                            out.writeUTF(days);

                            System.out.println(in.readUTF());
                            break;
                        case "6":
                            System.out.println(in.readUTF());//"Enter doctor ID:"
                            int doctorID_C6 = input.nextInt();
                            out.writeInt(doctorID_C6);//
                            String responseC6 = in.readUTF();
                            System.out.println(responseC6);
                            if (responseC6.equalsIgnoreCase("The doctor does not exist")) {
                                break;
                            }
                            System.out.println(in.readUTF());
                            break;
                        case "7":
                            System.out.println(in.readUTF());//Enter patient ID:
                            int patientID_C7 = input.nextInt();
                            out.writeInt(patientID_C7);//
                            String resC7 = in.readUTF();
                            if (resC7.equals("notfound")) {
                                System.out.println(in.readUTF());//Enter patient Name:
                                String nameC7 = input.next();
                                out.writeUTF(nameC7);//

                                System.out.println(in.readUTF());//Enter patient PhoneNumber:
                                String phoneC7 = input.next();
                                out.writeUTF(phoneC7);//

                                System.out.println(in.readUTF());//Enter patient email:
                                String emailC7 = input.next();
                                out.writeUTF(emailC7);//
                            }
                            System.out.println(in.readUTF());//"Enter doctor ID:"
                            int doctorID_C7 = input.nextInt();
                            out.writeInt(doctorID_C7);//
                            String resC7_2 = in.readUTF();
                            if (resC7_2.equals("found")) {
                                while (true) {
                                    System.out.println(in.readUTF());//
                                    String dateC7 = input.next();
                                    out.writeUTF(dateC7);//
                                    System.out.println(in.readUTF());//
                                    String timeC7 = input.next();
                                    out.writeUTF(timeC7);//
                                    String resC7_3 = in.readUTF();
                                    if (resC7_3.equals("done")) {
                                        break;
                                    } else {
                                        System.out.println(resC7_3);
                                    }
                                }
                            } else {
                                System.out.println(in.readUTF());
                            }
                            break;
                        case "0":
                            System.out.println(in.readUTF());
                            Server.tryAgain = false;
                            in.close();
                            out.close();
                            client.close();
                            break;
                        default:
                            System.out.println(in.readUTF());
                            break;
                        case "8":
                        case "9":
                            System.out.println(in.readUTF());
                            System.out.println(in.readUTF());
                            break;
                    }
                }
            }
        }

    }
}
