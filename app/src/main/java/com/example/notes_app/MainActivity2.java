package com.example.notes_app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    int count=0;
    ArrayList<Uri> imageBit= new ArrayList<>();
    EditText notes2,notes1,location;
    ImageButton savebutton,bookmark,delete1,del;
    TextView pagetitle;
    ImageView IVPreviewImage;
    TextView delete,date1;
    String title,content,docId,dateJ,loc;
    ImageButton italic,bold,underline,defaulted,BSelectImage;
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
    int mYear,mMonth,mDay;
    List<SlideModel> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        date1=findViewById(R.id.date1);
        location=findViewById(R.id.location);
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
//        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        BSelectImage=findViewById(R.id.img);
        delete1=findViewById(R.id.delete1);
        colorMap.put("RED", Color.RED);
        colorMap.put("GREEN", Color.GREEN);
        colorMap.put("DKGRAY", Color.DKGRAY);
        colorMap.put("BLACK", Color.BLACK);
        del=findViewById(R.id.del);



//        int radioId=radioGroup.getChildAt(0).getId();
//        radioGroup.check(radioId);
        title=getIntent().getStringExtra("title");
        loc=getIntent().getStringExtra("location");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");
        textColor=getIntent().getStringExtra("color");
        dateJ=getIntent().getStringExtra("date");
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
        location.setText(loc);
        notes2.setTextColor(colorMap.get(textColor));
        //formatText(notes2);
        if(isEdit){
            notes2.setTextColor(colorMap.get(textColor));
            ArrayList<Integer> stylesz=getIntent().getIntegerArrayListExtra("styles");
            ArrayList<Integer> s1=getIntent().getIntegerArrayListExtra("start");
            ArrayList<Integer> e1=getIntent().getIntegerArrayListExtra("end");
            ArrayList<String> urls=getIntent().getStringArrayListExtra("listImages");
            Log.d("myg", urls+"This is my message");
            if(urls!=null && urls.size()!=0 ) {
                Log.d("myffg", stylesz.size()+"This is my message");
                ImageSlider imageSlider = findViewById(R.id.image_slider);
                for (String s : urls) {
                    imageList.add(new SlideModel(s, ScaleTypes.CENTER_CROP));
                }
                imageSlider.setImageList(imageList);
                imageSlider.startSliding(1000000);
                imageSlider.stopSliding();
            }
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
            date1.setText(dateJ);
        }
        ArrayList<Integer> i1=getIntent().getIntegerArrayListExtra("styles");
        ArrayList<Integer> s1=getIntent().getIntegerArrayListExtra("start");
        ArrayList<Integer> e1=getIntent().getIntegerArrayListExtra("end");
        for(int i=1;i<=4;i++){
            styles.add(i1.get(i-1));
            start.add(s1.get(i-1));
            end.add(e1.get(i-1));
        }
//        IVPreviewImage.setOnClickListener((v)->{
//            if(isImageFitToScreen) {
//                isImageFitToScreen=false;
//                IVPreviewImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                IVPreviewImage.setAdjustViewBounds(true);
//            }else{
//                isImageFitToScreen=true;
//                IVPreviewImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//                IVPreviewImage.setScaleType(ImageView.ScaleType.FIT_XY);
//            }
//        });
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(MainActivity2.this, new DatePickerDialog.OnDateSetListener() {
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
        del.setOnClickListener((v)-> deleteNotefromFirebase());
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
//        IVPreviewImage.setImageBitmap(result);
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
        note.setArr(styles);
        note.setStart(start);
        note.setEnd(end);
        note.setColor(textColor);
        note.setFlag(flag);
        note.setDate(date1.getText().toString());
        if (count != 0) {
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

                Log.d("list finallly", imageBit + "");
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