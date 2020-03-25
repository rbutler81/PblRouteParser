package com.cimcorp.parser.pblRouteParser;

import java.util.ArrayList;
import java.util.List;

public class PblRoute {

    private double startTime = 0;
    private double endTime = 0;
    private double duration = 0;
    private String route = "";
    private List<PblPickOrder> pickOrders = new ArrayList<>();
    private List<Long> priority = new ArrayList<>();

    public PblRoute() {
        this.route = "";
    }

    public PblRoute(PblPickOrder ppo, List<PblRoute> pr) {

        boolean found = false;
        for (PblRoute p: pr) {

            System.out.println();
            if (ppo.getRoute().equals(p.getRoute()) && !ppo.getRoute().equals("")) {
                found = true;
                p.getPickOrders().add(ppo);
                if (p.getStartTime() > ppo.getStartTime()){
                    p.setStartTime(ppo.getStartTime());
                }
                if (p.getEndTime() < ppo.getEndTime()){
                    p.setEndTime(ppo.getEndTime());
                }
                p.setDuration(p.getEndTime() - p.getStartTime());
                if (p.getDuration() < 0) p.setDuration(0);

            }
            if (found) break;
        }

        if (!found) {
            PblRoute pblr = new PblRoute();
            pblr.setStartTime(ppo.getStartTime());
            pblr.setEndTime(ppo.getEndTime());
            pblr.setDuration(ppo.getEndTime() - ppo.getStartTime());
            pblr.setRoute(ppo.getRoute());
            pblr.getPickOrders().add(ppo);
            pblr.getPriority().add(ppo.getPriority());
            pr.add(pblr);
            System.out.println();
        }
    }

    public double getStartTime() {
        return startTime;
    }

    public void updateStartAndEndTimes() {
        for (PblPickOrder po : this.pickOrders) {
            if ((po.getStartTime() < this.startTime) && (po.getStartTime() != 0)) this.startTime = po.getStartTime();
            if ((po.getEndTime() > this.endTime) && (po.getEndTime() != 0)) this.endTime = po.getEndTime();
            this.duration = this.endTime - this.startTime;
        }
    }

    public boolean findPickOrderAndUpdateEndtime(long pickOrderId, double endTime) {
        boolean found = false;
        for (PblPickOrder ppo : this.pickOrders) {
            if (ppo.getPickOrderId() == pickOrderId) {
                found = true;
                ppo.setEndTimeAndUpdateDuration(endTime);
                /*if (endTime > this.endTime) {
                    this.endTime = endTime;
                    this.duration = this.endTime - this.startTime;
                }*/
            }
            if (found) break;
        }
        return found;
    }

    public PblRoute setStartTime(double startTime) {
        this.startTime = startTime;
        return this;
    }

    public double getEndTime() {
        return endTime;
    }

    public PblRoute setEndTime(double endTime) {
        this.endTime = endTime;
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public PblRoute setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public String getRoute() {
        return route;
    }

    public PblRoute setRoute(String route) {
        this.route = route;
        return this;
    }

    public List<PblPickOrder> getPickOrders() {
        return pickOrders;
    }

    public PblRoute setPickOrders(List<PblPickOrder> pickOrders) {
        this.pickOrders = pickOrders;
        return this;
    }

    public List<Long> getPriority() {
        return priority;
    }

    public PblRoute setPriority(List<Long> priority) {
        this.priority = priority;
        return this;
    }
}


