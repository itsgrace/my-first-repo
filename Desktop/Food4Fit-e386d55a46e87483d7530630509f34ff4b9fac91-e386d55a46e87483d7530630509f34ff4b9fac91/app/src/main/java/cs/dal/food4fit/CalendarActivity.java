package cs.dal.food4fit;

import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by graceliu on 2017-11-25.
 */

public class CalendarActivity extends Activity {

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        showDialog(DATE_DIALOG_ID);

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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth+1;
            day = selectedDay;

            String datestring = month+"-"+day+"-"+year;

            Intent mIntent = new Intent();
            mIntent.putExtra("dateString", datestring);
            setResult(RESULT_OK, mIntent);

            finish();

        }
    };

}
