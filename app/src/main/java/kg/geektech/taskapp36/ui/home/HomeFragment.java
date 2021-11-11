package kg.geektech.taskapp36.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import java.util.Collections;
import java.util.Comparator;
import kg.geektech.taskapp36.App;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentHomeBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.model.Task;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TaskAdapter adapter;
    private int pos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter();
        adapter.addItems(App.getInstance().getDataBase().taskDao().getAll());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                pos = position;
                Task task = adapter.getItem(position);
                openFragment(task);
            }

            @Override
            public void onLongClick(int position) {
                adapter.removeItem(position);
            }

        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fab).setOnClickListener(view1 -> {
            pos = -1;
            openFragment(null);
        });

        initList();
        //Collections.reverse(adapter.getList());

//        Collections.sort(adapter.getList(), new Comparator<Task>() {
//            @Override
//            public int compare(Task o1, Task o2) {
//                return o1.getText().compareTo(o2.getText());
//            }
//        });


    }


    private void initList() {
        binding.recyclerView.setAdapter(adapter);

        getParentFragmentManager().setFragmentResultListener("rk_task", getViewLifecycleOwner(),
                (requestKey, result) -> {

                    Task task = (Task) result.getSerializable("task");
                    if (pos == -1)
                        adapter.addItem(task);
                    else
                        adapter.updateItem(pos, task);
                });
    }

    private void openFragment(Task task) {
        Bundle bun = new Bundle();
        bun.putSerializable("task", task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.taskFragment, bun);


    }
}