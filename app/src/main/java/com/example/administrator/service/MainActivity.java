package com.example.administrator.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Service 使用例子说明，本例子提供 client-service交互，通过Messenger来相互通信，通过一条标识信息，
 * 来让服务器保存客户端Messenger。
 * <p>
 * 单个Activity
 * 正常的使用方式是 先 start service -> bind service -> unbind service -> stop service
 * <p>
 * 多个Activity只需要多次bind就ok了
 */

public class MainActivity extends AppCompatActivity {

	public static final String Tag = "MainActivity";

	Intent mIntentService;
	Messenger mMessengerServer;
	ServiceConnection mServiceConnection;

	Messenger mMessenger = new Messenger(new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Log.d(Tag, "Recv Server Msg = " + msg.toString());
		}
	});

	@OnClick(R.id.button)
	void onClick1() {
		startService(mIntentService);
	}

	@OnClick(R.id.button2)
	void onClick2() {
		if (mServiceConnection == null) {
			mServiceConnection = new ServiceConnection() {
				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					Log.d(Tag, "onServiceConnected");
					mMessengerServer = new Messenger(service);
					String obj = "123456";
					try {
						Message msg = Message.obtain(null, 1, obj);
						msg.replyTo = mMessenger;
						mMessengerServer.send(msg);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onServiceDisconnected(ComponentName name) {
					Log.d(Tag, "onServiceDisconnected");
				}
			};
		}

		bindService(mIntentService, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@OnClick(R.id.button3)
	void onClick3() {
		unbindService(mServiceConnection);
	}

	@OnClick(R.id.button4)
	void onClick4() {
		stopService(mIntentService);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		mIntentService = new Intent(this, TestService.class);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
