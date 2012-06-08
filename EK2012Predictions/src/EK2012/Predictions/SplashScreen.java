package EK2012.Predictions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {
	
	private Thread mSplashThread;   

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
        setContentView(R.layout.splash);
        
        final SplashScreen sPlashScreen = this;   
        
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(5000);
                    }
                }
                catch(InterruptedException ex){                    
                }

                finish();
                
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, EK2012PredictionsActivity.class);
                startActivity(intent);
                stop();                    
            }
        };
        
        mSplashThread.start();
	}

}
