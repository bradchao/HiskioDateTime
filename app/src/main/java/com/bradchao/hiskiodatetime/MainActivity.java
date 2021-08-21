package com.bradchao.hiskiodatetime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView dateText, timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
    }

    public void changeDate(View view) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar userSetDate = Calendar.getInstance();
                userSetDate.set(year, month, day);
                if (userSetDate.getTimeInMillis() < System.currentTimeMillis()){
                    Toast.makeText(MainActivity.this, "設定日期不合理", Toast.LENGTH_SHORT).show();
                }else {
                    dateText.setText(String.format("%04d-%02d-%02d", year, month + 1, day));
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void changeTime(View view) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private class CustomTimePickerDialog extends TimePickerDialog {
        private final static  int TIME_PICKER_INTERVAL = 15;
        private final OnTimeSetListener timeSetListener;
        private TimePicker timePicker;

        public CustomTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
            super(context, TimePickerDialog.THEME_HOLO_LIGHT ,listener, hourOfDay,
                    minute / TIME_PICKER_INTERVAL*30, is24HourView);
            timeSetListener = listener;
        }

        @Override
        public void updateTime(int hourOfDay, int minuteOfHour) {
            super.updateTime(hourOfDay, minuteOfHour);
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();

            try {
                Class<?> classFields = Class.forName("com.android.internal.R$id");
//                Field[] fields = classFields.getFields();
//                for (Field field : fields){
//                    Log.v("bradlog", field.getName());
//                }

                Field timePickerField = classFields.getField("timePicker");
                timePicker = (TimePicker) findViewById(timePickerField.getInt(null));
                Field minute = classFields.getField("minute");

                NumberPicker minuteSpinner = (NumberPicker) timePicker.findViewById(minute.getInt(null));
                minuteSpinner.setMinValue(0);
                minuteSpinner.setMaxValue(60 / TIME_PICKER_INTERVAL -1);
                ArrayList<String> displayedValues = new ArrayList<>();
                for (int i=0; i<60; i+=TIME_PICKER_INTERVAL){
                    displayedValues.add(String.format("%02d", i));
                }
                minuteSpinner.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));

            } catch (Exception e) {
                Log.v("bradlog", e.toString());
            }


        }
    }
}