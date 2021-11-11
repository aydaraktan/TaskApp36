package kg.geektech.taskapp36;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.Menu;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;

import kg.geektech.taskapp36.databinding.ActivityMainBinding;
import kg.geektech.taskapp36.model.Task;
import kg.geektech.taskapp36.ui.home.TaskAdapter;

public class MainActivity extends AppCompatActivity {
    MainActivity mainActivity=new MainActivity();
    TaskAdapter adapter=new TaskAdapter();
    private NavController navController;
    private ActivityMainBinding binding;
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id=item.getItemId();
//        switch (id)
//        {
//            case R.id.help:
//                if(item.getItemId()==R.id.help)
//                    Collections.sort(adapter.getList(), new Comparator<Task>() {
//                        @Override
//                        public int compare(Task o1, Task o2) {
//                            return o1.getText().compareTo(o2.getText());
//                        }
//                    });
//                adapter.setList(adapter.list);
//                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        Collections.reverse(adapter.getList());
//        adapter.setList(adapter.list);
//        Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profiles)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        Prefs prefs= new Prefs(this);
        if (!prefs.isBoardShown()){
            navController.navigate(R.id.boardFragment);
        }

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_home ||
                        destination.getId() == R.id.navigation_notifications ||
                        destination.getId() == R.id.navigation_profiles ||
                        destination.getId()==R.id.navigation_dashboard
                ) {
                    binding.navView.setVisibility(View.VISIBLE);
                } else {
                    binding.navView.setVisibility(View.GONE);
                }
                if (destination.getId() == R.id.boardFragment) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
                if (destination.getId() == R.id.boardFragment) ;
            }
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}