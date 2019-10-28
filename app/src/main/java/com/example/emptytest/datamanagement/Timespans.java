package com.example.emptytest.datamanagement;

public final class Timespans {
    public static class Timespan {
        private int id;

        private Timespan(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }

    public static Timespan ALL_TIME = new Timespan(0);
    public static Timespan THIS_WEEK = new Timespan(1);
    public static Timespan LAST_WEEK = new Timespan(2);
    public static Timespan THIS_MONTH = new Timespan(3);
    public static Timespan LAST_MONTH = new Timespan(4);

    public static Timespan getTimespan (int id){
        if (id == ALL_TIME.getId()) return ALL_TIME;
        else if (id == THIS_WEEK.getId()) return THIS_WEEK;
        else if (id == LAST_WEEK.getId()) return LAST_WEEK;
        else if (id == THIS_MONTH.getId()) return THIS_MONTH;
        else if (id == LAST_MONTH.getId()) return LAST_MONTH;
        else return null;
    }
}
