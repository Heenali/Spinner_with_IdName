package com.example.spinner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class MainActivity extends Activity 
{
	SpinAdapterTimeDelay adap_dri;
	Spinner spi_country;
	ArrayList<String> id=new ArrayList<String>();
	ArrayList<String> name=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		spi_country=(Spinner)findViewById(R.id.sp);

		try
		{
			SyncMethod("http://test.trukker.ae/trukkerUAEApitest/Api/postorder/GetHomeSizeTypeCode?typecode=s");
		}
		catch (Exception e) 
	 	{
	 	e.printStackTrace();
	 	}


		spi_country.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
			{
				Toast.makeText(getApplicationContext(), name.get(arg2), Toast.LENGTH_LONG).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});

	}
	
	public void SyncMethod(final String GetUrl)
 	{
 		  Log.i("Url.............",GetUrl);
 		  final Thread background = new Thread(new Runnable() 
 		  {
 		      // After call for background.start this run method call
 		      public void run() 
 		      {
 		          try 
 		          {
 		        	  String url=GetUrl;
 		        	  String SetServerString = "";
 		        	
 		        	
					  SetServerString=fetchResult(url);
 		              threadMsg(SetServerString);
 		          }
 		          catch (Throwable t) 
 		          {
 		        	  Log.e("Animation", "Thread  exception " + t);
 		          }
 		      }
 		      private void threadMsg(String msg) 
 		      {
 		      	
 		          if (!msg.equals(null) && !msg.equals("")) 
 		          {
 		              Message msgObj = handler11.obtainMessage();
 		              Bundle b = new Bundle();
 		              b.putString("message", msg);
 		              msgObj.setData(b);
 		              handler11.sendMessage(msgObj);
 		          }
 		      }
 		      // Define the Handler that receives messages from the thread and update the progress
 		      private final Handler handler11 = new Handler()
 		      {
 		          public void handleMessage(Message msg) 
 		          {
 		        	 try
 		        	 {
 		              String aResponse = msg.getData().getString("message");
 		               Log.e("Exam","screen>>"+aResponse);

						 aResponse = aResponse.trim();
						 aResponse= new JSONTokener(aResponse).nextValue().toString();
						 JSONObject get_res = new JSONObject(aResponse);

						 JSONArray array = new JSONArray();
						 array = get_res.getJSONArray("message");

 		                    			final String[] countryid = new String[array.length()];
 	 		       						final String[] countryname = new String[array.length()];

 		                    			for(int aa=0;aa<array.length();aa++)
	 		                   		  	{
 		                    				 countryid[aa]=array.getJSONObject(aa).getString("SizeTypeCode");
 		                    				 countryname[aa]=array.getJSONObject(aa).getString("SizeTypeDesc");
 		                    				 id.add(array.getJSONObject(aa).getString("SizeTypeCode"));
 		                    				 name.add(array.getJSONObject(aa).getString("SizeTypeDesc"));
 		                    				
 		        							 Log.i("countryid",countryid[aa]);
 		                    					
 		                    			
	 		                   		  	}
 		                    			//Toast.makeText(getApplicationContext(),name.toString() , Toast.LENGTH_LONG).show();
 		                    			adap_dri=new SpinAdapterTimeDelay(MainActivity.this, id);
 		                    		

 		                    			spi_country.setAdapter(adap_dri);

 		        						

 		                    		
 		        	 }
 		        	 catch(Exception e)
 		        	 {
 		        		 
 		        	 }
 		        	
 		                      
 		          }
 		      };
 		  });
 		  // Start Thread
 		  background.start();
 	 }
			
//Method for connection	which using in SyncMethod Method		
			
public String fetchResult(String urlString) throws JSONException
	{
		StringBuilder builder;
		BufferedReader reader;
		URLConnection connection=null;
		URL url=null;
		String line;
		builder=new StringBuilder();
		reader=null;
		try {
			url=new URL(urlString);
			connection=url.openConnection();
			
			reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line=reader.readLine())!=null)
			{
				builder.append(line);
			}
			//Log.d("DATA", builder.toString());
		} catch (Exception e) {

		}
		//JSONArray arr=new JSONArray(builder.toString());
		return builder.toString();
	}
public class MyAdapterc extends BaseAdapter
{
	 
	  private ArrayList<country_method> myList;
	 
	  private Activity parentActivity;
	  private LayoutInflater inflater;
	 
	  public MyAdapterc(Activity parent, ArrayList<country_method> l) {
	   parentActivity = parent;
	   myList = l;
	   inflater = (LayoutInflater) parentActivity
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }
	 
	  @Override
	  public int getCount()
	  {
	   return myList.size();
	  }
	 
	  @Override
	  public Object getItem(int position)
	  {
		  return myList.get(position);
	  }
	 
	  @Override
	  public long getItemId(int position) 
	  {
		  return position;
	  }
	 
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent)
	  {
	   View view = convertView;
	   if (convertView == null)
	    view = inflater.inflate(R.layout.row, null);
	 
	   TextView text1 = (TextView) view.findViewById(R.id.textView1);
	   TextView text2 = (TextView) view.findViewById(R.id.textView2);
	   country_method myObj = myList.get(position);
	   text1.setText(Html.fromHtml(String.valueOf(myObj.getNumber())));
	   text2.setText(Html.fromHtml(myObj.getName()));
	   return view;
	  }
	 
	 }
public class SpinAdapterTimeDelay extends BaseAdapter 
{
	ArrayList<String> list;
	Context a;
	private LayoutInflater mInflater;	
	    public SpinAdapterTimeDelay(Context cc,ArrayList<String> list) {
	       this.a=cc;
	       this.list=list;
	       this.mInflater=(LayoutInflater)a.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View rowView = mInflater.inflate(R.layout.row, parent, false);
	    

	        TextView item = (TextView) rowView.findViewById(R.id.textView2);	
	       // Toast.makeText(a, String.valueOf(list.get(position)),Toast.LENGTH_LONG).show();
	        item.setText(list.get(position));
	        return rowView;
	}

}


}
