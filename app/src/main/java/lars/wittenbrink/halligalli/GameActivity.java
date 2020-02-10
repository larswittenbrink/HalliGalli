package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
    private Player player1;
    private Player player2;

    //Deklaration der Test Variablen
    private TextView player1openText;
    private TextView player1closeText;
    private TextView player2openText;
    private TextView player2closeText;
    private ImageView player11;
    private ImageView player12;
    private ImageView player13;
    private ImageView player14;
    private ImageView player15;
    private ImageView player21;
    private ImageView player22;
    private ImageView player23;
    private ImageView player24;
    private ImageView player25;


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

        //Initialisierung anderer Variablen
         player1 = new Player("Lars");
         player2 = new Player("Justin");

        //Initialisierung der Test Variablen
        player1openText = findViewById(R.id.player1openText);
        player1closeText = findViewById(R.id.player1closeText);
        player2openText = findViewById(R.id.player2openText);
        player2closeText = findViewById(R.id.player2closeText);
        player11 = findViewById(R.id.player11);
        player12 = findViewById(R.id.player12);
        player13 = findViewById(R.id.player13);
        player14 = findViewById(R.id.player14);
        player15 = findViewById(R.id.player15);
        player21 = findViewById(R.id.player21);
        player22 = findViewById(R.id.player22);
        player23 = findViewById(R.id.player23);
        player24 = findViewById(R.id.player24);
        player25 = findViewById(R.id.player25);

         //Karten zuweisen
        distributeCards(mixCards(createCards()));
        refreshUI();

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


    public List<Card> createCards() {
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

    public List<Card> mixCards(List<Card> cards) {
        Collections.shuffle(cards);
        return cards;
    }

    public void distributeCards(List<Card> cards){
        while(!cards.isEmpty()){
            player1.addClosedCard(cards.get(cards.size()-1));
            cards.remove(cards.size()-1);
            player2.addClosedCard(cards.get(cards.size()-1));
            cards.remove(cards.size()-1);
        }
    }

    public void refreshUI(){
        if(player1.getOpenedCard()!=null)
        switch(player1.getOpenedCard().getFruitNumber()){
            case FIVE:
                player12.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
                player14.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
            case THREE:
                player15.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
                player11.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
            case ONE:
                player13.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
                break;
            case FOUR:
                player12.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
                player14.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
            case TWO:
                player11.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
                player15.setImageResource(getResIdFromAttribute(this, player1.getOpenedCard().getFruitIcon().getId()));
        }
        if(player2.getOpenedCard()!=null)
            switch(player1.getOpenedCard().getFruitNumber()){
                case FIVE:
                    player22.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                    player24.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                case THREE:
                    player25.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                    player21.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                case ONE:
                    player23.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                    break;
                case FOUR:
                    player22.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                    player24.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                case TWO:
                    player21.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
                    player25.setImageResource(getResIdFromAttribute(this, player2.getOpenedCard().getFruitIcon().getId()));
            }
        player1closeText.setText(Integer.toString(player1.getClosedStack().size()));
        player1openText.setText(Integer.toString(player1.getOpenedStack().size()));
        player2closeText.setText(Integer.toString(player2.getClosedStack().size()));
        player2openText.setText(Integer.toString(player2.getOpenedStack().size()));
    }

    //Methode, um die Ressourcen ID aus Attributen zu bekommen
    public static int getResIdFromAttribute(final Activity activity, final int attr) {
        if(attr==0)
            return 0;
        final TypedValue typedvalueattr=new TypedValue();
        activity.getTheme().resolveAttribute(attr,typedvalueattr,true);
        return typedvalueattr.resourceId;
    }
}
