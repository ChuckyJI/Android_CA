package com.example.android_ca;

import static com.example.android_ca.MainActivity.downloadImage;
import static com.example.android_ca.MainActivity.strings;
import static com.example.android_ca.MainActivity.sum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MatchActivity extends AppCompatActivity{

    private SoundPool soundpool;
    private final HashMap<Integer,Integer> soundmap=new HashMap<>();
    static Integer count = 0;
    static Boolean player = false; // false is player 1 and true is player 2
    static Integer scorePlayer1 = 0;
    static Integer scorePLayer2 = 0;
    private TextView countdown;
    private MyCount mc;
    final int[] benchmark = {0,0,0,0,0};
    final int[][] origin = {{0},{0},{0},{0},{0},{0}};
    static String weblink0="";
    static String weblink1="";
    static String weblink2="";
    static String weblink3="";
    static String weblink4="";
    static String weblink5="";

    static Integer[] importpic = {R.id.importid1,R.id.importid2,R.id.importid3,R.id.importid4,R.id.importid5,R.id.importid6,
            R.id.importid7,R.id.importid8,R.id.importid9,R.id.importid10,R.id.importid11,R.id.importid12};
    static Integer[] importpicOriginal = {R.id.importid1,R.id.importid2,R.id.importid3,R.id.importid4,R.id.importid5,R.id.importid6,
            R.id.importid7,R.id.importid8,R.id.importid9,R.id.importid10,R.id.importid11,R.id.importid12};

    static Integer[] buttonpic = {R.id.picbtn1,R.id.picbtn2,R.id.picbtn3,R.id.picbtn4,R.id.picbtn5,R.id.picbtn6,
            R.id.picbtn7,R.id.picbtn8,R.id.picbtn9,R.id.picbtn10,R.id.picbtn11,R.id.picbtn12};

    static Integer[] borderpic = {R.id.border1,R.id.border2,R.id.border3,R.id.border4,R.id.border5,R.id.border6,
            R.id.border7,R.id.border8,R.id.border9,R.id.border10,R.id.border11,R.id.border12};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
        initEnvi();
        soundLib();
        setupTimer();
        initComparation();
        compareGame();
        backToMain();
    }

    public int[] showpic(String imgURL,int picint,int picint2){
        String destFilename = UUID.randomUUID().toString() + imgURL.substring(imgURL.lastIndexOf("."));
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File destFile = new File(dir, destFilename);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                boolean download = downloadImage(imgURL,destFile);
//                if(download){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeFile(destFile.getAbsolutePath());
                            ImageView imageView = findViewById(picint);
//                            imageView.setImageBitmap(bitmap);
                            Picasso.get().load(imgURL).into(imageView);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            ImageView imageView1 = findViewById(picint2);
//                            imageView1.setImageBitmap(bitmap);
                            Picasso.get().load(imgURL).into(imageView1);
                            imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    });
