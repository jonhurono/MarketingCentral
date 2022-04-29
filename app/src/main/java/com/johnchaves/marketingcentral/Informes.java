package com.johnchaves.marketingcentral;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Informes extends AppCompatActivity {

    RadioButton excel, pdf;
    Button btnArchivo;
    EditText datePicker;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informes_layout);

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        excel = (findViewById(R.id.excelRadioBtn));
        pdf = (findViewById(R.id.pdfRadioBtn));
        btnArchivo = (findViewById(R.id.btnArchivo));
        datePicker = (findViewById(R.id.datePicker));

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        excel.setChecked(true);

        datePicker.setInputType(InputType.TYPE_NULL);
        datePicker.setOnClickListener(v ->
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC-3"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate  = format.format(calendar.getTime());

            datePicker.setText(formattedDate);
        });

        btnArchivo.setOnClickListener(view -> {
            if (excel.isChecked()) {
                crearExcel();
            } else if(pdf.isChecked()) {
                crearPdf();
            } else {
                Toast.makeText(getApplicationContext(),"DEBE SELECCIONAR FORMATO",Toast.LENGTH_SHORT).show();
            }
        });
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

    private void crearExcel() {

        /*@SuppressWarnings("unused")
        Workbook readWorkbook = WorkbookFactory.create(new FileInputStream(".xls file path(C:\\Users\\CEPL\\Desktop\\test.xls)") );
        @SuppressWarnings("resource")
        Workbook writeWorkbook = new HSSFWorkbook();
        Sheet desSheet = writeWorkbook.createSheet("new sheet");*/

        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC [Sp_AdminApp] 'BOFASRTUREXIMPNC'," +
                    " '"+datePicker.getText().toString()+"','"+datePicker.getText().toString()+"',0,0");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            /*Keyboard.Row desRow1 = desSheet.createRow(0);
            for(int col=0 ;col < columnsNumber;col++) {
                Cell newpath = desRow1.createCell(col);
                newpath.setCellValue(rsmd.getColumnLabel(col+1));
            }*/

            if(rs.next()){
                Toast.makeText(getApplicationContext(),"DATOS ENCONTRADOS, GENERADO PDF",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Error en consulta, vuelva a intentar",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        /*try{
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            Row desRow1 = desSheet.createRow(0);
            for(int col=0 ;col < columnsNumber;col++) {
                Cell newpath = desRow1.createCell(col);
                newpath.setCellValue(rsmd.getColumnLabel(col+1));
            }
            while(rs.next()) {
                System.out.println("Row number" + rs.getRow() );
                Row desRow = desSheet.createRow(rs.getRow());
                for(int col=0 ;col < columnsNumber;col++) {
                    Cell newpath = desRow.createCell(col);
                    newpath.setCellValue(rs.getString(col+1));
                }
                FileOutputStream fileOut = new FileOutputStream(".xls file path(C:\\Users\\CEPL\\Desktop\\test.xls)");
                writeWorkbook.write(fileOut);
                fileOut.close();
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to get data from database");
        }*/
    }

    private void crearPdf() {
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC [Sp_AdminApp] 'BOFASRTUREXIMPNC'," +
                    " '"+datePicker.getText().toString()+"','"+datePicker.getText().toString()+"',0,0");

            if(rs.next()){
                Toast.makeText(getApplicationContext(),"DATOS ENCONTRADOS, GENERADO PDF",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Error en consulta, vuelva a intentar",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
