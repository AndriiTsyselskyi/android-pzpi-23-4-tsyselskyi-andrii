package tsyselskyi.andrii.nure;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private final List<String> items;

    public ListItemAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(itemView, items);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTextView;
        private final TextView textTextView;
        private final List<String> items;

        public ListItemViewHolder(@NonNull View itemView, List<String> items) {
            super(itemView);
            this.items = items;
            idTextView = itemView.findViewById(R.id.list_item_id);
            textTextView = itemView.findViewById(R.id.list_item_text);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Toast.makeText(v.getContext(), "Item " + position + " has been clicked ", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindData(int position) {
            idTextView.setText(String.valueOf(position));
            textTextView.setText(this.items.get(position) + " " + (position + 1));
        }
    }
}
