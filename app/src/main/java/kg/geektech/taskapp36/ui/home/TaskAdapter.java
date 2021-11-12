package kg.geektech.taskapp36.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kg.geektech.taskapp36.App;
import kg.geektech.taskapp36.Prefs;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.TaskFragment;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> list;

    public List<Task> getList() {
        return list;
    }

    private OnItemClickListener onItemClickListener;

    public TaskAdapter() {
        list = new ArrayList<>();
    }


    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Prefs prefs = new Prefs(parent.getContext());
        View view;
        if (!prefs.cancel()) {
            prefs.trueBoolean();
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task, parent, false);
        } else {
            prefs.falseBoolean();
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task2, parent, false);
        }

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Task task) {
        list.add(0,task);
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }


    public void addItems(List<Task> tasks) {
        list.addAll(tasks);
        notifyDataSetChanged();
    }

    public Task getItem(int position) {
        return list.get(position);
    }

    public void updateItem(int pos, Task task) {
        list.set(pos, task);
        notifyItemChanged(pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.sort);
            textTitle = itemView.findViewById(R.id.textTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onLongClick(getAdapterPosition());
                    return true;
                }
            });

//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Collections.reverse(getList());
//                }
//            });

        }

        public void bind(Task task) {
            textTitle.setText(task.getText());
        }
    }
}
