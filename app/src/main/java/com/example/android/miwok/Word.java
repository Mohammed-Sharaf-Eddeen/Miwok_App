package com.example.android.miwok;

public class Word {

    private String englishWord;
    private String miwokWord;
    private int imageId;
    private int soundId;

    public Word(String englishWord, String miwokWord, int soundId) {
        this.englishWord = englishWord;
        this.miwokWord = miwokWord;
        this.soundId = soundId;
    }

    public Word(String englishWord, String miwokWord, int imageId, int soundId) {
        this.englishWord = englishWord;
        this.miwokWord = miwokWord;
        this.imageId = imageId;
        this.soundId = soundId;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getMiwokWord() {
        return miwokWord;
    }

    public int getImageId() {
        return imageId;
    }

    public int getSoundId() {
        return soundId;
    }

}
