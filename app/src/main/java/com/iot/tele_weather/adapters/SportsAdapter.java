package com.iot.tele_weather.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.tele_weather.R;
import com.iot.tele_weather.models.SportEvent;

import java.util.ArrayList;
import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportViewHolder>{
    private List<SportEvent> sportEvents = new ArrayList<>();

    public void setSportEvents(List<SportEvent> sportEvents) {
        this.sportEvents = sportEvents;
        notifyDataSetChanged();
    }

    public void clearSportEvents() {
        sportEvents.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sport, parent, false);
        return new SportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportViewHolder holder, int position) {
        SportEvent sportEvent = sportEvents.get(position);
        holder.bind(sportEvent);
    }

    @Override
    public int getItemCount() {
        return sportEvents.size();
    }

    class SportViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivSportIcon;
        private final TextView tvSportName, tvEventName, tvLocation, tvDate;

        public SportViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSportIcon = itemView.findViewById(R.id.ivSportIcon);
            tvSportName = itemView.findViewById(R.id.tvSportName);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        @SuppressLint("SetTextI18n")
        public void bind(SportEvent sportEvent) {
            // Mostrar el torneo como nombre del deporte
            tvSportName.setText(sportEvent.getTournament());

            // Mostrar el partido
            tvEventName.setText(sportEvent.getMatch());

            // Mostrar la ubicación
            tvLocation.setText("Stadium " + sportEvent.getFullLocation());

            // Mostrar la fecha y hora
            tvDate.setText(sportEvent.getStart());

            // Configurar icono
            ivSportIcon.setImageResource(R.drawable.ic_football);
        }
    }
}
