package com.cimcorp.parser.pblRouteParser;

import csvUtils.CSVUtil;
import csvUtils.CSVWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickingDay implements CSVWriter {

    private int pickDate = 0;
    private List<PblRoute> routes = new ArrayList<>();

    public PickingDay() {
    }

    public PickingDay(int day, PblRoute route) {
        this.pickDate = day;
        this.routes = new ArrayList<>();
        this.routes.add(route);
    }

    /*public void removeBadRoutes() {
        for (int i = 0; i < this.routes.size(); i++) {
            if (this.routes.get(i))
        }
    }*/

    public void findDuplicateRoutes(){
        for (int i = 0; i < this.routes.size(); i++) {
            for (int j = 0; j < this.routes.size(); j++) {
                if ((i != j) && (this.routes.get(i).getRoute().equals(this.routes.get(j).getRoute())) && (!this.routes.get(i).getRoute().equals(""))) {
                    System.out.println();
                }
            }
        }
    }

    public boolean findRouteByPickingIdAndUpdateEndTime(long pickOrderId, double endTime) {
        boolean found = false;
        for (PblRoute route : this.routes) {
            found = route.findPickOrderAndUpdateEndtime(pickOrderId, endTime);
            if (found) break;
        }
        return found;
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

    @Override
    public List<String[]> toCSV(List<?> l) {

        List<PickingDay> pd = (List<PickingDay>) l;
        List<String[]> ret = new ArrayList<>();

        int numberOfColumns = pd.size() + 1;

        // make a list of all the routes from all the pick days
        List<String> routes = new ArrayList<>();
        for (PickingDay p : pd) {
            for (PblRoute pr : p.getRoutes()) {
                if (!routes.contains(pr.getRoute())) {
                    routes.add(pr.getRoute());
                }
            }
        }
        int numberOfRows = routes.size() + 1;

        // header row
        String[] row = new String[numberOfColumns];
        row[0] = "";
        for (int i = 0; i < numberOfColumns - 1; i++) {
            row[i+1] = String.valueOf(pd.get(i).getPickDate());
        }
        ret.add(row);

        // loop through each day's routes and add start times
        for (String str : routes) {
            row = new String[numberOfColumns];
            int i = 0;
            row[i] = str;
            i++;
            for (PickingDay p : pd) {
                for (PblRoute pr : p.getRoutes()) {
                    if (str.equals(pr.getRoute())) {
                        row[i] = String.valueOf(pr.getStartTime());
                    }
                }
                if (row[i] == null) {
                    row[i] = "";
                }
                i++;
            }
            ret.add(row);
        }

        return ret;
    }
}
