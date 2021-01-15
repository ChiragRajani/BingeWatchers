package com.example.bingewatchers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.Format;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    // Use the current date as the default date in the picker
    Calendar c = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day =c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
      //  DatePickerDialog hello=new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day) ;

        return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day) ;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        System.out.println(month);

        String months=(month < 10 ? "0" : "") +month;
        String days=(day < 10 ? "0": "") + day ;
        String dateFormat =days+"/"+months+"/"+year;

        System.out.println(dateFormat);
        if (SignUp.dob!=null)
            SignUp.dob.setText(dateFormat);
        if(Activity_profile.dob!=null)
            Activity_profile.dob.setText(dateFormat);
        // Do something with the date chosen by the user
    }
}
