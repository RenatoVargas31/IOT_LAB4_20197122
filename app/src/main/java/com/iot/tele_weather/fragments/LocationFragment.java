package com.iot.tele_weather.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iot.tele_weather.R;
import com.iot.tele_weather.adapters.LocationAdapter;
import com.iot.tele_weather.api.WeatherApiClient;
import com.iot.tele_weather.databinding.FragmentLocationBinding;
import com.iot.tele_weather.models.LocationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends Fragment implements LocationAdapter.OnLocationClickListener {
    private FragmentLocationBinding binding;
    private LocationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Aquí irá la configuración del RecyclerView y la lógica para llamar a la API
        // Configurar RecyclerView
        adapter = new LocationAdapter((LocationAdapter.OnLocationClickListener) this);
        binding.rvLocations.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvLocations.setAdapter(adapter);

        // Configurar botón de búsqueda
        binding.btnSearch.setOnClickListener(v -> searchLocations());
    }
    private void searchLocations() {
        String query = binding.etLocationSearch.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese un lugar para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar progreso
        binding.btnSearch.setEnabled(false);

        // Realizar la petición a la API
        WeatherApiClient.getInstance().searchLocations(
                WeatherApiClient.getApiKey(),
                query
        ).enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                binding.btnSearch.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    List<LocationModel> locations = response.body();
                    if (locations.isEmpty()) {
                        Toast.makeText(requireContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                        adapter.clearLocations();
                    } else {
                        adapter.setLocations(locations);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                binding.btnSearch.setEnabled(true);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onLocationClick(LocationModel location) {
        // Navegar al fragmento de pronóstico con los datos de la ubicación
        Bundle bundle = new Bundle();
        bundle.putLong("locationId", location.getId());
        bundle.putString("locationName", location.getName());

        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_location_to_navigation_forecast, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}