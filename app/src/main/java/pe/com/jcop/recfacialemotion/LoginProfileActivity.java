package pe.com.jcop.recfacialemotion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

public class LoginProfileActivity extends AppCompatActivity {



    TextView nombre;
    Button continuar;
    ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profile);


        Intent login = getIntent();

        nombre = (TextView) findViewById(R.id.tv_nombre_face);
        continuar = (Button) findViewById(R.id.btn_continuar);
        profilePictureView = (ProfilePictureView) findViewById(R.id.imageFace);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginProfileActivity.this,RecFacialActivity.class);
                startActivity(intent);
            }
        });

        nombre.setText(login.getStringExtra("nombre"));
        profilePictureView.setProfileId(login.getStringExtra("userId"));
    }

}
