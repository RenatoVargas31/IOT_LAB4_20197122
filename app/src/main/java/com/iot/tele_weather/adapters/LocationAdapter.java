package com.iot.tele_weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.tele_weather.R;
import com.iot.tele_weather.models.LocationModel;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>{
    private List<LocationModel> locations = new ArrayList<>();
    private OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onLocationClick(LocationModel location);
    }

    public LocationAdapter(OnLocationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationModel location = locations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void setLocations(List<LocationModel> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    public void clearLocations() {
        this.locations.clear();
        notifyDataSetChanged();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocationName, tvLocationRegion, tvLocationCountry, tvLocationId, tvCoordinates;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvLocationRegion = itemView.findViewById(R.id.tvLocationRegion);
            tvLocationCountry = itemView.findViewById(R.id.tvLocationCountry);
            tvLocationId = itemView.findViewById(R.id.tvLocationId);
            tvCoordinates = itemView.findViewById(R.id.tvCoordinates);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onLocationClick(locations.get(position));
                }
            });
        }

        public void bind(LocationModel location) {
            tvLocationName.setText(location.getName());
            tvLocationRegion.setText(location.getRegion());
            tvLocationCountry.setText(location.getCountry());
            tvLocationId.setText(String.valueOf(location.getId()));
            tvCoordinates.setText(location.getLatitude() + ", " + location.getLongitude());
        }
    }
}
