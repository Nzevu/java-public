ConnectionHelper is a Java class to connect to online resources.

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
