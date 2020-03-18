package com.cimcorp.parser.pblRouteParser;

import com.custom.ArgNotFoundException;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {

    // static methods /////////////////////////////////////////////////////////////////////////

    // setup static variables ///////////////////////////////////////////////////////////////////
    static final String PATH = Paths.get(".").toAbsolutePath().normalize().toString() + "\\logs\\";

    public static void main(String[] args) {

        // Check for LOG file ///////////////////////////////////////////////////////////////
        if (Array.getLength(args) < 1){
            throw new ArgNotFoundException("<Log File>");
        }

        File folder = new File(PATH);
        File[] listOfFiles = folder.listFiles();


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
                if (p.getPriority().size() > 1) {
                    System.out.println();
                }
            }
        }
        System.out.println();



    }
}
