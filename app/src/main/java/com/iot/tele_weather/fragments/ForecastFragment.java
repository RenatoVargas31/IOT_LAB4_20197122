package com.iot.tele_weather.fragments;

import static android.content.Context.SENSOR_SERVICE;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iot.tele_weather.R;
import com.iot.tele_weather.adapters.ForecastAdapter;
import com.iot.tele_weather.api.WeatherApiClient;
import com.iot.tele_weather.databinding.FragmentForecastBinding;
import com.iot.tele_weather.models.ForecastResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastFragment extends Fragment implements SensorEventListener {
    private FragmentForecastBinding binding;
    private ForecastAdapter adapter;
    private long locationId = -1;
    private String locationName = "Ubicación";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isAccelerometerAvailable;
    private static final float ACCELERATION_THRESHOLD = 20.0f; // 20 m/s²
    private long lastShakeTime = 0;
    private static final long SHAKE_COOLDOWN = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Configurar RecyclerView
        adapter = new ForecastAdapter();
        binding.rvForecasts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvForecasts.setAdapter(adapter);

        // Obtener argumentos si llegan desde LocationFragment
        if (getArguments() != null) {
            locationId = getArguments().getLong("locationId", -1);
            locationName = getArguments().getString("locationName", "Ubicación");

            if (locationId != -1) {
                binding.etLocationId.setText(String.valueOf(locationId));
                binding.etDaysForecast.setText(String.valueOf(7));
                fetchForecast(locationId, 7); // Por defecto 7 días
            }
        }

        // Configurar botón de búsqueda
        binding.btnSearchForecast.setOnClickListener(v -> {
            String locationIdStr = binding.etLocationId.getText().toString().trim();
            String daysStr = binding.etDaysForecast.getText().toString().trim();

            if (locationIdStr.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese un ID de ubicación", Toast.LENGTH_SHORT).show();
                return;
            }

            int days = 7;
            if (!daysStr.isEmpty()) {
                days = Integer.parseInt(daysStr);
                if (days < 1 || days > 14) {
                    Toast.makeText(requireContext(), "Los días deben estar entre 1 y 14", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            long id = Long.parseLong(locationIdStr);
            fetchForecast(id, days);
        });
        // Inicializar el acelerómetro
        setupAccelerometer();

        // Toast informativo para el usuario
        Toast.makeText(requireContext(),
                "Agite el dispositivo para eliminar los pronósticos",
                Toast.LENGTH_LONG).show();
    }
    private void fetchForecast(long locationId, int days) {
        binding.btnSearchForecast.setEnabled(false);

        // Realizar la petición a la API
        WeatherApiClient.getInstance().getForecast(
                WeatherApiClient.getApiKey(),
                "id:" + locationId,
                days
        ).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                binding.btnSearchForecast.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    ForecastResponse forecastResponse = response.body();
                    if (forecastResponse.getForecast() != null &&
                            forecastResponse.getForecast().getForecastDays() != null) {

                        adapter.setForecastDays(forecastResponse.getForecast().getForecastDays());
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron pronósticos", Toast.LENGTH_SHORT).show();
                        adapter.clearForecastDays();
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                binding.btnSearchForecast.setEnabled(true);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupAccelerometer() {
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (accelerometer != null) {
                isAccelerometerAvailable = true;
            } else {
                isAccelerometerAvailable = false;
                Toast.makeText(requireContext(),
                        "Acelerómetro no disponible en este dispositivo",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAccelerometerAvailable) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isAccelerometerAvailable) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Obtener valores del acelerómetro (x, y, z)
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calcular la aceleración total usando la raíz cuadrada de la suma de los cuadrados
            float acceleration = (float) Math.sqrt(x*x + y*y + z*z) - SensorManager.GRAVITY_EARTH;

            // Si la aceleración es mayor que el umbral
            if (acceleration > ACCELERATION_THRESHOLD) {
                long currentTime = System.currentTimeMillis();

                // Verificar el período de enfriamiento para evitar activaciones múltiples
                if (currentTime - lastShakeTime > SHAKE_COOLDOWN) {
                    lastShakeTime = currentTime;

                    // Mostrar diálogo de confirmación
                    showClearForecastsDialog();
                }
            }
        }
    }

    private void showClearForecastsDialog() {
        // Verificar que el fragmento esté adjunto para evitar excepciones
        if (!isAdded()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar acción");
        builder.setMessage("¿Desea eliminar los pronósticos obtenidos?");

        builder.setPositiveButton("Sí", (dialog, which) -> {
            // Limpiar los pronósticos
            if (adapter != null) {
                adapter.clearForecastDays();
                Toast.makeText(requireContext(), "Pronósticos eliminados", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar nada aquí
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}