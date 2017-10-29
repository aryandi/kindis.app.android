package co.digdaya.kindis.live.network;

/**
 * Created by vincenttp on 9/18/17.
 */

public class ApiUtil {
    public ApiUtil() {
    }

    public static ApiCall callService(){
        return RetrofitClient.getClient().create(ApiCall.class);
    }
}
