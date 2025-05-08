package com.iot.tele_weather;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.iot.tele_weather.databinding.ActivityAppBinding;

public class AppActivity extends AppCompatActivity {

    ActivityAppBinding binding;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar la Toolbar
        setSupportActionBar(binding.toolbar);

        // Configurar Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        // Configurar los destinos considerados como nivel superior (no mostrarán botón de retroceso)
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_location, R.id.navigation_forecast, R.id.navigation_sports)
                .build();

        // Conectar la toolbar con Navigation Component
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Conectar Bottom Navigation con Navigation Component
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_location) {
                navController.navigate(R.id.navigation_location);
                return true;
            } else if (item.getItemId() == R.id.navigation_forecast) {
                navController.navigate(R.id.navigation_forecast);
                return true;
            } else if (item.getItemId() == R.id.navigation_sports) {
                navController.navigate(R.id.navigation_sports);
                return true;
            }

            return false;
        });
        // Sobrescribir comportamiento del botón atrás
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Simplemente finaliza la actividad, regresando al MainActivity
                finish();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}