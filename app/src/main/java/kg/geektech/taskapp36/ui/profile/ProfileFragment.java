package kg.geektech.taskapp36.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import kg.geektech.taskapp36.Prefs;
import kg.geektech.taskapp36.R;

public class ProfileFragment extends Fragment {
    private Bitmap bitmap;
    private EditText editText;
    private ImageView img;
    private Prefs prefs;
    private StorageReference mStorageRef;
    private Uri uploadUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefs = new Prefs(requireContext());
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners(view);

        if (prefs.getTxt() != null) {
            editText.setText(prefs.getTxt());
        }
        if(prefs.getImage()!=null)
        {
            img.setImageBitmap(decodeBase64(prefs.getImage()));
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(!editText.getText().toString().isEmpty()) {
            prefs.setTxt(editText.getText().toString().trim());
        }
        if(bitmap!=null){
            prefs.setImage(encodeToBase64(bitmap));
        }
    }

    private void initListeners(View view) {
        view.findViewById(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Выход с аккаунта");
                alertDialog.setMessage("Вы уверены что хотите выйти с аккаунта?");

                alertDialog.setNegativeButton("Нет", (dialog, which) -> {

                });

                alertDialog.setPositiveButton("Да", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.loginFragment);
                });

                alertDialog.show();

            }
        });
        img = view.findViewById(R.id.image);
        editText = view.findViewById(R.id.edit);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        img.setImageURI(uri);
                        uploadImage();
                    }
                }
        );
        img.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });

        mStorageRef= FirebaseStorage.getInstance().getReference("ImageDB");

    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void uploadImage(){
        Bitmap bitmap =((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference mRef =mStorageRef.child(System.currentTimeMillis()+"my_image");
        UploadTask up =mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri=task.getResult();
            }
        });
    }
}