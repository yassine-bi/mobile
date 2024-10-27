package com.example.user_module.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.user_module.R;

// ResetPasswordActivity.java
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.user_module.AppDatabase;
import com.example.user_module.Dao.UserDao;

import org.mindrot.jbcrypt.BCrypt;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText resetCodeEditText, newPasswordEditText;
    private Button confirmResetButton;
    private String expectedCode, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetCodeEditText = findViewById(R.id.resetCodeEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmResetButton = findViewById(R.id.confirmResetButton);

        email = getIntent().getStringExtra("email");
        expectedCode = getIntent().getStringExtra("resetCode");

        confirmResetButton.setOnClickListener(v -> {
            String enteredCode = resetCodeEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();

            if (enteredCode.equals(expectedCode)) {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_database").build();
                new Thread(() -> {
                    UserDao userDao = db.userDao();
                    userDao.updatePassword(email, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                    runOnUiThread(() -> {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }).start();
            } else {
                Toast.makeText(this, "Invalid reset code", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
