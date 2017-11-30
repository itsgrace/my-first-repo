package cs.dal.food4fit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



/**
 * Created by graceliu on 2017-11-25.
 */

public class AddMealActivity extends Activity {
    private TextView tvDisplayDate;
    private RadioGroup radioMealGroup;
    private RadioButton radioMealButton;
    private Button ok;

    private int year;
    private int month;
    private int day;

    private String str;


    static final int DATE_DIALOG_ID = 999;
    boolean tag = true;

//    HashMap<String, cs.dal.food4fit.Calendar>findMP =new HashMap<>();

    FirebaseDatabase firebaseDBInstance = FirebaseDatabase.getInstance();
    DatabaseReference firebaseReference = firebaseDBInstance.getReference("MealCalendar");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmeal);

        tvDisplayDate = (TextView) findViewById(R.id.tvDate);
        radioMealGroup = (RadioGroup) findViewById(R.id.mealtag);
        ok = (Button)findViewById(R.id.okay);



        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year));

//        // Attach a listener to read the data at our posts reference
//        firebaseReference.child("GraceTest").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                cs.dal.food4fit.Calendar calendar = dataSnapshot.getValue(cs.dal.food4fit.Calendar.class);
////                findMP.put(dataSnapshot.getKey(),calendar);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });


        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioMealGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioMealButton = (RadioButton) findViewById(selectedId);
                String mealtag = radioMealButton.getText().toString();
                String datestring = tvDisplayDate.getText().toString();

                Toast.makeText(AddMealActivity.this,mealtag+"/"+datestring,10).show();

//                MealPlan rawMP = new MealPlan(getData(R.array.image_idsbf),getData(R.array.image_idsbf),getData(R.array.image_idsbf));
//                cs.dal.food4fit.Calendar rawcalendar = new cs.dal.food4fit.Calendar("GraceTest",datestring,rawMP);
//
//                findMP.put(datestring,rawcalendar);
//
//
//                MealPlan tempMP = push2Mealplan(findMP, datestring,mealtag, getIntent().getIntExtra("ImageID",0));
//                cs.dal.food4fit.Calendar tempcalendar = new cs.dal.food4fit.Calendar("GraceTest",datestring,tempMP);
//                findMP.put(datestring,tempcalendar);

                int imageID = getIntent().getIntExtra("ImageID",0);

                retrievedata(mealtag,datestring,imageID);

                finish();

            }

        });



        tvDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth+1;
            day = selectedDay;


            String datestring = month+"-"+day+"-"+year;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(month)
                    .append("-").append(day).append("-").append(year));



        }
    };

//
//    private void push2Calendar(HashMap<String, cs.dal.food4fit.Calendar> findMP, MealPlan mealPlan, String date, String user){
//
//        final cs.dal.food4fit.Calendar calendar = new cs.dal.food4fit.Calendar("Grace",date,mealPlan);
//
//        findMP.put(date,calendar);
//
//    }
//
//    public MealPlan push2Mealplan(HashMap<String, cs.dal.food4fit.Calendar> findMP, String date, String mealtag, int ImageID) {
//        final MealPlan mealPlan = findMP.get(date).getMealPlan();
//        Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(ImageID));
//        if(mealtag.matches("Breakfast")){
//            mealPlan.getBreakfastList().add(new ImageItem(bitmap,"Image#",ImageID));
//        }
//        if(mealtag.matches("Lunch")){
//            mealPlan.getLunchList().add(new ImageItem(bitmap,"Image#",ImageID));
//        }
//        if(mealtag.matches("Dinner")){
//            mealPlan.getDinnerList().add(new ImageItem(bitmap,"Image#",ImageID));
//        }
//
//        return mealPlan;
//    }
//
//    private ArrayList<ImageItem> getData(int imagearray) {
//        final ArrayList<ImageItem> imageItems = new ArrayList<>();
//        TypedArray imgs = getResources().obtainTypedArray(imagearray);
//
//        //R.array.image_ids
//        for (int i = 0; i < imgs.length(); i++) {
//            Bitmap bitmap1 = BitmapFactory.decodeStream(getResources().openRawResource(imgs.getResourceId(i,-1)));
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
//            imageItems.add(new ImageItem(bitmap1, "Image#" + i,imgs.getResourceId(i, -1)));
//        }
//        return imageItems;
//    }
private void retrievedata(final String mealtag, final String datestring, final int imageID){

    FirebaseDatabase firebaseDBInstance = FirebaseDatabase.getInstance();
    final DatabaseReference firebaseReference = firebaseDBInstance.getReference("MealCalendar");

    // Attach a listener to read the data at our posts reference
    firebaseReference.child("Grace").child(datestring).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(tag){
                tag = false;
                if(dataSnapshot.getValue()!=null){
                    str = dataSnapshot.getValue().toString();
                    if(str != null){
                        if(mealtag.matches("Breakfast")){
                            firebaseReference.child("Grace").child(datestring).setValue(str+"B."+imageID+"/");

                        }
                        if(mealtag.matches("Lunch")){
                            firebaseReference.child("Grace").child(datestring).setValue(str+"L."+imageID+"/");

                        }
                        if(mealtag.matches("Dinner")){
                            firebaseReference.child("Grace").child(datestring).setValue(str+"D."+imageID+"/");

                        }

                    }
                }

                else{
                    if(mealtag.matches("Breakfast")){
                        firebaseReference.child("Grace").child(datestring).setValue("B."+imageID+"/");

                    }
                    if(mealtag.matches("Lunch")){
                        firebaseReference.child("Grace").child(datestring).setValue("L."+imageID+"/");

                    }
                    if(mealtag.matches("Dinner")){
                        firebaseReference.child("Grace").child(datestring).setValue("D."+imageID+"/");

                    }
                }

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getCode());
        }

    });


}

public void addmeal(String mealtag,String datestring,int imageID){

}


}
