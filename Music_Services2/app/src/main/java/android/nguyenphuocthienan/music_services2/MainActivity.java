package android.nguyenphuocthienan.music_services2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText edtDataIntent;
    private Button btnStartService, btnStopService;

    private RelativeLayout layoutBottom;
    private ImageView imgSong, imgPlayOrPause, imgClose;
    private TextView tvTitleSong, tvSingerSong;

    private SongMp3 sSong;
    private boolean status;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            // check bunble == null không xử lý gì hết
            if (bundle == null)
            {
                return;
            }
            sSong = (SongMp3) bundle.get("object_song");
            status = bundle.getBoolean("status");
            int actionMusic =  bundle.getInt("action_music");

            handleLayoutMusic (actionMusic);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lắng nghe broadcaseReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));

//        edtDataIntent = findViewById(R.id.edt_data_intent);
        btnStartService = findViewById(R.id.btn_start_service);
        btnStopService = findViewById(R.id.btn_stop_service);

        layoutBottom = findViewById(R.id.layout_music_bottom);
        imgSong = findViewById(R.id.img_song);
        imgPlayOrPause = findViewById(R.id.img_play_or_pause);
        imgClose = findViewById(R.id.img_close);
        tvTitleSong = findViewById(R.id.tv_title_song);
        tvSingerSong = findViewById(R.id.tv_singer_song);

        // start service
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartService();
            }
        });
        // stop service
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStopService();
            }
        });
    }

    private void clickStartService() {
        SongMp3 songMp3 = new SongMp3("Em hát ai nghe (remix)", "Orange", R.drawable.ehan_image, R.raw.ehan_song);

        Intent intent = new Intent(this, Services.class);
        // gửi đối tượng
        Bundle bundle = new Bundle();
            // 2 đối tượng là "key" để truyền data đi && "đối tượng" muốn truyền đi
        bundle.putSerializable("key_song", songMp3);
        intent.putExtras(bundle);

        startService(intent);
    }

    private void clickStopService() {
        Intent intent = new Intent(this, Services.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void handleLayoutMusic(int action)
    {
        switch (action)
        {
            case Services.START:
                //set layout hiển thị ở main_activity
                layoutBottom.setVisibility(View.VISIBLE);
                showInfoSong();
                setStatusInLayout();
                break;

            case Services.PAUSE:
                setStatusInLayout();
                break;

            case Services.RESUME:
                setStatusInLayout();
                break;

            case Services.CLOSE:
                layoutBottom.setVisibility(View.GONE);
                break;
        }
    }

    // set thông tin music lên layout
    private void showInfoSong()
    {
        if (sSong == null)
        {
            return;
        }
        imgSong.setImageResource(sSong.getImage());
        tvTitleSong.setText(sSong.getTitle());
        tvSingerSong.setText(sSong.getSinger());

        // tương tác trên layout điều khiển ngược lại trên service
        imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // nếu service đang chạy
                if (status)
                {
                    sendActionToService(Services.PAUSE);
                }
                else
                {
                    sendActionToService(Services.RESUME);
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendActionToService(Services.CLOSE);
            }
        });
    }

    // set status music lên layout
    private void setStatusInLayout()
    {
        if (status)
        {
            imgPlayOrPause.setImageResource(R.drawable.ic_pause);
        }
        else
        {
            imgPlayOrPause.setImageResource(R.drawable.ic_play);
        }
    }

    //
    private void sendActionToService(int action)
    {
        Intent intent = new Intent(this, Services.class);
        intent.putExtra("action_muzik_to_service", action);

        startService(intent);
    }
}