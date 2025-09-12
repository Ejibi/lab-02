package com.example.listcity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textInputEditText;
    private Button addButton;
    private Button deleteButton;
    private Button confirmButton;
    private ListView cityListView;

    private ArrayAdapter<String> cityAdapter;
    private ArrayList<String> cityDataList;

    private String confirmedCityName = null;
    private int selectedCityPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputEditText = findViewById(R.id.textInputEditText);
        addButton = findViewById(R.id.button6);
        deleteButton = findViewById(R.id.button5);
        confirmButton = findViewById(R.id.button7);
        cityListView = findViewById(R.id.city_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        cityDataList = new ArrayList<>();
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, cityDataList);
        cityListView.setAdapter(cityAdapter);
        cityListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        addButton.setEnabled(false);

        setupListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupListeners() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentInput = textInputEditText.getText().toString().trim();
                if (!currentInput.isEmpty()) {
                    confirmedCityName = currentInput;
                    Toast.makeText(MainActivity.this, "Confirmed: " + confirmedCityName + ". Press 'Add' to add to list.", Toast.LENGTH_LONG).show();
                    addButton.setEnabled(true);
                    textInputEditText.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city name to confirm", Toast.LENGTH_SHORT).show();
                    confirmedCityName = null;
                    addButton.setEnabled(false);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConfirmedCityToList();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedCity();
            }
        });

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCityPosition = position;
            }
        });
    }

    private void addConfirmedCityToList() {
        if (confirmedCityName != null && !confirmedCityName.isEmpty()) {
            if (!cityDataList.contains(confirmedCityName)) {
                cityDataList.add(confirmedCityName);
                cityAdapter.notifyDataSetChanged();
                Toast.makeText(this, "'" + confirmedCityName + "' added to the list.", Toast.LENGTH_SHORT).show();

                confirmedCityName = null;
                addButton.setEnabled(false);
                selectedCityPosition = -1;
                cityListView.clearChoices();
                cityListView.requestLayout();
            }
        } else {
            Toast.makeText(this, "No city name confirmed. Please use 'Confirm' first.", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteSelectedCity() {
        if (selectedCityPosition != -1 && selectedCityPosition < cityDataList.size()) {
            String removedCity = cityDataList.remove(selectedCityPosition);
            cityAdapter.notifyDataSetChanged();
            Toast.makeText(this, "'" + removedCity + "' deleted.", Toast.LENGTH_SHORT).show();

            selectedCityPosition = -1;
            cityListView.clearChoices();
            cityListView.requestLayout();
        } else {
            Toast.makeText(this, "No city selected to delete.", Toast.LENGTH_SHORT).show();
        }
    }
}
