package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Change_recept extends AppCompatActivity {

    public EditText l1, l2, l3, l4, l5, r1, r2, r3, r4, r5;
    public Button cnfrmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_recept);

        l1 = findViewById(R.id.OS_1);
        l2 = findViewById(R.id.OS_2);
        l3 = findViewById(R.id.OS_3);
        l4 = findViewById(R.id.OS_4);
        l5 = findViewById(R.id.OS_5);
        r1 = findViewById(R.id.OD_1);
        r2 = findViewById(R.id.OD_2);
        r3 = findViewById(R.id.OD_3);
        r4 = findViewById(R.id.OD_4);
        r5 = findViewById(R.id.OD_5);

        cnfrmBtn = findViewById(R.id.page3_confirm_button);
        cnfrmBtn.setOnClickListener(v -> {
            l1.setText(l1.getText());
            l2.setText(l2.getText());
            l3.setText(l3.getText());
            l4.setText(l4.getText());
            l5.setText(l5.getText());
            // бд
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        });

    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}