package quadrant.gametime.com.spendabot.LoginPage.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Akansh on 04-05-2018.
 */
class ApiClient {
    val BASE_URL = "https://spendabot.firebaseio.com/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit
    }
}