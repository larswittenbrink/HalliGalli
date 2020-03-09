package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import lars.wittenbrink.halligalli.game.GameController;
import lars.wittenbrink.halligalli.game.user.*;

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

    //Deklaration eines requestCode für die GameInit Activity
    private static final int INPUT_ACTIVITY_RESULT = 149;

    //Deklaration anderer Variablen
    private List<User> users = new LinkedList<>();
    private GameController gameController;

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

// TODO: 08.03.2020 Was wenn Users weg sind? 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialisierung der SharedPreferences
        sharedPreferences = getSharedPreferences("settings", 0);

        if(sharedPreferences.getBoolean("darkmode", false)){
            setTheme(R.style.MainThemeDark);
        }else{
            setTheme(R.style.MainTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Initialisierung der View Variablen
        imageButtonBell = findViewById(R.id.imageButtonBell);

        //Initialisierung des GestureDetector
        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());

        //Initialisierung des Vibrators und des MediaPlayers
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.ring);

        if(getIntent().getBooleanExtra("botPlayer1", false)){
            users.add(new Bot(getIntent().getStringExtra("namePlayer1"), sharedPreferences.getInt("difficulty", 50)));
        } else {
            users.add(new User(getIntent().getStringExtra("namePlayer1")));
        }
        if(getIntent().getBooleanExtra("botPlayer2", false)){
            users.add(new Bot(getIntent().getStringExtra("namePlayer2"), sharedPreferences.getInt("difficulty", 50)));
        } else {
            users.add(new User(getIntent().getStringExtra("namePlayer2")));
        }

        //Initialisierung anderer Variablen
        gameController = new GameController(users);

        /*//Initialisierung der Test Variablen
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
*/
        //Karten zuweisen
        //refreshUI();


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
                    vibrator.vibrate(200);

                gameController.move(users.get(0));
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
            if (e1.getY() > y && e1.getY() < y + (length / 4) && e2.getY() > y && e2.getY() < y + (length / 4)) {
                if(users.get(1) instanceof User){
                    gameController.move(users.get(1)); 
                }
                refreshUI();
            } else if (e1.getY() > (y + 3 * (length / 4)) && e1.getY() < y + length && e2.getY() > (y + 3 * (length / 4)) && e2.getY() < y + length) {
                if(users.get(0) instanceof User){
                    gameController.move(users.get(0));
                }
                refreshUI();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    public void refreshUI() {
        /*player11.setImageDrawable(null);
        player12.setImageDrawable(null);
        player13.setImageDrawable(null);
        player14.setImageDrawable(null);
        player15.setImageDrawable(null);
        player21.setImageDrawable(null);
        player22.setImageDrawable(null);
        player23.setImageDrawable(null);
        player24.setImageDrawable(null);
        player25.setImageDrawable(null);

        if (player1.getOpenedCard() != null)
            switch (player1.getOpenedCard().getFruitNumber()) {
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
        if (player2.getOpenedCard() != null)
            switch (player2.getOpenedCard().getFruitNumber()) {
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
        player2openText.setText(Integer.toString(player2.getOpenedStack().size()));*/
    }

    //Methode, um die Ressourcen ID aus Attributen zu bekommen
    public static int getResIdFromAttribute(final Activity activity, final int attr) {
        if (attr == 0)
            return 0;
        final TypedValue typedvalueattr = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedvalueattr, true);
        return typedvalueattr.resourceId;
    }

}
