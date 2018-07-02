package com.jamiahus.ifunieray;

/**
 * Created by jamia on 3/24/2018.
 */

public class Task {
    String TaskTitle;
    String TaskDescription;
    String Month;
    int DayOfMonth;
    int Year;
    int TaskID;

    public Task (String inputTaskTitle, String inputTaskDescription, String inputMonth,
                      int inputDayOfMonth, int inputYear, int taskID){
        TaskTitle = inputTaskTitle;
        TaskDescription = inputTaskDescription;
        Month = inputMonth;
        DayOfMonth = inputDayOfMonth;
        Year = inputYear;
        TaskID = taskID;
    }
    public Task (int taskID){
        TaskID =taskID;
    }

    public int GetTaskID(){
        return TaskID;
    }
}
