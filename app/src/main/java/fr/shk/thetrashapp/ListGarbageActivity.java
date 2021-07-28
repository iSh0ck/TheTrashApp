package fr.shk.thetrashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListGarbageActivity extends AppCompatActivity {

    private ArrayList<String> liste;
    private ListView list_view;
    AdapterPerso adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_garbage);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        list_view = findViewById(R.id.list_view_garbage);
        liste = new ArrayList<>();
        adapter = new AdapterPerso(this, liste);
        list_view.setAdapter(adapter);

        CheckBox vert = findViewById(R.id.checkbox_garbage_verte);
        CheckBox bleu = findViewById(R.id.checkbox_garbage_bleue);

        // Cas pour la poubelle bleue
        if (getIntent().getIntExtra("poubelle", -1) == -1) {
            showAllGarbage();
        } else if (getIntent().getIntExtra("poubelle", -1) == 0) {
            vert.setVisibility(View.INVISIBLE);
            bleu.setVisibility(View.INVISIBLE);
            showBlueGarbage();
        } else {
            vert.setVisibility(View.INVISIBLE);
            bleu.setVisibility(View.INVISIBLE);
            showGreenGarbage();
        }

        // Événement au clic sur le bouton retour
        Button btn_retour = (Button) findViewById(R.id.btn_garbage_back);
        btn_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!bleu.isChecked()) {
                        liste.clear();
                    }
                    showGreenGarbage();
                }
            }
        });

        bleu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!vert.isChecked()) {
                        liste.clear();
                    }
                    showBlueGarbage();
                }
            }
        });
    }

    private void showAllGarbage() {
        // Instanciation de la base de données
        ClientDbHelper bdd = new ClientDbHelper(this);
        SQLiteDatabase db = bdd.getWritableDatabase();

        // Récuppérer les déchets qui sont insérer dans la bdd
        String[] colonne = {"nom", "poubelle"};
        String[] select = {};
        Cursor curseur = db.query("garbage", colonne, "", select, null, null, "nom ASC");

        if (curseur.moveToFirst()) {
            do {
                String trashColor = curseur.getString(curseur.getColumnIndexOrThrow("poubelle"));
                String garbageName = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
                liste.add(garbageName + " (" + trashColor + ")");
            } while (curseur.moveToNext());
        }

        adapter.notifyDataSetChanged();

        curseur.close();
        db.close();
        bdd.close();
    }

    private void showBlueGarbage() {
        // Instanciation de la base de données
        ClientDbHelper bdd = new ClientDbHelper(ListGarbageActivity.this);
        SQLiteDatabase db = bdd.getWritableDatabase();

        // Récuppérer les déchets qui sont insérer dans la bdd
        String[] colonne = {"nom", "poubelle"};
        String[] select = {"bleue"};
        Cursor curseur = db.query("garbage", colonne, "poubelle=?", select, null, null, "nom ASC");

        if (curseur.moveToFirst()) {
            do {
                String trashColor = curseur.getString(curseur.getColumnIndexOrThrow("poubelle"));
                String garbageName = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
                liste.add(garbageName + " (" + trashColor + ")");
            } while (curseur.moveToNext());
        }

        adapter.notifyDataSetChanged();

        curseur.close();
        db.close();
        bdd.close();
    }

    private void showGreenGarbage() {
        // Instanciation de la base de données
        ClientDbHelper bdd = new ClientDbHelper(ListGarbageActivity.this);
        SQLiteDatabase db = bdd.getWritableDatabase();

        // Récuppérer les déchets qui sont insérer dans la bdd
        String[] colonne = {"nom", "poubelle"};
        String[] select = {"verte"};
        Cursor curseur = db.query("garbage", colonne, "poubelle=?", select, null, null, "nom ASC");

        if (curseur.moveToFirst()) {
            do {
                String trashColor = curseur.getString(curseur.getColumnIndexOrThrow("poubelle"));
                String garbageName = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
                liste.add(garbageName + " (" + trashColor + ")");
            } while (curseur.moveToNext());
        }

        adapter.notifyDataSetChanged();

        curseur.close();
        db.close();
        bdd.close();
    }
}