package pe.com.jcop.recfacialemotion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;


import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Locale;



import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class RecFacialActivity extends AppCompatActivity {


    ImageView foto;
    Button openCamara;

    Button btnanalizar, btnconsultar;
    double anger, contempt, disgust, fear, happiness, neutral, sadness, surprise;

    private Bitmap bmFoto;
    //private EmotionServiceClient client;


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

                long startTime = System.currentTimeMillis();

                Bitmap bitmap = bmFoto;
                String filename = "filename.png";
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), filename);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("picture", contentPart);
                String response = multipost("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize", reqEntity);


                Log.d("#########final ####" , response);

                /*
                if (client == null) {
                    client = new EmotionServiceRestClient(getString(R.string.subscription_key));
                }

                Log.d("######"," bmFoto  ->" + bmFoto);

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                bmFoto.compress(Bitmap.CompressFormat.JPEG, 100, output);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

                List<RecognizeResult> result = null;
                Gson gson = new Gson();
                try {
                    result = client.recognizeImage(inputStream);

                    String json = gson.toJson(result);
                    Log.d("result", json);

                    Log.d("emotion", String.format("Tiempo de respuesta del servicio: %d ms", (System.currentTimeMillis() - startTime)));

                } catch (EmotionServiceException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                */



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
            //Bitmap
            bmFoto = (Bitmap)data.getExtras().get("data");
            foto.setImageBitmap(bmFoto);
        }
    }

    private static String multipost(String urlString, MultipartEntity reqEntity) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength()+"");
            conn.addRequestProperty("Content-Type", "application/octet-stream");
            conn.addRequestProperty("Ocp-Apim-Subscription-Key", "f89257c4c8c2490e9893afc7c12a0a66");
            //conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readStream(conn.getInputStream());
            }

        } catch (Exception e) {
            Log.e("#################", "multipart post error " + e + "(" + urlString + ")");
        }
        return null;
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }


/*
    public void requestPost(){
        HttpURLConnection httpUrlConnection = null;
        URL url = new URL("http://example.com/server.cgi");
        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + this.boundary);

        DataOutputStream request = new DataOutputStream(
                httpUrlConnection.getOutputStream());

        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                this.attachmentName + "\";filename=\"" +
                this.attachmentFileName + "\"" + this.crlf);
        request.writeBytes(this.crlf);

    }
*/

    public static RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    /*
    public void postRequest(){

        HttpPost request = null;
        request = new HttpPost(url);
        boolean isStream = false;
        if(contentType != null && !contentType.isEmpty()) {
            request.setHeader("Content-Type", contentType);
            if(contentType.toLowerCase(Locale.ENGLISH).contains("octet-stream")) {
                isStream = true;
            }
        } else {
            request.setHeader("Content-Type", "application/json");
        }

        request.setHeader("ocp-apim-subscription-key", this.subscriptionKey);

        try {
            if(!isStream) {
                String e = this.gson.toJson(data).toString();
                StringEntity statusCode = new StringEntity(e);
                request.setEntity(statusCode);
            } else {
                request.setEntity(new ByteArrayEntity((byte[])((byte[])data.get("data"))));
            }

            HttpResponse e1 = this.client.execute(request);
            int statusCode1 = e1.getStatusLine().getStatusCode();
            if(statusCode1 == 200) {
                return !responseInputStream?this.readInput(e1.getEntity().getContent()):e1.getEntity().getContent();
            } else {
                throw new Exception("Error executing POST request! Received error code: " + e1.getStatusLine().getStatusCode());
            }
        } catch (Exception var10) {
            throw new EmotionServiceException(var10.getMessage());
        }
    }*/
}
