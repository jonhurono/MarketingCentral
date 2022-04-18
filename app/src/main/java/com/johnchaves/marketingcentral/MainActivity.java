package com.johnchaves.marketingcentral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText user, password;
    Button btnLogin, btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user        = findViewById(R.id.txtUser);
        password    = findViewById(R.id.txtPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        btnTest     = findViewById(R.id.btnTest);

        password.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == password.getImeActionId()) {
                login();
                handled = true;
            }
            return handled;
        });
        btnLogin.setOnClickListener(view -> login());

        /*btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });*/
    }

    /*public void test(){
        try {
            String url = Util.getProperty("db.fullurl",getApplicationContext());

            user.setText(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

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