package com.example.architectureexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureexample.R;
import com.example.architectureexample.User;

public class UserAdapter extends ListAdapter<User, UserAdapter.UserHolder> {
    private OnItemClickListener listener;

    public UserAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(User oldItem, User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(User oldItem, User newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDesc().equals(newItem.getDesc());
        }
    };


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = (User) getUserAt(position);
        holder.textViewTitle.setText(currentUser.getName());
        holder.textViewEmail.setText(currentUser.getEmail());
        holder.textViewGender.setText(currentUser.getGender());
        holder.textViewPhone.setText(currentUser.getPhone());
        holder.textViewDescription.setText(currentUser.getDesc());

    }

    public User getUserAt(int position) {
        return getItem(position);
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewEmail;
        private TextView textViewGender;
        private TextView textViewPhone;
        private TextView textViewDescription;

        public UserHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            textViewGender = itemView.findViewById(R.id.text_view_gender);
            textViewPhone = itemView.findViewById(R.id.text_view_phone);
            textViewDescription = itemView.findViewById(R.id.text_view_description);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}