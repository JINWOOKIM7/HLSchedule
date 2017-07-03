package jin.hlschedule.Network;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CookieManager
{
    private static CookieManager instance = null;

    private static final String COOKIE_ID = "SESSION";

    private CookieManager(){	}

    public static CookieManager getInstance()
    {
        if( instance == null )
            instance = new CookieManager();
        return instance;
    }

    public String doPost(String urlString, String data, String cook)
            throws Exception
    {
        List<String> cookie = null;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        if(connection != null)

        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");
        if( cook != null )
            connection.setRequestProperty("Cookie", cook);

        OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
        os.write(data);
        os.flush();

        Map<String,List<String>> headerFields = connection.getHeaderFields();
        Set<String> set = headerFields.keySet();
        Iterator<String> itor = set.iterator();
        while (itor.hasNext())
        {
            String key = itor.next();

            if( key != null )
            {
                if( key.equals("Set-Cookie") )
                {
                    cookie = headerFields.get(key);
                }
            }
        }

        os.close();
        if( cookie != null )
        {
            itor = cookie.iterator();
            while( itor.hasNext() )
            {
                String return_cookie = itor.next();
                if( return_cookie.contains(COOKIE_ID) )
                    return return_cookie;
            }
        }

        return null;

    }


    public URLConnection setCookie(String urlString, String cookie)
            throws Exception
    {
        URLConnection conn = null;

        URL url = new URL(urlString);
        conn = url.openConnection();
        conn.setRequestProperty("Cookie", cookie);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.connect();

        return conn;
    }
}
