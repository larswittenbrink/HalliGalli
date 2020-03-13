package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Deklaration der View Variablen
    private Button buttonPlay;
    private Button buttonSettings;

    //Deklaration der SharedPreferences
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialisierung der SharedPreferences
        sharedPreferences = getSharedPreferences("settings", 0);

        setTheme(sharedPreferences.getInt("theme", R.style.MainTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisierung der View Variablen
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonSettings = findViewById(R.id.buttonSettings);

        //Setzen der Listener für die View Objekte
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, GameInitActivity.class));
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }
}
