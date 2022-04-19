package com.johnchaves.marketingcentral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
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
    Button btnCreate, btnClear, btnUpdate, btnDelete, btnQuery, btnQR;
    Switch recibeOferta;
    TableRow row_wsp, botonera1, botonera2;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);

        Rut_Cli = findViewById(R.id.RUT_Cli);
        Nom_Cli = findViewById(R.id.Nom_Cli);
        Ape_Pat = findViewById(R.id.Ape_Pat_Cli);
        Ape_Mat = findViewById(R.id.Ape_Mat_Cli);
        Email   = findViewById(R.id.Email);
        Oferta  = findViewById(R.id.Oferta_Wsp);
        Nro_Wsp = findViewById(R.id.Nro_Wsp);

        recibeOferta = findViewById(R.id.switch1);
        botonera1    = findViewById(R.id.row1);
        botonera2    = findViewById(R.id.row2);
        row_wsp      = findViewById(R.id.row_wsp);

        btnQR       = findViewById(R.id.btnQR);
        btnQuery    = findViewById(R.id.btnQuery);
        btnCreate   = findViewById(R.id.btnCreate);
        btnClear    = findViewById(R.id.btnClear);
        btnUpdate   = findViewById(R.id.btnUpdate);
        btnDelete   = findViewById(R.id.btnDelete);

        recibeOferta.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                row_wsp.setVisibility(View.VISIBLE);
                Oferta.setText("1");
            } else{
                row_wsp.setVisibility(View.INVISIBLE);
                Oferta.setText("0");
            }
        });

        btnQR.setOnClickListener(view -> startActivity(new Intent(Formulario.this, CameraQR.class)));

        btnQuery.setOnClickListener(view -> readCliente());

        btnCreate.setOnClickListener(view -> createCliente());

        btnClear.setOnClickListener(view -> limpiar());

        btnUpdate.setOnClickListener(view -> updateCliente());

        btnDelete.setOnClickListener(view -> deleteCliente());
    }
    private void limpiar(){
        Rut_Cli.setText(null);
        Nom_Cli.setText(null);
        Ape_Pat.setText(null);
        Ape_Mat.setText(null);
        Email.setText(null);
        Nro_Wsp.setText(null);
    }

    public Connection conexionDB(){

        Connection conexion = null;

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
                Email.setText(rs.getString(5));
                Nro_Wsp.setText(rs.getString(7));
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
            Statement pst = conexionDB().createStatement();
            pst.executeUpdate("EXEC Sp_iudc_ClientesMarketing @Modo = 'I'," +
                    "@RUT_Cli = '" + Rut_Cli.getText().toString() + "'," +
                    "@Nom_Cli = '" + Nom_Cli.getText().toString() + "'," +
                    "@Ape_Pat_Cli = '" + Ape_Pat.getText().toString() + "'," +
                    "@Ape_Mat_Cli = '" + Ape_Mat.getText().toString() + "'," +
                    "@Email = '" + Email.getText().toString() + "'," +
                    "@Oferta_Wsp = '" + Oferta.getText().toString() + "'," +
                    "@Nro_Wsp = '" + Nro_Wsp.getText().toString() + "' ");

            sendMail();

            Toast.makeText(getApplicationContext(),"CLIENTE CREADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR EN INSERCIÓN: REVISAR LOS DATOS - "+e.getMessage(),Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCliente() {
        try{
            Statement upd = conexionDB().createStatement();
            upd.executeUpdate("EXEC Sp_iudc_ClientesMarketing @Modo = 'U'," +
                    "@RUT_Cli = '" + Rut_Cli.getText().toString() + "'," +
                    "@Nom_Cli = '" + Nom_Cli.getText().toString() + "'," +
                    "@Ape_Pat_Cli = '" + Ape_Pat.getText().toString() + "'," +
                    "@Ape_Mat_Cli = '" + Ape_Mat.getText().toString() + "'," +
                    "@Email = '" + Email.getText().toString() + "'," +
                    "@Oferta_Wsp = '" + Oferta.getText().toString() + "'," +
                    "@Nro_Wsp = '" + Nro_Wsp.getText().toString() + "' ");

            Toast.makeText(getApplicationContext(),"CLIENTE ACTUALIZADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR EN ACTUALIZAR: REVISAR LOS DATOS - "+e.getMessage(),Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCliente() {
        try{
            Statement drp = conexionDB().createStatement();
            drp.executeUpdate("EXEC Sp_iudc_ClientesMarketing @Modo = 'D'," +
                    "@RUT_Cli = '" + Rut_Cli.getText().toString() + "' ");

            Toast.makeText(getApplicationContext(),"CLIENTE ELIMINADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR EN ELIMINAR, INTENTAR NUEVAMENTE - "+e.getMessage(),Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMail(){
        try {
            String email = Util.getProperty("e.address",getApplicationContext());
            String pass = Util.getProperty("e.pass",getApplicationContext());


            GMailSender sender = new GMailSender(""+email+"",
                    ""+pass+"");
            sender.sendMail("Bienvenido a las promociones de Central de Carnes", "" +
                            "¡Hola "+Nom_Cli.getText()+"!,\n" +
                            "Supermercado Central de Carnes te da una cordial bienvenida.\n \n" +
                            "Este correo fue generado y enviado automáticamente, " +
                            "para confirmar que has sido registrado en la lista de clientes " +
                            "que desean recibir ofertas, promociones y descuentos.\n" +
                            "No olvides seguirnos en nuestras redes sociales, y así " +
                            "poder disfrutar de mayores beneficios.\n\n" +
                            "Se despide,\n" +
                            "Equipo de Marketing\n" +
                            "Central de Carnes.\n\n" +
                            "PD: No es necesario que respondas a este correo.",
                    ""+email+"", ""+Email.getText()+"");
            Toast.makeText(getApplicationContext(),"CORREO ENVIADO",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"ERROR - "+e.getMessage(),Toast.LENGTH_LONG).show();
            //Log.e("SendMail", , e);
        }
    }
}
