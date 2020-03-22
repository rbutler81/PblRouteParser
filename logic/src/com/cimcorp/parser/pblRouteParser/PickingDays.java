package com.cimcorp.parser.pblRouteParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PickingDays {

    private List<PickingDay> pickDay = new ArrayList();
    private List<PblRoute> unmatchedRoutes = new ArrayList<>();

    public PickingDays() {
    }

    public int getIndex(int day) {

        int r = -1;
        for (int i = 0; i < this.pickDay.size(); i++) {
            if (day == this.getPickDay().get(i).getPickDate()) {
                r = i;
            }
            if (r > 0) break;
        }

        return r;
    }

    public int containsDate(int date) {

        int r = -1;

        for (int i = 0; i < this.pickDay.size(); i++) {

            int k = (int) this.pickDay.get(i).getPickDate();
            if (k == date) {
                r = i;
            }
            if (r > -1) break;
        }
        return r;
    }

    public void add(PblRoute p) {

        int i = this.containsDate((int) p.getStartTime());
        if (i > -1) {
            this.pickDay.get(i).addRoute(p);
        }
        else {
            this.pickDay.add(new PickingDay((int)p.getStartTime(), p));
            System.out.println();

        }
    }

    public List<PickingDay> getPickDay() {
        return pickDay;
    }

    public PickingDays setPickDay(List<PickingDay> pickDay) {
        this.pickDay = pickDay;
        return this;
    }
}
