package phone.vishnu.quotes.repository;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import phone.vishnu.quotes.controller.AppController;
import phone.vishnu.quotes.model.Quote;
import phone.vishnu.quotes.response.QuoteListAsyncResponse;
import phone.vishnu.quotes.response.RandomQuoteAsyncResponse;

public class QuotesRepository {

    private final ArrayList<Quote> quoteArrayList = new ArrayList<>();

    public void getQuotes(final QuoteListAsyncResponse callBack) {

        final String url = "https://raw.githubusercontent.com/VishnuSanal/Quotes/master/Quotes.json";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        response -> {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject quoteObject = response.getJSONObject(i);

                                    Quote quote = new Quote();
                                    quote.setQuote(quoteObject.getString("quote"));
                                    quote.setAuthor(quoteObject.getString("name"));
                                    quoteArrayList.add(quote);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (null != callBack) {
                                callBack.processFinished(quoteArrayList);
                            }
                        },
                        Throwable::printStackTrace
                ) {
                    @Override
                    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                        try {
                            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                            if (cacheEntry == null) {
                                cacheEntry = new Cache.Entry();
                            }
                            final long cacheHitButRefreshed = 1000;
                            final long cacheExpired = 7 * 24 * 60 * 60 * 1000;
                            long now = System.currentTimeMillis();
                            final long softExpire = now + cacheHitButRefreshed;
                            final long ttl = now + cacheExpired;
                            cacheEntry.data = response.data;
                            cacheEntry.softTtl = softExpire;
                            cacheEntry.ttl = ttl;
                            String headerValue;
                            headerValue = response.headers.get("Date");
                            if (headerValue != null) {
                                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                            }
                            headerValue = response.headers.get("Last-Modified");
                            if (headerValue != null) {
                                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                            }
                            cacheEntry.responseHeaders = response.headers;
                            final String jsonString = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString), cacheEntry);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            return Response.error(new ParseError(e));
                        }
                    }

                    @Override
                    protected void deliverResponse(JSONArray response) {
                        super.deliverResponse(response);
                    }

                    @Override
                    public void deliverError(VolleyError error) {
                        super.deliverError(error);
                    }

                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        return super.parseNetworkError(volleyError);
                    }

                };
                AppController.getInstance().addToRequestQueue(jsonArrayRequest);
            }
        });
        thread.start();
    }

    public void getRandomQuote(final RandomQuoteAsyncResponse callBack) {

        final String url = "https://raw.githubusercontent.com/VishnuSanal/Quotes/master/Quotes.json";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        response -> {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject quoteObject = response.getJSONObject(i);

                                    Quote quote = new Quote();
                                    quote.setQuote(quoteObject.getString("quote"));
                                    quote.setAuthor(quoteObject.getString("name"));
                                    quoteArrayList.add(quote);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (null != callBack) {
                                Collections.shuffle(quoteArrayList);
                                callBack.processFinished(quoteArrayList.get(0));
                            }
                        },
                        Throwable::printStackTrace
                ) {
                    @Override
                    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                        try {
                            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                            if (cacheEntry == null) {
                                cacheEntry = new Cache.Entry();
                            }
                            final long cacheHitButRefreshed = 1000;
                            final long cacheExpired = 7 * 24 * 60 * 60 * 1000;
                            long now = System.currentTimeMillis();
                            final long softExpire = now + cacheHitButRefreshed;
                            final long ttl = now + cacheExpired;
                            cacheEntry.data = response.data;
                            cacheEntry.softTtl = softExpire;
                            cacheEntry.ttl = ttl;
                            String headerValue;
                            headerValue = response.headers.get("Date");
                            if (headerValue != null) {
                                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                            }
                            headerValue = response.headers.get("Last-Modified");
                            if (headerValue != null) {
                                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                            }
                            cacheEntry.responseHeaders = response.headers;
                            final String jsonString = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString), cacheEntry);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            return Response.error(new ParseError(e));
                        }
                    }

                    @Override
                    protected void deliverResponse(JSONArray response) {
                        super.deliverResponse(response);
                    }

                    @Override
                    public void deliverError(VolleyError error) {
                        super.deliverError(error);
                    }

                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        return super.parseNetworkError(volleyError);
                    }

                };
                AppController.getInstance().addToRequestQueue(jsonArrayRequest);
            }
        });
        thread.start();
    }
}
