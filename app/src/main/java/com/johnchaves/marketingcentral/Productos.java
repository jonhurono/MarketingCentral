package com.johnchaves.marketingcentral;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Productos extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> productos;
    private List<Integer> fotos;
    private MyAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);

        mRecyclerView = findViewById(R.id.recyclerView);

        productos = new ArrayList<>();
        fotos = new ArrayList<Integer>();
        adapter = new MyAdapter(this, productos, fotos);

        traerProductos();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    public Connection conexionDB(){

        Connection conexion=null;

        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String driver = Util.getProperty("db.driver",getApplicationContext());
            String url = Util.getProperty("db.url",getApplicationContext());

            Class.forName(""+driver+"").newInstance();

            conexion = DriverManager.getConnection(""+url+"");

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"SIN CONEXIÓN A BASE DE DATOS",Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    private void traerProductos() {
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'Z', @CodBod = 1");

            //List rowValues = new ArrayList();

            while(rs.next()){
                productos.add(rs.getString(1)+" - "+rs.getString(2));
                //fotos.add("http://192.168.0.18/fotoarticulo/"+rs.getString(1)+".png");

                File imgFile = new  File("http://192.168.0.18/fotoarticulo/"+rs.getString(1)+".png");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //fotos.add(myBitmap);
                fotos.add(R.drawable.icon_grocery);

            }
            /*else {
                Toast.makeText(getApplicationContext(),"Error en consulta, vuelva a intentar",Toast.LENGTH_SHORT).show();
            }*/
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
