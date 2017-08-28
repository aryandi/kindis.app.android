package co.digdaya.kindis.live.helper;

/**
 * Created by DELL on 2/6/2017.
 */

public class ParseHtml {
    String style = "<style type=\"text/css\">" +
            "@font-face {" +
            "    font-family: MyFont;" +
            "    src: url(\"file:///android_asset/CitrixSans-Regular.ttf\")" +
            "}" +
            "body {" +
            "    font-family: MyFont;" +
            "    font-size: medium;" +
            "    text-align: justify;" +
            "    padding: 16px 16px 16px 16px;" +
            "}" +
            "</style>";

    public void parse(String tag, ResultListener<String> listener){
        System.out.println("ParseHtml: "+style);
        System.out.println("ParseHtml: "+tag);
        String html = style+"<font color='#ffffff'>"+tag+"</font>";
        System.out.println("ParseHtml: "+html);
        listener.onResult(html);
    }

    public interface ResultListener<T> {
        void onResult(String html);
    }
}
