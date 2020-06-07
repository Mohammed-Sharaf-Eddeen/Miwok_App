package com.example.android.miwok;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsAdapter<T> extends ArrayAdapter<Word> {
    private int color;

    public WordsAdapter(Activity context, ArrayList<Word> words, int color) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context,0, words);
        /*
        SO IMPORTANT ... Resources IDs are integers which means R.color.category_colors and the others are integers, however this
        integer value represent them as resources in the application to be accessed, not the real int value of colors!
        so in the code you do not need to get the resource id, you need to get what this resource id represent as color
        This problem does not happen when referencing them in xml code, all of this is being considered there
        but here you have to take care
          */
        this.color = ContextCompat.getColor(getContext(),color);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Word word = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView englishText = (TextView) listItemView.findViewById(R.id.english_text);
        englishText.setText(word.getEnglishWord());

        TextView miwokText = (TextView) listItemView.findViewById(R.id.miwok_text);
        miwokText.setText(word.getMiwokWord());


       ImageView image = (ImageView) listItemView.findViewById(R.id.image);
       //We must handle the different cases of the Word object
        if (word.getImageId() != 0) {
            image.setVisibility(View.VISIBLE);
           image.setImageResource(word.getImageId());
        } else {
            image.setVisibility(View.GONE);
        }



        //Setting the color based on the color given in the initialization od the adapter
        LinearLayout cell = (LinearLayout) listItemView.findViewById(R.id.cell);
        cell.setBackgroundColor(color);



        // Return the whole list item layout (containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}



