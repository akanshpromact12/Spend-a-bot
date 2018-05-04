package quadrant.gametime.com.spendabot.LoginPage.Network

import quadrant.gametime.com.spendabot.LoginPage.Models.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Created by Akansh on 04-05-2018.
 */
interface ApiService {
    @PUT("user/new/{name}.json")
    fun createUser(@Path("name") name: String, @Body user: Users): Call<Users>
}