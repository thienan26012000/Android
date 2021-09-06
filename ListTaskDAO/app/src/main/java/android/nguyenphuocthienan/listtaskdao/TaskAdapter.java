package android.nguyenphuocthienan.listtaskdao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> mListTask;
    private IClickItemTask iClickItemTask;

    public interface IClickItemTask {
        void updateTask(Task task);
    }

    public TaskAdapter(IClickItemTask iClickItemTask) {
        this.iClickItemTask = iClickItemTask;
    }

    public void setData(List<Task> list){
        this.mListTask = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // set dữ liệu lên
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = mListTask.get(position);
        if (task == null) {
            return;
        }

        holder.name.setText(task.getName());
        holder.description.setText(task.getDescription());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemTask.updateTask(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListTask != null){
            return mListTask.size();
        }
        return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView description;
        private Button btnUpdate;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            initUi();
        }


        // Ánh xạ view
        private void initUi() {
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            btnUpdate = itemView.findViewById(R.id.btn_update);
        }
    }
}
