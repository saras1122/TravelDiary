package com.example.notes_app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Add_notes extends AppCompatActivity {
    EditText notes2,notes1;
    ImageButton savebutton,bookmark;
    ImageView IVPreviewImage;
    private RadioGroup radioGroup;
    TextView pagetitle;
    String title,content,docId;
    boolean isEdit=false;
    Button italic,bold,underline,defaulted,BSelectImage;
    String s="BLACK";
    ArrayList<Integer> styles = new ArrayList<>();
    ArrayList<Integer> start1 = new ArrayList<Integer>();
    ArrayList<Integer> end = new ArrayList<>();
    boolean flag=false;
    int SELECT_PICTURE = 200;
    Uri imageURL;
    Bitmap selectedImageBitmap = null;
    FirebaseStorage storageRef;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    String photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        notes1=findViewById(R.id.notes1);
        notes2=findViewById(R.id.notes2);
        pagetitle=findViewById(R.id.pagetitle);
        savebutton=findViewById(R.id.savebutton);
        italic=findViewById(R.id.italic);
        bold=findViewById(R.id.bold);
        underline=findViewById(R.id.underline);
        defaulted=findViewById(R.id.defaulted);
        radioGroup = (RadioGroup)findViewById(R.id.groupradio);
        bookmark=findViewById(R.id.bookmark);
        BSelectImage = findViewById(R.id.img);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        int radioId=radioGroup.getChildAt(0).getId();
        radioGroup.check(radioId);
        notes2.setTextColor(Color.BLACK);
        //
        firebaseFirestore= FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance();
        storageReference=storageRef.getReference();
        //
        savebutton.setOnClickListener((v) -> Save());
        for(int i=1;i<=4;i++){
            styles.add(0);
            start1.add(0);
            end.add(0);
        }
        Log.d("hello",start1.size()+"");
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                        s=radioButton.getText().toString().toUpperCase();
                        HashMap<String, Integer> colorMap = new HashMap<>();
                        colorMap.put("RED", Color.RED);
                        colorMap.put("GREEN", Color.GREEN);
                        colorMap.put("MAGENTA", Color.MAGENTA);
                        colorMap.put("BLACK", Color.BLACK);
                        notes2.setTextColor(colorMap.get(s));
                        Toast.makeText(Add_notes.this,
                                radioButton.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmark.setImageResource(R.drawable.bookmark);
                flag=true;
            }
        });
        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styles.set(0,1);
                Spannable spannable=new SpannableStringBuilder(notes2.getText());
                spannable.setSpan(new StyleSpan(Typeface.ITALIC),
                        notes2.getSelectionStart(),
                        notes2.getSelectionEnd(),
                        0);
                start1.set(0,notes2.getSelectionStart());
                end.set(0,notes2.getSelectionEnd());
                notes2.setText(spannable);
            }
        });
        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styles.set(1,1);
                Spannable spannable=new SpannableStringBuilder(notes2.getText());
                spannable.setSpan(new StyleSpan(Typeface.BOLD),
                        notes2.getSelectionStart(),
                        notes2.getSelectionEnd(),
                        0);
                start1.set(1,notes2.getSelectionStart());
                end.set(1,notes2.getSelectionEnd());
                notes2.setText(spannable);
            }
        });
        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styles.set(2,1);
                Spannable spannable=new SpannableStringBuilder(notes2.getText());
                spannable.setSpan(new UnderlineSpan(),
                        notes2.getSelectionStart(),
                        notes2.getSelectionEnd(),
                        0);
                start1.set(2,notes2.getSelectionStart());
                end.set(2,notes2.getSelectionEnd());
                notes2.setText(spannable);
            }
        });
        defaulted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styles.set(3,1);
                styles.set(0,0);
                styles.set(1,0);
                styles.set(2,0);
                Spannable spannable=new SpannableStringBuilder(notes2.getText());
                spannable.setSpan(new StyleSpan(Typeface.NORMAL),
                        notes2.getSelectionStart(),
                        notes2.getSelectionEnd(),
                        0);
                start1.set(3,notes2.getSelectionStart());
                notes2.setText(spannable);
            }
        });
    }
    void Save() {
        String title=notes1.getText().toString();
        String content=notes2.getText().toString();
        if(title.isEmpty() || title==null){
            notes1.setError("Please Enter Title Name :)");
            return;
        }
        Note note=new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setArr(styles);
        note.setStart(start1);
        note.setEnd(end);
        note.setColor(s);
        note.setFlag(flag);
        if (selectedImageBitmap != null) {
            uploadImageToStorage(selectedImageBitmap, note);
        }else{
            saveNote(note);
        }
    }

    private void saveNote(Note note) {
        DocumentReference documentReference=Utility.getCollectionReferenceForNotes().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Add_notes.this, "Note added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(Add_notes.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }
    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        imageURL=data.getData();
                        Uri selectedImageUri = data.getData();
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        IVPreviewImage.setImageBitmap(
                                selectedImageBitmap);
                    }
                }
            });
    private void uploadImageToStorage(Bitmap imageBitmap, Note note) {
        if(imageURL!=null){
            final StorageReference myref=storageReference.child("photo/" + imageURL.getLastPathSegment());
            myref.putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myref.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if(uri!=null){
                                        photoUrl=uri.toString();
                                        note.setImageUrl(photoUrl);
                                        Log.d("hello",note.getImageUrl()+"img");
                                        saveNote(note);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}