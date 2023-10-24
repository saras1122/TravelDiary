package com.example.notes_app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    EditText notes2,notes1;
    ImageButton savebutton,bookmark,delete1;
    TextView pagetitle;
    ImageView IVPreviewImage;
    TextView delete;
    String title,content,docId;
    Button italic,bold,underline,defaulted,BSelectImage;
    private RadioGroup radioGroup;
    boolean isEdit=false;
    ArrayList<Integer> styles = new ArrayList<>();
    ArrayList<Integer> start = new ArrayList<>();
    ArrayList<Integer> end = new ArrayList<>();
    HashMap<String, Integer> colorMap = new HashMap<>();
    String textColor="BLACK";
    boolean flag=false;
    String photoUrl;
    FirebaseStorage storageRef;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    int SELECT_PICTURE = 200;
    Uri imageURL;
    Bitmap selectedImageBitmap = null;

    private Bitmap bitmap;
    boolean isImageFitToScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        notes1=findViewById(R.id.notes1);
        notes2=findViewById(R.id.notes2);
        pagetitle=findViewById(R.id.pagetitle);
        savebutton=findViewById(R.id.savebutton);
        delete=findViewById(R.id.delete);
        italic=findViewById(R.id.italic);
        bold=findViewById(R.id.bold);
        underline=findViewById(R.id.underline);
        defaulted=findViewById(R.id.defaulted);
        bookmark=findViewById(R.id.bookmark);
        radioGroup = (RadioGroup)findViewById(R.id.groupradio);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        BSelectImage=findViewById(R.id.img);
        delete1=findViewById(R.id.delete1);
        colorMap.put("RED", Color.RED);
        colorMap.put("GREEN", Color.GREEN);
        colorMap.put("MAGENTA", Color.MAGENTA);
        colorMap.put("BLACK", Color.BLACK);
//        int radioId=radioGroup.getChildAt(0).getId();
//        radioGroup.check(radioId);
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");
        textColor=getIntent().getStringExtra("color");
        colors(textColor);
        //
        firebaseFirestore= FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance();
        storageReference=storageRef.getReference();
        //
        flag=getIntent().getBooleanExtra("flag",false);
        if(docId!=null && !docId.isEmpty()){
            isEdit=true;
        }

        notes1.setText(title);
        notes2.setText(content);
        notes2.setTextColor(colorMap.get(textColor));
        //formatText(notes2);
        if(isEdit){
            notes2.setTextColor(colorMap.get(textColor));
            ArrayList<Integer> stylesz=getIntent().getIntegerArrayListExtra("styles");
            ArrayList<Integer> s1=getIntent().getIntegerArrayListExtra("start");
            ArrayList<Integer> e1=getIntent().getIntegerArrayListExtra("end");
            Log.d("myTag", stylesz.size()+"This is my message");
            if(stylesz.get(0)==1){
                Spannable spannable=new SpannableStringBuilder(notes2.getText());
                spannable.setSpan(new StyleSpan(Typeface.ITALIC),
                        s1.get(0),
                        e1.get(0),
                        0);
                notes2.setText(spannable);
            }
            if(stylesz.get(1)==1){
                Spannable spannable=new SpannableStringBuilder(notes2.getText());
                spannable.setSpan(new StyleSpan(Typeface.BOLD),
                        s1.get(1),
                        e1.get(1),
                        0);
                notes2.setText(spannable);
            }
            if(stylesz.get(2)==1){
                Spannable spannable=new SpannableStringBuilder(notes2.getText());
                spannable.setSpan(new UnderlineSpan(),
                        s1.get(2),
                        e1.get(2),
                        0);
                notes2.setText(spannable);
            }
            if(flag){
                bookmark.setImageResource(R.drawable.bookmark);
                flag=true;
            }
            photoUrl=getIntent().getStringExtra("image");
            Log.d("hello5herreerre",photoUrl+"img2");
            if(photoUrl!=null && !photoUrl.equals("") ){

                URL url= null;
                try {
                    url = new URL(photoUrl);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                Bitmap res=doInBackground(url);
                selectedImageBitmap =res;
                onPostExecute(res);
                delete1.setVisibility(View.VISIBLE);
            }
            pagetitle.setText("Edit your note");
            delete.setVisibility(View.VISIBLE);
        }
        ArrayList<Integer> i1=getIntent().getIntegerArrayListExtra("styles");
        ArrayList<Integer> s1=getIntent().getIntegerArrayListExtra("start");
        ArrayList<Integer> e1=getIntent().getIntegerArrayListExtra("end");
        for(int i=1;i<=4;i++){
            styles.add(i1.get(i-1));
            start.add(s1.get(i-1));
            end.add(e1.get(i-1));
        }
        IVPreviewImage.setOnClickListener((v)->{
            if(isImageFitToScreen) {
                isImageFitToScreen=false;
                IVPreviewImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                IVPreviewImage.setAdjustViewBounds(true);
            }else{
                isImageFitToScreen=true;
                IVPreviewImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                IVPreviewImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        });
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    bookmark.setImageResource(R.drawable.bookmarkwhite);
                    flag=false;
                }else{
                    bookmark.setImageResource(R.drawable.bookmark);
                    flag=true;
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,
                                         int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                textColor=radioButton.getText().toString().toUpperCase();
                notes2.setTextColor(colorMap.get(textColor));
                Toast.makeText(MainActivity2.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();
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
                notes2.setText(spannable);
                start.set(0,notes2.getSelectionStart());
                end.set(0,notes2.getSelectionEnd());
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
                notes2.setText(spannable);
                start.set(1,notes2.getSelectionStart());
                end.set(1,notes2.getSelectionEnd());
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
                start.set(2,notes2.getSelectionStart());
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

                start.set(3,notes2.getSelectionStart());
                end.set(3,notes2.getSelectionEnd());
                notes2.setText(spannable);
            }
        });
        savebutton.setOnClickListener((v) -> Save());
        delete.setOnClickListener((v)-> deleteNotefromFirebase());
    }
    protected Bitmap doInBackground(URL url) {
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        while(thread.isAlive()){ }
        return bitmap;
    }
    protected void onPostExecute(Bitmap result) {
        Log.d("Error Message", result+"magw");
        IVPreviewImage.setImageBitmap(result);
    }

    public void colors(String s){
        if(s.equals("BLACK")){
            int radioId=radioGroup.getChildAt(0).getId();
            radioGroup.check(radioId);
        }
        if(s.equals("RED")){
            int radioId=radioGroup.getChildAt(3).getId();
            radioGroup.check(radioId);
        }
        if(s.equals("GREEN")){
            int radioId=radioGroup.getChildAt(1).getId();
            radioGroup.check(radioId);
        }
        if(s.equals("MAGENTA")){
            int radioId=radioGroup.getChildAt(2).getId();
            radioGroup.check(radioId);
        }
    }
    private void Save() {
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
        note.setStart(start);
        note.setEnd(end);
        note.setColor(textColor);
        note.setFlag(flag);
        if (selectedImageBitmap != null) {
            uploadImageToStorage(selectedImageBitmap, note);
        }else{
            saveNote(note);
        }
    }

    private void saveNote(Note note) {
        DocumentReference documentReference;
        if(isEdit){
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        }
        else{
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "Note added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void deleteNotefromFirebase() {
        DocumentReference documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void imageChooser() {
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