package com.cimcorp.parser.pblRouteParser;

import java.util.Calendar;
import java.util.TimeZone;

public class LogRow {

    private Calendar dateTime;
    private double excelDateTime;
    private long pickOrderId;
    private boolean begin;
    private boolean end;
    private String orderDescription = null;
    private long priority = 0;


    public LogRow(){}

    public LogRow(String str){

        String dt = str.substring(0,str.indexOf(","));
        parseExcelDateTime(dt);

        int i = str.indexOf("pickorderid=\"") + 13;
        int j = str.indexOf("\"",i);
        this.pickOrderId = Integer.parseInt(str.substring(i,j));
        this.begin = str.contains("JMS_MESSAGE_RECEIVED");
        this.end = str.contains("JMS_MESSAGE_SENT");
        if (this.begin) {
            i = str.indexOf("orderdescription=\"") + 18;
            j = str.indexOf("\"",i);
            this.orderDescription = str.substring(i,j);
           i = str.indexOf("priority=\"") + 10;
           j = str.indexOf("\"",i);
           this.priority = Long.parseLong(str.substring(i,j));
           System.out.println();
        }

    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public LogRow setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public double getExcelDateTime() {
        return excelDateTime;
    }

    public LogRow setExcelDateTime(double excelDateTime) {
        this.excelDateTime = excelDateTime;
        return this;
    }

    public long getPickOrderId() {
        return pickOrderId;
    }

    public LogRow setPickOrderId(long pickOrderId) {
        this.pickOrderId = pickOrderId;
        return this;
    }

    public boolean isBegin() {
        return begin;
    }

    public LogRow setBegin(boolean begin) {
        this.begin = begin;
        return this;
    }

    public boolean isEnd() {
        return end;
    }

    public LogRow setEnd(boolean end) {
        this.end = end;
        return this;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public LogRow setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
        return this;
    }


    private void parseExcelDateTime(String s) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.setLenient(false);

        Calendar excelRef = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        excelRef.clear();
        excelRef.setLenient(false);
        excelRef.set(1900,00,01);
        long excelRefDays = (excelRef.getTimeInMillis() * -1) / 1000 / 60 / 60 / 24;

        int year = Integer.parseInt(s.substring(0,4));
        int month = Integer.parseInt(s.substring(5,7)) - 1;
        int day = Integer.parseInt(s.substring(8,10));

        int hour = Integer.parseInt(s.substring(11,13));
        int minute = Integer.parseInt(s.substring(14,16));
        int second = Integer.parseInt(s.substring(17,19));
        int milliSecond = 0;

        c.set(year,month,day,hour,minute,second);
        long k = c.getTimeInMillis() + (long)milliSecond;
        c.setTimeInMillis(k);
        this.setDateTime(c);

        double excelTime = (long) k;
        excelTime = (excelTime / 1000 / 60 / 60 / 24) + excelRefDays + 2;

        double timeInMillis = ((hour*1000*60*60)+(minute*1000*60)+(second*1000)+milliSecond) / (1000*60*60*24);
        excelTime = excelTime + timeInMillis;
        this.setExcelDateTime(excelTime);
    }

    public long getPriority() {
        return priority;
    }

    public LogRow setPriority(long priority) {
        this.priority = priority;
        return this;
    }

    /*private String[] buildCsvLine(LogRow lr, String[] orderDescription) {

        String[] s = new String[Array.getLength(orderDescription)];

        s[0] = Double.toString(lr.getExcelDateTime());
        s[1] = Integer.toString(lr.getDateTime().get(Calendar.HOUR_OF_DAY));
        s[2] = Integer.toString(lr.getDateTime().get(Calendar.MINUTE));

        for (int i = 3; i < Array.getLength(orderDescription); i++){
            if (lr.getColumnLabel().get(i-3).equals(lr.getDescription())) {
                // s[i] = Integer.toString(lr.getCrates());
            }
            else {
                s[i] = "0";
            }
        }

        return s;
    }*/

    /*@Override
    public List<String[]> toCSV(List<?> l) {

        List<LogRow> lr = (List<LogRow>) l;
        List<String[]> ls = new ArrayList<>();

        String[] orderDescription = new String[3 + lr.get(0).getColumnLabel().size()];
        orderDescription[0] = "DateTime";
        orderDescription[1] = "Hour";
        orderDescription[2] = "Minute";
        for (int i = 3; i < Array.getLength(orderDescription); i++){
            orderDescription[i] = lr.get(0).getColumnLabel().get(i-3);
        }

        ls.add(orderDescription);
        for (LogRow p : lr) {
            ls.add(buildCsvLine(p, orderDescription));
        }

        return ls;
    }*/


}