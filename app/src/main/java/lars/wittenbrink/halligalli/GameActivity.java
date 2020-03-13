package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import lars.wittenbrink.halligalli.game.GameController;
import lars.wittenbrink.halligalli.game.cards.Card;
import lars.wittenbrink.halligalli.game.cards.FruitIcon;
import lars.wittenbrink.halligalli.game.cards.FruitNumber;
import lars.wittenbrink.halligalli.game.user.*;

public class GameActivity extends AppCompatActivity {

    //Deklaration der View Variablen
    private ImageButton imageButtonBell;
    private FrameLayout openedCardPlayer1;
    private FrameLayout openedCardPlayer2;
    private FrameLayout closedCardPlayer1;
    private FrameLayout closedCardPlayer2;

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


// TODO: 08.03.2020 Was wenn Users weg sind? 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialisierung der SharedPreferences
        sharedPreferences = getSharedPreferences("settings", 0);

        setTheme(sharedPreferences.getInt("theme", R.style.MainTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Initialisierung der View Variablen
        imageButtonBell = findViewById(R.id.imageButtonBell);
        openedCardPlayer1 = findViewById(R.id.openedCardPlayer1);
        openedCardPlayer2 = findViewById(R.id.openedCardPlayer2);
        closedCardPlayer1 = findViewById(R.id.closedCardPlayer1);
        closedCardPlayer2 = findViewById(R.id.closedCardPlayer2);

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
                    vibrator.vibrate(200);

                gameController.move(users.get(0));
                refreshUI();
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
                    refreshUI();
                }
                refreshUI();
            } else if (e1.getY() > (y + 3 * (length / 4)) && e1.getY() < y + length && e2.getY() > (y + 3 * (length / 4)) && e2.getY() < y + length) {
                if(users.get(0) instanceof User){
                    gameController.move(users.get(0));
                    refreshUI();
                }
                refreshUI();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    public void refreshUI() {
        openedCardPlayer1.removeAllViews();
        openedCardPlayer2.removeAllViews();
        closedCardPlayer1.removeAllViews();
        closedCardPlayer2.removeAllViews();

        if(users.get(0) != null ){
                openedCardPlayer1.addView(createCard(users.get(0).getOpenedCards().peekFirst(), true, openedCardPlayer1.getHeight()));
                closedCardPlayer1.addView(createCard(users.get(0).getClosedCards().peekFirst(), false, closedCardPlayer1.getHeight()));
        }
        if(users.get(0) != null ){
            openedCardPlayer2.addView(createCard(users.get(1).getOpenedCards().peekFirst(), true, openedCardPlayer2.getHeight()));
            closedCardPlayer2.addView(createCard(users.get(1).getClosedCards().peekFirst(), false, closedCardPlayer2.getHeight()));
        }
    }

    public View createCard(Card card, boolean opened, int height) {
        // View
        View view = getLayoutInflater().inflate(R.layout.card,null);

        if(card == null){
            return view;
        }

        //Layout
        ConstraintLayout layout = view.findViewById(R.id.cardLayout);
        layout.setLayoutParams(new ConstraintLayout.LayoutParams(height/3*2, height));

        //Shape für den Hintergrund
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(getColorFromAttribute(this, R.attr.colorMain));
        shape.setStroke((int)(height*0.006), getColorFromAttribute(this, R.attr.colorBorder));
        shape.setCornerRadius(height * 0.08F);
        layout.setBackground(shape);

        if(opened) {
            ImageView[] fruit = new ImageView[5];
            fruit[0] = view.findViewById(R.id.fruit1);
            fruit[1] = view.findViewById(R.id.fruit2);
            fruit[2] = view.findViewById(R.id.fruit3);
            fruit[3] = view.findViewById(R.id.fruit4);
            fruit[4] = view.findViewById(R.id.fruit5);

            for (ImageView imageView : fruit) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)(imageView.getLayoutParams());
                params.height = (int) (height / 4.5);
                params.width = (int) (height / 4.5);
                params.leftMargin = (int) (height*0.04);
                params.rightMargin = (int) (height*0.04);
                params.topMargin = (int) (height*0.04);
                params.bottomMargin = (int) (height*0.04);
            }

            switch (card.getFruitNumber()) {
                case FIVE:
                    fruit[0].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                    fruit[4].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                case THREE:
                    fruit[1].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                    fruit[3].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                case ONE:
                    fruit[2].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                    break;
                case FOUR:
                    fruit[0].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                    fruit[4].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                case TWO:
                    fruit[1].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                    fruit[3].setImageResource(getResIdFromAttribute(this, card.getFruitIcon().getId()));
                    break;
            }
        } else{
            layout.setForeground(this.getDrawable(R.drawable.card_background));
        }
        return view;
    }

    //Methode, um die Ressourcen ID aus Attributen zu bekommen
    public static int getResIdFromAttribute(final Activity activity, final int attr) {
        if (attr == 0)
            return 0;
        final TypedValue typedvalueattr = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedvalueattr, true);
        return typedvalueattr.resourceId;
    }

    public int getColorFromAttribute(final Activity activity, int attr){
        if (attr == 0)
            return 0;
        TypedValue typedvalueattr = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedvalueattr, true);
        return typedvalueattr.data;
    }

}
