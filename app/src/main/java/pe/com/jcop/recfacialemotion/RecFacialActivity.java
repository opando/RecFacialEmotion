package pe.com.jcop.recfacialemotion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.contract.Scores;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class RecFacialActivity extends AppCompatActivity {


    ImageView foto;
    Button openCamara;

    Button btnanalizar, btnconsultar, salir;
    double anger, contempt, disgust, fear, happiness, neutral, sadness, surprise;

    private Bitmap bmFoto;
    private EmotionServiceClient emotionServiceClient;


    public static final Integer CAMERA_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_facial);

        if (emotionServiceClient == null) {
            emotionServiceClient = new EmotionServiceRestClient(getString(R.string.subscription_key));
        }


        foto = (ImageView) findViewById(R.id.iv_foto);
        openCamara = (Button) findViewById(R.id.btn_abrirCamara);

        btnanalizar = (Button) findViewById(R.id.btn_analizar);
        btnconsultar = (Button) findViewById(R.id.btn_consultar);
        salir = (Button) findViewById(R.id.btn_salir2);

        btnanalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Invocamos ala clase Async
                try {
                    new RequestFaceEmotionAsync().execute();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });

        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecFacialActivity.this, ConsultaDBActivity.class);
                startActivity(intent);
            }
        });


        openCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, CAMERA_REQUEST_CODE);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();

                Intent intent = new Intent(RecFacialActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //Bitmap
            bmFoto = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(bmFoto);
        }
    }


    public void doRecognize() {

        try {
            new RequestFaceEmotionAsync().execute();
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private List<RecognizeResult> requestFaceEmotion() throws EmotionServiceException, IOException {
        Log.d("#CALL_REST##", "inicio invocacion al servicio");

        Gson gson = new Gson();


        ByteArrayOutputStream output = new ByteArrayOutputStream();
        //Compresion
        bmFoto.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        long startTime = System.currentTimeMillis();


        List<RecognizeResult> listRecognizeResults = this.emotionServiceClient.recognizeImage(inputStream);
        ;
        //json de respuesta
        String json = gson.toJson(listRecognizeResults);
        Log.d("#JSON#", json);

        Log.d("#CALL_REST##", "fin invocacion al servicio" + (System.currentTimeMillis() - startTime) + " ms ");


        return listRecognizeResults;
    }




    private class RequestFaceEmotionAsync extends AsyncTask<String, String, List<RecognizeResult>> {

        public RequestFaceEmotionAsync() {

        }

        @Override
        protected List<RecognizeResult> doInBackground(String... args) {

            //Inovacamos al servicio en background
            try {
                return requestFaceEmotion();
            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);

            //Se reciben los resultados de forma asincrona

            if (result==null) return;

            RecognizeResult recognizeResult = result.get(0);
            Scores scores = recognizeResult.scores;
            Log.d("# anger : ", scores.anger + "");
            Log.d("# contempt : ", scores.contempt + "");

            //Seteamos el resultado del servicio
            anger = scores.anger;
            contempt = scores.contempt;
            disgust = scores.disgust;
            fear = scores.fear;
            happiness = scores.happiness;
            neutral = scores.neutral;
            sadness = scores.sadness;
            surprise = scores.surprise;


            Log.d("FINAL##", anger + "");

            Bundle b = new Bundle();
            b.putDouble("anger", anger);
            b.putDouble("contempt", contempt);
            b.putDouble("disgust", disgust);
            b.putDouble("fear", fear);
            b.putDouble("happiness", happiness);
            b.putDouble("neutral", neutral);
            b.putDouble("sadness", sadness);
            b.putDouble("surprise", surprise);
            b.putString("imagen", "MiFoto.jpg");

            //Invocamos a la Activity de Analisis
            Intent intent = new Intent(RecFacialActivity.this, AnalysisFacialActivity.class);
            intent.putExtras(b);
            startActivity(intent);


        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(!isLogged()) {
            Intent intent = new Intent(RecFacialActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }


    public boolean isLogged() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
