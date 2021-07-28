package fr.shk.thetrashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ParametreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Événement pour le clic sur le bouton retour
        Button btn_back = findViewById(R.id.btn_param_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // Événement pour le clic sur le bouton effacer
        Button btn_erase = findViewById(R.id.btn_param_erase);
        btn_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instanciation de la base de données
                ClientDbHelper bdd = new ClientDbHelper(ParametreActivity.this);
                SQLiteDatabase db = bdd.getWritableDatabase();

                // Récuppérer les poubelles qui sont insérer dans la bdd
                db.execSQL("DELETE FROM trash;");

                db.close();
                bdd.close();

                setResult(RESULT_OK);
                finish();
            }
        });
    }
}