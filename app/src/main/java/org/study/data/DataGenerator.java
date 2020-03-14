package org.study.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.study.R;
import org.study.data.entity.ProgramCategoryEntity;
import org.study.data.entity.ProgramDetailEntity;
import org.study.data.entity.ProgramInformationEntity;
import org.study.utilities.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    public static List<ProgramCategoryEntity> generateProgramCategory(Context context) throws Exception {
        Log.i("Read json for ", ProgramCategoryEntity.class.getSimpleName());

        JSONArray jsonArry = readJsonFile(context, R.raw.program_category, "program_category");

        List<ProgramCategoryEntity> programCategoryList = new ArrayList<>();
        for (int i = 0; i < jsonArry.length(); i++) {
            JSONObject obj = jsonArry.getJSONObject(i);
            ProgramCategoryEntity programCategory = new ProgramCategoryEntity();

            programCategory.setId(Integer.parseInt(obj.getString("id")));
            programCategory.setName(obj.getString("name"));
            programCategory.setDescription(obj.getString("description"));
            programCategory.setImage(obj.getString("image"));

            programCategoryList.add(programCategory);
        }

        return programCategoryList;
    }

    public static List<ProgramInformationEntity> generateProgramInformation(Context context) throws Exception {
        Log.i("Read json for ", ProgramInformationEntity.class.getSimpleName());

        JSONArray jsonArry = readJsonFile(context, R.raw.program_info, "program_information");

        List<ProgramInformationEntity> programInformationList = new ArrayList<>();

        byte[] imgBlob = convertDrawableToByteArray(context, R.drawable.not_found);

        for (int i = 0; i < jsonArry.length(); i++) {
            JSONObject obj = jsonArry.getJSONObject(i);
            ProgramInformationEntity programInformation = new ProgramInformationEntity();

            programInformation.setId(Integer.parseInt(obj.getString("id")));
            programInformation.setProgramCategoryId(Integer.parseInt(obj.getString("program_category_id")));
            programInformation.setQualificationName(obj.getString("qualification_name"));
            programInformation.setIcon(obj.getString("icon"));
            programInformation.setCredit(Integer.parseInt(obj.getString("credit")));
            programInformation.setLevel(Integer.parseInt(obj.getString("level")));
            programInformation.setDuration(obj.getString("duration"));
            programInformation.setIconBlob(imgBlob);

            programInformationList.add(programInformation);
        }

        return programInformationList;
    }

    public static List<ProgramDetailEntity> generateProgramDetail(Context context) throws Exception {
        Log.i("Read json for ", ProgramDetailEntity.class.getSimpleName());

        JSONArray jsonArry = readJsonFile(context, R.raw.program_detail, "program_detail");

        List<ProgramDetailEntity> programDetailList = new ArrayList<>();
        for (int i = 0; i < jsonArry.length(); i++) {
            JSONObject obj = jsonArry.getJSONObject(i);
            ProgramDetailEntity programDetail = new ProgramDetailEntity();

            programDetail.setId(Integer.parseInt(obj.getString("id")));
            programDetail.setProgramInformationId(Integer.parseInt(obj.getString("program_information_id")));
            programDetail.setName(obj.getString("name"));
            programDetail.setProgramOverview(obj.getJSONArray("program_overview").join("\\n"));
            programDetail.setTuitionFeeDomestic(new Double(obj.getDouble("tuition_fee_domestic")));
            programDetail.setTuitionFeeInter(new Double(obj.getDouble("tuition_fee_inter")));

            programDetailList.add(programDetail);
        }

        return programDetailList;
    }

    private static JSONArray readJsonFile(Context context, int rawResource, String entityName) throws Exception {
        String jsonStr = readRawResource(context, rawResource);
        JSONObject jObj = new JSONObject(jsonStr);
        JSONArray jsonArry = jObj.getJSONArray(entityName);
        return jsonArry;
    }

    private static String readRawResource(Context context, int rawResource) throws IOException {
        InputStream is = context.getResources().openRawResource(rawResource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e(Constants.tag_data, "Failed to read JSON from raw resource.", e);
            }
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();
        return jsonString;
    }

    private static byte[] convertDrawableToByteArray(Context context, int drawableInt){
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), drawableInt, null);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }
}
