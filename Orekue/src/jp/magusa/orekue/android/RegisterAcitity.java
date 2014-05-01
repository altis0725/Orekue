package jp.magusa.orekue.android;

import java.io.IOException;
import java.net.MalformedURLException;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OResponse;
import jp.magusa.orekue.android.model.OTask;
import jp.magusa.orekue.android.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterAcitity extends Activity implements OnClickListener{
	EditText mEditUsername, mEditPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (DataStore.getMyUserId() >= 0){
			Intent main = new Intent(this, MainActivity.class);
			startActivity(main);
			finish();
		}
		
		setContentView(R.layout.activity_register);
		
		mEditUsername = (EditText) findViewById(R.id.editText_username);
		mEditPassword = (EditText) findViewById(R.id.editText_password);
		findViewById(R.id.button_register).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		//Register
		new OTask<Void, Void, Long>() {

			@Override
			protected OResponse<Long> do_in_background(Void... params) {
				User user = new User();
				user.setName(mEditUsername.getText().toString());
				user.setDeviceId(Orekue.getDeviceId());
				user.setAccountName(mEditUsername.getText().toString());
				try {
					return DataStore.register(user, mEditPassword.getText().toString());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onFinish(int errorCode, String errorMessage,
					Long result) {
				if (errorCode == 0){
					DataStore.setMyUserId(result);
					Orekue.sEditor.putLong("my_user_id", result);
					Orekue.sEditor.commit();
					Intent main = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(main);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
				}
			}
		}.execute();
	}
}
