package com.example.icetouch.dorest;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private String name;
    private int id;
    private int imageId;
    private int numId;
    private int timeId;
    private String order;
    private int priority;
    private int imageArray[];
    private String answer[];

    public Test(String name, int id, int imageId, int numId, int timeId, String order, int priority, int imageArray[], String answer[]){
        this.name = name;
        this.id = id;
        this.imageId = imageId;
        this.numId = numId;
        this.timeId = timeId;
        this.order = order;
        this.priority = priority;
        this.imageArray = imageArray;
        this.answer = answer;
    }

    public static List<Test> getAllTests() {
        List<Test> Tests = new ArrayList<>();
        Tests.add(new Test("线性代数 · 基础篇I", 1, R.drawable.zma, 8, 25, "A1~A8", 1,
                new int[]{0, R.drawable.exlm_a_01, R.drawable.exlm_a_02, R.drawable.exlm_a_03, R.drawable.exlm_a_04, R.drawable.exlm_a_05, R.drawable.exlm_a_06, R.drawable.exlm_a_07, R.drawable.exlm_a_08} ,
                new String[]{"", "D", "B", "C", "C", "D", "D", "B", "BC"}));
        Tests.add(new Test("线性代数 · 基础篇II", 2, R.drawable.zmb, 8, 35, "A9~A16", 1,
                new int[]{0, R.drawable.exlm_a_09, R.drawable.exlm_a_10, R.drawable.exlm_a_11, R.drawable.exlm_a_12, R.drawable.exlm_a_13, R.drawable.exlm_a_14, R.drawable.exlm_a_15, R.drawable.exlm_a_16} ,
                new String[]{"", "B", "C", "A", "C", "B", "B", "C", "BCD"}));
        Tests.add(new Test("线性代数 · 提高篇I", 3, R.drawable.zmc, 8, 40, "B1~B8", 2,
                new int[]{0, R.drawable.exlm_b_01, R.drawable.exlm_b_02, R.drawable.exlm_b_03, R.drawable.exlm_b_04, R.drawable.exlm_b_05, R.drawable.exlm_b_06, R.drawable.exlm_b_07, R.drawable.exlm_b_08} ,
                new String[]{"", "B", "D", "C", "D", "B", "A", "C", "AC"}));
        Tests.add(new Test("线性代数 · 提高篇II", 4, R.drawable.zmd, 8, 50, "B9~B16", 2,
                new int[]{0, R.drawable.exlm_b_09, R.drawable.exlm_b_10, R.drawable.exlm_b_11, R.drawable.exlm_b_12, R.drawable.exlm_b_13, R.drawable.exlm_b_14, R.drawable.exlm_b_15, R.drawable.exlm_b_16} ,
                new String[]{"", "B", "C", "B", "C", "ABC", "ABC", "ACD", "BCD"}));
        Tests.add(new Test("线性代数 · 真题篇（2019）", 5, R.drawable.zme, 9, 55, "Z1~Z9", 3,
                new int[]{0, R.drawable.exlm_z_01, R.drawable.exlm_z_02, R.drawable.exlm_z_03, R.drawable.exlm_z_04, R.drawable.exlm_z_05, R.drawable.exlm_z_06, R.drawable.exlm_z_07, R.drawable.exlm_z_08, R.drawable.exlm_z_09},
                new String[]{"", "C", "A", "D", "A", "D", "D", "C", "C", "AC"}));
        return Tests;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getNumId(){
        return numId;
    }
    public void setNumId(){
        this.numId = numId;
    }

    public int getTimeId() {
        return timeId;
    }
    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public String getOrder(){
        return order;
    }
    public void setOrder(String order)
    {
        this.order = order;
    }

    public int getPriority(){
        return priority;
    }
    public void setPriority(int priority){
        this.priority = priority;
    }

    public int[] getImageArray(){ return imageArray; }
    public void setImageArray(){ this.imageArray = imageArray;}

    public String[] getAnswer(){
        return answer;
    }
    public void setAnswer(){
        this.answer = answer;
    }
}