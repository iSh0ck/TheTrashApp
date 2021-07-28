package fr.shk.thetrashapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPerso extends BaseAdapter {

    private Context context;
    private ArrayList<String> liste;
    // private String [] valeurs;

    // Classe permettant de générer à la volée des composants à partir d'un layout
    private static LayoutInflater inflater = null;

    public AdapterPerso(Context context, ArrayList<String> liste/*,  String[] valeurs*/) {
        this.context = context;
        this.liste = liste;

        // Instancie le générateur de composant à partir du layout
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.liste.size();
    }

    @Override
    public Object getItem(int position) {
        return this.liste.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View previous = convertView; // Vue existante
        View retour = previous;

        if (retour == null) {
            retour = inflater.inflate(R.layout.listitemlayout, null);
        }

        // Mise à jour du label avec la valeur du tableau
        TextView textView = (TextView) retour.findViewById(R.id.text_view);
        textView.setText(this.liste.get(position));

        // Mise à jour de l'image avec la valeur du tableau
        ImageView imageView = (ImageView) retour.findViewById(R.id.image_view_garbage);
        imageView.setImageResource(R.drawable.pb_bleue);

        return retour;
    }
}
