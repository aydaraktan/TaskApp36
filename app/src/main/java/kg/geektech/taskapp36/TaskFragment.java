package kg.geektech.taskapp36;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Random;

import kg.geektech.taskapp36.databinding.FragmentDashboardBinding;
import kg.geektech.taskapp36.databinding.FragmentTaskBinding;
import kg.geektech.taskapp36.model.Task;
import kg.geektech.taskapp36.ui.home.TaskAdapter;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private EditText editText;
    private Task task;
    private ActivityResultLauncher<String> getImage;
    private Uri urri;
    String time;
    boolean enable;

    @Override
    public void onStart() {
        super.onStart();
        if (editText.toString().isEmpty() || binding.image == null) {
            binding.btnSave.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText);
        task = (Task) requireArguments().getSerializable("task");
        binding.progressBar.setVisibility(View.GONE);

        if (task != null) {
            editText.setText(task.getText());
            Glide.with(requireContext()).load(task.getImageUrl()).into(binding.image);
        }

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result!=null){
            binding.image.setImageURI(result);
            urri = result;
            upload(result);}
        });
        binding.image.setOnClickListener(v -> {
            getImage.launch("image/*");
            time = String.valueOf(System.currentTimeMillis());
        });


    }


    private void upload(Uri uri) {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("avatar_" + userId + time + "jpeg");
        com.google.android.gms.tasks.Task<Uri> task = reference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, com.google.android.gms.tasks.Task<Uri>>() {
            @Override
            public com.google.android.gms.tasks.Task<Uri> then(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) throws Exception {
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Uri> task) {
            }
        });
    }

    private void save() {

        String text = editText.getText().toString().trim();

        if (text.isEmpty() || binding.image.getDrawable()==null) {
            Toast.makeText(requireActivity(), "пустой", Toast.LENGTH_SHORT).show();
        } else {

            if (task == null) {
                task = new Task(text, System.currentTimeMillis(), urri.toString());
                App.getInstance().getDataBase().taskDao().insert(task);
                saveToFireStore(task);
            } else {
                task.setText(text);
                if (urri != null)
                    task.setImageUrl(urri.toString());
                App.getInstance().getDataBase().taskDao().update(task);
                if (task.getDocId() != null) {
                    updateToFireStore(task);
                } else
                    close();
            }
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSave.setVisibility(View.GONE);
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

    private void updateToFireStore(Task task) {

        FirebaseFirestore.getInstance().collection("tasks").document(task.getDocId())
                .update("text", task.getText(), "imageUrl", task.getImageUrl())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        close();
                    }
                });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}