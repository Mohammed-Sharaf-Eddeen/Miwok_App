/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        //Numbers Activity
        TextView numbers = (TextView) findViewById(R.id.numbers);
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumbers(v);
            }
        });

        //Family Activity
        TextView family = (TextView) findViewById(R.id.family);
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFamily(v);
            }
        });

        //Colors Activity
        TextView colors = (TextView) findViewById(R.id.colors);
        colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColors(v);
            }
        });

        //Phrases Activity
        TextView phrases = (TextView) findViewById(R.id.phrases);
        phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhrases(v);
            }
        });



    }


    public void openNumbers(View view) {
        Intent intent = new Intent(this,Numbers.class);
        startActivity(intent);
    }

    public void openFamily(View view) {
        Intent intent = new Intent(this,Family.class);
        startActivity(intent);
    }

    public void openColors(View view) {
        Intent intent = new Intent(this,Colors.class);
        startActivity(intent);
    }

    public void openPhrases(View view) {
        Intent intent = new Intent(this,Phrases.class);
        startActivity(intent);
    }
}
