/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import java.time.LocalDate;

public class OurDate {

    int month = 0;
    int day = 0;
    Days dayName;

    public OurDate(int day, int month) {
        this.month = month;
        this.day = day;
        LocalDate localDate = LocalDate.of(2022, month,day);
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        this.dayName = StringToDay(dayOfWeek.toString().toLowerCase());
    }
    
 private Days StringToDay(String day) {
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
 
 

}