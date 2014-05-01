package jp.magusa.orekue.android.friends;

import jp.maguro.vs.samon.orekue.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MakeFriendView extends RelativeLayout implements OnClickListener, 
OnNdefPushCompleteCallback, CreateNdefMessageCallback {
	private static final String TAG = "Beam";
    private static final int MESSAGE_SENT = 1;
    Activity context;
    NfcAdapter mNfcAdapter;
    FrameLayout mFrame;
    TextView mInfoText;
    EditText mEditText;
    PendingIntent pendingIntent;
    IntentFilter ndef;
    String[][] techListsArray = new String[][] { new String[] { NfcF.class.getName() } };

    public MakeFriendView(Activity context, Handler handler) {
        super(context);
        this.context = context;
        
        // レイアウト読み込み
        View layout = LayoutInflater.from(context).inflate(R.layout.friends_exchange, this);
        
        // nfcが利用可能かチェック
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (mNfcAdapter == null) {
            Toast.makeText(context, "NFC is not available", Toast.LENGTH_LONG).show();
            //finish();
            return;
        }
        
    }

	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
    
	public void findFriend(){
		//Toast.makeText(context, text, duration)
	}
    
}
