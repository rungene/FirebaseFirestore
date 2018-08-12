package com.rungenes.firebasefirestore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {


    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");

    private DocumentSnapshot lastResult;

   // private DocumentReference noteRef = db.document("Notebook/My First Note");

    //private DocumentReference noteRef = db.collection("Notebook").document("My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        textViewData = findViewById(R.id.text_view_data);
    }

/*    @Override
    protected void onStart() {
        super.onStart();

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }
                String data = "";

                for (DocumentSnapshot documentSnapshot : documentSnapshots) {

                    Note note = documentSnapshot.toObject(Note.class);

                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();

                    String title = note.getTitle();
                    String description = note.getDescription();
                    int number = note.getPriority();


                    data += "ID" + documentId +
                            "\nTitle: " + title + "\nDescription: " + description + "\nPriority; " + number + "\n\n";

                }
                textViewData.setText(data);

            }
        });

    }*/

    @Override
    protected void onStart() {
        super.onStart();

        notebookRef.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(e != null){

                    return;
                }

                for (DocumentChange dc : documentSnapshots.getDocumentChanges()){

                    DocumentSnapshot documentSnapshot = dc.getDocument();

                    String id = documentSnapshot.getId();

                    int oldIndex = dc.getOldIndex();
                    int newIndex = dc.getNewIndex();

                    switch (dc.getType()){
                        case ADDED:
                            textViewData.append("\nAdded "+id+ "\nOld idex"+ oldIndex+
                            "\nNew index "+newIndex
                            );
                            break;
                        case MODIFIED:
                            textViewData.append("\nModified "+id+ "\nOld idex"+ oldIndex+
                                    "\nNew index "+newIndex
                            );
                            break;
                        case REMOVED:
                            textViewData.append("\nRemoved "+id+ "\nOld idex"+ oldIndex+
                                    "\nNew index "+newIndex
                            );
                            break;
                    }

                }
            }
        });
    }

    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        // editTextTitle.setText("");

        String description = editTextDescription.getText().toString();
        // editTextDescription.setText("");

        if (editTextPriority.length() == 0) {

            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());
        //editTextNumber.setText("");

   /*     Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);
        note.put(KEY_NUMBER,number);*/

        Note note = new Note(title, description, priority);

        notebookRef.add(note);
    }


    public void loadNotes(View v) {

        Query query;

        if (lastResult==null){
           query= notebookRef.orderBy("priority")
                    .limit(3);
        }else {

           query= notebookRef.orderBy("priority")
                    .startAfter(lastResult)
                    .limit(3);

        }

                query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        String data = "";

                        for (DocumentSnapshot documentSnapshot:documentSnapshots){

                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();
                            String title = note.getTitle();
                            String description = note.getDescription();
                            int priority = note.getPriority();


                            data += "ID" + documentId +
                                    "\nTitle: " + title + "\nDescription: " + description + "\nPriority; " + priority + "\n\n";

                        }

                        if (documentSnapshots.size()>0) {
                            data += "_______________\n\n";
                            textViewData.append(data);

                            lastResult = documentSnapshots.getDocuments()
                                    .get(documentSnapshots.size() - 1);
                        }
                    }
                });

    }
}