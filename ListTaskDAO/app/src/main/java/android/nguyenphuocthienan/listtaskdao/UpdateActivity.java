package android.nguyenphuocthienan.listtaskdao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.nguyenphuocthienan.listtaskdao.database.TaskDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    private EditText edtName, edtDescription;
    private Button btnUpdateTask;

    private Task mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Ánh xạ view
        edtName = findViewById(R.id.edt_name);
        edtDescription = findViewById(R.id.edt_description);
        btnUpdateTask = findViewById(R.id.btn_update_task);

        mTask = (Task) getIntent().getExtras().get("object_task");
        if (mTask != null) {
            edtName.setText(mTask.getName());
            edtDescription.setText(mTask.getDescription());
        }

        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });
    }

    // update task
    private void updateTask() {
        String strName = edtName.getText().toString().trim();
        String strDescription = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(strName) || TextUtils.isEmpty(strDescription)) {
            return;
        }

        // update task trong database
        mTask.setName(strName);
        mTask.setDescription(strDescription);

        TaskDatabase.getInstance(this).taskDAO().updateTask(mTask);
        Toast.makeText(this, "Update task successfully", Toast.LENGTH_SHORT).show();

        // Update xong quay trở lại MainActivity
        Intent intentResult = new Intent();
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }
}