package com.example.myparkingproj2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

    private final List<Reservation> reservationList;

    public ReservationsAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvSpotId, tvDate, tvTimeRange;

        ReservationViewHolder(View itemView) {
            super(itemView);
            tvSpotId = itemView.findViewById(R.id.tvSpotId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTimeRange = itemView.findViewById(R.id.tvTimeRange);
        }

        void bind(Reservation reservation) {
            tvSpotId.setText(reservation.getSpotId());
            tvDate.setText(reservation.getDate());
            tvTimeRange.setText(reservation.getStartTime() + " - " + reservation.getEndTime());
        }
    }
}