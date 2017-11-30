package cs.dal.food4fit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mihyar on 11/7/2017.
 * Login activity
 */

public class LoginActivity extends AppCompatActivity{
    // Declare Page Content
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    Button loginButton;
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    TextView passwordText, emailText, forgotText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Generate Signature code
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "cs.dal.food4fit",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */
        // Define a progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        // Initialize Page Content
        loginButton   = (Button)   findViewById(R.id.btn_login);
        passwordText  = (TextView) findViewById(R.id.input_password);
        emailText     = (TextView) findViewById(R.id.input_email);
        forgotText    = (TextView) findViewById(R.id.link_forgot);
        mAuth          = FirebaseAuth.getInstance();

        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton .setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();

        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook:onError", exception);
            }
        });
    }

    // Facebook Login Result return
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Login Functionality
    public void login (View view){
        Log.d(TAG, "Login");

        // Get the content of the input
        String email    = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (!email.equals("") && !password.equals("")){
            // Show Progress Dialog
            progressDialog.show();
            // call login function
            userLogin(email, password);
        }
    }

    // Login function
    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, go to Home Page
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()){
                                // If User is Verified, Save the user locally then go to Home Page
                                editor.putString("Email",user.getEmail() );
                                editor.putString("DisplayName", user.getDisplayName());
                                editor.putString("Photo", String.valueOf(user.getPhotoUrl()));
                                editor.putString("ID",user.getUid());
                                editor.putString("Facebook","False");
                                editor.commit();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                // Hide Progress Dialog
                                progressDialog.dismiss();
                                finish();
                            }else{
                                // If User is not Verified, Show text to the user
                                forgotText.setText("Email is not verified, please verify your email");
                                forgotText.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Incorrect Email or Password.",
                                    Toast.LENGTH_SHORT).show();
                            forgotText.setVisibility(View.VISIBLE);
                            // Hide Progress Dialog
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // Go to Signup page
    public void goSignup(View view){
        startActivity(new Intent(this,SignupActivity.class));
        finish();
    }

    // Go to Password Reset Page
    public void forgotPassword (View view){
        startActivity(new Intent(this, ForgotActivity.class));
//        finish();
    }

    // Handling Facebook token
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        // Show Progress Dialog
        progressDialog.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, save the user locally then Go Home Page
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            editor.putString("Email",user.getEmail() );
                            editor.putString("DisplayName", user.getDisplayName());
                            editor.putString("Photo", String.valueOf(user.getPhotoUrl()));
                            editor.putString("ID",user.getUid());
                            editor.putString("Facebook","True");
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            progressDialog.dismiss();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
