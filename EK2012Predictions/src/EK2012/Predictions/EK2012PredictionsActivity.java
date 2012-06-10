package EK2012.Predictions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EK2012PredictionsActivity extends Activity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        
	        showContent(R.string.res_url_ranking);
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
				showContent(R.string.res_url_ranking);
				break;
			case R.id.itemPredictions:
				showContent(R.string.res_url_predictions);
				break;
			case R.id.itemMatches:
				showContent(R.string.res_url_matches);
				break;
			case R.id.itemNews:
				showContent(R.string.res_url_news);
				break;
			default:
				break;
			}
			return true;
		}
	    
	    public void showHeader(int option){
	    	TextView headerView = (TextView) findViewById(R.id.Header);
	    	
	    	switch (option) {
				case R.string.res_url_ranking:
					headerView.setText(getString(R.string.res_text_header_ranking));
					break;
				case R.string.res_url_predictions:
					headerView.setText(getString(R.string.res_text_header_predictions));
					break;
				case R.string.res_url_matches:
					headerView.setText(getString(R.string.res_text_header_matches));
					break;
				case R.string.res_url_news:
					headerView.setText(getString(R.string.res_text_header_news));
					break;
				default:
					break;
	    	}
	    }
	    
	    public void showDetails(int option, JSONArray data) throws JSONException, ParseException{
            TextView contentView = (TextView) findViewById(R.id.Content);
            TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
            contentView.setText("");
            
            clearTableRows();
            
            switch (option) {
				case R.string.res_url_ranking:
					table.setStretchAllColumns(true);
			    	table.setShrinkAllColumns(false);
			    	table.setColumnStretchable(2, true);
					addRankingHeaderRow();
					break;
				case R.string.res_url_predictions:
					table.setStretchAllColumns(true);
			    	table.setShrinkAllColumns(false);
			    	table.setColumnStretchable(1, true);
					addPredictionsHeaderRow(data.getJSONObject(0).getString("HomeTeam"), data.getJSONObject(0).getString("AwayTeam"));
					break;
				case R.string.res_url_matches:
					table.setStretchAllColumns(true);
			    	table.setShrinkAllColumns(false);
			    	table.setColumnStretchable(1, true);
					break;
				case R.string.res_url_news:
					table.setStretchAllColumns(false);
			    	table.setShrinkAllColumns(true);
					break;
				default:
					break;
            }
            
            for (int i = 0; i < data.length(); i++) {
            	JSONObject row = data.getJSONObject(i);
            	switch (option) {
     				case R.string.res_url_ranking:
     					addRankingRow(row, i); 
     					break;
     				case R.string.res_url_predictions:
     					addPredictionsRow(row, i);
     					break;
     				case R.string.res_url_matches:
     					addMatchesRow(row, i);
     					break;
     				case R.string.res_url_news:
     					addNewsRow(row, i);
     					break;
     				default:
     					break;
            	}
            }
	    }
	    
	    public void showContent(int option){
	    	BufferedReader in = null;
	        try 
	        {
	            HttpClient client = new DefaultHttpClient();
	            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
	            HttpGet request = new HttpGet();
	            request.setHeader("Content-Type", "text/plain; charset=utf-8");
	            request.setURI(new URI(getString(option)));
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
	           
	            showHeader(option);
	            showDetails(option, data);
	            
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
	    
	    public TextView getColumnRightAligned(String displayText){
	    	TextView column = getColumn(displayText);
	    	column.setGravity(Gravity.RIGHT);
	    	return column;
	    }
	    
	    public TextView getColumn(String displayText){
	    	TextView tableColumn = new TextView(EK2012PredictionsActivity.this);
			tableColumn.setPadding(10, 5, 10, 5);
			tableColumn.setGravity(Gravity.LEFT);
			tableColumn.setText(displayText);
			return tableColumn;
	    }
	    
	    public void addRankingHeaderRow() {
	    	TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
			TableRow tableRow = new TableRow(EK2012PredictionsActivity.this);
			
			tableRow.addView(getColumn("Position"));
			tableRow.addView(getColumn("User"));
			tableRow.addView(getColumn(""));
			tableRow.addView(getColumnRightAligned("Score"));
			
			table.addView(tableRow);
	    }
	    
	    public void addRankingRow(JSONObject row, int rowCount) throws JSONException{
	    	TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
			TableRow tableRow = new TableRow(EK2012PredictionsActivity.this);
			
			tableRow.setBackgroundColor(getAlternatingColorResourceId(rowCount));
			
			tableRow.addView(getColumn(String.valueOf(rowCount + 1)));
			tableRow.addView(getColumn(row.getString("Name")));
			tableRow.addView(getColumn(""));
			tableRow.addView(getColumnRightAligned(row.getString("Score")));		
			
			table.addView(tableRow);
	    }
	    
	    public void addMatchesRow(JSONObject row, int rowCount) throws JSONException, ParseException{
	    	TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
			TableRow tableRow = new TableRow(EK2012PredictionsActivity.this);
			
			tableRow.setBackgroundColor(getAlternatingColorResourceId(rowCount));
			
			tableRow.addView(getColumn(convertDateTimeFormat(row.getString("kickoff"))));
			
			tableRow.addView(getColumn(""));
			
			tableRow.addView(getCountryImage(row.getString("HomeTeam")));
			if (row.getInt("Ended") == 1) {
				tableRow.addView(getColumnRightAligned(row.getString("home_goals")));
				tableRow.addView(getColumn("    - "));
				tableRow.addView(getColumnRightAligned(row.getString("away_goals")));
			}
			else {
				tableRow.addView(getColumn(""));
				tableRow.addView(getColumn(""));
				tableRow.addView(getColumn(""));
			}
			tableRow.addView(getCountryImage(row.getString("AwayTeam")));
			
			table.addView(tableRow);
	    }
	    
	    public void addPredictionsHeaderRow(String homeCountry, String awayCountry) {
	    	TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
			TableRow tableRow = new TableRow(EK2012PredictionsActivity.this);
			
			tableRow.addView(getColumn("User"));
			tableRow.addView(getColumn(""));
			tableRow.addView(getCountryImage(homeCountry));
			tableRow.addView(getCountryImage(awayCountry));
			
			table.addView(tableRow);
	    }
	    
	    public void addPredictionsRow(JSONObject row, int rowCount) throws JSONException{
	    	TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
			TableRow tableRow = new TableRow(EK2012PredictionsActivity.this);
			
			tableRow.setBackgroundColor(getAlternatingColorResourceId(rowCount));
			
			tableRow.addView(getColumn(row.getString("user_nicename")));
			tableRow.addView(getColumn(""));
			tableRow.addView(getColumnRightAligned(row.getString("home_goals")));
			tableRow.addView(getColumnRightAligned(row.getString("away_goals")));
			
			table.addView(tableRow);
	    }
	    
	    public void addNewsRow(JSONObject row, int rowCount) throws JSONException{
	    	TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
			TableRow tableRow = new TableRow(EK2012PredictionsActivity.this);
			TextView tvPostDate = getColumn(row.getString("post_date"));
			TextView tvTitle = getColumn(row.getString("post_title"));
			
			tvTitle.setTextAppearance(getApplicationContext(), R.style.newsTitle);
			tvPostDate.setTextAppearance(getApplicationContext(), R.style.newsPostDate);
			
			tableRow.setBackgroundColor(getAlternatingColorResourceId(rowCount));
			tableRow.addView(tvTitle);
			table.addView(tableRow);
			
			tableRow = new TableRow(EK2012PredictionsActivity.this);
			tableRow.setBackgroundColor(getAlternatingColorResourceId(rowCount));
			tableRow.addView(getColumn(row.getString("post_content")));
			table.addView(tableRow);
			
			tableRow = new TableRow(EK2012PredictionsActivity.this);
			tableRow.setBackgroundColor(getAlternatingColorResourceId(rowCount));
			tableRow.addView(tvPostDate);
			table.addView(tableRow);
	    }
	    
	    public void clearTableRows() {
	    	TableLayout table = (TableLayout) findViewById(R.id.ContentTable);
	    	table.removeAllViews();
	    }
	    
	    public int getCountryImageResourceId(String country){
	    	return getResources().getIdentifier("drawable/" + country.toLowerCase().replace(" ", ""), "drawable", getPackageName());
	    }
	    
	    public ImageView getCountryImage(String country){
	    	ImageView ivCountry = new ImageView(this);
	    	ivCountry.setImageResource(getCountryImageResourceId(country));
			return ivCountry;
	    }
	    
	    public int getAlternatingColorResourceId(int counter){
	    	if ((counter % 2) == 0) {
				return getResources().getColor(R.color.res_color_bg_alternatingEven);
			}

			else {
				return getResources().getColor(R.color.res_color_bg_alternatingOdd);
			}
	    }
	    
	    public String convertDateTimeFormat(String input) throws ParseException{
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd kk:mm:ss");
			Date kickoffDate = dateFormat.parse(input);
			kickoffDate.setHours(kickoffDate.getHours() + 2);
			return (String) DateFormat.format("dd-MM-yyyy kk:mm", kickoffDate);
	    }
	}