package phone.vishnu.quotes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import phone.vishnu.quotes.R;

public class BGImageRVAdapter extends ListAdapter<Uri, BGImageRVAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public BGImageRVAdapter() {
        super(new DiffUtil.ItemCallback<Uri>() {
            @Override
            public boolean areItemsTheSame(@NonNull Uri oldItem, @NonNull Uri newItem) {
                return Objects.equals(oldItem.getEncodedPath(), newItem.getEncodedPath());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Uri oldItem, @NonNull Uri newItem) {
                return Objects.equals(oldItem.getEncodedPath(), newItem.getEncodedPath());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.background_image_pick_single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (getItem(position).toString().startsWith("file://"))
            Picasso.get()
                    .load(new File(getItem(position).toString().replace("file://", holder.itemView.getContext().getFilesDir().getPath())))
                    .into(holder.imageView);
        else
            Picasso.get()
                    .load(getItem(position))
                    .into(holder.imageView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Uri uri);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.defaultSingleImage);
            imageView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onItemClick(getItem(getAdapterPosition()));
            });
        }
    }
}
