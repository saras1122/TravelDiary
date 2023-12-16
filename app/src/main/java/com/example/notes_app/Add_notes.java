package com.example.notes_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Add_notes extends AppCompatActivity {
    EditText notes2,notes1,location;
    ImageButton savebutton,bookmark,defaulted;
    ImageView IVPreviewImage;
    private RadioGroup radioGroup;
    TextView pagetitle,date1,blink;
    String title,content,docId;
    boolean isEdit=false;
    ImageButton italic,bold,underline,BSelectImage;
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
    ArrayList<Uri> imageBit= new ArrayList<>();
    ArrayList<Bitmap> bitmaps= new ArrayList<>();
    int count=0;
    int mYear,mMonth,mDay;
    List<SlideModel> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        date1=findViewById(R.id.date1);
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        // set current date into textview
        date1.setText(sdf.format(c.getTime()));
//        date1.setText(new StringBuilder()
//                // Month is 0 based, just add 1
//                .append(yy).append(" ").append("-").append(mm + 1).append("-")
//                .append(dd));
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Add_notes.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        date1.setText(sdf.format(myCalendar.getTime()));
                        Log.d("date",date1.getText().toString() + "");
                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
        notes1=findViewById(R.id.notes1);
        location=findViewById(R.id.location);
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
                        colorMap.put("DKGRAY", Color.DKGRAY);
                        colorMap.put("BLACK", Color.BLACK);

                        //Log.d("id",checkedId+""+colorMap.get(s));
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
        String location1=location.getText().toString();
        if(title.isEmpty() || title==null){
            notes1.setError("Please Enter Title Name :)");
            return;
        }
        if(location1.isEmpty() || location1==null){
            location.setError("Please Enter Location :)");
            return;
        }
        Note note=new Note();
        note.setTitle(title);
        note.setLocation(location1);
        note.setContent(content);
        note.setDate(date1.getText().toString());
        note.setArr(styles);
        note.setStart(start1);
        note.setEnd(end);
        note.setColor(s);
        note.setFlag(flag);
        Log.d("count",count+"");
        if (count != 0) {
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
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
                            && data.getClipData() != null) {
                        imageURL=data.getData();
                        count = data.getClipData().getItemCount();
                        int CurrentImageSelect = 0;
                        while (CurrentImageSelect < count) {
                            Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                            imageBit.add(imageuri);
                            CurrentImageSelect = CurrentImageSelect + 1;
                        }
                        ImageSlider imageSlider = findViewById(R.id.image_slider);
                        List<SlideModel> imageList1 = new ArrayList<>();
                        for(Uri i:imageBit){
                            imageList1.add(new SlideModel(i.toString(),"To see preview first save images then come back"
                                    , ScaleTypes.CENTER_CROP));
                        }
                        imageSlider.setImageList(imageList1);
                        imageSlider.startSliding(1000000);
                        imageSlider.stopSliding();
                        Log.d("count",count+"");
                        Log.d("list i see",imageBit+"");
                    }else{
                        count = 1;
                        Uri imageuri = data.getData();
                        imageBit.add(imageuri);
                        ImageSlider imageSlider = findViewById(R.id.image_slider);
                        List<SlideModel> imageList1 = new ArrayList<>();
                        imageList1.add(new SlideModel(imageuri.toString(),"To see preview first save images then come back"
                                , ScaleTypes.CENTER_CROP));
                        imageSlider.setImageList(imageList1);
                        imageSlider.startSliding(1000000);
                        imageSlider.stopSliding();
                    }
                }
            });
    private void uploadImageToStorage(Bitmap imageBitmap, Note note) {
        ArrayList<String> a=new ArrayList<>();
        if(count!=0){
            for(int i=0;i<imageBit.size();i++) {
                Uri individualImage = imageBit.get(i);

                Log.d("list", imageBit + "");
                final StorageReference myref = storageReference.child("photo/" + individualImage.getLastPathSegment());
                myref.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        myref.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        if (uri != null) {
                                            photoUrl = uri.toString();
                                            a.add(photoUrl);
                                            if(a.size()==count){
                                                note.setImages(a);
                                                saveNote(note);
                                            }
                                            note.setImageUrl(photoUrl);
                                            Log.d("hello", note.getImageUrl() + "img");
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
}