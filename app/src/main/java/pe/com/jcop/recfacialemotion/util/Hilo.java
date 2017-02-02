package pe.com.jcop.recfacialemotion.util;


import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;

/**
 * Created by jchata on 01/02/2017.
 */

public class Hilo extends Thread {
    private int progressStatus = 0;
    private Handler handler = new Handler();
    Context context;
    ProgressBar pb;
    int cont;

    public Hilo(ProgressBar pb1,int contador){
        this.pb = pb1;
        this.cont = contador;
    }

    @Override
    public void run() {
        while(progressStatus < cont){
            progressStatus +=1;
            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pb.setProgress(progressStatus);
                }
            });
        }
    }

}
