package com.johnchaves.marketingcentral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.text.Normalizer;

public class Menu extends Activity {

    CardView cv_clientes,cv_productos, cv_informes, cv_ajustes;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        cv_clientes = findViewById(R.id.cv_clientes);
        cv_productos = findViewById(R.id.cv_productos);
        cv_informes = findViewById(R.id.cv_informes);
        cv_ajustes = findViewById(R.id.cv_ajustes);

        cv_clientes.setOnClickListener(view -> startActivity(new Intent(Menu.this, Formulario.class)));

    }
}
