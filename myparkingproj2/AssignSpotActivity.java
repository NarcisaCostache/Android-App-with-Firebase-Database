package com.example.myparkingproj2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AssignSpotActivity extends AppCompatActivity {

    private Spinner startTimeSpinner;
    private Spinner endTimeSpinner;
    private TextView assignedSpotTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_spot);

        startTimeSpinner = findViewById(R.id.startTimeSpinner);
        endTimeSpinner = findViewById(R.id.endTimeSpinner);
        assignedSpotTextView = findViewById(R.id.assignedSpotTextView);
        Button checkAvailabilityButton = findViewById(R.id.checkAvailabilityButton);

        checkAvailabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndAssignSpot();
            }
        });

        String[] timeSlots = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
        populateSpinners(timeSlots);
    }

    private void checkAndAssignSpot() {
        String startTime = startTimeSpinner.getSelectedItem().toString();
        String endTime = endTimeSpinner.getSelectedItem().toString();

        // Get the current date in the format "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(Calendar.getInstance().getTime());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference spotsRef = database.getReference("ParkingSpots");

        spotsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean spotFound = false;

                for (DataSnapshot spotSnapshot : dataSnapshot.getChildren()) {
                    // Get the availability data for the current date of the spot
                    DataSnapshot dateSnapshot = spotSnapshot.child(currentDate);

                    // Initialize a flag to track spot availability for the entire selected range
                    boolean isSpotAvailable = true;

                    // Iterate through time slots from startTime to endTime
                    for (int i = Integer.parseInt(startTime.substring(0, 2)); i <= Integer.parseInt(endTime.substring(0, 2)); i++) {
                        String timeSlot = String.format(Locale.getDefault(), "%02d:00", i);
                        Boolean isAvailable = dateSnapshot.child(timeSlot).getValue(Boolean.class);

                        if (isAvailable != null && !isAvailable) {
                            // Spot is not available for the entire range, break the loop
                            isSpotAvailable = false;
                            break;
                        }
                    }

                    // If the spot is available for the entire range, assign it
                    if (isSpotAvailable) {
                        String spotKey = spotSnapshot.getKey(); // e.g., "Spot1"
                        for (int i = Integer.parseInt(startTime.substring(0, 2)); i <= Integer.parseInt(endTime.substring(0, 2)); i++) {
                            String timeSlot = String.format(Locale.getDefault(), "%02d:00", i);
                            spotsRef.child(spotKey).child(currentDate).child(timeSlot).setValue(false);
                        }
                        assignedSpotTextView.setText("Assigned Spot: " + spotKey);
                        spotFound = true;
                        break;
                    }
                }

                if (!spotFound) {
                    assignedSpotTextView.setText("No available spots for the selected time range.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assignedSpotTextView.setText("Error: " + databaseError.getMessage());
            }
        });
    }

    private void populateSpinners(String[] timeSlots) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startTimeSpinner.setAdapter(adapter);
        endTimeSpinner.setAdapter(adapter);
    }
}