//                }
            }
        }).start();
        return new int[]{picint,picint2};
    }

    public void invisible(){
        for(int i =0;i<importpic.length;i++){
            findViewById(importpic[i]).setVisibility(ImageView.INVISIBLE);
        }
    }

    public boolean isEqual(int[] a,int[] b){
        int count = 0;
        int[] c = {0,0};
        c[0]=a[0];
        c[1]=a[1];
        for(int data: c){
            for(int data1:b){
                if(data==data1){
                    count+=1;
                }
            }
        }

        if(count==2){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isEqualForAll(int[][] origin, int[] benchmark){
        int[] c = {0,0};
        c[0]=benchmark[0];
        c[1]=benchmark[1];
        boolean temp = false;
        for(int i=0;i<origin.length;i++){
            temp = isEqual(benchmark,origin[i]);
            if (temp){
                break;
            }
        }
        return temp;
    }

    public void comparePic(int[] benchmark,int[][] origin,int importId,int buttonid,int borderid){
        Button picBtn = findViewById(buttonid);
        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageViewself = findViewById(importId);
                imageViewself.setVisibility(ImageView.VISIBLE);
                if(benchmark[0]==0){
                    benchmark[0] = imageViewself.getId();
                    benchmark[2] = importId;
                    benchmark[3] = buttonid;
                    benchmark[4] = borderid;
                }
                else{
                    benchmark[1] = imageViewself.getId();
                    if(!isEqualForAll(origin,benchmark)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    ImageView imageViewother = findViewById(benchmark[2]);
                                    imageViewother.setVisibility(ImageView.INVISIBLE);
                                    imageViewself.setVisibility(ImageView.INVISIBLE);
                                    setscore(false);
                                    soundpool.play(soundmap.get(2), 1,1,0,0,1);

                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                finally{
                                    benchmark[0]=0;
                                    benchmark[1]=0;
                                    benchmark[2]=0;
                                    benchmark[3]=0;
                                    benchmark[4]=0;
                                }
                            }
                        }).start();
                    }
                    else{
                        count +=1;
                        TextView textView = findViewById(R.id.correctPairs);
                        textView.setText(String.valueOf(count));
                        picBtn.setEnabled(false);
                        Button test1 = findViewById(benchmark[3]);
                        test1.setEnabled(false);

                        findViewById(benchmark[4]).setVisibility(View.INVISIBLE);
                        findViewById(borderid).setVisibility(View.INVISIBLE);

                        setscore(true);

                        soundpool.play(soundmap.get(1), 1,1,0,0,1);

                        benchmark[0]=0;
                        benchmark[1]=0;
                        benchmark[2]=0;
                        benchmark[3]=0;
                        benchmark[4]=0;
                        if (count == 6) {
                            releaseResult();
                            onDestroy();
                        }
                    }
                }
            }
        });
    }

    public void setscore(boolean result){
        if(!result){
            if(player){
                scorePlayer1 += 0;
                scorePLayer2 -= 50;
                player = false;
                TextView textView = findViewById(R.id.player1);
                textView.setText(String.valueOf(scorePlayer1));
                TextView textView1 = findViewById(R.id.player2);
                textView1.setText(String.valueOf(scorePLayer2));
                initEnvi();
            }
            else{
                scorePlayer1 -= 50;
                scorePLayer2 += 0;
                player = true;
                TextView textView = findViewById(R.id.player1);
                textView.setText(String.valueOf(scorePlayer1));
                TextView textView1 = findViewById(R.id.player2);
                textView1.setText(String.valueOf(scorePLayer2));
                initEnviInverse();
            }
        }
        else{
            if(player){
                scorePlayer1 -= 0;
                scorePLayer2 += 100;
                player = true;
                TextView textView2 = findViewById(R.id.player1);
                textView2.setText(String.valueOf(scorePlayer1));
                TextView textView1 = findViewById(R.id.player2);
                textView1.setText(String.valueOf(scorePLayer2));
            }
            else{
                scorePlayer1 += 100;
                scorePLayer2 -= 0;
                player = false;
                TextView textView2 = findViewById(R.id.player1);
                textView2.setText(String.valueOf(scorePlayer1));
                TextView textView1 = findViewById(R.id.player2);
                textView1.setText(String.valueOf(scorePLayer2));
            }
        }
    }

    public void releaseResult(){
        TextView textView = findViewById(R.id.correctPairs);
        int result = Integer.parseInt(textView.getText().toString());
        if (scorePlayer1>scorePLayer2){
            Toast toast;
            toast = Toast.makeText(this.getApplicationContext(),"Congratulations! Player 1 wins!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations!")
                    .setMessage("The winner is Player 1 ! \nThe score is "+scorePlayer1+".")
                    .setPositiveButton("Play again!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(MatchActivity.this,MatchActivity.class);
                            resetParamWhenAgain(intent);
                            startActivity(intent);
                            finish();
                        }})
                    .setNegativeButton("OK",null) .show();
        }
        else if(scorePlayer1<scorePLayer2){
            Toast toast;
            toast = Toast.makeText(this.getApplicationContext(),"Congratulations! Player 2 wins!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations!")
                    .setMessage("The winner is Player 2 ! \nThe score is "+scorePLayer2+".")
                    .setPositiveButton("Play again!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(MatchActivity.this,MatchActivity.class);
                            resetParamWhenAgain(intent);
                            startActivity(intent);
                            finish();
                        }})
                    .setNegativeButton("OK",null) .show();
        }
        else{
            Toast toast;
            toast = Toast.makeText(this.getApplicationContext(),"We are friends!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations!")
                    .setMessage("The winner is Player 1 and Player 2 ! \nWe are friends!")
                    .setPositiveButton("Play again!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(MatchActivity.this,MatchActivity.class);
                            resetParamWhenAgain(intent);
                            startActivity(intent);
                            finish();
                        }})
                    .setNegativeButton("OK",null) .show();
        }
        soundpool.play(soundmap.get(4), 1,1,0,0,1);
    }
    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            Date date = new Date(millisUntilFinished);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            String str = sdf.format(date);
            System.out.println(str);
            countdown.setText(millisUntilFinished / 1000 +"S");
            if(millisUntilFinished / 1000 == 10){
                soundpool.play(soundmap.get(3), 1,1,0,0,1);
            }
            if(millisUntilFinished / 1000 == 5){
                soundpool.play(soundmap.get(3), 1,1,0,0,1);
            }
            if(millisUntilFinished / 1000 == 0){
                releaseResult();
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onFinish() {
            countdown.setText("0S");
            for(int i=0;i<buttonpic.length;i++){
                findViewById(buttonpic[i]).setEnabled(false);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mc.cancel();
    }

    public void initComparation(){
        weblink0 = getIntent().getStringExtra("playbtn0");
        weblink1 = getIntent().getStringExtra("playbtn1");
        weblink2 = getIntent().getStringExtra("playbtn2");
        weblink3 = getIntent().getStringExtra("playbtn3");
        weblink4 = getIntent().getStringExtra("playbtn4");
        weblink5 = getIntent().getStringExtra("playbtn5");


        Collections.shuffle(Arrays.asList(importpic));

        int[] pair1 = showpic(weblink0,importpic[0],importpic[1]);
        int[] pair2 = showpic(weblink1,importpic[2],importpic[3]);
        int[] pair3 = showpic(weblink2,importpic[4],importpic[5]);
        int[] pair4 = showpic(weblink3,importpic[6],importpic[7]);
        int[] pair5 = showpic(weblink4,importpic[8],importpic[9]);
        int[] pair6 = showpic(weblink5,importpic[10],importpic[11]);

        origin[0]=pair1;
        origin[1]=pair2;
        origin[2]=pair3;
        origin[3]=pair4;
        origin[4]=pair5;
        origin[5]=pair6;

        invisible();
    }

    public void setupTimer(){
        countdown = findViewById(R.id.timer);
        mc = new MyCount(101000, 1000);
        mc.start();
    }

    public void compareGame(){
        for(int i = 0;i<buttonpic.length;i++){
            comparePic(benchmark,origin,importpicOriginal[i],buttonpic[i],borderpic[i]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicServer.stop(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MusicServer.play(this, R.raw.game);
    }

    public void soundLib(){
        soundpool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        soundmap.put(1,soundpool.load(this, R.raw.right,1));
        soundmap.put(2,soundpool.load(this, R.raw.wrong,1));
        soundmap.put(3,soundpool.load(this, R.raw.timer,1));
        soundmap.put(4,soundpool.load(this, R.raw.win,1));
    }

    public void backToMain(){
        Button backbotton = findViewById(R.id.back);
        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MatchActivity.this,MainActivity.class);
                sum=0;
                scorePlayer1=0;
                scorePLayer2=0;
                player = false;
                for (Integer integer : borderpic) {
                    findViewById(integer).setVisibility(View.VISIBLE);
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void resetParamWhenAgain(Intent intent){
        sum=0;
        scorePlayer1=0;
        scorePLayer2=0;
        player = false;
        intent.putExtra("playbtn0",weblink0);
        intent.putExtra("playbtn1",weblink1);
        intent.putExtra("playbtn2",weblink2);
        intent.putExtra("playbtn3",weblink3);
        intent.putExtra("playbtn4",weblink4);
        intent.putExtra("playbtn5",weblink5);
    }

    public void initEnvi(){
        TextView textViewPlayer1 = findViewById(R.id.player1Name);
        textViewPlayer1.setTextColor(Color.RED);
        TextView textViewPlayer2 = findViewById(R.id.player2Name);
        textViewPlayer2.setTextColor(Color.GRAY);
        TextView textViewPlayer1Score = findViewById(R.id.player1);
        textViewPlayer1Score.setTextColor(Color.RED);
        TextView textViewPlayer2Score = findViewById(R.id.player2);
        textViewPlayer2Score.setTextColor(Color.GRAY);
    }

    public void initEnviInverse(){
        TextView textViewPlayer1 = findViewById(R.id.player1Name);
        textViewPlayer1.setTextColor(Color.GRAY);
        TextView textViewPlayer2 = findViewById(R.id.player2Name);
        textViewPlayer2.setTextColor(Color.RED);
        TextView textViewPlayer1Score = findViewById(R.id.player1);
        textViewPlayer1Score.setTextColor(Color.GRAY);
        TextView textViewPlayer2Score = findViewById(R.id.player2);
        textViewPlayer2Score.setTextColor(Color.RED);
    }
}

