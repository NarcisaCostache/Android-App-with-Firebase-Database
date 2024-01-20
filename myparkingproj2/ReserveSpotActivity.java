package com.example.myparkingproj2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReserveSpotActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Spinner spotSpinner, startTimeSpinner, endTimeSpinner;
    private TextView reservationTextView;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_spot);

        calendarView = findViewById(R.id.calendarView);
        spotSpinner = findViewById(R.id.spotSpinner);
        startTimeSpinner = findViewById(R.id.startTimeSpinner);
        endTimeSpinner = findViewById(R.id.endTimeSpinner);
        reservationTextView = findViewById(R.id.reservationTextView);
        Button makeReservationButton = findViewById(R.id.makeReservationButton);

        initializeSpinners();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(calendarView.getDate());
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
        });

        makeReservationButton.setOnClickListener(v -> makeReservation());
    }

    private void initializeSpinners() {
        String[] spots = {"Spot1", "Spot2", "Spot3", "Spot4", "Spot5", "Spot6", "Spot7", "Spot8", "Spot9", "Spot10"};
        ArrayAdapter<String> spotAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spots);
        spotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spotSpinner.setAdapter(spotAdapter);

        String[] timeSlots = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setAdapter(timeAdapter);
        endTimeSpinner.setAdapter(timeAdapter);
    }

    private void makeReservation() {
        String spot = spotSpinner.getSelectedItem().toString();
        String startTime = startTimeSpinner.getSelectedItem().toString();
        String endTime = endTimeSpinner.getSelectedItem().toString();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("ParkingSpots");
        DatabaseReference spotDateRef = databaseRef.child(spot).child(selectedDate);

        spotDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAvailable = true;
                for (DataSnapshot timeSlotSnapshot : dataSnapshot.getChildren()) {
                    String timeSlot = timeSlotSnapshot.getKey();
                    if (timeSlot.compareTo(startTime) >= 0 && timeSlot.compareTo(endTime) <= 0) {
                        Boolean isSlotAvailable = timeSlotSnapshot.getValue(Boolean.class);
                        if (isSlotAvailable == null || !isSlotAvailable) {
                            isAvailable = false;
                            break;
                        }
                    }
                }

                if (isAvailable) {
                    for (String time = startTime; !time.equals(endTime); ) {
                        spotDateRef.child(time).setValue(false);
                        int hour = Integer.parseInt(time.split(":")[0]);
                        hour++;
                        time = String.format(Locale.getDefault(), "%02d:00", hour);
                    }

                    Reservation newReservation = new Reservation();
                    newReservation.setSpotId(spot);
                    newReservation.setDate(selectedDate);
                    newReservation.setStartTime(startTime);
                    newReservation.setEndTime(endTime);
                    newReservation.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");
                    String reservationId = reservationsRef.push().getKey();
                    reservationsRef.child(reservationId).setValue(newReservation);

                    reservationTextView.setText(String.format(Locale.getDefault(), "Reservation made for %s on %s from %s to %s", spot, selectedDate, startTime, endTime));
                } else {
                    reservationTextView.setText("Selected time slot is not available.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                reservationTextView.setText("Error: " + databaseError.getMessage());
            }
        });
    }
}
