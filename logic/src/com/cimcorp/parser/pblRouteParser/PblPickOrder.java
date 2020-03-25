package com.cimcorp.parser.pblRouteParser;

import java.util.List;

public class PblPickOrder {

    private double startTime = 0;
    private double endTime = 0;
    private double duration = 0;
    private long pickOrderId = 0;
    private long priority = 0;
    private String route = "";
    private String delivery = "";
    private boolean detailsBad = false;

    public PblPickOrder(LogRow lr) {

        this.route = "";
        this.delivery = "";

        if (lr.isBegin()) {
            this.startTime = lr.getExcelDateTime();
        }
        else if (lr.isEnd()){
            this.endTime = lr.getExcelDateTime();
        }

        this.setPickOrderId(lr.getPickOrderId());

        if (lr.getOrderDescription() != null){
            int i = lr.getOrderDescription().indexOf("Run: ") + 5;
            int j = lr.getOrderDescription().indexOf(",");
            if ((i != j) && (i > 0) && (j > i)) {
                this.route = lr.getOrderDescription().substring(i,j);
            } else {
                this.route = lr.getOrderDescription();
                this.detailsBad = true;
            }

            i = lr.getOrderDescription().indexOf("Delivery: ");
            if (i >= 0) {
                i = i + 10;
                this.delivery = lr.getOrderDescription().substring(i);
            } else {
                this.delivery = "";
                this.detailsBad = true;
            }
            this.priority = lr.getPriority();
        }


    }

    public PblPickOrder(LogRow lr, List<PblPickOrder> ppo) {

        boolean found = false;
        for(PblPickOrder p: ppo) {
            if (p.getPickOrderId() == lr.getPickOrderId()) {
                found = true;
                if (lr.isBegin()) {
                    p.setStartTime(lr.getExcelDateTime());
                }
                else if (lr.isEnd()) {
                    p.setEndTime(lr.getExcelDateTime());
                }
                p.setDuration(p.getEndTime() - p.getStartTime());
            }
            if (found) break;
        }

        if (!found) {
            ppo.add(new PblPickOrder(lr));
        }

    }

    public PblPickOrder setEndTimeAndUpdateDuration(double endTime) {
        this.endTime = endTime;
        this.duration = this.endTime - this.startTime;
        return this;
    }

    public double getStartTime() {
        return startTime;
    }

    public PblPickOrder setStartTime(double startTime) {
        this.startTime = startTime;
        return this;
    }

    public double getEndTime() {
        return endTime;
    }

    public PblPickOrder setEndTime(double endTime) {
        this.endTime = endTime;
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public PblPickOrder setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public long getPickOrderId() {
        return pickOrderId;
    }

    public PblPickOrder setPickOrderId(long pickOrderId) {
        this.pickOrderId = pickOrderId;
        return this;
    }

    public String getRoute() {
        return route;
    }

    public PblPickOrder setRoute(String route) {
        this.route = route;
        return this;
    }

    public String getDelivery() {
        return delivery;
    }

    public PblPickOrder setDelivery(String delivery) {
        this.delivery = delivery;
        return this;
    }

    public long getPriority() {
        return priority;
    }

    public PblPickOrder setPriority(long priority) {
        this.priority = priority;
        return this;
    }

    public boolean isDetailsBad() {
        return detailsBad;
    }

    public PblPickOrder setDetailsBad(boolean detailsBad) {
        this.detailsBad = detailsBad;
        return this;
    }
}
