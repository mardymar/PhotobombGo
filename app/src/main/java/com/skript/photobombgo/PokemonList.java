package com.skript.photobombgo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.skript.photobombgo.R;

import java.util.Arrays;

public class PokemonList extends ListActivity {

    ArrayAdapter<String> adapter;
    final String[] names = {"Absol", "Aerodactyl", "Aggron", "Alakazam", "Arcanine", "Arceus", "Articuno", "Blastoise", "Blaziken", "Braviary", "Bulbasaur", "Celebi", "Chansey", "Charizard", "Charmander", "Charmeleon", "Cubone", "Cyndaquil", "Darkai", "Deoxys", "Dialga", "Dragonair", "Dragonite", "Dratini", "Empoleon", "Entei", "Espeon", "Feraligatr", "Flareon", "Flygon", "Garchomp", "Gastly", "Gengar", "Giratina", "Glaceon", "Groudon", "Growlithe", "Gyarados", "Haunter", "Haxorus", "Houndoom", "Hydreigon", "Ivysaur", "Jolteon", "Kadabra", "Kyogre", "Kyurem", "Lapras", "Latias", "Latios", "Lucario", "Lugia", "Luxray", "Machamp", "Metagross", "Mew", "Mewtwo", "Milotic", "Moltres", "Ninetales", "Onix", "Palkia", "Pidgeot", "Pikachu", "Pokeball", "Quilava", "Raichu", "Raikou", "Rapidash", "Rayquaza", "Reshiram", "Rhydon", "Salamence", "Samurott", "Sceptile", "Scizor", "Scyther", "Serperior", "Snorlax", "Squirtle", "Steelix", "Suicune", "Swampert", "Torterra", "Typhlosion", "Tyranitar", "Umbreon", "Vaporeon", "Venusaur", "Wartortle", "Zapdos", "Zekrom", "Zoroark"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pokemon_list);

        Arrays.sort(names);

        adapter = new ArrayAdapter<>(this, R.layout.selector_list_item, names);
        setListAdapter(adapter);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String itemValue = (String) l.getItemAtPosition(position);

        Intent intent = new Intent(this, PictureGallery.class);
        intent.putExtra("name", itemValue);
        startActivity(intent);

    }

}
