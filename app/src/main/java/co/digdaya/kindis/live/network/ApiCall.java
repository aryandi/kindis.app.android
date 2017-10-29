package co.digdaya.kindis.live.network;

import co.digdaya.kindis.live.model.PriceListModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by vincenttp on 10/28/17.
 */

public interface ApiCall {
    @GET
    Call<PriceListModel> priceList(@Url String url);
}
