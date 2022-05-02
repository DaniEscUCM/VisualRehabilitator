package com.macularehab.internalStorage;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { //API 19
                inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1);
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);

            int character;
            StringBuilder stringBuilder = new StringBuilder();
            while ((character = reader.read()) != -1) {
                stringBuilder.append((char) character);
            }

            String fin = stringBuilder.toString();
            fileInputStream.close();

            Gson gson = new Gson();
            map = gson.fromJson(fin, HashMap.class);

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return map;
    }
}
