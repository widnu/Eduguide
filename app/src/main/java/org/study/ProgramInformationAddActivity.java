package org.study;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import org.study.data.AppDatabase;
import org.study.data.entity.ProgramDetailEntity;
import org.study.data.entity.ProgramInformationEntity;
import org.study.utilities.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgramInformationAddActivity extends AppCompatActivity {

    static final int RC_CAMERA_APP = 10;

    File mPhotoFile;

    ImageView pictureIV;
    Button cameraBtn;

    Spinner programCategorySp;
    EditText qualificationEt;
    Spinner creditSp;
    Spinner levelSp;
    Spinner durationSp;
    EditText programNameEt;
    EditText programOverviewEt;
    EditText feeDomesticEt;
    EditText feeInterEt;

    Button submitBtn;
    Button cancelBtn;

    AppDatabase appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_information_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pictureIV = (ImageView) findViewById(R.id.picture_imageView);
        cameraBtn = (Button) findViewById(R.id.camera_button);

        programCategorySp = (Spinner) findViewById(R.id.program_category_spinner);
        qualificationEt = (EditText) findViewById(R.id.qualification_editText);
        creditSp = (Spinner) findViewById(R.id.credit_spinner);
        levelSp = (Spinner) findViewById(R.id.level_spinner);
        durationSp = (Spinner) findViewById(R.id.duration_spinner);
        programNameEt = (EditText) findViewById(R.id.program_name_editText);
        programOverviewEt = (EditText) findViewById(R.id.program_overview_editText);
        feeDomesticEt = (EditText) findViewById(R.id.fee_domestic_editText);
        feeInterEt = (EditText) findViewById(R.id.fee_international_editText);

        submitBtn = (Button) findViewById(R.id.submit_program_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_program_btn);

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, RC_CAMERA_APP);

                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImage();
                    } catch (IOException ex) {
                        Log.e(Constants.tag_activity, "Cannot use camera.", ex);
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);

                        mPhotoFile = photoFile;
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, RC_CAMERA_APP);
                    }
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fields validation
                if (!isFormValid()) {
                    return;
                }

                // populate items
                long programInfoId = populateProgramInformation();
                populateProgramDetail(programInfoId);

                Toast.makeText(getApplicationContext(), "Saved.", Toast.LENGTH_SHORT).show();

                // return to caller
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    public long populateProgramInformation() {
        ProgramInformationEntity programInfo = new ProgramInformationEntity();

        String category = programCategorySp.getSelectedItem().toString();
        if (category.equals("Business")) {
            programInfo.setProgramCategoryId(1);
        } else if (category.equals("Information Technology")) {
            programInfo.setProgramCategoryId(2);
        } else if (category.equals("English Language")) {
            programInfo.setProgramCategoryId(3);
        } else if (category.equals("Construction")) {
            programInfo.setProgramCategoryId(4);
        }

        programInfo.setQualificationName(qualificationEt.getText().toString());

        if (null != mPhotoFile) {
            programInfo.setIcon(mPhotoFile.getName());
            programInfo.setIconBlob(convertImageFileToByteArray(getApplicationContext(), mPhotoFile));
        }

        programInfo.setCredit(Integer.valueOf(creditSp.getSelectedItem().toString()));
        programInfo.setLevel(Integer.valueOf(levelSp.getSelectedItem().toString()));
        programInfo.setDuration(String.valueOf(durationSp.getSelectedItem()));

        Log.i(Constants.tag_data, programInfo.toString());

        appDb = AppDatabase.getInstance(getApplicationContext());
        long programInfoId = appDb.programInformationDao().insert(programInfo);
        return programInfoId;
    }

    public void populateProgramDetail(long programInfoId) {
        ProgramDetailEntity programDetail = new ProgramDetailEntity();
        programDetail.setProgramInformationId((int) programInfoId);
        programDetail.setName(programNameEt.getText().toString());
        programDetail.setProgramOverview(programOverviewEt.getText().toString());
        programDetail.setTuitionFeeDomestic(Double.valueOf(feeDomesticEt.getText().toString()));
        programDetail.setTuitionFeeInter(Double.valueOf(feeInterEt.getText().toString()));

        Log.i(Constants.tag_data, programDetail.toString());

        appDb.programDetailDao().insert(programDetail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RC_CAMERA_APP) {
            if (resultCode == RESULT_OK) {
                if (mPhotoFile.exists()) {
                    pictureIV.setImageURI(Uri.fromFile(mPhotoFile));
                }

            }
        }
    }

    private boolean isFormValid() {
        String qualification = qualificationEt.getText().toString();
        if (null == qualification || qualification.length() == 0) {
            qualificationEt.setError("Qualification is required!");
            return false;
        }

        String programName = programNameEt.getText().toString();
        if (null == programName || programName.length() == 0) {
            programNameEt.setError("Program Overview is required!");
            return false;
        }

        String programOverview = programOverviewEt.getText().toString();
        if (null == programOverview || programOverview.length() == 0) {
            programOverviewEt.setError("Program Overview is required!");
            return false;
        }

        String feeDomestic = feeDomesticEt.getText().toString();
        if (null == feeDomestic || feeDomestic.length() == 0) {
            feeDomesticEt.setError("Domestic Tuition Fee is required!");
            return false;
        }

        String feeInter = feeInterEt.getText().toString();
        if (null == feeInter || feeInter.length() == 0) {
            feeInterEt.setError("International Tuition Fee is required!");
            return false;
        }

        return true;
    }

    private File createImage() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    private static byte[] convertImageFileToByteArray(Context context, File imageFile) {
        Bitmap bitmap = null;

        // make bitmap from the captured picture
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(imageFile));
        } catch (Exception e) {
            Log.e(Constants.tag_activity, "Unable to create bitmap for the picture", e);
        }

        // in case the the picture is crashed, make bitmap from default
        if (null == bitmap) {
            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.not_found, null);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
