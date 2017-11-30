package cs.dal.food4fit;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mihyar on 11/26/2017.
 * Update User Info
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    TextView profileName, profilePassword, profileConfirm;
    String name, oldName,facebook, oldEmail, password, confirm, photoUrl, oldPhotoUrl;
    Button updateInfo;
    FirebaseUser user;
    FirebaseAuth mAuth;
    ImageView profilePhoto;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Layout Content
        profilePhoto    = (ImageView) findViewById(R.id.profile_photo);
        profileName     = (TextView) findViewById(R.id.profile_name);
        profilePassword = (TextView) findViewById(R.id.profile_password);
        profileConfirm  = (TextView) findViewById(R.id.profile_confirm);
        updateInfo      = (Button)   findViewById(R.id.btn_profile_update);
        mAuth           = FirebaseAuth.getInstance();

        // Get the Current User Info
        sharedPreferences   = this.getSharedPreferences("Login", MODE_PRIVATE);
        oldName        = sharedPreferences.getString("DisplayName",null);
        oldPhotoUrl    = sharedPreferences.getString("Photo",null);
        oldEmail       = sharedPreferences.getString("Email", null);
        facebook     = sharedPreferences.getString("Facebook", null);

        // Populate Layout with User Info
        profileName.setText(oldName);
        // Populate User Photo
        if(oldPhotoUrl.equals("null")){
            oldPhotoUrl = "http://i.imgur.com/FlEXhZo.jpg?1";
        }
        loadProfilePic(oldPhotoUrl);

        // Facebook can not change info using app
        if (facebook != null){
            if (facebook.equals("True")){
                profilePhoto.setEnabled(false);
                profileName.setEnabled(false);
                profileName.setInputType(InputType.TYPE_NULL);
                profilePassword.setEnabled(false);
                profilePassword.setInputType(InputType.TYPE_NULL);
                profileConfirm.setEnabled(false);
                profileConfirm.setInputType(InputType.TYPE_NULL);
                updateInfo.setEnabled(false);
            }
        }
    }

    public void updateUserInfo(View view){
        name     = profileName.getText().toString();
        password = profilePassword.getText().toString();
        confirm  = profileConfirm.getText().toString();

        if (!password.equals("") && !confirm.equals("")){
            // Update User Password
            updatePassword();
        }
        if (oldName == null){
            oldName = "";
        }
        if (!oldName.equals(name)){
            // Update User Display Name
            user = mAuth.getCurrentUser();
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
            user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user = mAuth.getCurrentUser();
                        editor = sharedPreferences.edit();
                        editor.putString("DisplayName", user.getDisplayName());
                        editor.apply();
                        Toast.makeText(ProfileActivity.this, "Name Update Success!",Toast.LENGTH_SHORT).show();
                        finish();
                        onBackPressed();
                    }else{
                        Toast.makeText(ProfileActivity.this, "Name Update Failed!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Update User Password
    private void updatePassword() {
        mAuth.signOut();
        mAuth.signInWithEmailAndPassword(oldEmail, password)
                .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.updatePassword(confirm).isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Password Update Failed!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ProfileActivity.this, "Password Update Success!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(ProfileActivity.this, "Incorrect Password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loadProfilePic (final String link){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL newurl = null;
                    try {
                        newurl = new URL(link);
                        Bitmap userPhoto = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
                        profilePhoto.setImageBitmap(userPhoto);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
