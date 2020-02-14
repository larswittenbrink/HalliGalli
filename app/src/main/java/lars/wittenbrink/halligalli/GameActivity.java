package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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

        //Initialisierung des GestureDetector
        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());

        //Initialisierung des Vibrators und des MediaPlayers
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.ring);

        //Initialisierung anderer Variablen

        //configuratePlayers();
        player1 = new Player("lars", false);
        player2 = new Player("hubddd", true);

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
        refreshUI();
    player1.setOnTurn(true);

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

            }
        });


    }

    // TODO: 11.02.2020 Methode beenden
    public void configuratePlayers() {

        //LinearLayout für die Zeilen
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding((int) getResources().getDimension(R.dimen.AlertDialogPadding), 0, (int) getResources().getDimension(R.dimen.AlertDialogPadding), 0);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //EditText für den Namen
        final EditText input = new EditText(this);
        input.setHint(R.string.name);
        input.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.addView(input);

        final LinearLayout layout = new LinearLayout(this);
        layout.setPadding((int) getResources().getDimension(R.dimen.AlertDialogPadding), 0, (int) getResources().getDimension(R.dimen.AlertDialogPadding), 0);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final TextView textView = new TextView(this);
        textView.setText(R.string.bot);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1f));
        linearLayout.addView(textView);

        final CheckBox checkBox = new CheckBox(this);
        //checkBox.setText(R.string.bot);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,0f));
        checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        layout.addView(checkBox);
        linearLayout.addView(layout);


        new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.player1)
                .setView(linearLayout)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().trim().equals(""))
                            player1 = new Player(getResources().getString(R.string.player1), false);
                        else
                            player1 = new Player(input.getText().toString().trim(), false);
                        Toast.makeText(getApplicationContext(),player1.getName(),Toast.LENGTH_SHORT).show();
                    }
                })
                .show();


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
            if (e1.getY() > y && e1.getY() < y + (length / 4) && e2.getY() > y && e2.getY() < y + (length / 4)) {
                turn(player2);
                refreshUI();
            } else if (e1.getY() > (y + 3 * (length / 4)) && e1.getY() < y + length && e2.getY() > (y + 3 * (length / 4)) && e2.getY() < y + length) {
                turn(player1);
                refreshUI();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void turn(Player player) {

        if(player.isOnTurn()) {
            player.move();
            //wenn der andere keine karten mehr hat nicht wechseln
            player1.setOnTurn(!player1.isOnTurn());
            player2.setOnTurn(!player2.isOnTurn());
        }
    }


    public void refreshUI() {
        player11.setImageDrawable(null);
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
        player2openText.setText(Integer.toString(player2.getOpenedStack().size()));
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
