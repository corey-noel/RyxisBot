package ryxis.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import ryxis.command.core.CommandEvent;
import ryxis.tools.StringTools;

public class LinkAnalyzer {
	
	private static final String FA_COOKIES = "a=9b428eb1-fa7e-4f7f-a244-285f8cdfac2f; b=8e3bb9f9-3489-4dce-9bcd-a181b2dc868b";
	private static final int CON_TIMEOUT = 15000;
	private static final int READ_TIMEOUT = 15000;
	
	private final static String YOUTUBE_LINK = "http://www.youtube.com/watch?v=";
	private final static String FA_LINK = "http://www.furaffinity.net/view/";
	private final static String PASTEBIN_LINK = "http://pastebin.com/";
			
	public String analyzeMessage(CommandEvent event) {
		String info = null;
		if (StringTools.containsURL(event.getMessage())) {
			String link = StringTools.getURL(event.getMessage());
			try {
				if (link.contains(FA_LINK))
					info = furAffinityAnalyzer(link);
				else if (link.contains(YOUTUBE_LINK))
					info = youTubeAnalyzer(link);
				else if (link.contains(PASTEBIN_LINK)) 
					info = pastebinAnalyzer(link);
			} catch (IOException e) {}
		}
		return info;
	}

	private static String pastebinAnalyzer(String link) throws IOException {
		return getTitle(link, true);
	}

	private static String furAffinityAnalyzer(String link) throws IOException {
		URLConnection con = new URL(link).openConnection();
		con.setConnectTimeout(CON_TIMEOUT);
		con.setReadTimeout(READ_TIMEOUT);
		con.addRequestProperty("Cookie", FA_COOKIES);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine = "", directLink = null, theme = null, title = null, category = null;
		while ((inputLine = in.readLine()) != null && (directLink == null || theme == null || title == null || category == null)) {
			if(inputLine.contains("Download")) {
				directLink = "http:" + inputLine.substring(inputLine.indexOf("//d.facdn.net"), inputLine.indexOf("Download"));
				directLink = directLink.substring(0, directLink.indexOf("\""));
			}
			else if(inputLine.contains("Theme"))
				theme = inputLine.substring(inputLine.indexOf("Theme:</b> ") + 11, inputLine.indexOf("<br/>"));
			else if(inputLine.contains("<title>"))
					title = inputLine.substring(inputLine.indexOf("<title>") + 7, inputLine.indexOf(" --"));
			else if(inputLine.contains("Category"))
				category = inputLine.substring(inputLine.indexOf("Category:</b> ") + 14, inputLine.indexOf("<br/>"));
		}		
		if (directLink == null || theme == null || title == null || category == null)
			return null;
		return title + ": " + directLink + " (" + theme + ") "
				+ (theme.contains("Adult") ? "(NSFW) " : "")  + 
				((!category.equalsIgnoreCase("all") && !category.equalsIgnoreCase("digitalart")) ? "(" + category + ") " : "" );
	}
	
	public static String sparkleeAnalyzer(String link) throws IOException {
		URLConnection con = (new URL(link).openConnection());
		con.setConnectTimeout(CON_TIMEOUT);
		con.setReadTimeout(READ_TIMEOUT);
		BufferedReader in = new BufferedReader(new InputStreamReader((con).getInputStream()));
		String inputLine = "", directLink = null;
		while ((inputLine = in.readLine()) != null && directLink == null)
			if(inputLine.contains("Direct Link")) {
				String temp = inputLine.substring(inputLine.indexOf("Direct Link"));
				directLink = temp.substring(temp.indexOf("http"), temp.indexOf(".gif") + 4);
			}
		return directLink;
	}
	
	public static String youTubeAnalyzer(String link) throws IOException {
		URLConnection con = new URL(link).openConnection();
		con.setConnectTimeout(CON_TIMEOUT);
		con.setReadTimeout(READ_TIMEOUT);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine = "", title = null, length = null;
		int numLikes = -1, numDislikes = -1;
		while ((inputLine = in.readLine()) != null && (title == null || length == null || numLikes == -1 || numDislikes == -1)) {
			if (inputLine.contains("<title>"))
				title = inputLine.substring(inputLine.indexOf("<title>") + 7, inputLine.indexOf("</title>") - 10);
			else if (inputLine.contains("<span class=\"likes-count\">"))
				numLikes = Integer.parseInt(StringTools.removeChar(inputLine.substring(
						inputLine.indexOf("<span class=\"likes-count\">") + 26, inputLine.indexOf("</span>")), ','));
			else if (inputLine.contains("<span class=\"dislikes-count\">"))
				numDislikes = Integer.parseInt(StringTools.removeChar(inputLine.substring(
						inputLine.indexOf("<span class=\"dislikes-count\">") + 29, inputLine.indexOf("</span>")), ','));
			else if (inputLine.contains("<meta itemprop=\"duration\" content=\""))
				length = inputLine.substring(inputLine.indexOf("<meta itemprop=\"duration\" content=\"PT") + 37, inputLine.indexOf("\">"));
		}
		if (title == null || length == null || numLikes == -1 || numDislikes == -1)
			return null;
		int minutes = Integer.parseInt(length.substring(0, length.indexOf("M"))), seconds = Integer.parseInt(length.substring(length.indexOf("M") + 1, length.length() - 1));
		return  title + " (" + StringTools.formatTime(0, minutes, seconds) + ") (Likes: " + numLikes + " Dislikes: " + numDislikes + ")";
	}
	
	public static String wolframAlphaAnalyzer(String link) throws IOException {
		URLConnection con = new URL(link).openConnection();
		con.setConnectTimeout(CON_TIMEOUT);
		con.setReadTimeout(READ_TIMEOUT);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String answer = "", inputLine, plaintext = "<plaintext>";
		int plaintextCount = 0;
		while ((inputLine = in.readLine()) != null && plaintextCount < 2) {
			System.out.println(inputLine);
			if (inputLine.contains("success='false'"))
				throw new IOException();
			if (inputLine.contains(plaintext)) {
				plaintextCount++;
				if (inputLine.contains(plaintext))
					answer += inputLine.substring(inputLine.indexOf(plaintext) + plaintext.length(), inputLine.indexOf("</plaintext>"));
				else
					answer += inputLine.substring(inputLine.indexOf(plaintext) + plaintext.length());
				if (plaintextCount == 1)
					answer += " | ";
			}
		}
		return answer;
	}
		
	public static String getTitle(String link, boolean formatted) throws IOException {
		URLConnection con = new URL(link).openConnection();
		con.setConnectTimeout(CON_TIMEOUT);
		con.setReadTimeout(READ_TIMEOUT);
		BufferedReader in = new BufferedReader(new InputStreamReader((con).getInputStream()));
		String inputLine = "", title = null;
		while ((inputLine = in.readLine()) != null && title == null)
			if (inputLine.contains("<title>"))
				title = inputLine.substring(inputLine.indexOf("<title>") + 7,
						inputLine.indexOf("</title>"));
		if (formatted)
			return title == null ? null : link + " - " + title;
		return title;
	}
	
}