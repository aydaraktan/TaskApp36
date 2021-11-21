package kg.geektech.taskapp36.ui.dashboard;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentDashboardBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.model.Task;
import kg.geektech.taskapp36.ui.home.TaskAdapter;

public class DashboardFragment extends Fragment {

    private TaskAdapter adapter;
    private FragmentDashboardBinding binding;
    private Uri uri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new TaskAdapter();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Task task=adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task",task);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.taskFragment, bundle);


            }
            @Override
            public void onLongClick(int position) {
                FirebaseFirestore.getInstance().collection("tasks").document(adapter.getItem(position).getDocId()).delete();
                adapter.removeItem(position);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(adapter);
        getData();
    }
    private void getData() {
        adapter.getList().clear();
        FirebaseFirestore.getInstance().collection("tasks")
                .get().addOnSuccessListener(snapshots -> {
             // List<Task> list = snapshots.toObjects(Task.class);
            List<Task> list = new ArrayList<>();
            for (DocumentSnapshot snapshot : snapshots) {
                        Task task = snapshot.toObject(Task.class);
                        task.setDocId(snapshot.getId());
                        task.getImageUrl();
                        list.add(task);
                    }


            adapter.addItems(list);
                });

    }
}