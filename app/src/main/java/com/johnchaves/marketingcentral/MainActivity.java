package com.johnchaves.marketingcentral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText user, password;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.txtUser);
        password = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == password.getImeActionId()) {
                    login();
                    handled = true;
                }
                return handled;
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public Connection conexionDB(){
        Connection conexion=null;
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.11;databaseName=Terra;user=Movil;password=Mv2021;");
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"SIN CONEXIÓN A BASE DE DATOS",Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    private void login() {
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_Login @user = '"+user.getText().toString()+"', " +
                    "@pass = '"+password.getText().toString()+"' ");

            if(rs.next()){
                Toast.makeText(getApplicationContext(),"CREDENCIALES CORRECTAS",Toast.LENGTH_SHORT).show();
                user.setText(null);
                password.setText(null);
                startActivity(new Intent(MainActivity.this, Formulario.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"CREDENCIALES INCORRECTAS",Toast.LENGTH_SHORT).show();
                password.setText(null);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}