package com.alexmncn.p6_servicios_moviles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();

        bundle.putString("message", "Integracion de Firebase Analytics");
        mFirebaseAnalytics.logEvent("InitScreen", bundle);

        authSetup();
    }

    private void authSetup() {
        // Configurar los elementos de la interfaz de usuario
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);
        EditText email = findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);

        // Configurar el boton de registro
        registerButton.setOnClickListener(view-> {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            if (!(emailValue.isEmpty() && passwordValue.isEmpty())) {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(emailValue,passwordValue)
                        .addOnCompleteListener(task-> {
                            if (task.isSuccessful()) {
                                // Mostrar la pantalla principal si el registro es exitoso
                                showHome(emailValue);
                            }else{
                                // Mostrar una alerta si hay un error
                                showAlert();
                            }
                        });
            }
        });

        // Configurar el boton de login
        loginButton.setOnClickListener(view-> {
            String emailInput = email.getText().toString();
            String pass = password.getText().toString();
            if (!(emailInput.isEmpty() && pass.isEmpty())) {
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(emailInput,pass)
                        .addOnCompleteListener(task-> {
                            if (task.isSuccessful()) {
                                // Mostrar la pantalla principal si el acceso es exitoso
                                showHome(emailInput);
                            }else{
                                // Mostrar una alerta si hay un error
                                showAlert();
                            }
                        });
            }
        });
    }

    // Mostrar una alerta de error
    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Mostrar la pantalla principal
    private void showHome(String email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Obtiene el ususario actual
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("email", email);
        i.putExtra("provider", user.getProviderData().get(0).getProviderId());
        startActivity(i);
    }
}