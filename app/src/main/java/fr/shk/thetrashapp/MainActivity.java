package fr.shk.thetrashapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    static final int REQUEST_VALUE_ADD_TRASH = 1;
    static final int REQUEST_VALUE_PARAM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Événement lors du clic sur le bouton d'ajout d'une poubelle
        Button btn_add_trash = findViewById(R.id.btn_main_add_trash);
        btn_add_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addTrash = new Intent(MainActivity.this, AddTrashActivity.class);
                startActivityForResult(intent_addTrash, REQUEST_VALUE_ADD_TRASH);
            }
        });

        // Création de la base de données
        ClientDbHelper bdd = new ClientDbHelper(this);
        SQLiteDatabase db = bdd.getWritableDatabase();

        // Création de notre liste
        listView = findViewById(R.id.listview_trash);
        listItems = new ArrayList<>();
        // Création de notre adaptateur simple
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        // Récupération du nombre de poubelles
        Cursor cursor = db.rawQuery("SELECT * FROM trash", null, null);
        int nbTrash = cursor.getCount();
        cursor.close();

        if (nbTrash != 0) {
            // Notre liste est alors visible
            listView.setVisibility(View.VISIBLE);
            // Récuppérer les poubelles qui sont insérer dans la bdd
            String[] colonne = {"color"};
            String[] select = {};
            Cursor curseur = db.query("trash", colonne, "", select, null, null, "color ASC");

            if (curseur.moveToFirst()) {
                do {
                    String trashColor = curseur.getString(curseur.getColumnIndexOrThrow("color"));
                    listItems.add(getString(R.string.poubelle) + " " + trashColor);
                } while (curseur.moveToNext());
            }

            curseur.close();
        } else {
            listView.setVisibility(View.INVISIBLE);
        }

        db.close();
        bdd.close();

        // Événement lors du clic d'un élément de la liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListGarbageActivity.class);
                intent.putExtra("poubelle", position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Code de l'activitée dont on arrive
        if (requestCode == 1) {
            // Etat de retour
            if (resultCode == RESULT_OK) {
                // Réccupération de la couleur de la poubelle rentrée
                String value = data.getStringExtra("color");
                Toast.makeText(this, "La poubelle " + value + " a été ajoutée.", Toast.LENGTH_SHORT).show();

                updateList();
            }
        }

        // Traitement pour l'activité des paramètres
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                updateList();
            }
        }
    }

    // Affichage du menu dans notre activité
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menu, menu);
        return true;
    }

    // Événements sur les clics du menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Récupération de l'ID du bouton cliqué
        switch (item.getItemId()) {
            case R.id.menuAddTrash:
            case R.id.menuAddTrashAlways:
                // Traitement pour le clic sur l'ajout d'une poubelle
                Intent intent_addTrash = new Intent(MainActivity.this, AddTrashActivity.class);
                startActivity(intent_addTrash);
                break;
            case R.id.menuListGarbage:
                // Traitement pour le clic sur l'ajout de déchets
                Intent intent_addGarbage = new Intent(MainActivity.this, ListGarbageActivity.class);
                startActivity(intent_addGarbage);
                break;
            case R.id.menuParams:
                // Traitement pour le clic sur les params
                Intent intent_params = new Intent(MainActivity.this, ParametreActivity.class);
                startActivityForResult(intent_params, REQUEST_VALUE_PARAM);
                break;
            case R.id.menuQuit:
                // Traitement pour le clic sur le bouton quitter
                System.exit(0);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateList() {
        // Instanciation de la base de données
        ClientDbHelper bdd = new ClientDbHelper(this);
        SQLiteDatabase db = bdd.getWritableDatabase();

        ListView listView = findViewById(R.id.listview_trash);

        // Notre liste est alors visible
        listView.setVisibility(View.VISIBLE);
        listItems.clear();

        // Récuppérer les poubelles qui sont insérer dans la bdd
        String[] colonne = {"color"};
        String[] select = {};
        Cursor curseur = db.query("trash", colonne, "", select, null, null, "color ASC");

        if (curseur.moveToFirst()) {
            do {
                String trashColor = curseur.getString(curseur.getColumnIndexOrThrow("color"));
                listItems.add(getString(R.string.poubelle) + " " + trashColor);
            } while (curseur.moveToNext());
        }

        adapter.notifyDataSetChanged();

        curseur.close();
        db.close();
        bdd.close();
    }
}