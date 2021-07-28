package fr.shk.thetrashapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTrashActivity extends AppCompatActivity {

    private boolean firstLaunch = true;

    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;

    private EditText editTextDate;
    private EditText editTextTime;
    private RadioGroup radioGroup;
    private RadioButton radioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trash);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Supprimmer l'affichage du clavier au lancement de l'activitée
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        this.editTextDate = (EditText) this.findViewById(R.id.textdate_trash);
        this.editTextDate.setShowSoftInputOnFocus(false);
        this.editTextTime = (EditText) this.findViewById(R.id.texttime_trash);
        this.editTextTime.setShowSoftInputOnFocus(false);

        this.editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        this.editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!firstLaunch) {
                        showDatePicker();
                    } else {
                        firstLaunch = false;
                    }
                }
            }
        });

        this.editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        this.editTextTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePicker();
                }
            }
        });

        Button btn_back = (Button) findViewById(R.id.btn_trash_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRetour = new Intent();
                setResult(RESULT_CANCELED, intentRetour);
                finish();
            }
        });

        Button btn_valider = (Button) findViewById(R.id.btn_trash_validate);
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDate.length() != 0 || editTextTime.length() != 0) {
                    radioGroup = (RadioGroup) findViewById(R.id.radiogrp_trash);
                    int idrg = radioGroup.getCheckedRadioButtonId();
                    if (idrg != -1) {
                        ClientDbHelper bdd = new ClientDbHelper(AddTrashActivity.this);
                        SQLiteDatabase db = bdd.getWritableDatabase();

                        radioButton = (RadioButton) findViewById(idrg);
                        String radioValue = radioButton.getText().toString();

                        if (!alreadyExist(db, "trash", radioValue)) {
                            ContentValues values = new ContentValues();
                            values.put("color", radioValue);
                            values.put("jourPassage", editTextDate.getText().toString());
                            values.put("heurePassage", editTextTime.getText().toString());
                            db.insert("trash", null, values);
                            Intent intentRetour = new Intent();
                            intentRetour.putExtra("color", radioValue);
                            setResult(RESULT_OK, intentRetour);
                            finish();
                        } else {
                            Toast.makeText(AddTrashActivity.this, getString(R.string.err_trash_already_exist), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddTrashActivity.this, getString(R.string.err_trash_not_selected), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddTrashActivity.this, getString(R.string.err_trash_empty_field), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDatePicker() {
        DatePickerDialog picker;

        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        // Listener pour le date picker
        picker = new DatePickerDialog(AddTrashActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
                String goal = "";
                try {
                    Date date = inFormat.parse(dayOfMonth + "/" + (month +1) + "/" + year);
                    SimpleDateFormat outFormat;
                    if (LocaleList.getDefault().get(0).toString().equalsIgnoreCase("fr_FR")) {
                        outFormat = new SimpleDateFormat("EEEE", Locale.FRANCE);
                    } else {
                        outFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                    }
                    goal = outFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                editTextDate.setText(goal);
            }
        }, year, month, day);
        picker.show();
    }

    private void showTimePicker() {
        if (lastSelectedHour == -1) {
            // Récupération de l'heure actuelle
            final Calendar c = Calendar.getInstance();
            lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            lastSelectedMinute = c.get(Calendar.MINUTE);
        }

        // Listener pour le time set
        TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm");
                String goal = "";
                try {
                    Date date = inFormat.parse(hourOfDay + ":" + minute);
                    SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                    goal = outFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                editTextTime.setText(goal);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };

        // Création du dialog
        TimePickerDialog timePickerDialog = null;

        // Modification du mode du timepicker
        //timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, lastSelectedHour, lastSelectedMinute, true);
        timePickerDialog = new TimePickerDialog(AddTrashActivity.this, timeSetListener, lastSelectedHour, lastSelectedMinute, true);

        timePickerDialog.show();
    }

    private boolean alreadyExist(SQLiteDatabase db, String table, String checkValue) {
        if (table == "trash") {
            String[] colonne = {"color", "jourPassage", "heurePassage"};
            String[] select = {};
            Cursor curseur = db.query("trash", colonne, "", select, null, null, "color ASC");

            if (curseur.moveToFirst()) {
                do {
                    String trashColor = curseur.getString(curseur.getColumnIndexOrThrow("color"));

                    if (checkValue.equalsIgnoreCase(trashColor)) {
                        curseur.close();
                        return true;
                    }

                } while (curseur.moveToNext());
            }

            curseur.close();
            return false;
        }
        return false;
    }
}