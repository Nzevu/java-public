import java.util.*;
import java.net.*;
import java.io.*;
//import android.util.*;

/*
 General class to connect to online resources through HTTP by Bodika Kanyinda (eyowa muanetu). January 2024.
 Free to use. No warranties (written or implied). Use at own risk.
 */
public class ConnectionHelper implements Runnable
{
	//Use this map to all key/value pairs to be appended to the url address as a query string
	private Map<String, String> fieldMap=new LinkedHashMap<String, String>();

	//This is a default url used to test this Java class
	private String url="https://api.flickr.com/services/rest/?method=flickr.photos.getRecent";

	//Will hold response from the htttp server
	private String response="";

	//Will contain the query string to send to the http server.
	private String queryString="";

	//Set this to true if you are using this in a command line environment
	private boolean isCommandLine=true;

	//Does your url already contain a question mark? Then set this to false.
	private boolean useQuestionMark=true;

	public ConnectionHelper(){
		super();
	}

	public ConnectionHelper(String url){
		this.url=url;
	}

	public void clearFieldMap(){
		fieldMap.clear();
	}

	public void removeFieldFromMap(String key){
		fieldMap.remove(key);
	}

	public void addFieldToMap(String key, String value){
		fieldMap.put(key, value);
	}

	@Override
	public void run()
	{

		HttpURLConnection connection=null;
		try{
			URL dataUrl=new URL(buildUrlString());
			connection=(HttpURLConnection)dataUrl.openConnection();
			connection.connect();
			int status=connection.getResponseCode();
			if(isCommandLine)
				System.out.println("status is "+ status);
			else{
				//Log.d("connection", "status "+status);
			}
			if(status==200){
				InputStream is=connection.getInputStream();
				BufferedReader bReader=new BufferedReader(new InputStreamReader(is));
				StringBuilder sb=new StringBuilder();
			
				while((response=bReader.readLine())!=null){
					sb.append(response +"\n");
				}
				response=sb.toString();
				if(isCommandLine)
					System.out.println(response);
				else{
					//Log.i("response", response);
				}
			}

		}catch(Exception e){
			if(isCommandLine )
				System.out.println("error: "+e.getMessage());
			else{
				//Log.e("error", e.getMessage());
			}
				
		}finally{
			connection.disconnect();
		}
	}

	public void reset(){

		url="https://api.flickr.com/sevices/rest/?method=flickr.photos.getRecent&per_page=6&nojsoncallback=1";
		response="";
		queryString="";
		fieldMap.clear();
	}

	public String getResponse(){
		return this.response;
	}

	public String getQueryString(){
		setQueryString();
		return this.queryString;
	}

	public String getUrl(){
		return url;
	}

	public void setIsCommandLine(boolean val){
		isCommandLine=val;
	}


	//Examine your url, and if it already has a question mark, call setUseQuestionMark with the argument of (false). 
	public void setUseQuestionMark(boolean val){
		useQuestionMark=val;
	}

	public void setResponse(String val){
		this.response=val;
	}

	public void setUrl(String val){
		this.url=val;
	}

	public void setQueryString(){
		this.queryString="";
		StringBuilder sb=new StringBuilder();
		int nCount=fieldMap.size();
		int i=0;
		for(Map.Entry entry: fieldMap.entrySet()){
			i++;
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			if(i<nCount)
				sb.append("&");
		}
		this.queryString=sb.toString();
	}

	//Examine your url, and if it already has a question mark, call setUseQuestionMark with the argument of (false). 
	//It asks whether to use the question mark or the ampersand
	//to link the query string to the url address. Set this parameterto true to use a question mark.
	public String buildUrlString(){
		StringBuilder sb=new StringBuilder();
		sb.append(url);
		getQueryString();
		if(queryString.length()>0){
			if(useQuestionMark){
				sb.append("?");
			}
			else{
				sb.append("&");
			}
			sb.append(queryString);
		}
		return sb.toString();
	}
}

/*
Usage example:

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Hello World!");
		try{
			
			ConnectionHelper ch=new ConnectionHelper();
			ch.setIsCommandLine(true);
			ch.setUrl("https://api.flickr.com/services/rest/?method=flickr.photos.getRecent");
			ch.addFieldToMap("api_key", "YourApiKey");
			ch.addFieldToMap("per_page", "6");
			ch.addFieldToMap("format", "json");
			ch.setUseQuestionMark(false);
			
			new Thread(ch).start();
			String httpResponse=ch.getResponse();
			System.out.println(httpResponse);
		}catch(Exception e){
			System.out.println("error: "+e.getMessage());
		}
	}
}
*/
