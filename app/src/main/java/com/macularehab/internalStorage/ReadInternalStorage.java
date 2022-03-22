package com.macularehab.internalStorage;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ReadInternalStorage {

    public ReadInternalStorage() {
        //Do Nothing
    }

    public HashMap<String, Object> read(Context context, String filename) {

        HashMap<String, Object> map = new HashMap<>();

        try {
            File file = new File(context.getFilesDir(), filename);
            FileInputStream fileInputStream = new FileInputStream(file);
            //openFileInput(filenameProfessionalPatientList);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fileInputStream.read()) != -1) {
                temp.append((char) a);
            }

            String fin = temp.toString();
            fileInputStream.close();

            Gson gson = new Gson();
            map = gson.fromJson(fin, HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
