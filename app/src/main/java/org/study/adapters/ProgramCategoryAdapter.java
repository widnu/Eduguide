package org.study.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.study.R;
import org.study.data.entity.ProgramCategoryEntity;

import java.util.List;

public class ProgramCategoryAdapter extends RecyclerView.Adapter<ProgramCategoryAdapter.ViewHolder> {

    private Context context;

    private List<ProgramCategoryEntity> programCategoryList;

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProgramCategoryEntity programCategory);
    }

    public ProgramCategoryAdapter(List<ProgramCategoryEntity> programCategoryList, OnItemClickListener listener) {
        this.programCategoryList = programCategoryList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView bgImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.program_category_name);
            bgImageView = itemView.findViewById(R.id.program_category_image);

        }

        public void bind(final ProgramCategoryEntity programCategory, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(programCategory);
                }
            });
        }
    }

    @Override
    public ProgramCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);

        View cardViewLayout = inflater.inflate(R.layout.card_program_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardViewLayout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProgramCategoryAdapter.ViewHolder viewHolder, int position) {
        ProgramCategoryEntity programCategory = programCategoryList.get(position);

        viewHolder.bind(programCategoryList.get(position), listener);

        viewHolder.nameTextView.setText(programCategory.getName());

        int resID = this.context.getResources().getIdentifier(programCategory.getImage(), "drawable", this.context.getPackageName());
        viewHolder.bgImageView.setImageResource(resID);
    }

    @Override
    public int getItemCount() {
        return programCategoryList.size();
    }
}