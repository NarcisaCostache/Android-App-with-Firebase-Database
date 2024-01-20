package com.example.myparkingproj2;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myparkingproj2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class YourReservations extends AppCompatActivity {

    private RecyclerView rvReservationsList;
    private List<Reservation> reservationList;
    private ReservationsAdapter reservationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_reservations);

        rvReservationsList = findViewById(R.id.rvReservationsList);
        reservationList = new ArrayList<>();
        reservationsAdapter = new ReservationsAdapter(reservationList);

        rvReservationsList.setLayoutManager(new LinearLayoutManager(this));
        rvReservationsList.setAdapter(reservationsAdapter);

        loadReservations();
    }

    private void loadReservations() {
        DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");

        reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    if (reservation != null && reservation.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        reservationList.add(reservation);
                    }
                }
                reservationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    // Other methods as needed
}
