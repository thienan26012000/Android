package android.nguyenphuocthienan.music_services2;

import static android.nguyenphuocthienan.music_services2.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class Services extends Service {
    private MediaPlayer mediaPlayer;

    public static final int PAUSE = 1;
    public static final int RESUME = 2;
    public static final int CLOSE = 3;
    public static final int START = 4;

    // check trạng thái
    private boolean status;

    private SongMp3 sSong;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "MyService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    // nhận intent
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        // để đảm bảo có dữ liệu
        if (bundle != null)
        {
            // get "key" đã khai báo
            SongMp3 songMp3 = (SongMp3) bundle.get("key_song");

            if (songMp3 != null)
            {
                sSong = songMp3;
                // khai báo 1 trình phát nhạc để play mp3 của "songMp3"
                startMusic(songMp3);
                sendNotificationMedia(songMp3);
            }
        }

        // nhận action từ broadcast receiver
        int actionMusic = intent.getIntExtra("action_muzik_to_service", 0);
        handleActionForMusic(actionMusic);


        return START_NOT_STICKY;
    }

    // xử lý chạy mp3
    private void startMusic(SongMp3 songMp3) {
        // mediaPlayer == null thì mới khởi tạo
        if (mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), songMp3.getResource());
        }
        mediaPlayer.start();
        status = true;
        sendActionToActivity(START);
    }

    // xử lý các action "PAUSE, RESUME, CLOSE"
    private void handleActionForMusic(int action)
    {
        switch (action)
        {
            case PAUSE:
                pauseMusic();
                break;
            case RESUME:
                resumeMusic();
                break;
            case CLOSE:
                // dừng hẳn service
                stopSelf();
                sendActionToActivity(CLOSE);
                break;
        }
    }
    // pause music
    private void pauseMusic()
    {
        if (mediaPlayer != null && status)
        {
            mediaPlayer.pause();
            status = false; // khi pause
            // dùng để update lại view
            sendNotificationMedia(sSong);
            sendActionToActivity(PAUSE);
        }
    }
    // resume music
    private void resumeMusic()
    {
        // status đang false
        if (mediaPlayer != null && !status)
        {
            mediaPlayer.start();
            status = true;
            // dùng để update lại view
            sendNotificationMedia(sSong);
            sendActionToActivity(RESUME);
        }
    }

    // sendNotification không dùng MediaStyle
//    private void sendNotification(SongMp3 songMp3) {
//        // xác định nơi đến
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), songMp3.getImage());
//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
//        // set dữ liệu cho title
//        remoteViews.setTextViewText(R.id.tv_title_song, songMp3.getTitle());
//        // set dữ liệu cho singer
//        remoteViews.setTextViewText(R.id.tv_singer_song, songMp3.getSinger());
//        // set dữ liệu cho image
//        remoteViews.setImageViewBitmap(R.id.img_song, bitmap);
//
//        remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_pause);
//
//        // nếu đang chạy click vào thì pause lại
//        if (status)
//        {
//            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, PAUSE));
//            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_pause);
//        }
//        // nếu đang pause click vào thì resume lại
//        else
//        {
//            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, RESUME));
//            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_play);
//        }
//        // nếu đang play or pause click vào thì close lại
//        remoteViews.setOnClickPendingIntent(R.id.img_close, getPendingIntent(this, CLOSE));
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_music)
//                .setContentIntent(pendingIntent)
//                .setCustomContentView(remoteViews)
//                .build();
//
//        startForeground( 1, notification);
//    }


    // sendNotification bằng MediaStyle
    private void sendNotificationMedia(SongMp3 songMp3)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ehan_image);

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setSubText("AnMp3")
                .setContentTitle(songMp3.getTitle())
                .setContentText(songMp3.getSinger())
                .setLargeIcon(bitmap)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    // 0,1,2 là số lượng btn hiển thị khi thu nhỏ mediaStyle
                    // nếu đặt 1 số 3 thì nó sẽ hiển thị mỗi btn close
                    .setShowActionsInCompactView(0, 1, 2 /* #1: pause button */)
                    .setMediaSession(mediaSessionCompat.getSessionToken()));

        // check status khi pause or play
        if (status)
        {
            notificationBuilder
                    // Thêm các button điều khiển media gợi ý định media service
                    .addAction(R.drawable.ic_previous, "previous", null) // #0
                    .addAction(R.drawable.ic_pause, "pause", getPendingIntent(this, PAUSE)) // #1
                    .addAction(R.drawable.ic_next, "next", null) // #2
                    .addAction(R.drawable.ic_close, "next", getPendingIntent(this, CLOSE)); // #3
        }
        else
        {
            notificationBuilder
            // Thêm các button điều khiển media gợi ý định media service
                    .addAction(R.drawable.ic_previous, "previous", null) // #0
                    .addAction(R.drawable.ic_play, "pause", getPendingIntent(this, RESUME)) // #1
                    .addAction(R.drawable.ic_next, "next", null) // #2
                    .addAction(R.drawable.ic_close, "next", getPendingIntent(this, CLOSE)); // #3
        }

        Notification notification = notificationBuilder.build();

        startForeground( 1, notification);
    }

    // truyền dữ liệu action đi
    private PendingIntent getPendingIntent(Context context, int action)
    {
        Intent intent = new Intent(this, Receivers.class);
        intent.putExtra("action_muzik", action);

        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //  xử lý dừng service
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service", "MyService onDestroy");
        // giả sử mediaPlayer bị Destroy
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // gửi action qua main_activity
    private void sendActionToActivity(int action)
    {
        Intent intent = new Intent("send_data_to_activity");
        // gửi 1 obj Song, trạng thái của service
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", sSong);
        bundle.putBoolean("status", status);
        bundle.putInt("action_music", action);

        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
