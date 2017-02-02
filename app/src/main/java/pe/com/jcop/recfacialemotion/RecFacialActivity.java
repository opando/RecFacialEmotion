package pe.com.jcop.recfacialemotion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RecFacialActivity extends AppCompatActivity {


    ImageView foto;
    Button openCamara;

    Button btnanalizar, btnconsultar;
    double anger, contempt, disgust, fear, happiness, neutral, sadness, surprise;


    public static final Integer CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_facial);

        //Dialog dialog = new AlertDialog.Builder(this).setTitle("").show();

        foto = (ImageView)findViewById(R.id.iv_foto);
        openCamara = (Button) findViewById(R.id.btn_abrirCamara);

        btnanalizar = (Button) findViewById(R.id.btn_analizar);
        btnconsultar = (Button) findViewById(R.id.btn_consultar);

        anger = 0.08891568;
        contempt = 0.00363058178;
        disgust = 0.7236219;
        fear = 0.00000603674971;
        happiness = 9.00591459e-8;
        neutral = 0.001907167;
        sadness = 0.181909069;
        surprise = 0.000009478085;

        btnanalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putDouble("anger", anger);
                b.putDouble("contempt", contempt);
                b.putDouble("disgust", disgust);
                b.putDouble("fear", fear);
                b.putDouble("happiness", happiness);
                b.putDouble("neutral", neutral);
                b.putDouble("sadness", sadness);
                b.putDouble("surprise", surprise);
                b.putString("imagen","MiFoto.jpg");

                Intent intent = new Intent(RecFacialActivity.this,AnalysisFacialActivity.class);
                intent.putExtras(b);
                startActivity(intent);

            }
        });

        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecFacialActivity.this,ConsultaDBActivity.class);
                startActivity(intent);
            }
        });


        openCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera,CAMERA_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bitmap bmFoto = (Bitmap)data.getExtras().get("data");
            foto.setImageBitmap(bmFoto);
        }
    }
}
