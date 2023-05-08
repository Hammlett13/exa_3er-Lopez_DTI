package com.example.exa_3er_lopez_dti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    public EditText etNoEmpHL, etNombreHL, etPuesto, etDiasTHL;
    public TextView tvBono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNoEmpHL = findViewById(R.id.etNumEmpHL);
        etNombreHL = findViewById(R.id.etNomHL);
        etPuesto = findViewById(R.id.etPuestoHL);
        etDiasTHL = findViewById(R.id.etDiasHL);

        tvBono = findViewById(R.id.tvBonoHL);
    }

    public void registroEmp(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        String nEmpleado = etNoEmpHL.getText().toString();
        String nombreE = etNombreHL.getText().toString();
        String puesto = etPuesto.getText().toString();
        String diasT = etDiasTHL.getText().toString();

        //Guardar datos en la tabla articulo utilizando un contenedor
        ContentValues registro = new ContentValues();
        registro.put("cod", nEmpleado);
        registro.put("descripcion", nombreE);
        registro.put("ubicacion", puesto);
        registro.put("existencia", diasT);

        bd.insert("articulo", null, registro);

        etNoEmpHL.setText(null);
        etNombreHL.setText(null);
        etPuesto.setText(null);
        etDiasTHL.setText(null);

        Toast.makeText(this, "Se registro el empleado exitosamente", Toast.LENGTH_SHORT).show();


    }

    public void consultaEmpelado(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();//objetos de base de datos se reescribible

        //se asigna una variable para busqueda y consulta por campo distintivo
        String codigoConsulta = etNoEmpHL.getText().toString();
        //Cursor recorre los campos d euna tabla hasta encontralo por campo distintivo
        Cursor fila = bd.rawQuery("SELECT descripcion,ubicacion,existencia from articulo where cod=" + codigoConsulta, null);

        if (fila.moveToFirst()) {//si condicion es verdadera, es decir, encontro un campo y sus datos
            etNombreHL.setText(fila.getString(0));
            etPuesto.setText(fila.getString(1));
            etDiasTHL.setText(fila.getString(2));

            int dias = Integer.parseInt(etDiasTHL.getText().toString());
            if (dias >= 15) {
                double bono = 0.15 * dias;
                tvBono.setText("El bono es: " + String.valueOf(bono));
            } else {
                tvBono.setText("Sin bono");
            }
            Toast.makeText(this, "Registro encontrado de forma EXITOSA", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No existe artículo con ese código", Toast.LENGTH_SHORT).show();
            bd.close();
        }


    }

    public void eliminarEmpleado(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();//objetos de base de datos  reescribible

        //se asigna variable para busqueda por campo distitivo caodigo producto
        String codigoBaja = etNoEmpHL.getText().toString();
        //Se genera instrtuccion SQL para que se elimine el registro de producto
        int c = bd.delete("articulo", "cod=" + codigoBaja, null);
        if (c == 1) {
            Toast.makeText(this, "Registro eliminado de BD exitoso\nVerifica Consulta", Toast.LENGTH_LONG).show();
            //Limpia cajas de texto
            this.etNoEmpHL.setText("");
            this.etNombreHL.setText("");
            this.etPuesto.setText("");
            this.etDiasTHL.setText("");
        } else {
            Toast.makeText(this, "Error\nNo existe Empleado con ese codigo", Toast.LENGTH_LONG).show();
        }

    }
    public void modificarEmpleado(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();//objetos de base de datos  reescribible

        String noEmpleado = etNoEmpHL.getText().toString();
        String nombre = etNombreHL.getText().toString();
        String puesto = etPuesto.getText().toString();
        String diasT = etDiasTHL.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("cod", noEmpleado);
        registro.put("descripcion", nombre);
        registro.put("ubicacion", puesto);
        registro.put("existencia", diasT);

        int filasActualizadas = bd.update("articulo", registro, "cod=" + noEmpleado, null);

        if (filasActualizadas > 0) {
            Toast.makeText(this, "Registro actualizado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se pudo actualizar el registro", Toast.LENGTH_SHORT).show();
        }

        bd.close();
    }




}