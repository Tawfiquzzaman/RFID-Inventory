package com.example.Util.Connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {

    public static String readFile(File fileEvents) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
        }
        String result = text.toString();
        return result;
    }

    public static void writeFile(File file, String value) throws IOException {

        File gpxfile = new File(file, "SerialNo");
        FileWriter writer = new FileWriter(gpxfile);

        try {
            String[] ArrforValue = value.split("-");
            ArrforValue[1] = String.valueOf(Integer.parseInt(ArrforValue[1]) + 1);

            String SNno;
            int countArrforValue = 0;
            int nArrforValue = Integer.parseInt(ArrforValue[1]);
            while (nArrforValue != 0) {
                nArrforValue = nArrforValue / 10;
                ++countArrforValue;
            }
            if (countArrforValue == 1) {
                SNno = ArrforValue[0]+ "-00000" + ArrforValue[1];
            } else if (countArrforValue == 2) {
                SNno = ArrforValue[0]+ "-0000" + ArrforValue[1];
            } else if (countArrforValue == 3) {
                SNno = ArrforValue[0]+ "-000" + ArrforValue[1];
            } else if (countArrforValue == 4) {
                SNno = ArrforValue[0]+ "-00" + ArrforValue[1];
            } else if (countArrforValue == 5) {
                SNno = ArrforValue[0]+ "-0" + ArrforValue[1];
            } else {
                SNno = ArrforValue[0]+ "-" + ArrforValue[1];
            }
            writer.append(SNno);

        } finally {
            writer.flush();
            writer.close();
        }
    }
}
