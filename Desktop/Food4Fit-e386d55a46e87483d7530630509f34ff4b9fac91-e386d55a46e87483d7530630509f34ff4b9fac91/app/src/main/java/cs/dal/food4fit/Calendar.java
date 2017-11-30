package cs.dal.food4fit;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by graceliu on 2017-11-24.
 */

public class Calendar {
    private String userID;
    private String date;
    private MealPlan mealPlan;

    public Calendar(String user, String date, MealPlan mealPlan) {
        super();
        this.userID = user;
        this.date = date;
        this.mealPlan = mealPlan;
    }

    public String getUserID() {
        return userID;
    }

    public String getDate() {
        return date;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMealPlan(MealPlan mealPlan){this.mealPlan = mealPlan;}
}
