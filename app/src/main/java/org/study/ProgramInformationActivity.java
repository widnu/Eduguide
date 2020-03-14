package org.study;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.study.adapters.ProgramInfoAdapter;
import org.study.data.AppDatabase;
import org.study.data.entity.ProgramInformationEntity;
import org.study.utilities.Constants;

import java.util.List;

public class ProgramInformationActivity extends AppCompatActivity implements ProgramInfoAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        render();
    }

    private void render() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvProgramInfo);

        Intent intent = getIntent();
        int programCategoryId = intent.getIntExtra("PROGRAM_CATE_ID", 0);

        AppDatabase appDb = AppDatabase.getInstance(getApplicationContext());
        List<ProgramInformationEntity> programInfoList = appDb.programInformationDao().loadByProgramCategoryId(programCategoryId);
        Log.i(Constants.tag_activity, "programInfoList = " + programInfoList);

        ProgramInfoAdapter adapter = new ProgramInfoAdapter(programInfoList, this::onItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(ProgramInformationEntity programInformation) {
        Intent intent = new Intent(getApplicationContext(), ProgramDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("PROGRAM_INFO_ID", programInformation.getId());
        intent.putExtra("PROGRAM_INFO_OBJ", programInformation);
        getApplicationContext().startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
