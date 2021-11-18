package kg.geektech.taskapp36;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;

import kg.geektech.taskapp36.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityMainBinding binding;

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
        Prefs prefs = new Prefs(this);
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            navController.navigate(R.id.loginFragment);
        }
        if (!prefs.isBoardShown()) {
            navController.navigate(R.id.boardFragment);
        }

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_home ||
                        destination.getId() == R.id.navigation_notifications ||
                        destination.getId() == R.id.navigation_profiles ||
                        destination.getId() == R.id.navigation_dashboard
                ) {
                    binding.navView.setVisibility(View.VISIBLE);
                } else {
                    binding.navView.setVisibility(View.GONE);
                }
                if (destination.getId() == R.id.boardFragment || destination.getId() == R.id.loginFragment) {
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