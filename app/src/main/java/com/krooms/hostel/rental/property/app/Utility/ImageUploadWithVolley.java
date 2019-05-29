package com.krooms.hostel.rental.property.app.Utility;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadWithVolley<T> extends Request<JSONObject> {

    private final Response.Listener<JSONObject> mListener;
    private final File yourImageFile;
    protected Map<String, String> headers;
    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private JSONObject jsonObject;

    public ImageUploadWithVolley(String url, Response.ErrorListener errorListener, Response.Listener<JSONObject> listener, File imageFile, JSONObject jsonObject) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        this.jsonObject = jsonObject;
        yourImageFile = imageFile;
        addImageEntity();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        headers.put("Accept", "application/json");
        return headers;
    }

    private void addImageEntity() {
        mBuilder.addTextBody("data", jsonObject.toString());
        mBuilder.addBinaryBody("_FILE", yourImageFile, ContentType.create("image/jpg"), yourImageFile.getName());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    @Override
    public String getBodyContentType() {
        String content = mBuilder.build().getContentType().getValue();
        return content;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        JSONObject result = null;
        try {
            result = new JSONObject(new String(response.data));
        } catch (Exception e) {

        }
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
