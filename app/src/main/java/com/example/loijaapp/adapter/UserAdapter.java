package com.example.loijaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loijaapp.R;
import com.example.loijaapp.model.MyUser;
import com.example.loijaapp.utils.ClickListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{

    private List<MyUser> users;
    private Context context;
    private ClickListener clickListener;

    public UserAdapter(List<MyUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_holder, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        MyUser user = users.get(position);
        holder.textUsername.setText(user.getUsername());
        holder.textRoles.setText(user.getRoles().toString());
        holder.textFirstname.setText(user.getFirstname());
        holder.textSurname.setText(user.getSurname());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textUsername, textRoles, textFirstname, textSurname;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            textUsername = itemView.findViewById(R.id.uHolder_textUsername);
            textRoles = itemView.findViewById(R.id.uHolder_textRoles);
            textFirstname = itemView.findViewById(R.id.uHolder_textFirstname);
            textSurname = itemView.findViewById(R.id.uHolder_textSurname);
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
