package EK2012.Predictions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import EK2012.Predictions.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class EK2012PredictionsActivity extends Activity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	    }
	    
	    @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.mainmenu, menu);
			return true;
		}
	    
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.itemRanking:
				showRanking();
				break;
			case R.id.itemPredictions:
				showPredictions();
				break;
			default:
				break;
			}
			return true;
		}
	    
	    public void showRanking() {
	    	BufferedReader in = null;
	        try 
	        {
	            HttpClient client = new DefaultHttpClient();
	            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
	            HttpGet request = new HttpGet();
	            request.setHeader("Content-Type", "text/plain; charset=utf-8");
	            request.setURI(new URI("http://www.sitemasters.be/ek2012/json.php?type=ranking"));
	            HttpResponse response = client.execute(request);
	            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	            StringBuilder sb = new StringBuilder();
	            String line = "";

	            while ((line = in.readLine()) != null) 
	            {
	                sb.append(line + "\n");
	            }
	            in.close();
	           
	            JSONArray data = new JSONArray(sb.toString());
	            
	            TextView contentView = (TextView) findViewById(R.id.Content);
	            TextView headerView = (TextView) findViewById(R.id.Header);
	            contentView.setText("");
	            headerView.setText("Ranking");
	            
	            for (int i = 0; i < data.length(); i++) {
	            	JSONObject row = data.getJSONObject(i);
	            	contentView.append(row.getString("Name"));
	            	contentView.append(" - ");
	            	contentView.append(row.getString("Score"));
	            	contentView.append("\n"); 
	            }
	            
	            
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        catch (Exception e)
	    	{
	    		// this is the line of code that sends a real error message to the log
	    		Log.e("ERROR", "ERROR IN CODE: " + e.toString());
	     
	    		// this is the line that prints out the location in
	    		// the code where the error occurred.
	    		e.printStackTrace();
	    	}
	        finally 
	        {
	            
	        }
	    }
	    
	    public void showPredictions() {
	    	BufferedReader in = null;
	        try 
	        {
	            HttpClient client = new DefaultHttpClient();
	            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
	            HttpGet request = new HttpGet();
	            request.setHeader("Content-Type", "text/plain; charset=utf-8");
	            request.setURI(new URI("http://www.sitemasters.be/ek2012/json.php?type=predictions"));
	            HttpResponse response = client.execute(request);
	            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	            StringBuilder sb = new StringBuilder();
	            String line = "";

	            while ((line = in.readLine()) != null) 
	            {
	                sb.append(line + "\n");
	            }
	            in.close();
	            
	            TextView contentView = (TextView) findViewById(R.id.Content);
	            TextView headerView = (TextView) findViewById(R.id.Header);
	            contentView.setText("");
	            headerView.setText("Predictions");
	           
	            if (sb.toString() == "[]\n") {
	            	contentView.setText("Predictions are only shown when the match is in progress.");
	            }
	            else
	            {
		            JSONArray data = new JSONArray(sb.toString());

	            	for (int i = 0; i < data.length(); i++) {
	                	JSONObject row = data.getJSONObject(i);
	                	contentView.append(row.getString("user_nicename"));
	                	contentView.append(" - ");
	                	contentView.append(row.getString("HomeTeam"));
	                	contentView.append(" - ");
	                	contentView.append(row.getString("AwayTeam"));
	                	contentView.append(" - ");
	                	contentView.append(row.getString("home_goals"));
	                	contentView.append(" - ");
	                	contentView.append(row.getString("away_goals"));
	                	contentView.append("\n"); 
	                }
	            }   
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        catch (Exception e)
	    	{
	    		// this is the line of code that sends a real error message to the log
	    		Log.e("ERROR", "ERROR IN CODE: " + e.toString());
	     
	    		// this is the line that prints out the location in
	    		// the code where the error occurred.
	    		e.printStackTrace();
	    	}
	        finally 
	        {
	            
	        }
	    }
	}