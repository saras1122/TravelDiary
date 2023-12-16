package com.example.notes_app;

import static android.content.ContentValues.TAG;
import static com.example.notes_app.MyNotificationPublisher.channelID;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationServices extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Timer timer ;
    TimerTask timerTask ;
    String TAG = "Timers" ;
    int Your_X_SECS = 5 ;
    @Override
    public IBinder onBind (Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        Log. e ( TAG , "onStartCommand" ) ;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // User is not authenticated, stop the service
            stopSelf();
            return START_NOT_STICKY;
        }
        super.onStartCommand(intent, flags, startId);
        createNotificationChannel();
        if(solve().equals("")){
            return START_STICKY;
        }
        String s=solve();
        LocalDate today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MM-dd");
        }
        String monthAndDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            monthAndDate = today.format(formatter);
        }

        String s1="",s2="",s3="";
        s1+=s.charAt(0);s1+=s.charAt(1);
        s2+=s.charAt(3);s2+=s.charAt(4);
        s3+="20";
        s3+=s.charAt(6);s3+=s.charAt(7);
        int d=Integer.parseInt(s1);
        int m=Integer.parseInt(s2);
        int y=Integer.parseInt(s3);
        s2=String.valueOf(m);
        s2=s2+"-"+s1;
        if(!s2.equals(monthAndDate)){
            System.out.println(s2);
            return START_STICKY;
        }
        Log.d("f",d+""+m+""+y +"  " +s1);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        m--;
        LocalTime localTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localTime = LocalTime.now();
        }
        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hour = localTime.getHour();
        }
        int minute = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            minute = localTime.getMinute();
        }
        System.out.println(hour);
        if(hour>17 || (hour == 17 && minute > 5)){
            System.out.println("shfj");
            return START_STICKY;
        }
        cal.set(y,m,d,17,5);
        Intent intent1 = new Intent(getApplicationContext(), MyNotificationPublisher.class);
        int uniqueNotificationID = (int) System.currentTimeMillis();
        long time = cal.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), uniqueNotificationID, intent1,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
        return START_STICKY;
    }
    @Override
    public void onCreate () {
        Log. e ( TAG , "onCreate" ) ;
    }
    @Override
    public void onDestroy () {
        Log. e ( TAG , "onDestroy" ) ;
        stopTimerTask() ;
        super .onDestroy() ;
    }
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler() ;
    public void startTimer () {

    }
    public void stopTimerTask () {
    }
        private void createNotification () {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
            mBuilder.setContentTitle( "My Notification" ) ;
            mBuilder.setContentText( "Notification Listener Service Example" ) ;
            mBuilder.setTicker( "Notification Listener Service Example" ) ;
            mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
            mBuilder.setAutoCancel( true ) ;
            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager. IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel) ;
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
        }
    private void createNotificationChannel() {
        String name = "Notif Channel";
        String desc = "A Description of the Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelID, name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(desc);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }
    String solve() {
        String s = datePick();
        Query query = Utility.getCollectionReferenceForNotes()
                .whereEqualTo("date", s);
        Set<String> al= new HashSet<>();
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        if (!options.getSnapshots().isEmpty()) {
        } else {
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String imageUrl = document.getString("date");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                            al.add(imageUrl);
                    } else {
                        Log.d(TAG, document.getId() + " => No images field or value is null");
                    }
                }
                Log.d("images", al + "");
            }).addOnFailureListener(e -> {
                Log.w(TAG, "Error getting documents.", e);
            });
            return s;
        }
        return "";
    }
    String datePick(){
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Add one year to the current date
        LocalDate futureDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            futureDate = currentDate.plusYears(1);
        }

        // Specify the desired date format
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        }
        String currentDateString="";
        // Format the dates to strings
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateString = currentDate.format(formatter);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String futureDateString = futureDate.format(formatter);
        }
        return currentDateString;
    }

}
