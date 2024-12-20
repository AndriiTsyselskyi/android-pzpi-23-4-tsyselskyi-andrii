package tsyselskyi.andrii.nure;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tsyselskyi.andrii.nure.databinding.ListUserBinding;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> usersList = new ArrayList<>();

    public void updateUserList(List<User> newUsers) {
        if (newUsers != null && !newUsers.isEmpty()) {
            usersList.clear();
            usersList.addAll(newUsers);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListUserBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_user,
                parent,
                false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = usersList.get(position);
        holder.binding.setNameString(currentUser.getName());
        holder.binding.setAgeString(String.valueOf(currentUser.getAge()));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        final ListUserBinding binding;

        public UserViewHolder(@NonNull ListUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
