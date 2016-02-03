package com.nichhk.remotestream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sveder.remotestream.R;

/**
 * Created by nich on 1/23/16.
 */
public class ConfigActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        Button button = (Button) findViewById(R.id.button);
        final EditText ipText = (EditText) findViewById(R.id.editText);
        final Context me = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(me, StreamActivity.class);
                intent.putExtra("ip", ipText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
