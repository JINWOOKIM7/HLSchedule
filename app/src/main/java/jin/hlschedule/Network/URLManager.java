package jin.hlschedule.Network;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLManager
{
    public static final String ENCODING_UTF8 = "UTF-8";
    public static final String ENCODING_EUCKR = "euc-kr";
    public static final String USER_AGENT_PC = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0";

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";

    public static InputStream getURLInputStream(String url)
    {
        URLConnection conn = getURLConnection(url, null, null);

        if( conn != null ){try{return conn.getInputStream();}catch(IOException e){}}

        // null is invalid url
        return null;
    }

    public static InputStream getURLInputStream(String url, String userAgent)
    {
        URLConnection conn = getURLConnection(url, userAgent, null);

        if( conn != null ){try{return conn.getInputStream();}catch(IOException e){}}

        // null is invalid url
        return null;
    }

    public static InputStream getURLInputStream(String url, String userAgent, String referer)
    {
        URLConnection conn = getURLConnection(url, userAgent, referer);

        if( conn != null ){try{return conn.getInputStream();}catch(IOException e){}}

        // null is invalid url
        return null;
    }

    private static URLConnection getURLConnection(String url, String userAgent, String referer)
    {
        URLConnection conn = null;

        try
        {
            URL _url = new URL(url);
            conn = _url.openConnection();
            if( userAgent != null )
                conn.addRequestProperty("User-Agent", userAgent);
            if( referer != null )
                conn.addRequestProperty("Referer", referer);
        }catch(Exception e)
        {
            // ignore
        }

        return conn;
    }

    public static InputStream doPost(String url, String data, String referer) throws Exception
    {
        HttpURLConnection connection = (HttpURLConnection)getURLConnection(url, USER_AGENT_PC, referer);
        connection.setInstanceFollowRedirects(false);
        connection.addRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(data);
        wr.flush();

        return connection.getInputStream();
    }

    public static byte[] getImage(String url, String referer)
    {
        InputStream in = getURLInputStream(url, USER_AGENT_PC, referer);

        if( in == null )
            return null;

        BufferedInputStream bis_url = new BufferedInputStream(in);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int len = 0;
        byte[] buf = new byte[1024];

        try
        {
            while( (len = bis_url.read(buf, 0, 1024)) != -1)
            {
                bos.write(buf, 0, len);
            }
            bis_url.close();
        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }

        // Convert to byte array
        byte[] img_raw = bos.toByteArray();

        return img_raw;
    }
}
