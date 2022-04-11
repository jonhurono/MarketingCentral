package com.johnchaves.marketingcentral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Formulario extends Activity {

    EditText Rut_Cli, Nom_Cli, Ape_Pat, Ape_Mat, Email, Oferta, Nro_Wsp;
    Button btnCreate, btnClear, btnUpdate, btnDelete;
    Switch recibeOferta;
    TableRow botonera1, botonera2;

    @Override
    protected void onCreate (Bundle savedInstace){
        super.onCreate(savedInstace);
        setContentView(R.layout.formulario);

        Rut_Cli = (EditText) findViewById(R.id.RUT_Cli);
        Nom_Cli = (EditText) findViewById(R.id.Nom_Cli);
        Ape_Pat = (EditText) findViewById(R.id.Ape_Pat_Cli);
        Ape_Mat = (EditText) findViewById(R.id.Ape_Mat_Cli);
        Email   = (EditText) findViewById(R.id.Email);
        Oferta  = (EditText) findViewById(R.id.Oferta_Wsp);
        Nro_Wsp = (EditText) findViewById(R.id.Nro_Wsp);

        recibeOferta = (Switch) findViewById(R.id.switch1);
        botonera1    = (TableRow) findViewById(R.id.row1);
        botonera2    = (TableRow) findViewById(R.id.row2);

        btnCreate   = (Button) findViewById(R.id.btnCreate);
        btnClear    = (Button) findViewById(R.id.btnClear);
        btnUpdate   = (Button) findViewById(R.id.btnUpdate);
        btnDelete   = (Button) findViewById(R.id.btnDelete);
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

    private void readCliente() {
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_iudc_ClientesMarketing @Modo = 'C'," +
                    "@RUT_Cli = '"+Rut_Cli.getText().toString()+"' ");

            if(rs.next()){
                Toast.makeText(getApplicationContext(),"CLIENTE ENCONTRADO",Toast.LENGTH_SHORT).show();
                Rut_Cli.setText(rs.getString(1));
                Nom_Cli.setText(rs.getString(2));
                Ape_Pat.setText(rs.getString(3));
                Ape_Mat.setText(rs.getString(4));
                Email.setText(rs.getString(6));
                Nro_Wsp.setText(rs.getString(8));
            }
            else{
                Toast.makeText(getApplicationContext(),"CLIENTE NO ENCONTRADO",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void createCliente() {
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_iudc_ClientesMarketing @Modo = 'I'," +
                    "@RUT_Cli = '"+Rut_Cli.getText().toString()+"' ");

            if(rs.next()){
                Toast.makeText(getApplicationContext(),"CLIENTE CREADO",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"ERROR AL GUARDAR CLIENTE",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCliente() {
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_iudc_ClientesMarketing @Modo = 'U'," +
                    "@RUT_Cli = '"+Rut_Cli.getText().toString()+"' ");

            if(rs.next()){
                Toast.makeText(getApplicationContext(),"CLIENTE ACTUALIZADO",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"ERROR AL ACTUALIZAR",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCliente() {
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_iudc_ClientesMarketing @Modo = 'D'," +
                    "@RUT_Cli = '"+Rut_Cli.getText().toString()+"' ");

            if(rs.next()){
                Toast.makeText(getApplicationContext(),"CLIENTE ELIMINADO",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"ERROR EN ELIMINACIÓN",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
