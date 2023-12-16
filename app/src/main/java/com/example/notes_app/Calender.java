package com.example.notes_app;

import static android.content.ContentValues.TAG;

import static org.billthefarmer.view.CalendarUtils.isSameDay;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.metrics.Event;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.billthefarmer.view.CalendarListener;
import org.billthefarmer.view.CustomCalendarDialog;
import org.billthefarmer.view.CustomCalendarView;
import org.billthefarmer.view.DayDecorator;
import org.billthefarmer.view.DayView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calender extends AppCompatActivity{
    CalendarView simpleCalendarView;
    private List<Date> highlightedDates;
    private String currentDateText;
    private SimpleDateFormat dateFormat= new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

//        simpleCalendarView = (CalendarView)findViewById(R.id.calender);
//        simpleCalendarView.setFocusedMonthDateColor(Color.RED); // set the red color for the dates of  focused month
//        simpleCalendarView.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
//        simpleCalendarView.setSelectedWeekBackgroundColor(Color.RED); // red color for the selected week's background
//        simpleCalendarView.setWeekSeparatorLineColor(Color.GREEN);
//        // Add Listener in calendar
//        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                // display the selected date by using a toast
//                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
//            }
//        });
//        Calendar tomorrow = Calendar.getInstance();
//        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
//        CustomCalendarView calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
//
//        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
//
//        calendarView.refreshCalendar(currentCalendar);
//        calendarView.setCalendarListener(new CalendarListener()
//        {
//            @Override
//            public void onMonthChanged(Calendar calendar) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
//                Log.d("kk", df.format(calendar.getTime()));
//            }
//
//            @Override
//            public void onDateSelected(Calendar calendar)
//            {
//                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                Log.d("kk", df.format(calendar.getTime()));
//                Calendar tomorrow = Calendar.getInstance();
//                tomorrow.add(Calendar.DAY_OF_MONTH, 1);
//                List<DayDecorator> decorators = new ArrayList<DayDecorator>();
//            }
//        });

    }

//    @Override
//    public void onDateSet(CustomCalendarView view, int year, int month, int date) {
//
//    }
//
//    public interface OnDateSetListener
//    {
//        public abstract void onDateSet (CustomCalendarView view,
//                                        int year, int month, int date);
//    }
//    private OnDateSetListener onDateSetListener = new OnDateSetListener() {
//        @Override
//        public void onDateSet(CustomCalendarView view, int year, int month, int date) {
//            // Handle the date set event here
//            // You can implement your logic to deal with the selected date
//            // For example, display it in a TextView or perform some other action
//            Toast.makeText(Calender.this, "Date set: " + year + "-" + (month + 1) + "-" + date, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//
//    public void showCustomCalendarDialog(Calendar date) {
//        CustomCalendarDialog dialog = new CustomCalendarDialog(
//                this,
//                this, // Pass the activity as the OnDateSetListener
//                date.get(Calendar.YEAR),
//                date.get(Calendar.MONTH),
//                date.get(Calendar.DATE)
//        );
//
//        // Get the CustomCalendarView from the dialog
//        CustomCalendarView calendarView = dialog.getCalendarView();
//
//        // Create a list of decorators
//        List<DayDecorator> decorators = new ArrayList<>();
//
//        // Add your custom date highlighting decorator
//        decorators.add(new HighlightDecorator());
//
//        // Set the decorators to the CustomCalendarView
//        calendarView.setDecorators(decorators);
//
//        // Show the dialog
//        dialog.show();
//    }
//    public class HighlightDecorator implements DayDecorator {
//
//        private List<Calendar> highlightedDates = getHighlightedDates();
//
//        @Override
//        public void decorate(DayView dayView) {
//            // Check if the date should be highlighted
//            Calendar currentDate = dayView.getDate();
//
//            // Get tomorrow's date
//            Calendar tomorrow = Calendar.getInstance();
//            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
//
//            // Debugging statements
//            Log.d("HighlightDecorator", "Current Date: " + currentDate.getTime().toString());
//            Log.d("HighlightDecorator", "Tomorrow's Date: " + tomorrow.getTime().toString());
//
//            // Check if the date is tomorrow
//            if (isSameDay(currentDate, tomorrow)) {
//                // Debugging statement
//                Log.d("HighlightDecorator", "Highlighting tomorrow's date!");
//
//                // Apply your highlighting logic here
//                dayView.setBackgroundResource(R.drawable.baseline_add_24);
//            }
//
//            if (highlightedDates.contains(currentDate)) {
//                // Debugging statement
//                Log.d("HighlightDecorator", "Highlighting custom date!");
//
//                // Apply your highlighting logic here
//                dayView.setBackgroundResource(R.drawable.baseline_add_24);
//            }
//        }
//
//        private List<Calendar> getHighlightedDates() {
//            // Your logic to get the list of dates to be highlighted
//            // Example: return a list of Calendar objects representing highlighted dates
//            List<Calendar> highlightedDates = new ArrayList<>();
//            // Add your highlighted dates to the list
//            // highlightedDates.add(calendarInstance);
//            return highlightedDates;
//        }
//    }


}