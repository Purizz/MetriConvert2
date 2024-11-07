// MainActivity.java
package com.example.projekmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner metricSpinner, unitSpinnerFrom, unitSpinnerTo;
    private EditText inputValue;
    private TextView resultTextView;

    private Map<String, List<String>> unitsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen
        metricSpinner = findViewById(R.id.metricSpinner);
        unitSpinnerFrom = findViewById(R.id.unitSpinnerFrom);
        unitSpinnerTo = findViewById(R.id.unitSpinnerTo);
        inputValue = findViewById(R.id.inputValue);
        resultTextView = findViewById(R.id.resultTextView);

        // Data metrik dan satuan
        String[] metrics = {"Panjang", "Berat"};
        unitsMap.put("Panjang", List.of("Meter", "Kilometer", "Centimeter"));
        unitsMap.put("Berat", List.of("Gram", "Kilogram", "Pound"));

        // Adapter untuk metrik
        ArrayAdapter<String> metricAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, metrics);
        metricAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metricSpinner.setAdapter(metricAdapter);

        metricSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMetric = metrics[position];
                List<String> units = unitsMap.get(selectedMetric);

                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, units);
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                unitSpinnerFrom.setAdapter(unitAdapter);
                unitSpinnerTo.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        inputValue.setOnEditorActionListener((v, actionId, event) -> {
            String fromUnit = unitSpinnerFrom.getSelectedItem().toString();
            String toUnit = unitSpinnerTo.getSelectedItem().toString();
            String valueStr = inputValue.getText().toString();

            if (!valueStr.isEmpty()) {
                double value = Double.parseDouble(valueStr);
                double result = convertMetric(value, fromUnit, toUnit);
                resultTextView.setText("Hasil Konversi: " + result);
            } else {
                Toast.makeText(MainActivity.this, "Masukkan nilai yang valid", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private double convertMetric(double value, String fromUnit, String toUnit) {
        if (fromUnit.equals("Meter") && toUnit.equals("Kilometer")) return value / 1000;
        if (fromUnit.equals("Kilometer") && toUnit.equals("Meter")) return value * 1000;
        if (fromUnit.equals("Gram") && toUnit.equals("Kilogram")) return value / 1000;
        if (fromUnit.equals("Kilogram") && toUnit.equals("Gram")) return value * 1000;
        if (fromUnit.equals("Pound") && toUnit.equals("Kilogram")) return value * 0.453592;
        if (fromUnit.equals("Kilogram") && toUnit.equals("Pound")) return value / 0.453592;
        return value; // Jika satuan tidak berubah
    }
}
