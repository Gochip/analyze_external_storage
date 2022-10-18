package com.gochip.analyzeexternalstorage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String tutorials[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        ArrayAdapter<String> listViewData;
        listViewData = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String stringText= ((TextView)view).getText().toString();
                stringText = stringText.replace("file: ", "");
                stringText = stringText.replace("dir: ", "");

                File fileOpen = new File(stringText);
                if (fileOpen.isDirectory()) {
                    Toast.makeText(MainActivity.this, "Is a directory", Toast.LENGTH_LONG).show();
                } else {
                    Scanner myReader = null;
                    try {
                        String dataFile = "";
                        myReader = new Scanner(fileOpen);
                        while (myReader.hasNextLine()) {
                            dataFile = myReader.nextLine();
                        }
                        Toast.makeText(MainActivity.this, dataFile, Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            List<String> paths = analyzeExternalStorage();
            if (paths != null) {
                for (String path: paths) {
                    File file = new File(path);
                    if (file.isDirectory()) {
                        listViewData.add("dir: " + path);
                    } else if(file.isFile()) {
                        listViewData.add("file: " + path);
                    } else {
                        listViewData.add(path);
                    }
                }
                Log.d("AnalyzeExternalStorage", "Success");
                listView.setAdapter(listViewData);
            } else {
                Log.d("AnalyzeExternalStorage", "paths es nulo");
            }
        } catch (IOException ex) {
            Log.d("AnalyzeExternalStorage", "File error: " + ex.getMessage());
        }
    }


    private List<String> analyzeExternalStorage() throws IOException {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        List<String> pathToRegularFiles = new ArrayList<String>();

        for (File currentFile : externalStorageDirectory.listFiles()) {
            pathToRegularFiles.add(currentFile.getAbsolutePath());
        }
        return pathToRegularFiles;
    }
}