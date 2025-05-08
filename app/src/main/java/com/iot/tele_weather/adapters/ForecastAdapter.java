package com.iot.tele_weather.adapters;

import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iot.tele_weather.R;
import com.iot.tele_weather.models.ForecastDayModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>{
    private List<ForecastDayModel> forecastDays = new ArrayList<>();
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastDayModel forecastDay = forecastDays.get(position);
        holder.bind(forecastDay);
    }

    @Override
    public int getItemCount() {
        return forecastDays.size();
    }

    public void setForecastDays(List<ForecastDayModel> forecastDays) {
        this.forecastDays = forecastDays;
        notifyDataSetChanged();
    }

    public void clearForecastDays() {
        this.forecastDays.clear();
        notifyDataSetChanged();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvCondition, tvMaxTemp, tvMinTemp;
        private ImageView ivWeatherIcon;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
            tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
        }

        public void bind(ForecastDayModel forecastDay) {
            // Formatear fecha
            try {
                Date date = inputFormat.parse(forecastDay.getDate());
                tvDate.setText(outputFormat.format(date));
            } catch (ParseException e) {
                tvDate.setText(forecastDay.getDate());
            } catch (java.text.ParseException e) {
                throw new RuntimeException(e);
            }

            // Mostrar condición y temperaturas
            tvCondition.setText(forecastDay.getDay().getCondition().getText());
            tvMaxTemp.setText(String.format(Locale.getDefault(), "%.1f°C", forecastDay.getDay().getMaxTempC()));
            tvMinTemp.setText(String.format(Locale.getDefault(), "%.1f°C", forecastDay.getDay().getMinTempC()));

            // Cargar icono
            String iconUrl = "https:" + forecastDay.getDay().getCondition().getIcon();
            Glide.with(itemView.getContext())
                    .load(iconUrl)
                    .into(ivWeatherIcon);
        }
    }
}
