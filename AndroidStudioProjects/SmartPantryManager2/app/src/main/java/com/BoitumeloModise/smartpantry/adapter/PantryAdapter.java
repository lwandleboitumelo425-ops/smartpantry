package com.BoitumeloModise.smartpantry.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.BoitumeloModise.smartpantry.R;
import com.BoitumeloModise.smartpantry.model.PantryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Custom adapter binding a List<PantryItem> from SQLite to item_pantry.xml rows. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {

    public interface Listener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private List<PantryItem> items;
    private final Listener listener;
    private boolean highlightExpiry = true;

    public PantryAdapter(List<PantryItem> items, Listener listener) {
        this.items = items; this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems, boolean highlightExpiry) {
        this.items = newItems;
        this.highlightExpiry = highlightExpiry;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        PantryItem item = items.get(position);
        h.tvName.setText(item.getName());
        h.tvQty.setText(item.getQuantity() + " " + item.getUnit());

        String exp = item.getExpiryDate();
        if (exp == null || exp.isEmpty()) {
            h.tvExpiry.setText("No expiry date");
            h.tvExpiry.setTextColor(Color.GRAY);
        } else {
            h.tvExpiry.setText("Expires: " + exp);
            h.tvExpiry.setTextColor(highlightExpiry && isExpiringSoon(exp) ? Color.RED : Color.GRAY);
        }
        h.itemView.setOnClickListener(v -> listener.onItemClick(item));
        h.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    private boolean isExpiringSoon(String yyyyMMdd) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(yyyyMMdd);
            long days = (d.getTime() - System.currentTimeMillis()) / (1000L * 60 * 60 * 24);
            return days <= 3;
        } catch (ParseException | NullPointerException e) {
            return false;
        }
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvExpiry; ImageButton btnDelete;
        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvQty = v.findViewById(R.id.tvQty);
            tvExpiry = v.findViewById(R.id.tvExpiry);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}