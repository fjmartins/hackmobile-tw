package com.bot.hackmobtw.hackathonmobiletw.activity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bot.hackmobtw.hackathonmobiletw.R;
import com.bot.hackmobtw.hackathonmobiletw.activity.adapter.MessageAdapter;
import com.bot.hackmobtw.hackathonmobiletw.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private Button btnEnviar;
    private ListView listViewMessages;
    private List<Message> listMessages;
    private MessageAdapter adapter;
    private EditText inputMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnEnviar = (Button) findViewById(R.id.btnSend);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        inputMsg = (EditText) findViewById(R.id.inputMsg);

        btnEnviar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!inputMsg.getText().toString().trim().isEmpty()) {
                    Message message = new Message("Me", inputMsg.getText().toString(), true);
                    inputMsg.setText("");

                    listMessages.add(message);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listMessages = new ArrayList<>();

        adapter = new MessageAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        Message message = new Message("Jerkbot", "Oi delicia", false);
        appendMessage(message);
    }

    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    public void playBeep() {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
