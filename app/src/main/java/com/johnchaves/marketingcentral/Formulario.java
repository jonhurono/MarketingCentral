package com.johnchaves.marketingcentral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
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
    Button btnCreate, btnClear, btnUpdate, btnDelete, btnQuery, btnQR, btnBack, btnModify;
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
        btnBack     = findViewById(R.id.btnBack);
        btnQuery    = findViewById(R.id.btnQuery);
        btnCreate   = findViewById(R.id.btnCreate);
        btnClear    = findViewById(R.id.btnClear);
        btnModify   = findViewById(R.id.btnModify);
        btnUpdate   = findViewById(R.id.btnUpdate);
        btnDelete   = findViewById(R.id.btnDelete);

        Rut_Cli.addTextChangedListener(watcher);
        Nom_Cli.addTextChangedListener(watcher);
        Ape_Pat.addTextChangedListener(watcher);
        Email.addTextChangedListener(watcher);

        recibeOferta.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                row_wsp.setVisibility(View.VISIBLE);
                Oferta.setText("1");
            } else{
                row_wsp.setVisibility(View.INVISIBLE);
                Oferta.setText("0");
                Nro_Wsp.setText(null);
                Nro_Wsp.requestFocus();
            }
        });

        Rut_Cli.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btnQuery.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        btnQR.setOnClickListener(view -> startActivity(new Intent(Formulario.this, CameraQR.class)));

        btnQuery.setOnClickListener(view -> readCliente());

        btnCreate.setOnClickListener(view -> isValidEmailandRut());

        btnClear.setOnClickListener(view -> limpiar());

        btnUpdate.setOnClickListener(view -> updateCliente());

        btnDelete.setOnClickListener(view -> deleteCliente());

        btnModify.setOnClickListener(view -> modifyCliente());

        btnBack.setOnClickListener(view -> {
            limpiar();
            btnBack.setVisibility(View.GONE);
            btnQuery.setVisibility(View.VISIBLE);
            botonera2.setVisibility(View.INVISIBLE);
            botonera1.setVisibility(View.VISIBLE);
        });
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            checkFieldForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
    };

    private void checkFieldForEmptyValues() {

        btnCreate.setEnabled(Rut_Cli.length() > 0 && Nom_Cli.length() > 0
                && Ape_Pat.length() > 0 && Email.length() > 0);
    }

    private void isValidEmailandRut(){

        String email = Email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String rut = Rut_Cli.getText().toString().trim();
        String rutPattern = "[0-9]+-[k0-9]+";

        if ( email.matches(emailPattern) && rut.matches(rutPattern) )
        {
            createCliente();
        }
        else if( email.matches(emailPattern) )
        {
            Toast.makeText(getApplicationContext(),"Cliente NO creado - Revisar campo RUT",Toast.LENGTH_SHORT).show();
            Rut_Cli.setError("RUT inválido");
        }
        else if( rut.matches(rutPattern) )
        {
            Toast.makeText(getApplicationContext(),"Cliente NO creado - Revisar campo Email",Toast.LENGTH_SHORT).show();
            Email.setError("Email inválido");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Cliente NO creado - Revisar los campos",Toast.LENGTH_SHORT).show();
            Rut_Cli.setError("RUT inválido");
            Email.setError("Correo inválido");
        }
    }

    private void limpiar(){
        Rut_Cli.setText(null);
        Rut_Cli.setEnabled(true);
        Nom_Cli.setText(null);
        Nom_Cli.setEnabled(true);
        Ape_Pat.setText(null);
        Ape_Pat.setEnabled(true);
        Ape_Mat.setText(null);
        Ape_Mat.setEnabled(true);
        Email.setText(null);
        Email.setEnabled(true);
        Nro_Wsp.setText(null);
        Nro_Wsp.setEnabled(true);

        Rut_Cli.requestFocus();
        btnBack.setVisibility(View.GONE);
        btnQuery.setVisibility(View.VISIBLE);
        botonera2.setVisibility(View.GONE);
        botonera1.setVisibility(View.VISIBLE);
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
                Rut_Cli.setEnabled(false);
                Nom_Cli.setText(rs.getString(2));
                Nom_Cli.setEnabled(false);
                Ape_Pat.setText(rs.getString(3));
                Ape_Pat.setEnabled(false);
                Ape_Mat.setText(rs.getString(4));
                Ape_Mat.setEnabled(false);
                Email.setText(rs.getString(5));
                Email.setEnabled(false);
                Nro_Wsp.setText(rs.getString(7));
                Nro_Wsp.setEnabled(false);

                btnQuery.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                botonera1.setVisibility(View.GONE);
                botonera2.setVisibility(View.VISIBLE);
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

            sendMailCreado();
            limpiar();
            Toast.makeText(getApplicationContext(),"CLIENTE CREADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR EN INSERCIÓN: REVISAR LOS DATOS - "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void modifyCliente(){
        Rut_Cli.setEnabled(true);
        Nom_Cli.setEnabled(true);
        Ape_Pat.setEnabled(true);
        Ape_Mat.setEnabled(true);
        Email.setEnabled(true);
        Nro_Wsp.setEnabled(true);

        btnModify.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
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

            sendMailModificado();
            limpiar();
            Toast.makeText(getApplicationContext(),"CLIENTE ACTUALIZADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR EN ACTUALIZAR: REVISAR LOS DATOS - "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void deleteCliente() {
        try{
            Statement drp = conexionDB().createStatement();
            drp.executeUpdate("EXEC Sp_iudc_ClientesMarketing @Modo = 'D'," +
                    "@RUT_Cli = '" + Rut_Cli.getText().toString() + "' ");

            sendMailEliminado();
            limpiar();
            Toast.makeText(getApplicationContext(),"CLIENTE ELIMINADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR EN ELIMINAR, INTENTAR NUEVAMENTE - "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void sendMailCreado(){
        try {
            String email = Util.getProperty("e.address",getApplicationContext());
            String pass = Util.getProperty("e.pass",getApplicationContext());

            GMailSender sender = new GMailSender(""+email+"",
                    ""+pass+"");
            sender.sendMail("Bienvenido a las promociones de Central de Carnes",
                            "¡Hola "+Nom_Cli.getText()+"!,\n" +
                            "Supermercado Central de Carnes te da una cordial bienvenida.\n \n" +
                            "Este correo fue generado y enviado automáticamente, " +
                            "para confirmar que has sido registrado en la lista de clientes " +
                            "que desean recibir ofertas, promociones y descuentos.\n" +
                            "No olvides seguirnos en nuestras redes sociales, y así " +
                            "poder disfrutar de mayores beneficios.\n\n" +
                            "Se despide,\n" +
                            "Equipo de Marketing\n" +
                            "Central de Carnes 🐑🐖🐄.\n\n" +
                            "PD: No es necesario que respondas a este correo.",
                    ""+email+"", ""+Email.getText()+"");
            Toast.makeText(getApplicationContext(),"CORREO ENVIADO",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"ERROR - "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void sendMailModificado(){
        try {
            String email = Util.getProperty("e.address",getApplicationContext());
            String pass = Util.getProperty("e.pass",getApplicationContext());

            GMailSender sender = new GMailSender(""+email+"",
                    ""+pass+"");
            sender.sendMail("Modificación de información en promociones de Central de Carnes",
                            "¡Hola "+Nom_Cli.getText()+"!,\n" +
                            "Supermercado Central de Carnes te informa que tus datos han sido " +
                            "actualizados correctamente.\n \n" +
                            "No olvides seguirnos en nuestras redes sociales, y así " +
                            "podrás estar informado de todas las novedades.\n\n" +
                            "Se despide,\n" +
                            "Equipo de Marketing\n" +
                            "Central de Carnes 🐑🐖🐄.\n\n" +
                            "PD: No es necesario que respondas a este correo.",
                    ""+email+"", ""+Email.getText()+"");
            Toast.makeText(getApplicationContext(),"CORREO ENVIADO",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"ERROR - "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void sendMailEliminado(){
        try {
            String email = Util.getProperty("e.address",getApplicationContext());
            String pass = Util.getProperty("e.pass",getApplicationContext());

            GMailSender sender = new GMailSender(""+email+"",
                    ""+pass+"");
            sender.sendMail("Eliminación en promociones de Central de Carnes",
                            "¡Hola "+Nom_Cli.getText()+"!,\n" +
                            "Supermercado Central de Carnes te informa que tu registro en las promociones ha sido eliminado.\n \n" +
                            "Lamentamos que hayas tomado esta elección. " +
                            "Esperamos que nos sigas en nuestras redes sociales para que estés " +
                            "informado a todas las novedades.\n\n" +
                            "Se despide,\n" +
                            "Equipo de Marketing\n" +
                            "Central de Carnes 🐑🐖🐄.\n\n" +
                            "PD: No es necesario que respondas a este correo.",
                    ""+email+"", ""+Email.getText()+"");
            Toast.makeText(getApplicationContext(),"CORREO ENVIADO",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"ERROR - "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
