package com.alexmncn.p6_servicios_moviles;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String provider = bundle.getString("provider");

        Log.d("values", "Email: " + email + " Provider: " + provider);
        setup(email, provider);
    }

    private void setup(String email, String provider) {
        // Obtener referencias a los TextViews y establecer su texto
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView providerTextView = findViewById(R.id.providerTextView);
        emailTextView.setText(email);
        providerTextView.setText(provider);

        // Obtener referencia al boton de cerrar sesion
        Button cerrarSesion = findViewById(R.id.logoutButton);

        // Configurar el listener para el boton de cerrar sesion
        cerrarSesion.setOnClickListener(view-> {
            // Cerrar sesion en Firebase y volver a la actividad anterior
            FirebaseAuth.getInstance().signOut();
            super.getOnBackPressedDispatcher().onBackPressed();
        });
    }
}