package pe.com.jcop.recfacialemotion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.com.jcop.recfacialemotion.util.AyudaBD;

public class ConsultaDBActivity extends AppCompatActivity {


    EditText edt_contenido;
    Button btnvolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_db);

        final AyudaBD ayudaBD = new AyudaBD(getApplicationContext());

        btnvolver = (Button) findViewById(R.id.btn_volver);
        edt_contenido=(EditText)findViewById(R.id.t_contenido);

        SQLiteDatabase sqLiteDatabase = ayudaBD
                .getWritableDatabase();

        // String[] args = {};

        String[] proyeccion = {
                AyudaBD.DatosTabla.COLUMNA_NOMBRE_ARCHIVO,
                AyudaBD.DatosTabla.COLUMNA_EMOCION,
                AyudaBD.DatosTabla.COLUMNA_PORCENTAJE
        };

        edt_contenido.setText("archivo  emocion  porcentaje");

        Cursor c = sqLiteDatabase.query
                (AyudaBD.DatosTabla.NOMBRE_TABLA,
                        proyeccion, null, null, null, null, null);

        while (c.moveToNext()) {
            edt_contenido.append(c.getString(0)+"-"+c.getString(1)+"-"+c.getString(2) + "\n");
        }
        c.close();

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultaDBActivity.this,RecFacialActivity.class);
                startActivity(intent);
            }
        });

    }
}
