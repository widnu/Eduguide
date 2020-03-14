package org.study.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.study.R;
import org.study.data.entity.ProgramInformationEntity;
import org.study.utilities.Constants;

import java.io.File;
import java.util.List;

public class ProgramInfoAdapter extends RecyclerView.Adapter<ProgramInfoAdapter.ViewHolder> {

    private Context context;

    private List<ProgramInformationEntity> programInfoList;

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProgramInformationEntity programInformationEntity);
    }

    public ProgramInfoAdapter(List<ProgramInformationEntity> programInfoList, OnItemClickListener listener) {
        this.programInfoList = programInfoList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconImageView;
        public TextView nameTextView;
        public TextView creditTextView;
        public TextView levelTextView;
        public TextView durationTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            iconImageView = itemView.findViewById(R.id.program_info_image);
            nameTextView = (TextView) itemView.findViewById(R.id.program_info_name);

            creditTextView = (TextView) itemView.findViewById(R.id.credit);
            levelTextView = (TextView) itemView.findViewById(R.id.level);
            durationTextView = (TextView) itemView.findViewById(R.id.duration);
        }

        public void bind(final ProgramInformationEntity programInformation, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(programInformation);
                }
            });
        }
    }

    @Override
    public ProgramInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cardViewLayout = inflater.inflate(R.layout.card_program_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardViewLayout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProgramInfoAdapter.ViewHolder viewHolder, int position) {
        ProgramInformationEntity programInfo = programInfoList.get(position);

        viewHolder.bind(programInfoList.get(position), listener);

        String iconName = programInfo.getIcon();
        if (null == iconName) {
            iconName = "not_found";
        }
        int resID = this.context.getResources().getIdentifier(iconName, "drawable", this.context.getPackageName());
//        Log.i(Constants.tag_activity, "resID = " + String.valueOf(resID));

        if (resID != 0) {
            viewHolder.iconImageView.setImageResource(resID);
        } else {
            try {
                if (null != programInfo.getIcon() && programInfo.getIcon().contains(".")) {
                    // get image from local storage
                    File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File imgFile = new File(storageDir.getAbsolutePath() + "/" + programInfo.getIcon());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

                    viewHolder.iconImageView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                Log.e(Constants.tag_activity, "Cannot get image file from local storage", e);
            }
        }

        viewHolder.nameTextView.setText(programInfo.getQualificationName());
        viewHolder.creditTextView.setText("Credit: " + programInfo.getCredit());
        viewHolder.levelTextView.setText("Level: " + programInfo.getLevel());
        viewHolder.durationTextView.setText("Duration: " + programInfo.getDuration());
    }

    @Override
    public int getItemCount() {
        return programInfoList.size();
    }

}
