package com.cimcorp.parser.pblRouteParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickingDay {

    private int pickDate = 0;
    private List<PblRoute> routes = new ArrayList<>();

    public PickingDay() {
    }

    public PickingDay(int day, PblRoute route) {
        this.pickDate = day;
        this.routes = new ArrayList<>();
        this.routes.add(route);
    }

    public void addRoute(PblRoute r) {
        int day = (int)r.getStartTime();
        this.routes.add(r);
        System.out.println();
    }

    public int getPickDate() {
        return pickDate;
    }

    public PickingDay setPickDate(int pickDate) {
        this.pickDate = pickDate;
        return this;
    }

    public List<PblRoute> getRoutes() {
        return routes;
    }

    public PickingDay setRoutes(List<PblRoute> routes) {
        this.routes = routes;
        return this;
    }
}
