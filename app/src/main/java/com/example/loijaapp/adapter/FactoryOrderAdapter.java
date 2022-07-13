package com.example.loijaapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loijaapp.R;
import com.example.loijaapp.model.FactoryOrder;
import com.example.loijaapp.utils.ClickListener;

import java.util.List;

public class FactoryOrderAdapter extends RecyclerView.Adapter<FactoryOrderAdapter.FactoryOrderHolder> {

    private List<FactoryOrder> orders;
    private Context context;
    private ClickListener clickListener;

    public FactoryOrderAdapter(List<FactoryOrder> orders, Context context) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public FactoryOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_holder, parent, false);
        return new FactoryOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactoryOrderHolder holder, int position) {
        FactoryOrder order = orders.get(position);
        holder.textMaterial.setText(order.getMaterial().getName());
        holder.textDeadline.setText(String.format(context.getString(R.string.orderholder_deadline), order.getDeadline().toString()));
        holder.textAmount.setText(String.format(context.getString(R.string.orderholder_amount), order.getAmount()));
        holder.textId.setText(String.valueOf(order.getId()));
        if (order.isCompleted())
            holder.itemView.setBackgroundColor(context.getColor(R.color.verde_completed));
        else
            holder.itemView.setBackgroundColor(context.getColor(R.color.naranja_no_completed));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class FactoryOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textMaterial, textAmount, textDeadline, textId;

        public FactoryOrderHolder(@NonNull View itemView) {
            super(itemView);
            textMaterial = itemView.findViewById(R.id.oHolder_textMaterial);
            textDeadline = itemView.findViewById(R.id.oHolder_textDeadline);
            textAmount = itemView.findViewById(R.id.oHolder_textAmount);
            textId = itemView.findViewById(R.id.oHolder_textId);
            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }
}
