package com.example.baitap;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ThongBaoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = "nuoc_channel";
        String channelName = "Nhắc uống nước";

        // Tạo channel nếu cần (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Thông báo mỗi giờ nhắc uống nước");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // icon mặc định để test
                .setContentTitle("💧 Uống nước thôi nào!")
                .setContentText("Đừng quên bổ sung nước để giữ sức khỏe nhé 💙")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify((int) System.currentTimeMillis(), builder.build());
    }
}
