package com.rungenes.firebasefirestore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NUMBER = "number";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextNumber;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference noteRef = db.collection("Notebook").document("My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextNumber = findViewById(R.id.edit_text_number);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (e!=null){

                    String errorLoading = e.getMessage().toString();
                    Toast.makeText(MainActivity.this, "Error while loading"+errorLoading, Toast.LENGTH_SHORT).show();

                    Log.d(TAG, e.toString());
                    return;
                }

                if (documentSnapshot.exists()){
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
                    String number = documentSnapshot.getString(KEY_NUMBER);

                    textViewData.setText("Title: "+title+"\n"+ "Description: "+description+"\n"+"Number "+number);




                }

            }
        });


    }

    public void saveNote(View v) {
        String title = editTextTitle.getText().toString();
        editTextTitle.setText("");

        String description = editTextDescription.getText().toString();
        editTextDescription.setText("");
        String number = editTextNumber.getText().toString();
        editTextNumber.setText("");

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);
        note.put(KEY_NUMBER,number);

        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public  void updateDescription(View view){

        String description = editTextDescription.getText().toString();

     /*   Map<String,Object> note = new HashMap<>();
        note.put(KEY_DESCRIPTION,description);*/

      //  noteRef.set(note, SetOptions.merge());
        noteRef.update(KEY_DESCRIPTION,description);
    }

    public void loadNote(View v){

        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
                    String number = documentSnapshot.getString(KEY_NUMBER);

                    textViewData.setText("Title: "+title+"\n"+ "Description: "+description+"\n"+"Number "+number);




                }else {
                    Toast.makeText(MainActivity.this, "Document dose not exist", Toast.LENGTH_SHORT).show();
                }

            }
        })
          .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

                  Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                  Log.d(TAG, "error: "+e.toString());

              }
          });
    }
}