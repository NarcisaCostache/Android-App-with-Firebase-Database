package com.example.myparkingproj2;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Existing code
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("USER_EMAIL");
        TextView welcomeMessage = findViewById(R.id.welcomeText);
        welcomeMessage.setText("Welcome " + userEmail);

        Button btnGetParkingSpot = findViewById(R.id.btnGetParkingSpot);
        Button btnReserveParkingSpot = findViewById(R.id.btnReserveParkingSpot);
        Button btnViewMyReservations = findViewById(R.id.btnViewMyReservations);

        btnGetParkingSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, AssignSpotActivity.class);
                startActivity(intent);
            }
        });

        btnReserveParkingSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, ReserveSpotActivity.class);
                startActivity(intent);
            }
        });

        btnViewMyReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, YourReservations.class);
                startActivity(intent);
            }
        });

    }

}
