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
    private boolean b;

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Task task = adapter.getItem(position);
                adapter.updateItem(position,task);
                openFragment(task);
            }
            @Override
            public void onLongClick(int position) {
                Task task = adapter.getItem(position);
                adapter.removeItem(position);
                App.getInstance().getDataBase().taskDao().delete(task);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

            @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(!b) {
            adapter.getList().clear();
            adapter.addItems(App.getInstance().getDataBase().taskDao().getAllSortedByTitle());
            b = true;
        }
        else
        {
            adapter.getList().clear();
            adapter.addItems(App.getInstance().getDataBase().taskDao().getAll());
            b=false;
        }
                adapter.notifyDataSetChanged();

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
            openFragment(null);
        });

        initList();
    }
    private void initList() {
        binding.recyclerView.setAdapter(adapter);
        adapter.getList().clear();
        adapter.addItems(App.getInstance().getDataBase().taskDao().getAll());
    }

    private void openFragment(Task task) {
        Bundle bun = new Bundle();
        bun.putSerializable("task", task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.taskFragment, bun);
    }
}