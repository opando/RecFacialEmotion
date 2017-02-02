package pe.com.jcop.recfacialemotion;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {


    TextView tvLogin;
    LoginButton loginButton;
    CallbackManager callbackManager;
    public static final String APP_PACKAGE = "pe.com.jcop.recfacialemotion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_login);

        tvLogin = (TextView)findViewById(R.id.tv_login);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        //Para obtener le hash de la App y registrarla en Facebook Developer
        getHashApp();
        //Seteando permisos
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));


        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {



                Profile profile = Profile.getCurrentProfile();

                Intent intent = new Intent(LoginActivity.this,LoginProfileActivity.class);

                intent.putExtra("foto",profile.getProfilePictureUri(100,100));
                intent.putExtra("nombre",profile.getFirstName()+" "+profile.getLastName());
                intent.putExtra("id",profile.getId());
                intent.putExtra("userId",loginResult.getAccessToken().getUserId());

                startActivity(intent);

            }

            @Override
            public void onCancel() {

                tvLogin.setText("Deslogeado");

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void loginFacebook() {


    }

    private void getHashApp() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(APP_PACKAGE, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("##### hash key #######", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
