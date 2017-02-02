package pe.com.jcop.recfacialemotion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import pe.com.jcop.recfacialemotion.util.AyudaBD;
import pe.com.jcop.recfacialemotion.util.Hilo;

public class AnalysisFacialActivity extends AppCompatActivity {

    Context context = this;
    Button btnver, btngrabar, btnregresar;
    TextView tvemocion;
    int anger, contempt, disgust, fear, happiness, neutral, sadness, surprise;
    ProgressBar pbanger, pbcontempt, pbdisgust, pbfear, pbhappiness, pbneutral, pbsadness, pbsurprise;
    int valor = 0;
    String emocion = "";
    String imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_facial);
        btnver = (Button) findViewById(R.id.btn_ver);
        btngrabar = (Button) findViewById(R.id.btn_grabar);
        btnregresar = (Button) findViewById(R.id.btn_regresar);

        tvemocion = (TextView) findViewById(R.id.tv_emocion);

        pbanger = (ProgressBar) findViewById(R.id.pb_anger);
        pbcontempt = (ProgressBar) findViewById(R.id.pb_contempt);
        pbdisgust = (ProgressBar) findViewById(R.id.pb_disgust);
        pbfear = (ProgressBar) findViewById(R.id.pb_fear);
        pbhappiness = (ProgressBar) findViewById(R.id.pb_happiness);
        pbsadness = (ProgressBar) findViewById(R.id.pb_sadness);
        pbneutral = (ProgressBar) findViewById(R.id.pb_neutral);
        pbsurprise = (ProgressBar) findViewById(R.id.pb_surprise);

        Bundle datos = getIntent().getExtras();

        anger = (int) (datos.getDouble("anger")*100 + 0.5);
        contempt = (int) (datos.getDouble("contempt")*100 + 0.5);
        disgust = (int) (datos.getDouble("disgust")*100 + 0.5);
        fear = (int) (datos.getDouble("fear")*100 + 0.5);
        happiness  = (int) (datos.getDouble("happiness")*100 + 0.5);
        neutral = (int) (datos.getDouble("neutral")*100 + 0.5);
        sadness = (int) (datos.getDouble("sadness")*100 + 0.5);
        surprise = (int) (datos.getDouble("surprise")*100 + 0.5);
        imagen = datos.getString("imagen");


        final AyudaBD ayudaBD = new AyudaBD(getApplicationContext());
        btngrabar.setEnabled(false);

        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hilo hanger = new Hilo(pbanger, anger);
                hanger.start();
                valor = anger;
                emocion = "Enojo";

                Hilo hcontempt = new Hilo(pbcontempt, contempt);
                hcontempt.start();
                if (contempt > valor) {
                    valor = contempt;
                    emocion = "Desprecio";
                }

                Hilo hdisgust = new Hilo(pbdisgust, disgust);
                hdisgust.start();

                if (disgust > valor) {
                    valor = disgust;
                    emocion = "Repugnancia";
                }

                Hilo hfear = new Hilo(pbfear, fear);
                hfear.start();

                if (fear > valor) {
                    valor = fear;
                    emocion = "Miedo";
                }

                Hilo hhappiness = new Hilo(pbhappiness, happiness);
                hhappiness.start();

                if (happiness > valor) {
                    valor = happiness;
                    emocion = "Felicidad";
                }

                Hilo hneutral = new Hilo(pbneutral, neutral);
                hneutral.start();

                if (neutral > valor) {
                    valor = neutral;
                    emocion = "Neutral";
                }

                Hilo hsadness = new Hilo(pbsadness, sadness);
                hsadness.start();

                if (sadness > valor) {
                    valor = sadness;
                    emocion = "Tristeza";
                }

                Hilo hsurprise = new Hilo(pbsurprise, surprise);
                hsurprise.start();

                if (surprise > valor) {
                    valor = surprise;
                    emocion = "Sorpresa";
                }

                tvemocion.setText("La emocion es de : "+emocion);
                btngrabar.setEnabled(true);
            }
        });

        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalysisFacialActivity.this,RecFacialActivity.class);
                startActivity(intent);
            }
        });

        btngrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialogobuilder = new AlertDialog.Builder(context);

                alertdialogobuilder.setMessage("Estas seguro de grabar ?")
                        .setTitle("Registro de datos")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // grabar

                                SQLiteDatabase sqLiteDatabase = ayudaBD
                                        .getWritableDatabase();

                                ContentValues contentValues = new ContentValues();

                                contentValues.put(AyudaBD.DatosTabla.COLUMNA_NOMBRE_ARCHIVO, imagen);
                                contentValues.put(AyudaBD.DatosTabla.COLUMNA_EMOCION, emocion);
                                contentValues.put(AyudaBD.DatosTabla.COLUMNA_PORCENTAJE, String.valueOf(valor));

                                Long r = sqLiteDatabase.insert(AyudaBD.DatosTabla.NOMBRE_TABLA,AyudaBD.DatosTabla.COLUMNA_ID,contentValues);

                                Toast.makeText(getApplicationContext(),"Se guardo el registro : " + r,Toast.LENGTH_SHORT).show();

                                btngrabar.setEnabled(false);
                                btnver.setEnabled(false);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // nada
                            }
                        });

                AlertDialog alertdialogo = alertdialogobuilder.create();
                alertdialogo.show();

            }
        });

    }
}
