package tsyselskyi.andrii.nure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final Context context;
    private final List<Note> notesList;
    private final OnNoteClickListener onNoteClickListener;

    public NoteAdapter(Context context, List<Note> notesList, OnNoteClickListener onNoteClickListener) {
        this.context = context;
        this.notesList = notesList;
        this.onNoteClickListener = onNoteClickListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private void showContextMenu(View view, Note note, int position) {
        androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(context, view);
        popupMenu.inflate(R.menu.context_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {
                onNoteClickListener.onEditNoteClick(note, position);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                onNoteClickListener.onDeleteNoteClick(note, position);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private int getImportanceIcon(int importance) {
        switch (importance) {
            case 0:
                return R.drawable.low_importance;
            case 1:
                return R.drawable.medium_importance;
            case 2:
                return R.drawable.high_importance;
            default:
                return R.drawable.low_importance;
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onEditNoteClick(Note note, int position);
        void onDeleteNoteClick(Note note, int position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView description;
        private final TextView dateTime;
        private final ImageView image;
        private final ImageView importance;

        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            dateTime = itemView.findViewById(R.id.dateTime);
            image = itemView.findViewById(R.id.image);
            importance = itemView.findViewById(R.id.importance);
        }

        public void bind(Note note) {
            title.setText(note.getTitle());
            description.setText(note.getDescription());
            dateTime.setText(note.getDateTime());
            image.setImageURI(note.getImageUri());
            importance.setImageResource(getImportanceIcon(note.getImportance()));

            itemView.setOnClickListener(v -> onNoteClickListener.onNoteClick(note));

            itemView.setOnLongClickListener(v -> {
                showContextMenu(itemView, note, getAdapterPosition());
                return true;
            });
        }
    }
}
