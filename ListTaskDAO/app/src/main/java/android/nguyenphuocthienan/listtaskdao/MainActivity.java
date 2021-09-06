package android.nguyenphuocthienan.listtaskdao;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.nguyenphuocthienan.listtaskdao.database.TaskDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private EditText edtName, edtDescription;
    private Button btnAddTask;
    private RecyclerView rcvTask;

    private TaskAdapter taskAdapter;
    private List<Task> mListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        initUi();

        taskAdapter = new TaskAdapter(new TaskAdapter.IClickItemTask() {
            @Override
            public void updateTask(Task task) {
                clickUpdateTask(task);
            }
        });
        mListTask = new ArrayList<>();
        taskAdapter.setData(mListTask);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTask.setLayoutManager(linearLayoutManager);

        rcvTask.setAdapter(taskAdapter);

        // Add list to recycler view
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

        // Load dữ liệu
        LoadData();
    }

    // Ánh xạ view
    private void initUi() {
        edtName = findViewById(R.id.edt_name);
        edtDescription = findViewById(R.id.edt_description);
        btnAddTask = findViewById(R.id.btn_add_task);
        rcvTask = findViewById(R.id.rcv_task);
    }

    private void addTask() {
        String strName = edtName.getText().toString().trim();
        String strDescription = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(strName) || TextUtils.isEmpty(strDescription)) {
            return;
        }

        Task task = new Task(strName, strDescription);

        if (isTaskExist(task)) {
            Toast.makeText(this, "Task exists", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskDatabase.getInstance(this).taskDAO().insertTask(task);
        Toast.makeText(this, "Add task successfully", Toast.LENGTH_SHORT).show();

        edtName.setText("");
        edtDescription.setText("");

        hideSoftKeyboard();

        LoadData();
    }

    // Ẩn bản phím
    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    // Load dữ liệu
    private void LoadData() {
        mListTask = TaskDatabase.getInstance(this).taskDAO().getListTask();
        taskAdapter.setData(mListTask);
    }

    // check name data tồn tại
    private boolean isTaskExist(Task task) {
        List<Task> list = TaskDatabase.getInstance(this).taskDAO().checkTask(task.getName());
        return list != null && !list.isEmpty();
    }

    // chuyển data update task
    private void clickUpdateTask(Task task) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_task", task);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    // nhận result từ Update Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            LoadData();
        }

    }
}