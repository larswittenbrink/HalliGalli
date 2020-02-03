package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    //Deklaration der View Variablen
    private Switch switchSounds;
    private Switch switchVibrations;
    private SeekBar seekBarDifficulty;

    //Deklaration der SharedPreferences und des SharedPreferences.Editor
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Initialisierung der View Variablen
        switchSounds = findViewById(R.id.switchSounds);
        switchVibrations = findViewById(R.id.switchVibrations);
        seekBarDifficulty = findViewById(R.id.seekBarDifficulty);

        //Initialisierung der SharedPreferences und des SharedPreferences.Editor
        sharedPreferences = getSharedPreferences("settings", 0);
        sharedPreferencesEditor = sharedPreferences.edit();

        //Setzen der View Objekte auf die gespeicherten Werte
        switchSounds.setChecked(sharedPreferences.getBoolean("sounds", false));
        switchVibrations.setChecked(sharedPreferences.getBoolean("vibrations", false));
        seekBarDifficulty.setProgress(sharedPreferences.getInt("difficulty", 0));

        //Setzen der Listener für die View Objekte
        switchSounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferencesEditor.putBoolean("sounds", isChecked);
            }
        });
        switchVibrations.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferencesEditor.putBoolean("vibrations", isChecked);
            }
        }));
        seekBarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharedPreferencesEditor.putInt("difficulty", progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        //Speichern der SharedPreferences
        sharedPreferencesEditor.commit();
    }
}
