package android.nguyenphuocthienan.music_services2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receivers extends BroadcastReceiver {
    @Override
    // nhận dữ liệu action đã truyền đi
    public void onReceive(Context context, Intent intent) {
        // defaultValue = 0 nếu không nhận được intent này
        int actionMuzik = intent.getIntExtra("action_muzik",0);

        // truyền dữ liệu ngược lại service để xử lý sự kiện
        Intent intentService = new Intent(context, Services.class);
        intentService.putExtra("action_muzik_to_service", actionMuzik);
        // start service
        context.startService(intentService);

    }
}
