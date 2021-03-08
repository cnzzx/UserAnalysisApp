package com.example.useranalysisapp.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * 情感原因对
 * <p>包括情感（字符串）、原因（字符串为元素的链表）</p>
 */
public class EmotionCausePair {
    private String emotion;
    private LinkedList<String> cause;

    /**
     * 按原因数目逆序排序
     */
    public static Comparator<EmotionCausePair> ecpCompare = new Comparator<EmotionCausePair>() {
        @Override
        public int compare(EmotionCausePair o1, EmotionCausePair o2) {
            int delta = o1.cause.size() - o2.cause.size();
            if(delta < 0) return 1;
            if(delta == 0) return 0;
            if(delta > 0) return -1;
            return 0;
        }
    };

    public EmotionCausePair(){
        this.emotion = "unknown";
        this.cause = new LinkedList<String>();
    }
    public EmotionCausePair(String emo, String[] cau){
        this.emotion = emo;
        this.cause = new LinkedList<String>(Arrays.asList(cau));
    }

    public String getEmotion() {
        return this.emotion;
    }
    public LinkedList<String> getCause(){
        return this.cause;
    }
}
