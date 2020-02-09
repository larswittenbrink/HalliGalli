package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
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
                if(sharedPreferences.getBoolean("sounds", false))
                    mediaPlayer.start();
                if(sharedPreferences.getBoolean("vibrations", false))
                    vibrator.vibrate(100);

                Toast toast=Toast.makeText(getApplicationContext(),"Difficulty: " + sharedPreferences.getInt("difficulty", 0),Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.exit)
                .setMessage(R.string.exitMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public static List<Card> createCards(){
        List<Card> cards = new LinkedList<>();
        for (FruitIcon fruitIcon : FruitIcon.values()) {
            for (FruitNumber fruitNumber:FruitNumber.values()) {
                for (int i = 0; i < fruitNumber.getNumber(); i++) {
                    cards.add(new Card(fruitIcon, fruitNumber));
                }
            }
        }
        return cards;
    }

    public static List<Card> mixCards(List<Card> cards){
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
