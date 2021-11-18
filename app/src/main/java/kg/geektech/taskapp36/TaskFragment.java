package kg.geektech.taskapp36;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        if(text.isEmpty())
        {
            Toast.makeText(requireActivity(), "пустой", Toast.LENGTH_SHORT).show();
        }
        else {
            if (task == null) {
                task = new Task(text, System.currentTimeMillis());
                App.getInstance().getDataBase().taskDao().insert(task);
                saveToFireStore(task);
            } else {
                task.setText(text);
                App.getInstance().getDataBase().taskDao().update(task);
                if (task.getDocId() != null) {
                    updateToFireStore(task);
                }
                else
                close();
            }
        }
    }

    private void saveToFireStore(Task task) {
        FirebaseFirestore.getInstance().collection("tasks")
                .add(task).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> t) {
                if (t.isSuccessful()) {
                    close();
                }
            }
        });
    }
    private void updateToFireStore(Task task){

        FirebaseFirestore.getInstance().collection("tasks").document(task.getDocId())
                .update("text",task.getText()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireActivity(), "успешно обновлено", Toast.LENGTH_SHORT).show();

                close();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "не обновлено", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}