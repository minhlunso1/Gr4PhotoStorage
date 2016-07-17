package group4.gr4photostorage.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.quickblox.content.model.QBFile;

import group4.gr4photostorage.R;
import group4.gr4photostorage.helper.Consts;
import group4.gr4photostorage.helper.QuickBloxHelper;

/**
 * Created by Minh on 7/10/2016.
 */
public class RvMainAdapter extends RecyclerView.Adapter<RvMainAdapter.ImageViewHolder> {

    private Context context;
    public SparseArray<QBFile> qbFileSparseArray;
    private int previousGetCount = 0;
    private DownloadMoreListener downloadListener;

    public interface DownloadMoreListener {
        void downloadMore();
    }

    public RvMainAdapter(Context context, SparseArray<QBFile> qbFileSparseArray) {
        this.context = context;
        this.qbFileSparseArray = qbFileSparseArray;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        ImageViewHolder holder = new ImageViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final QBFile qbFile = getItem(position);
        loadImage(holder, qbFile);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImageActivity.start(context, qbFile.getId());
            }
        });

        downloadMore(position);
    }

    @Override
    public int getItemCount() {
        if (qbFileSparseArray==null)
            return 0;
        return qbFileSparseArray.size();
    }

    public QBFile getItem(int position) {
        return qbFileSparseArray.valueAt(position);
    }
    public long getItemId(int position) {
        return qbFileSparseArray.keyAt(position);
    }
    public void updateData(SparseArray<QBFile> qbFileSparseArray) {
        this.qbFileSparseArray = qbFileSparseArray;
        notifyDataSetChanged();
    }
    public void updateInsertedData(SparseArray<QBFile> qbFileSparseArray) {
        this.qbFileSparseArray = qbFileSparseArray;
        notifyItemInserted(qbFileSparseArray.size()-1);
    }

    private void downloadMore(int position) {
        int count = getItemCount();
        if (count - 1 == position && getItemCount() % MainActivity.IMAGES_PER_PAGE == 0) {
            if (count != previousGetCount) {
                downloadListener.downloadMore();
                previousGetCount = count;
            }
        }
    }

    private void loadImage(final ImageViewHolder holder, QBFile qbFile) {
        holder.progressBar.setVisibility(View.VISIBLE);

        Priority customPriority = qbFile.getSize() > Consts.PRIORITY_MAX_IMAGE_SIZE
                ? Priority.LOW
                : Priority.NORMAL;

        Glide.with(context)
                .load(QuickBloxHelper.getUrlFromQBFile(qbFile))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(customPriority)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .error(R.drawable.ic_menu_gallery)
                .dontAnimate()
                .dontTransform()
                .override(Consts.PREFERRED_IMAGE_WIDTH_PREVIEW, Consts.PREFERRED_IMAGE_HEIGHT_PREVIEW)
                .into(holder.imageView);
    }

    public void setDownloadMoreListener(DownloadMoreListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_preview);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_adapter);
        }
    }
}
