package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Numbers extends AppCompatActivity {

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


    // Declaration of a class and instantiating one object from it, on completion listenner that listens to the end of each sound
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
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));




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

        WordsAdapter<Word> itemsAdapter = new WordsAdapter(this, words,R.color.category_numbers);

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
                    audioPlayer = MediaPlayer.create(Numbers.this, words.get(position).getSoundId());
                    audioPlayer.start();
                    audioPlayer.setOnCompletionListener(onCompletionListener);
                }

            }
        });
    }


    //Stopping the mediaplayer when the application is paused and moved to the stopped state
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
