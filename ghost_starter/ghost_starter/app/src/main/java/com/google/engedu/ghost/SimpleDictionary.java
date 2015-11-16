package com.google.engedu.ghost;

import android.text.format.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random r = new Random(System.currentTimeMillis());

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        String word = null;
        int size = words.size();

        if(prefix == null){
            int index = r.nextInt(size);
            word = words.get(index);
        }
        else {
            word = binarySearch(prefix,size);
        }

        return word;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }

    public String binarySearch(String prefix,int size){
        int low = 0;
        int high = size-1;
        int mid;
        String word;

        while(low <= high){
            mid = (low+high)/2;
            word = words.get(mid);
            if(word.startsWith(prefix)){
                return word;
            }
            else if(word.compareToIgnoreCase(prefix) < 0){
                low = mid+1;
            }
            else {
                high = mid - 1;
            }
        }

        return null;
    }
}
