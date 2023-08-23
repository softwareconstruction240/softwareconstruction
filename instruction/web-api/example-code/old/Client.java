import java.io.*;
import java.net.*;

/*
	The Client class shows how to call a web API operation from
	a Java program.  This is typical of how your Android client
	app will call the web API operations of your server.
*/
public class Client {

	// The client's "main" method.
	// The "args" parameter should contain two command-line arguments:
	// 1. The IP address or domain name of the machine running the server
	// 2. The port number on which the server is accepting client connections
	public static void main(String[] args) {
	
		String serverHost = args[0];
		String serverPort = args[1];
		
		getGameList(serverHost, serverPort);
		claimRoute(serverHost, serverPort);	
	}
	
	// The getGameList method calls the server's "/games/list" operation to
	// retrieve a list of games running in the server in JSON format
	private static void getGameList(String serverHost, String serverPort) {
		
		// This method shows how to send a GET request to a server
		
		try {
			// Create a URL indicating where the server is running, and which
			// web API operation we want to call
			URL url = new URL("http://" + serverHost + ":" + serverPort + "/games/list");
			
			
			// Start constructing our HTTP request
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			
			
			// Specify that we are sending an HTTP GET request
			http.setRequestMethod("GET");
			
			// Indicate that this request will not contain an HTTP request body
			http.setDoOutput(false);
			
			
			// Add an auth token to the request in the HTTP "Authorization" header
			http.addRequestProperty("Authorization", "afj232hj2332");
			
			// Specify that we would like to receive the server's response in JSON
			// format by putting an HTTP "Accept" header on the request (this is not
			// necessary because our server only returns JSON responses, but it
			// provides one more example of how to add a header to an HTTP request).
			http.addRequestProperty("Accept", "application/json");
			
			
			// Connect to the server and send the HTTP request
			http.connect();
			
			// By the time we get here, the HTTP response has been received from the server.
			// Check to make sure that the HTTP response from the server contains a 200
			// status code, which means "success".  Treat anything else as a failure.
			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				
				// Get the input stream containing the HTTP response body
				InputStream respBody = http.getInputStream();			
			
				// Extract JSON data from the HTTP response body
				String respData = readString(respBody);

				// Display the JSON data returned from the server
				System.out.println(respData);
			}
			else {
				// The HTTP response status code indicates an error 
				// occurred, so print out the message from the HTTP response
				System.out.println("ERROR: " + http.getResponseMessage());

				// Get the error stream containing the HTTP response body (if any)
				InputStream respBody = http.getErrorStream();

				// Extract data from the HTTP response body
				String respData = readString(respBody);

				// Display the data returned from the server
				System.out.println(respData);
			}
		}
		catch (IOException e) {
			// An exception was thrown, so display the exception's stack trace
			e.printStackTrace();
		}
	}
	
	// The claimRoute method calls the server's "/routes/claim" operation to
	// claim the route between Atlanta and Miami
	private static void claimRoute(String serverHost, String serverPort) {
		
		// This method shows how to send a POST request to a server

		try {
			// Create a URL indicating where the server is running, and which
			// web API operation we want to call
			URL url = new URL("http://" + serverHost + ":" + serverPort + "/routes/claim");
			
			
			// Start constructing our HTTP request
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			
			
			// Specify that we are sending an HTTP POST request
			http.setRequestMethod("POST");

			// Indicate that this request will contain an HTTP request body
			http.setDoOutput(true);	// There is a request body

			
			// Add an auth token to the request in the HTTP "Authorization" header
			http.addRequestProperty("Authorization", "afj232hj2332");

			// Specify that we would like to receive the server's response in JSON
			// format by putting an HTTP "Accept" header on the request (this is not
			// necessary because our server only returns JSON responses, but it
			// provides one more example of how to add a header to an HTTP request).
			http.addRequestProperty("Accept", "application/json");
					
			// Connect to the server and send the HTTP request
			http.connect();
			
			// This is the JSON string we will send in the HTTP request body
			String reqData = 
			"{" +
				"\"route\": \"atlanta-miami\"" + 
			"}";

			
			// Get the output stream containing the HTTP request body
			OutputStream reqBody = http.getOutputStream();

			// Write the JSON data to the request body
			writeString(reqData, reqBody);

			// Close the request body output stream, indicating that the
			// request is complete
			reqBody.close();

			
			// By the time we get here, the HTTP response has been received from the server.
			// Check to make sure that the HTTP response from the server contains a 200
			// status code, which means "success".  Treat anything else as a failure.
			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

				// The HTTP response status code indicates success, 
				// so print a success message
				System.out.println("Route successfully claimed.");
			}
			else {
				
				// The HTTP response status code indicates an error 
				// occurred, so print out the message from the HTTP response
				System.out.println("ERROR: " + http.getResponseMessage());

				// Get the error stream containing the HTTP response body (if any)
				InputStream respBody = http.getErrorStream();

				// Extract data from the HTTP response body
				String respData = readString(respBody);

				// Display the data returned from the server
				System.out.println(respData);
			}
		}
		catch (IOException e) {
			// An exception was thrown, so display the exception's stack trace
			e.printStackTrace();
		}
	}
	
	/*
		The readString method shows how to read a String from an InputStream.
	*/
	private static String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader sr = new InputStreamReader(is);
		char[] buf = new char[1024];
		int len;
		while ((len = sr.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}
	
	/*
		The writeString method shows how to write a String to an OutputStream.
	*/
	private static void writeString(String str, OutputStream os) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(os);
		sw.write(str);
		sw.flush();
	}

}