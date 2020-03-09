package lars.wittenbrink.halligalli;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class GameInitActivity extends AppCompatActivity {

    //Deklaration der View Variablen
    private EditText editTextPlayer1;
    private EditText editTextPlayer2;
    private CheckBox checkBoxPlayer1;
    private CheckBox checkBoxPlayer2;
    private Button buttonConfirm;

    //Deklaration der SharedPreferences
    private SharedPreferences sharedPreferences;

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
        setContentView(R.layout.activity_game_init);

        //Initialisierung der View Variablen
        editTextPlayer1 = findViewById(R.id.editTextPlayer1);
        editTextPlayer2 = findViewById(R.id.editTextPlayer2);
        checkBoxPlayer1 = findViewById(R.id.checkBoxPlayer1);
        checkBoxPlayer2 = findViewById(R.id.checkBoxPlayer2);
        buttonConfirm = findViewById(R.id.buttonConfirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameInitActivity.this, GameActivity.class);
                intent.putExtra("namePlayer1", (editTextPlayer1.getText().toString().isEmpty()) ?  getResources().getString(R.string.player1) : editTextPlayer1.getText().toString());
                intent.putExtra("namePlayer2", (editTextPlayer2.getText().toString().isEmpty()) ?  getResources().getString(R.string.player2) : editTextPlayer2.getText().toString());
                intent.putExtra("botPlayer1", checkBoxPlayer1.isChecked());
                intent.putExtra("botPlayer2", checkBoxPlayer2.isChecked());
                startActivity(intent);
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
}
