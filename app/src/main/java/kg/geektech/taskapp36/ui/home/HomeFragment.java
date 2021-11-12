package kg.geektech.taskapp36.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import kg.geektech.taskapp36.App;
import kg.geektech.taskapp36.Prefs;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentHomeBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.model.Task;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private TaskAdapter adapter;
    private int pos;
    private RecyclerView res;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
                Task task = adapter.getItem(position);
                App.getInstance().getDataBase().taskDao().delete(task);
                adapter.removeItem(position);
            }
        });
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public boolean lol;
            @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                Prefs prefs = new Prefs(requireContext());
        Toast.makeText(requireContext(), "help", Toast.LENGTH_SHORT).show();

        if(prefs.cancel2()) {
            Collections.sort(adapter.getList(), new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getText().compareTo(o2.getText());
                }
            });
            prefs.getLol();
        }

        else
        {
            Collections.reverse(adapter.getList());
            prefs.setLol();
        }


                binding.recyclerView.setAdapter(adapter);
        return super.onOptionsItemSelected(item);
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