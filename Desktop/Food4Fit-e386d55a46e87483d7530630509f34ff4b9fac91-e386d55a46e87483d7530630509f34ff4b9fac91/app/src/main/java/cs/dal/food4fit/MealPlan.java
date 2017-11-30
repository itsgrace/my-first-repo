package cs.dal.food4fit;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by graceliu on 2017-11-21.
 */

public class MealPlan {



    private ArrayList<Recipe> Breakfast;
    private ArrayList<Recipe> Lunch;
    private ArrayList<Recipe> Dinner;

    public MealPlan(ArrayList<Recipe> Breakfast, ArrayList<Recipe> Lunch, ArrayList<Recipe> Dinner) {

        this.Breakfast = Breakfast;
        this.Lunch = Lunch;
        this.Dinner = Dinner;

    }

    public ArrayList<Recipe> getBreakfastList() {
        return Breakfast;
    }

    public ArrayList<Recipe> getLunchList() {
        return Lunch;
    }

    public ArrayList<Recipe> getDinnerList() {
        return Dinner;
    }

}
