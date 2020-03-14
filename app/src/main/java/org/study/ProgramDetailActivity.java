package org.study;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.study.data.AppDatabase;
import org.study.data.entity.ProgramDetailEntity;
import org.study.data.entity.ProgramInformationEntity;
import org.study.utilities.Constants;

import java.io.File;
import java.util.List;

public class ProgramDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        render();
    }

    private void render() {
        Intent intent = getIntent();
        int programInfoId = intent.getIntExtra("PROGRAM_INFO_ID", 0);
        ProgramInformationEntity programInfo = (ProgramInformationEntity) intent.getSerializableExtra("PROGRAM_INFO_OBJ");

        AppDatabase appDb = AppDatabase.getInstance(getApplicationContext());
        List<ProgramDetailEntity> programDetailList = appDb.programDetailDao().loadByProgramInformationId(programInfoId);
//        Log.i(Constants.tag_activity, "programDetailList = " + programDetailList);


//        for (ProgramDetailEntity item : programDetailList) {
//            Log.i(Constants.tag_activity, item.getName());
//            Log.i(Constants.tag_activity, item.getProgramOverview());
//
//
//        }
        if (null != programDetailList && !programDetailList.isEmpty()) {
            ProgramDetailEntity programDetail = programDetailList.get(0);

            ImageView iconImageView = findViewById(R.id.pictureDetailImageView);
            int resID = getApplicationContext().getResources().getIdentifier(programInfo.getIcon(), "drawable", getApplicationContext().getPackageName());

            if (resID != 0) {
                iconImageView.setImageResource(resID);
            } else {
                try {
                    if (null != programInfo.getIcon() && programInfo.getIcon().contains(".")) {
                        // get image from local storage
                        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File imgFile = new File(storageDir.getAbsolutePath() + "/" + programInfo.getIcon());
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

                        iconImageView.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    Log.e(Constants.tag_activity, "Cannot get image file from local storage", e);
                }
            }

            ImageView programCategoryImageView = findViewById(R.id.categoryImageView);
            programCategoryImageView.setImageResource(getProgramCategoryImage(programInfo.getProgramCategoryId()));

            TextView titleTextView = findViewById(R.id.programDetailsTitle);
            titleTextView.setText(programDetail.getName());

            TextView programOverviewTextView = findViewById(R.id.programOverview);
            programOverviewTextView.setText(programDetail.getProgramOverview());
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private int getProgramCategoryImage(int programCategoryId) {
        int resourceId = R.drawable.not_found;
        switch (programCategoryId) {
            case 1:
                resourceId = R.drawable.business_img;
                break;
            case 2:
                resourceId = R.drawable.infotech_img;
                break;
            case 3:
                resourceId = R.drawable.englang_img;
                break;
            case 4:
                resourceId = R.drawable.construction_img;
                break;

        }
        return resourceId;
    }
}