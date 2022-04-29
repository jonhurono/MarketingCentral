package com.johnchaves.marketingcentral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.text.Normalizer;

public class Menu extends Activity {

    CardView cv_clientes,cv_productos, cv_informes, cv_ajustes;
    ImageView icon_client,icon_grocery, icon_report, icon_settings;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        cv_clientes = findViewById(R.id.cv_clientes);
        cv_productos = findViewById(R.id.cv_productos);
        cv_informes = findViewById(R.id.cv_informes);
        cv_ajustes = findViewById(R.id.cv_ajustes);
        icon_client = findViewById(R.id.clientesImg);
        icon_grocery = findViewById(R.id.productosImg);
        icon_report = findViewById(R.id.informesImg);
        icon_settings = findViewById(R.id.ajustesImg);

        cv_clientes.setOnClickListener(view -> startActivity(new Intent(Menu.this, Formulario.class)));
        cv_productos.setOnClickListener(view -> startActivity(new Intent(Menu.this, Productos.class)));
        cv_informes.setOnClickListener(view -> startActivity(new Intent(Menu.this, Informes.class)));
        cv_ajustes.setOnClickListener(view -> Toast.makeText(getApplicationContext(),"Aún no implementado",Toast.LENGTH_SHORT).show());
    }
}
