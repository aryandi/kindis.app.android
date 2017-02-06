package sangmaneproject.kindis.helper;

/**
 * Created by DELL on 2/6/2017.
 */

public class ParseHtml {
    String style = "<style type=\"text/css\">\n" +
            "@font-face {\n" +
            "    font-family: MyFont;\n" +
            "    src: url(\"file:///android_asset/CitrixSans-Regular.ttf\")\n" +
            "}\n" +
            "body {\n" +
            "    font-family: MyFont;\n" +
            "    font-size: medium;\n" +
            "    text-align: justify;\n" +
            "    padding: 16px 16px 16px 16px;\n" +
            "}\n" +
            "</style>";

    public void parse(String tag, ResultListener<String> listener){
        String html = style+"<font color='white'>"+tag+"</font>";
        listener.onResult(html);
    }

    public interface ResultListener<T> {
        void onResult(String html);
    }
}
