package com.example.emptytest.datamanagement;

import android.util.Log;

public class Categories {
    public static class Category {
        private int id;
        private String name;

        private Category(int id, String name){
            this.name = name;
            this.id = id;
        }

        public String getName(){
            return name;
        }

        public int getId(){
            return id;
        }
    }

    public static Category OFFICIAL = new Category(0,"Official");
    public static Category NECESSARY = new Category(1,"Necessary");
    public static Category FUN = new Category(2,"Fun");

    public static Category getCategory (int id){
        if (id == OFFICIAL.getId()) return OFFICIAL;
        else if (id == NECESSARY.getId()) return NECESSARY;
        else if (id == FUN.getId()) return FUN;
        else return null;
    }
}
