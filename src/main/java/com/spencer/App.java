package com.spencer;

import org.json.JSONObject;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        JSONObject jsFirst = new JSONObject();
        jsFirst.put("firstName", "John");
        jsFirst.put("lastName", "Doe");
        jsFirst.put("email", "john.doe@gmail.com");
        System.out.println( jsFirst.toString() );

        ApiCall apiCall = new ApiCall();

        ApiCall.getCategories();
    }
}
