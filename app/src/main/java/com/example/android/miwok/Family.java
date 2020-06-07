package com.example.android.miwok;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class Family extends AppCompatActivity {

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

    /*
    Declaring the listener this way better than an annonymous class so that it does ot get insatniated many times
    in every item click
     */
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
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother ", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));





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

        WordsAdapter<Word> itemsAdapter = new WordsAdapter(this, words, R.color.category_family);

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
                    audioPlayer = MediaPlayer.create(Family.this, words.get(position).getSoundId());
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
