package br.ufrn.bestMatch;

import java.io.Serializable;

public class Word implements Serializable {
    private Integer distance;
    private String word;

    public Word(Integer distance, String word) {
        this.distance = distance;
        this.word = word;
    }

    public void fromWord(Word other) {
        this.distance = other.getDistance();
        this.word = other.getWord();
    }

    public Integer getDistance() {
        return distance;
    }

    public String getWord() {
        return word;
    }
}