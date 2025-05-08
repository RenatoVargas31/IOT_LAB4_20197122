package com.iot.tele_weather.fragments;

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
import com.iot.tele_weather.adapters.SportsAdapter;
import com.iot.tele_weather.api.WeatherApiClient;
import com.iot.tele_weather.databinding.FragmentSportsBinding;
import com.iot.tele_weather.models.SportEvent;
import com.iot.tele_weather.models.SportsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportsFragment extends Fragment {

    private FragmentSportsBinding binding;
    private SportsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSportsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Aquí irá la lógica para mostrar eventos deportivos y configurar el RecyclerView
        // Inicializar RecyclerView
        adapter = new SportsAdapter();
        binding.rvSports.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSports.setAdapter(adapter);

        // Configurar botón de búsqueda
        binding.btnSearchSports.setOnClickListener(v -> {
            String location = binding.etSportsLocation.getText().toString().trim();
            if (location.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingrese un lugar", Toast.LENGTH_SHORT).show();
                return;
            }

            searchSportsEvents(location);
        });
    }
    private void searchSportsEvents(String location) {
        // Mostrar estado de carga
        binding.btnSearchSports.setEnabled(false);
        binding.btnSearchSports.setText("Buscando...");

        // Realizar la solicitud a la API
        WeatherApiClient.getInstance().getSports(
                WeatherApiClient.getApiKey(),
                location
        ).enqueue(new Callback<SportsResponse>() {
            @Override
            public void onResponse(Call<SportsResponse> call, Response<SportsResponse> response) {
                binding.btnSearchSports.setEnabled(true);
                binding.btnSearchSports.setText("Buscar Eventos Deportivos");

                if (response.isSuccessful() && response.body() != null) {
                    processSportsResponse(response.body(), location);
                } else {
                    Toast.makeText(requireContext(),
                            "Error al obtener eventos: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SportsResponse> call, Throwable t) {
                binding.btnSearchSports.setEnabled(true);
                binding.btnSearchSports.setText("Buscar Eventos Deportivos");

                Toast.makeText(requireContext(),
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processSportsResponse(SportsResponse response, String location) {
        // Obtener todos los eventos deportivos
        int totalEvents = 0;

        if (response.getFootballEvents() != null) {
            adapter.setSportEvents(response.getFootballEvents());
            totalEvents = response.getFootballEvents().size();
        }

        if (response.getCricketEvents() != null) {
            totalEvents += response.getCricketEvents().size();
        }

        if (response.getGolfEvents() != null) {
            totalEvents += response.getGolfEvents().size();
        }

        // Mostrar mensaje según los resultados
        if (totalEvents == 0) {
            Toast.makeText(requireContext(),
                    "No se encontraron eventos deportivos en " + location,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(),
                    "Se encontraron " + totalEvents + " eventos en " + location,
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}