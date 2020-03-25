package com.cimcorp.parser.pblRouteParser;

import csvUtils.CSVWriter;

import java.util.ArrayList;
import java.util.List;

public class PickStartEndEvent implements CSVWriter {

    private boolean start = false;
    private boolean end = false;
    private double time = 0.0;

    public PickStartEndEvent() {
    }

    public PickStartEndEvent(boolean start, double time) {
        this.start = start;
        this.end = !start;
        this.time = time;
    }

    public boolean isStart() {
        return start;
    }

    public PickStartEndEvent setStart(boolean start) {
        this.start = start;
        return this;
    }

    public boolean isEnd() {
        return end;
    }

    public PickStartEndEvent setEnd(boolean end) {
        this.end = end;
        return this;
    }

    public double getTime() {
        return time;
    }

    public PickStartEndEvent setTime(double time) {
        this.time = time;
        return this;
    }

    @Override
    public List<String[]> toCSV(List<?> l) {

        List<PickStartEndEvent> p = (List<PickStartEndEvent>) l;
        List<String[]> ret = new ArrayList<>();

        int i = 0;
        String s = "";
        for (PickStartEndEvent pe : p) {
            if (pe.isStart()) {
                i++;
                s = "Start";
            }
           else {
               i--;
               s = "End";
            }
            ret.add(new String[]{String.valueOf(pe.getTime()), s, String.valueOf(i)});
        }

        return ret;
    }
}
