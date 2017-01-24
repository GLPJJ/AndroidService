package com.example.administrator.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/24.
 */

public class TestService extends IntentService {

	public static final String Tag = "TestService";

	boolean mIsRunning = true;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {//启动
				if (msg.replyTo != null && !mMessengersClient.contains(msg.replyTo)) {
					mMessengersClient.add(msg.replyTo);
				}
			}

			Log.d(Tag, "Recv Client Msg = " + msg.toString());
		}
	};

	Messenger mMessenger = new Messenger(mHandler);
	List<Messenger> mMessengersClient;

	public TestService() {
		super("TestService");
		mIsRunning = false;

		mMessengersClient = new ArrayList<>();
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		Log.i(Tag, Thread.currentThread().toString());
		while (mIsRunning) {
			try {
				Thread.sleep(1000);
			} catch (Throwable e) {

			}

			for (Messenger messenger : mMessengersClient) {
				if (messenger != null) {
					String obj = "654321";

					try {
						messenger.send(Message.obtain(null, 2, obj));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}

			Log.d(Tag, "Hi");
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(Tag, "onCreate");
	}

	@Override
	public void onStart(@Nullable Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(Tag, "onStart");

		mIsRunning = true;
	}

	@Override
	public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
		Log.d(Tag, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(Tag, "onDestroy");
		mIsRunning = false;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(Tag, "onBind");
		return mMessenger.getBinder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(Tag, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		Log.d(Tag, "onRebind");
	}
}
