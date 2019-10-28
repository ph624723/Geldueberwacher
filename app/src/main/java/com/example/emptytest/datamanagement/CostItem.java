package com.example.emptytest.datamanagement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class CostItem {
    public static final DecimalFormat decimalFormat = new DecimalFormat("+#.00;-#");
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public static final Comparator DateComparator = new Comparator<CostItem>() {
        @Override
        public int compare(CostItem lhs, CostItem rhs) {
            return lhs.getDate().after(rhs.getDate()) ? 1 : 0;   //or whatever your sorting algorithm
        }
    };

    private int uid;
    private String subject;
    private double value;
    private Date date = Calendar.getInstance().getTime();
    private Categories.Category category;

    public CostItem (String subject, double value, Categories.Category category){
        this.subject = subject;
        this.value = value;
        this.category = category;
    }

    public CostItem (String subject, double value, Categories.Category category, Date date){
        this(subject,value, category);
        if(date != null) this.date = date;
    }

    public CostItem (int uid, String subject, double value, Categories.Category category, Date date){
        this(subject,value, category, date);
        this.uid = uid;
    }

    public String subjectString (){
        return getSubject();
    }

    public String valueString (){
        return decimalFormat.format(value);
    }

    public String dateString (){
        return simpleDateFormat.format(date);
    }

    public String categoryString(){
        return category.getName();
    }

    public double getValue(){
        return value;
    }

    public Date getDate() {
        return date;
    }

    public String getSubject(){
        return subject;
    }

    public int getUid(){
        return uid;
    }

    public Categories.Category getCategory (){
        return category;
    }

    public void setSubject(String in){
        subject = in;
    }

    public void setValue (double value){
        this.value = value;
    }

    public void setDate (Date date){
        this.date = date;
    }

    public void setCategory (Categories.Category category){
        this.category = category;
    }
}
