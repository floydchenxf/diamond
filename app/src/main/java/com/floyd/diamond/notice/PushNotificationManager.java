package com.floyd.diamond.notice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.text.TextUtils;

public class PushNotificationManager {

    private static final String TAG = PushNotificationManager.class
            .getSimpleName();
    private static PushNotificationManager instance;
    private static final long[] vibrateLong = new long[]{100, 250, 100, 500};

    private Context context;
    //	private Vibrator vibrator;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private NotificationManager notificationManager;

    private long lastSoundTime;

    private PushNotificationManager() {

    }

    public static synchronized PushNotificationManager getInstance(
            Context context) {
        if (instance == null) {
            instance = new PushNotificationManager();
            instance.context = context;
            instance.audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_HEADSET_PLUG);
            instance.notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            XPushHandlerThread.getInstance().init();
        }
        return instance;
    }

    public void showNotification(NoticeVO noticeVO) {
        runNotification(noticeVO);
    }

    public void runNotification(NoticeVO noticeVO) {
        Runnable tipNotificationRunning = new TipNotificationRunning(noticeVO);
        XPushHandlerThread.getInstance().getHandler()
                .postDelayed(tipNotificationRunning, 500);
    }


    private class TipNotificationRunning implements Runnable {
        private NoticeVO noticeVO;

        public TipNotificationRunning(NoticeVO noticeVO) {
            this.noticeVO = noticeVO;
        }

        @Override
        public void run() {
            Builder builder = new Builder(context)
                    .setContentTitle(noticeVO.getTitle()).setTicker(noticeVO.getTitle())
//					.setContentText(noticeVO.content)
                    .setLights(Color.GREEN, 300, 1000).setAutoCancel(true)
                    .setSmallIcon(noticeVO.ticketIconId);


            String contextText = noticeVO.content;
            if (!TextUtils.isEmpty(contextText)) {
                String[] lines = contextText.split("\r\n");
                if (lines.length <= 1) {
                    builder.setContentText(contextText);
                } else {
                    InboxStyle inboxStyle = new InboxStyle();
                    for (int i = 0; i < lines.length; i++) {
                        if (i < 4) {
                            inboxStyle.addLine(lines[i]);
                        } else {
                            inboxStyle.setSummaryText("more...");
                            break;
                        }
                    }

                    builder.setStyle(inboxStyle);
                }
            } else {
                builder.setContentText(contextText);
            }

            if (noticeVO.vibrate) {
                builder.setVibrate(vibrateLong);
            }

            if (noticeVO.tip) {
                if (NotificationAppConfig.appNoticeSound == 0) {
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                } else {
                    Uri soundUri = Uri.parse("android.resource://"
                            + context.getPackageName() + "/"
                            + NotificationAppConfig.appNoticeSound);
                    if (audioManager.isWiredHeadsetOn()) {
                        builder.setSound(soundUri, AudioManager.STREAM_MUSIC);
                    } else {
                        builder.setSound(soundUri);
                    }
                }
            }

            long now = System.currentTimeMillis();
            if (noticeVO.vibrate || noticeVO.tip) {
                long dd = now - lastSoundTime;
                if (dd < 5000) {
                    builder.setVibrate(null).setSound(null).setDefaults(0);
                }
                lastSoundTime = now;
            }

            String iconUrl = noticeVO.iconUrl;

            int seq = Sequence.getCurrentSeq();
            OpenAppVO appVO = noticeVO.openAppVO;
            OpenAppIntent openAppIntent = OpenAppFactory.getInstance().createOpenApp(context, appVO);
            if (openAppIntent == null) {
                return;
            }
            Intent it = openAppIntent.createOpenApp();
            PendingIntent preIntent = PendingIntent.getActivity(context, seq, it, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(preIntent);
            int mode = seq % 3;

            Notification notif = builder.build();
            if (audioManager.isWiredHeadsetOn()) {
                notif.audioStreamType = AudioManager.STREAM_MUSIC;
            }
            notificationManager.notify(noticeVO.ticketIconId + mode, notif);
        }
    }

    public void recycle() {
        if (instance != null) {
            if (instance.mediaPlayer != null) {
                instance.mediaPlayer.release();
                instance.mediaPlayer = null;
            }

            if (XPushHandlerThread.getInstance().getLooper() != null) {
                XPushHandlerThread.getInstance().getLooper().quit();
            }

            instance.audioManager = null;
            instance.notificationManager = null;
            instance = null;
        }
    }

}
