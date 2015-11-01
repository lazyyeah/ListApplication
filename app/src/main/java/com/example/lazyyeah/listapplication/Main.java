package com.example.lazyyeah.listapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {

    private Button mSelectAutoRunApplication;
    private String mPackageName;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        mSelectAutoRunApplication = (Button) findViewById(R.id.button);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0);

    }

    public void onNewIntent(Intent intent) {
        if (mPackageName == null)
            return;

        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        writeNFCTag(detectedTag);
    }

    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null,
                    null);

    }

    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }
    public void onClick_SelectAutoRunApplication(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 0);
    }
    public void writeNFCTag(Tag tag) {
        if (tag == null) {
            return;
        }
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[] { NdefRecord
                        .createApplicationRecord(mPackageName) });
        int size = ndefMessage.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if(ndef!=null)
            {
                ndef.connect();

                if(!ndef.isWritable())
                {
                    return;
                }
                if(ndef.getMaxSize() < size)
                {
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                Toast.makeText(this, "ok", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == 1)
        {
            mSelectAutoRunApplication.setText(data.getExtras().getString(
                    "package_name"));
            String temp = mSelectAutoRunApplication.getText().toString();
            mPackageName = temp.substring(temp.indexOf("\n") + 1);

        }

    }




}
