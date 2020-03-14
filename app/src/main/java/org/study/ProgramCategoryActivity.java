package org.study;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.study.adapters.ProgramCategoryAdapter;
import org.study.data.AppDatabase;
import org.study.data.entity.ProgramCategoryEntity;
import org.study.utilities.Constants;

import java.util.List;

public class ProgramCategoryActivity extends AppCompatActivity implements ProgramCategoryAdapter.OnItemClickListener {

    static final int RC_PROGRAM_INFORMATION_ADD = 1;

    private CardView cvProgramCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            render();
        } catch (Exception e) {
            Log.e(Constants.tag_activity, "", e);
        }

        bindAddProgramInformationButton();
    }

    private void render() throws Exception {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvProgramCategory);

        // Initialize program list
        AppDatabase appDb = AppDatabase.getInstance(getApplicationContext());
        List<ProgramCategoryEntity> programCategoryList = appDb.programCategoryDao().loadAll();
//        Log.i(Constants.tag_activity, "programCategoryList = " + programCategoryList);

        ProgramCategoryAdapter adapter = new ProgramCategoryAdapter(programCategoryList, this::onItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void bindAddProgramInformationButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProgramInformationAddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onItemClick(ProgramCategoryEntity programCategory) {
        Intent intent = new Intent(getApplicationContext(), ProgramInformationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("PROGRAM_CATE_ID", programCategory.getId());
        startActivityForResult(intent, RC_PROGRAM_INFORMATION_ADD);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RC_PROGRAM_INFORMATION_ADD) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "OnClickListener cancel:", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
