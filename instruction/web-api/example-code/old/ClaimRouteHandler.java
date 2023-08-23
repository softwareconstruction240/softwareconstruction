import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

/*
	The ClaimRouteHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/routes/claim" URL path.
	
	Notice that ClaimRouteHandler implements the HttpHandler interface,
	which is define by Java.  This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/routes/claim" URL path, it calls 
	ClaimRouteHandler.handle() which actually processes the request.
*/
class ClaimRouteHandler implements HttpHandler {
	
	// Handles HTTP requests containing the "/routes/claim" URL path.
	// The "exchange" parameter is an HttpExchange object, which is
	// defined by Java.
	// In this context, an "exchange" is an HTTP request/response pair
	// (i.e., the client and server exchange a request and response).
	// The HttpExchange object gives the handler access to all of the
	// details of the HTTP request (Request type [GET or POST], 
	// request headers, request body, etc.).
	// The HttpExchange object also gives the handler the ability
	// to construct an HTTP response and send it back to the client
	// (Status code, headers, response body, etc.).
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		// This handler allows a "Ticket to Ride" player to claim ability
		// route between two cities (part of the Ticket to Ride game).
		// The HTTP request body contains a JSON object indicating which
		// route the caller wants to claim (a route is defined by two cities).
		// This implementation is clearly unrealistic, because it
		// doesn't actually do anything other than print out the received JSON string.
		// It is also unrealistic in that it accepts only one specific
		// hard-coded auth token.
		// However, it does demonstrate the following:
		// 1. How to get the HTTP request type (or, "method")
		// 2. How to access HTTP request headers
		// 3. How to read JSON data from the HTTP request body
		// 4. How to return the desired status code (200, 404, etc.)
		//		in an HTTP response
		// 5. How to check an incoming HTTP request for an auth token
		
		boolean success = false;
		
		try {
			// Determine the HTTP request type (GET, POST, etc.).
			// Only allow POST requests for this operation.
			// This operation requires a POST request, because the
			// client is "posting" information to the server for processing.
			if (exchange.getRequestMethod().toLowerCase().equals("post")) {
				
				// Get the HTTP request headers
				Headers reqHeaders = exchange.getRequestHeaders();
				// Check to see if an "Authorization" header is present
				if (reqHeaders.containsKey("Authorization")) {
				
					// Extract the auth token from the "Authorization" header
					String authToken = reqHeaders.getFirst("Authorization");
					
					// Verify that the auth token is the one we're looking for
					// (this is not realistic, because clients will use different
					// auth tokens over time, not the same one all the time).
					if (authToken.equals("afj232hj2332")) {
					
						// Extract the JSON string from the HTTP request body
						
						// Get the request body input stream
						InputStream reqBody = exchange.getRequestBody();
						
						// Read JSON string from the input stream
						String reqData = readString(reqBody);
						
						// Display/log the request JSON data
						System.out.println(reqData);

						// TODO: Claim a route based on the request data

						/*
						LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);

						LoginService service = new LoginService();
						LoginResult result = service.login(request);

						exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
						OutputStream resBody = exchange.getResponseBody();
						gson.toJson(result, resBody);
						resBody.close();
						*/

						// Start sending the HTTP response to the client, starting with
						// the status code and any defined headers.
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
						
						// We are not sending a response body, so close the response body
						// output stream, indicating that the response is complete.
						exchange.getResponseBody().close();
											
						success = true;
					}
				}
			}
			
			if (!success) {
				// The HTTP request was invalid somehow, so we return a "bad request"
				// status code to the client.
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
				
				// We are not sending a response body, so close the response body
				// output stream, indicating that the response is complete.
				exchange.getResponseBody().close();
			}
		}
		catch (IOException e) {
			// Some kind of internal error has occurred inside the server (not the 
			// client's fault), so we return an "internal server error" status code
			// to the client.
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
			
			// We are not sending a response body, so close the response body
			// output stream, indicating that the response is complete.
			exchange.getResponseBody().close();
			
			// Display/log the stack trace
			e.printStackTrace();
		}
	}
	
	/*
		The readString method shows how to read a String from an InputStream.
	*/
	private String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader sr = new InputStreamReader(is);
		char[] buf = new char[1024];
		int len;
		while ((len = sr.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}
}
