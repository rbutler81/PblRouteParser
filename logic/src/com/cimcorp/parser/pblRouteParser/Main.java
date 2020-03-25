package com.cimcorp.parser.pblRouteParser;

import com.custom.ArgNotFoundException;
import csvUtils.CSVUtil;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    // static methods /////////////////////////////////////////////////////////////////////////

    // setup static variables ///////////////////////////////////////////////////////////////////
    static final String PATH = Paths.get(".").toAbsolutePath().normalize().toString() + "\\logs\\";

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {

        final String OUTPUT_FILE = Paths.get(".").toAbsolutePath().normalize().toString() + "\\output.csv";
        final String COUNT_FILE = Paths.get(".").toAbsolutePath().normalize().toString() + "\\count.csv";

        // Check for LOG file ///////////////////////////////////////////////////////////////
        File folder = new File(PATH);
        File[] listOfFiles = folder.listFiles();

        PickingDays pickDays = new PickingDays();

        for (File f : listOfFiles) {

            String file = PATH + f.getName();

            List<LogRow> lr = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                for (String line; (line = br.readLine()) != null; ) {
                    if (line.contains("JMS_MESSAGE") && line.contains("pickorderid=") && !line.contains("EXCEPTION") && !line.contains("DUPLICATE_PICK_ORDER")) {
                        lr.add(new LogRow(line));
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Convert log rows into pbl pick orders - add to list
            List<PblPickOrder> ppo = new ArrayList<>();
            for (LogRow l : lr) {
                new PblPickOrder(l, ppo);
            }

            // group pbl pick order into pbl routes
            List<PblRoute> pblr = new ArrayList<>();
            for (PblPickOrder p : ppo) {
                new PblRoute(p, pblr);
            }

            for (PblRoute p : pblr) {
                pickDays.add(p);
            }

            System.out.println();
        }

        for (PickingDay p : pickDays.getPickDay()) {
            p.findDuplicateRoutes();
        }

        // Go through routes with 0 start date - consolidate picking times across days

        // find entry with pickdate = 0 (pickorders with only end times)
        int pickDateEqualsZeroIndex = 0;
        for (int i = 0; i < pickDays.getPickDay().size(); i++) {
            if (pickDays.getPickDay().get(i).getPickDate() == 0) {
                pickDateEqualsZeroIndex = i;
            }
        }

        for (int i = 0; i < pickDays.getPickDay().get(pickDateEqualsZeroIndex).getRoutes().size(); i++) {

            double endTime = pickDays.getPickDay().get(pickDateEqualsZeroIndex).getRoutes().get(i).getEndTime();
            int previousDay = (int) endTime - 1;
            int pickDayIndex = pickDays.getIndex(previousDay);
            if (pickDayIndex > -1) {

                long poid = pickDays.getPickDay().get(pickDateEqualsZeroIndex).getRoutes().get(i).getPickOrders().get(0).getPickOrderId();
                pickDays.getPickDay().get(pickDayIndex).findRouteByPickingIdAndUpdateEndTime(poid, endTime);
                System.out.println();

            } else {
                pickDays.getUnmatchedRoutes().add(pickDays.getPickDay().get(pickDateEqualsZeroIndex).getRoutes().get(i));
            }

        }

        // remove the picking day = 0
        pickDays.getPickDay().remove(pickDateEqualsZeroIndex);

        // update route start / end times based on the pickorder start / end times
        for (PickingDay pd : pickDays.getPickDay()) {
            for (PblRoute pr : pd.getRoutes()) {
                pr.updateStartAndEndTimes();
            }
        }

        ;

        CSVUtil.writeObject(pickDays.getPickDay().get(12).generateActivePickOrders(), COUNT_FILE, ",");

        try {
            CSVUtil.writeObject(pickDays.getPickDay(), OUTPUT_FILE, ",");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        System.out.println();
    }
}
