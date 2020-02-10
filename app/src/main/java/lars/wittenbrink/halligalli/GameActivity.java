package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    //Deklaration der View Variablen
    private ImageButton imageButtonBell;

    //Deklaration der SharedPreferences
    private SharedPreferences sharedPreferences;

    //Deklaration des GesturesDetector
    private GestureDetectorCompat gestureDetectorCompat;

    //Deklaration des Vibrators und des MediaPlayers
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    //Deklaration anderer Variablen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Initialisierung der View Variablen
        imageButtonBell = findViewById(R.id.imageButtonBell);

        //Initialisierung der SharedPreferences
        sharedPreferences = getSharedPreferences("settings", 0);

        //Initialisierung des GesturesDetector
        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());

        //Initialisierung des Vibrators und des MediaPlayers
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.ring);

        //Setzen der Listener für die View Objekte
        imageButtonBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (sharedPreferences.getBoolean("sounds", false))
                    mediaPlayer.start();
                if (sharedPreferences.getBoolean("vibrations", false))
                    vibrator.vibrate(100);
                Toast toast = Toast.makeText(getApplicationContext(), "Difficulty: " + sharedPreferences.getInt("difficulty", 0), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.exit)
                .setMessage(R.string.exitMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int[] point = new int[2];
            findViewById(R.id.layoutGame).getLocationOnScreen(point);
            int y = point[1];
            int length = findViewById(R.id.layoutGame).getHeight();
                if(e1.getY() > y && e1.getY() < y+(length/4) && e2.getY() > y && e2.getY() < y+(length/4)){
                    (Toast.makeText(getApplicationContext(), "aa", Toast.LENGTH_SHORT)).show();

                }
                else if(e1.getY() > (y + 3*(length/4)) && e1.getY() < y+length && e2.getY() > (y + 3*(length/4)) && e2.getY() < y+length){
                    (Toast.makeText(getApplicationContext(), "bb", Toast.LENGTH_SHORT)).show();

                }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }



    public static List<Card> createCards() {
        List<Card> cards = new LinkedList<>();
        for (FruitIcon fruitIcon : FruitIcon.values()) {
            for (FruitNumber fruitNumber : FruitNumber.values()) {
                for (int i = 0; i < fruitNumber.getNumber(); i++) {
                    cards.add(new Card(fruitIcon, fruitNumber));
                }
            }
        }
        return cards;
    }

    public static List<Card> mixCards(List<Card> cards) {
        Collections.shuffle(cards);
        return cards;
    }

    public static void main(String[] args) {
        List<Card> cards = mixCards(createCards());
        for (Card card : cards) {
            System.out.println(card.getFruitIcon() + " " + card.getFruitNumber());
        }
        System.out.println(cards.size());
    }
}
