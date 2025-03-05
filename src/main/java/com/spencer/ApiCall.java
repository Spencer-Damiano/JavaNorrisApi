package com.spencer;


import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class ApiCall {
    /*
     * Table: Summary of Key Components
     * | Component                  | Description                                      | Example Usage                          |
     * |----------------------------|--------------------------------------------------|----------------------------------------|
     * | CloseableHttpClient        | Manages HTTP connections                        | HttpClients.createDefault()            |
     * | HttpGet                    | Represents GET request                          | new HttpGet("http://targethost")       |
     * | execute                    | Sends request and gets response                 | httpclient.execute(httpGet)            |
     * | getStatusLine              | Gets HTTP status line                           | response1.getStatusLine()              |
     * | getEntity                  | Gets response body                              | response1.getEntity()                  |
     * | EntityUtils.toString       | Converts entity to string                       | EntityUtils.toString(entity1)          |
     * | EntityUtils.consume        | Releases entity resources                       | EntityUtils.consume(entity1)           |
     * | close                      | Closes response to free resources               | response1.close()                      |
     */
    public static void main(String[] args) throws IOException {
        getCategories();
    }

    public static void getCategories() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault() ){
            // 'CloseableHttpClient' used without 'try'-with-resources statement

            // Chuck Norris Joke Categories
            HttpGet httpGet = new HttpGet("https://api.chucknorris.io/jokes/categories");

            try (ClassicHttpResponse response = httpClient.execute(httpGet)) { // warning on this line: `'execute(org.apache.hc.core5.http.ClassicHttpRequest)' is deprecated`

                System.out.println(response.getCode() + " " + response.getReasonPhrase());

                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println(responseBody);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}