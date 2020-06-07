package com.example.android.miwok;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Phrases extends AppCompatActivity {

    private MediaPlayer audioPlayer;


    /* Audio focus is not forced by the android framework, it is just recommended which means I can play
     my sound even if if I don't have the audiofocus on me, so these are just guidelines to follow for the best practices
             */
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        releaseAudioPlayer();

                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        // Pause playback
                        audioPlayer.pause();

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Lower the volume, keep playing
                        // I dont wanna duck
                        audioPlayer.pause();

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        audioPlayer.start();
                    }
                }
            };

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseAudioPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //Inastantiating the audio manager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //Numbers List
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinnә oyaase'nә",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good","kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming","hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming","әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.","yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here","әnni'nem", R.raw.phrase_come_here));



/*
       //Add Views for the list elements
        LinearLayout rootView = (LinearLayout)findViewById(R.id.rootView);
        int i = 0;
        while (i < words.size()){
            TextView textView = new TextView(this);
            textView.setText(words.get(i));
            rootView.addView(textView);
            i = i+1;
        }*/

        WordsAdapter<Word> itemsAdapter = new WordsAdapter(this, words, R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* To close the the previous media file if a new file has been clicked while playing the previous one
                giving the audioPlayer another object to reference while the previous object, he was refrencing, is still working
                will not stop the previous object from continueing playing the file, but when you force audioplayer to release
                its resources which is the previous object, it gets deleted immediately, however the previous one,
                it lets the object hanging in there waiting for the garbage collecter to delete it
                 */
                releaseAudioPlayer();
                /* Request AudioFocus and giving it the listener that will handle all the other situation like when the focus is
                taking away from me, this wil be handled above in instantiating the listener*/
                /* This is a call to request audiofocus, but we wanna know whether it has succeeded or not to act based on that
                so we but the return value of it in a variable and check its value*/
                int result = audioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request the type of Focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                /*
                If Audiofocus is accepted, then create the music and play, otherwise, Pop a toast message to tell the user
                this is because audio focus is not forced by the android framework, it is just recommended which means I can play
                my sound even if if I don't have the audiofocus on me
                */
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    audioPlayer = MediaPlayer.create(Phrases.this, words.get(position).getSoundId());
                    audioPlayer.start();
                    audioPlayer.setOnCompletionListener(onCompletionListener);
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseAudioPlayer();
    }

    private void releaseAudioPlayer(){
        if (audioPlayer != null){
            audioPlayer.release();
            audioPlayer = null;
            // Abandon audio focus when playback complete
            audioManager.abandonAudioFocus(afChangeListener);
        }
    }
}
