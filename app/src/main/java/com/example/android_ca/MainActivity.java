package com.example.android_ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity{

    List<Integer> result = new ArrayList<>();
    Map<Integer,String> weblink = new HashMap<>();
    static Integer sum=0;
    static Integer downcount = 0;
    static String[] strings = new String[20];

    static Integer[] importpic = {R.id.importid1,R.id.importid2,R.id.importid3,R.id.importid4,R.id.importid5,R.id.importid6,
            R.id.importid7,R.id.importid8,R.id.importid9,R.id.importid10,R.id.importid11,R.id.importid12,R.id.importid13,R.id.importid14,
            R.id.importid15,R.id.importid16,R.id.importid17,R.id.importid18,R.id.importid19,R.id.importid20};
    static Integer[] buttonpic = {R.id.picbtn1,R.id.picbtn2,R.id.picbtn3,R.id.picbtn4,R.id.picbtn5,R.id.picbtn6,
            R.id.picbtn7,R.id.picbtn8,R.id.picbtn9,R.id.picbtn10,R.id.picbtn11,R.id.picbtn12,R.id.picbtn13,R.id.picbtn14,
            R.id.picbtn15,R.id.picbtn16,R.id.picbtn17,R.id.picbtn18,R.id.picbtn19,R.id.picbtn20};
    static Integer[] processpic = {R.id.process1,R.id.process2,R.id.process3,R.id.process4,R.id.process5,R.id.process6,
            R.id.process7,R.id.process8,R.id.process9,R.id.process10,R.id.process11,R.id.process12,R.id.process13,R.id.process14,
            R.id.process15,R.id.process16,R.id.process17,R.id.process18,R.id.process19,R.id.process20};
    static Integer[] successpic = {R.id.successid1,R.id.successid2,R.id.successid3,R.id.successid4,R.id.successid5,R.id.successid6,
            R.id.successid7,R.id.successid8,R.id.successid9,R.id.successid10,R.id.successid11,R.id.successid12,R.id.successid13,R.id.successid14,
            R.id.successid15,R.id.successid16,R.id.successid17,R.id.successid18,R.id.successid19,R.id.successid20};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unablePicbtn();
        checkURL();
        Btn(importpic,processpic);
        readyForPicButton();
        readyToPlay();
        backToMain();
    }

    public String getURL(){
        EditText editText=findViewById(R.id.urlLink);
        String url = editText.getText().toString();
        return url;
    }

    private static List<String> extractImageUrls(String url) {
        List<String> imageUrls = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements imgElements = document.getElementsByClass("js-search-result-thumbnail non-js-link");

            for (Element element : imgElements) {
                String imageUrl = element.childNode(17).childNode(3).attr("src");
                imageUrls.add(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrls;
    }

    public void Btn(Integer[] picint,Integer[] processint){
        Button fetch = findViewById(R.id.fetchBtn);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAllBtn();
                arrangePic(picint,processint,strings);
            }
        });
    }

    protected static boolean downloadImage(String imgURL, File destFile) {
        try {
            URL url = new URL(imgURL);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] buf = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            return false;

        }
    }

    public void sendBtnToPlay(Integer picbtn,Integer importbtn,Integer successint,TextView textView){
        Button btn = findViewById(picbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView1 = findViewById(successint);
                if(imageView1.getVisibility()== View.INVISIBLE){
                    result.add(importbtn);
                    imageView1.setVisibility(View.VISIBLE);
                    sum+=1;
                    textView.setText(String.valueOf(sum));
                }

                else{
                    result.remove(importbtn);
                    imageView1.setVisibility(View.INVISIBLE);
                    sum-=1;
                    textView.setText(String.valueOf(sum));
                }
            }
        });
    }

    public void readyForPicButton(){
        TextView textView = findViewById(R.id.showtest);
        deployButtonForPic(textView);
    }

    public void deployButtonForPic(TextView textView){
        for(int i=0;i<importpic.length;i++){
            sendBtnToPlay(buttonpic[i], importpic[i],successpic[i],textView);
        }
    }

    public void readyToPlay(){
        Button playBtn = findViewById(R.id.Play);
        playBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(result.size()==6){
                    Intent intent = new Intent(MainActivity.this, MatchActivity.class);
                    intent.putExtra("playbtn0",weblink.get(result.get(0)));
                    intent.putExtra("playbtn1",weblink.get(result.get(1)));
                    intent.putExtra("playbtn2",weblink.get(result.get(2)));
                    intent.putExtra("playbtn3",weblink.get(result.get(3)));
                    intent.putExtra("playbtn4",weblink.get(result.get(4)));
                    intent.putExtra("playbtn5",weblink.get(result.get(5)));
                    downcount=0;
                    sum=0;
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(v.getContext(), "Please make sure you have select 6 pics", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicServer.stop(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MusicServer.play(this, R.raw.selectpic);
    }

    public void backToMain(){
        Button backbotton = findViewById(R.id.reset);
        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                sum=0;
                downcount=0;
                weblink.clear();
                startActivity(intent);
                finish();
            }
        });
    }

    public void unablePicbtn(){
        for(int i =0;i<buttonpic.length;i++){
            findViewById(buttonpic[i]).setEnabled(false);
        }
        findViewById(R.id.Play).setEnabled(false);
        findViewById(R.id.fetchBtn).setEnabled(false);
    }
    public void enablePicbtn(){
        for(int i =0;i<buttonpic.length;i++){
            findViewById(buttonpic[i]).setEnabled(true);
        }
    }

    public static List<String> scrapeImageUrls(String websiteUrl) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        Document document = Jsoup.connect(websiteUrl).get();
        String title = document.title();
        Elements imageElements = document.select("img");

        for (Element imageElement : imageElements) {
            String imageUrl = imageElement.absUrl("src");
            if (isImageFile(imageUrl)) {
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }

    public static boolean isImageFile(String imageUrl) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png"};

        for (String extension : imageExtensions) {
            if (imageUrl.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public void arrangePic(Integer[] picint,Integer[] processint,String[] strings1){
        for(int i =0;i<picint.length;i++){
            String destFilename = UUID.randomUUID().toString() + strings1[i].substring(strings1[i].lastIndexOf("."));
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File destFile = new File(dir, destFilename);
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean download = downloadImage(strings1[finalI],destFile);
                    if(download){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = BitmapFactory.decodeFile(destFile.getAbsolutePath());
                                ImageView imageView = findViewById(picint[finalI]);
                                imageView.setImageBitmap(bitmap);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                TextView textView = findViewById(R.id.downloadcount);
                                downcount+=1;
                                textView.setText(String.valueOf(downcount));

                                TextView textView1 = findViewById(processint[finalI]);
                                textView1.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    public void initAllBtn(){
        enablePicbtn();
        downcount=0;
        findViewById(R.id.Play).setEnabled(true);
    }

    public void checkURL(){
        Button checkbtn = findViewById(R.id.checklink);
        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.fetchBtn).setEnabled(true);
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        String url = getURL();
                        if(url.equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(view.getContext(),"Please enter a valid URL!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            try {
                                List<String> stringList = scrapeImageUrls(url);
                                Collections.shuffle(stringList);
                                if(stringList.size()<=20){
                                    throw new RuntimeException();
                                }
                                for(int i = 0; i<20;i++){
                                    strings[i]=stringList.get(i);
                                }
                                Collections.shuffle(Arrays.asList(strings));
                                for(int i=0;i<importpic.length;i++){
                                    weblink.put(importpic[i],strings[i]);}
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(view.getContext(),"URL is valid. Please click FETCH!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(view.getContext(),"HTML parsing is invalid", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            catch (RuntimeException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(view.getContext(),"The number of valid pic is less than 20.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });
    }

}
