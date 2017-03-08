package com.example.phanluongha.myfirstapplication.request;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class JsonParser {

    final String TAG = "JsonParser.java";
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    static JSONObject jObj = null;
    static JSONArray jArr = null;
    public static final int HTTP_TIMEOUT = 300 * 1000; // milliseconds
    private Context context;

    public JsonParser(Context context) {
        this.context = context;
    }

    public JSONObject getPostJSONFromUrl(String url, JSONObject jsonobj) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();
        RequestBody body = RequestBody.create(JSON, jsonobj.toString());
        Request request = new Request.Builder().url(url)
                .post(body).build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }

    public JSONObject getPostJSONFromUrl(String url, MultipartBody.Builder m) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();
//        RequestBody body = RequestBody.create(JSON, jsonobj.toString());
//        RequestBody.
//        RequestBody body =
//        m = new MultipartBody.Builder();
//        m.setType(MultipartBody.FORM);
//        Iterator<String> keys = jsonobj.keys();
////        m.addFormDataPart("type", "exhibitor");
////        m.addFormDataPart("idExhibitor", "1");
////        m.addFormDataPart("idDevice", "12");
////        m.addFormDataPart("token", "Y2MzNWRjNWY3NjhkNDgzNg==");
//        while (keys.hasNext()) {
//            try {
//                String key = (String) keys.next();
//                m.addFormDataPart(key, jsonobj.getString(key));
//                Log.e(key, jsonobj.getString(key));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        Request request = new Request.Builder().url(url)
                .post(m.build()).build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }

    public JSONObject getPutJSONFromUrl(String url, JSONObject jsonobj) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // end check internet connection
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();
        RequestBody body = RequestBody.create(JSON, jsonobj.toString());
        Request request = new Request.Builder().url(url)
                .put(body).build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }

    public JSONObject getDeleteJSONFromUrl(String url, JSONObject jsonobj) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // end check internet connection

        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();
        RequestBody body = RequestBody.create(JSON, jsonobj.toString());
        Request request = new Request.Builder().url(url)
                .delete(body).build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }

    public JSONObject getJSONFromUrl(String url) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // end check internet connection
        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();

        Request request = new Request.Builder().url(url)
                .build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }

    public JSONArray getJSONArrayFromUrl(String url) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONArray("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // end check internet connection
        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();

        Request request = new Request.Builder().url(url)
                .build();
        jArr = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jArr = new JSONArray(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jArr = new JSONArray("[{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}]");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jArr = new JSONArray("[{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}]");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jArr == null)
            try {
                jArr = new JSONArray("[{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}]");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jArr;
    }

    public JSONObject getJSONFromUrl(String url, String header) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // end check internet connection
        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();

        Request request = new Request.Builder().url(url)
                .addHeader("Authentication", header).build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }

    public JSONObject getPostJSONFromUrl(String url, String header,
                                         JSONObject jsonobj) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // end check internet connection
        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();
        RequestBody body = RequestBody.create(JSON, jsonobj.toString());
        Request request = new Request.Builder().url(url)
                .addHeader("Authentication", header).post(body).build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }


    public JSONObject getDeleteJSONFromUrl(String url) {
        // end check internet connection
        if (context != null) {
            Boolean flag = isNetworkAvailable();
            if (!flag) {
                try {
                    return new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Không có kết nối mạng"
                            + "\"},\"status\":false}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // end check internet connection
        // end check location and timezone
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS).build();
        Request request = new Request.Builder().url(url)
                .delete(null).build();
        jObj = null;
        try {
            Response response = client.newCall(request).execute();
            try {
                jObj = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketTimeoutException) {
                try {
                    jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                            + "Hết thời gian kết nối"
                            + "\"},\"status\":false}");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if (jObj == null)
            try {
                jObj = new JSONObject("{\"error\":{\"code\":651,\"msg\":\""
                        + "Lỗi kết nối"
                        + "\"},\"status\":false}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return jObj;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
