package cs.dal.food4fit;

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
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Mihyar on 11/11/2017.
 * This activity is to reset password for users
 */

public class ForgotActivity extends AppCompatActivity {

    // Declare Page Content
    private static final String TAG = "ForgotActivity";
    private FirebaseAuth mAuth;
    TextView emailText, resetFail;
    Button btn_reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        // Initialize Page Content
        emailText = (TextView) findViewById(R.id.input_email_reset);
        resetFail = (TextView) findViewById(R.id.text_resetFail);
        btn_reset = (Button)   findViewById(R.id.btn_reset);
        mAuth     = FirebaseAuth.getInstance();
    }

    // Password Reset Functionality
    public void resetPassword (View view){
        final String email    = emailText.getText().toString();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(ForgotActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Reset Success, Show message then go to login page
                    Log.d(TAG, "ResetEmail:success");
                    Toast.makeText(ForgotActivity.this, "Reset Link was sent to " + email,
                            Toast.LENGTH_SHORT).show();
                    // Show Text that Sending Failed
                    resetFail.setVisibility(View.INVISIBLE);
                    // Call Login Page
                    goLogin();
                }else{
                    // Reset Fail, Show Text that send Failed
                    Log.d(TAG, "ResetEmail:failure");
                    resetFail.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // Move to Login Page
    public void goLogin(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
        finish();
    }
}
