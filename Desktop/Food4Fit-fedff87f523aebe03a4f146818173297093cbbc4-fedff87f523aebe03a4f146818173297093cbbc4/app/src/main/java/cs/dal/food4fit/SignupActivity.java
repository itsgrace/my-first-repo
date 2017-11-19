package cs.dal.food4fit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

/**
 * Created by Mihyar on 11/7/2017.
 * Sign up Activity
 */

public class SignupActivity extends AppCompatActivity {

    // Declare page content
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    Button signupButton;
    ProgressDialog progressDialog;
    TextView passwordText, emailText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize page content
        signupButton   = (Button) findViewById(R.id.btn_signup);
        passwordText  = (TextView) findViewById(R.id.input_password);
        emailText     = (TextView) findViewById(R.id.input_email);
        mAuth          = FirebaseAuth.getInstance();
    }

    public void signup(final View view){
        Log.d(TAG, "Create Account");
        // Check if the input is valid
        if (!validate()) {
            Toast.makeText(getBaseContext(), "Account Creation Failed", Toast.LENGTH_LONG).show();
            return;
        }

        // Show a progress Dialog
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // Get the content of the input
        String email    = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // Create new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, Send Verification E-Mail then go to sign in page
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification(user);
                            goLogin(view);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "User Already Exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // Hide the progress dialog
                        progressDialog.hide();
                    }
                });
    }

    // Validate input against listed rules
    public boolean validate(){
        // Get the value in the input
        boolean valid   = true;
        String email    = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // Validate E-Mail
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        // Validate Password
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }

    // Go to Login Page
    public void goLogin(View view){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    // Send Verification E-Mail
    private void sendEmailVerification(final FirebaseUser user) {

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // E-Mail sent successfully
                            Toast.makeText(SignupActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Failure to send E-Mail
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignupActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }
}
