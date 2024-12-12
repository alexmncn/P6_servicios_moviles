package com.alexmncn.p6_servicios_moviles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String provider = bundle.getString("provider");

        Log.d("values", "Email: " + email + " Provider: " + provider);

        setup(email, provider);
        saveData(email);
        getData(email);
        deleteData(email);
        getImage();
    }

    private void saveData(String email) {
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            TextView providerTextView = findViewById(R.id.providerTextView);
            EditText addressEditText = findViewById(R.id.addressEditText);
            EditText numberEditText = findViewById(R.id.numberEditText);

            String provider = providerTextView.getText().toString();
            String address = addressEditText.getText().toString();
            String number = numberEditText.getText().toString();

            Map<String, Object> data = new HashMap<>();
            data.put("provider", provider);
            data.put("address", address);
            data.put("number", number);

            db.collection("users").document(email).set(data);
        });
    }

    private void getData(String email) {
        Button getButton = findViewById(R.id.getButton);

        getButton.setOnClickListener(v -> {
            EditText addressEditText = findViewById(R.id.addressEditText);
            EditText numberEditText = findViewById(R.id.numberEditText);

            db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
               Map<String, Object> data = documentSnapshot.getData();

               if (data != null) {
                   addressEditText.setText(data.get("address").toString());
                   numberEditText.setText(data.get("number").toString());
                }
            });
        });
    }

    private void deleteData(String email) {
        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(v -> {
            EditText addressEditText = findViewById(R.id.addressEditText);
            EditText numberEditText = findViewById(R.id.numberEditText);
            addressEditText.setText("");
            numberEditText.setText("");

            db.collection("users").document(email).delete();
        });
    }

    private void getImage() {
        Button getImage = findViewById(R.id.getImageButton);
        ImageView imageView = findViewById(R.id.remoteImageView);

        getImage.setOnClickListener(v -> {
            String filename = "Logo_Tienda_linea_cesta.jpg";
            StorageReference islandRef = storageRef.child(filename);

            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("image", e.toString());
                }
            });
        });
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