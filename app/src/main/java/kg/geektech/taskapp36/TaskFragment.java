package kg.geektech.taskapp36;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import kg.geektech.taskapp36.model.Task;
import kg.geektech.taskapp36.ui.home.TaskAdapter;

public class TaskFragment extends Fragment {

    private EditText editText;
    private Task task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText);
        task = (Task) requireArguments().getSerializable("task");

        if (task != null) {
            editText.setText(task.getText());
        }

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }

    private void save() {
        String text = editText.getText().toString().trim();

        if (task == null) {
            task = new Task(text, System.currentTimeMillis());
            App.getInstance().getDataBase().taskDao().insert(task);
        } else {
            task.setText(text);
            App.getInstance().getDataBase().taskDao().update(task);
        }

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("task", task);
//        getParentFragmentManager().setFragmentResult("rk_task", bundle);
        close();

    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}