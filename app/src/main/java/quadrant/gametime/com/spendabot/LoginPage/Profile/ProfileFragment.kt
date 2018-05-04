package quadrant.gametime.com.spendabot.LoginPage.Profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import quadrant.gametime.com.spendabot.LoginPage.Home.HomeActivity
import quadrant.gametime.com.spendabot.LoginPage.Models.Users
import quadrant.gametime.com.spendabot.LoginPage.Network.ApiClient
import quadrant.gametime.com.spendabot.LoginPage.Network.ApiService
import quadrant.gametime.com.spendabot.LoginPage.Utils.FileUtils
import quadrant.gametime.com.spendabot.LoginPage.Utils.PrefManager

import quadrant.gametime.com.spendabot.R
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

class ProfileFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var profileImage: ImageView
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var phoneNumber: TextInputEditText
    private lateinit var phoneVerify: TextInputEditText
    private lateinit var register: AppCompatButton
    private lateinit var prefManager: PrefManager
    private lateinit var storageRef: StorageReference
    private val FILE_SELECT_CODE = 0
    private val TAG = this.javaClass.name
    private lateinit var auth: FirebaseAuth
    private lateinit var apiService: ApiService
    private lateinit var progress: FrameLayout
    private var downloadUrl: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImage = rootView.findViewById(R.id.profileImage)
        firstName = rootView.findViewById(R.id.firstName)
        lastName = rootView.findViewById(R.id.lastName)
        phoneNumber = rootView.findViewById(R.id.phoneNumber)
        phoneVerify = rootView.findViewById(R.id.phoneVerify)
        register = rootView.findViewById(R.id.registerBtn)
        progress = rootView.findViewById(R.id.progress)
        prefManager = PrefManager(rootView.context)
        storageRef = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val apiClient = ApiClient()
        apiService = apiClient.getClient()!!.create(ApiService::class.java)

        profileImage.setOnClickListener {
            showFileChooser()
        }

        register.setOnClickListener {
            if (!TextUtils.isEmpty(firstName.text.toString()) && !TextUtils.isEmpty(lastName.text.toString()) &&
                    !TextUtils.isEmpty(phoneNumber.text.toString()) && !TextUtils.isEmpty(phoneVerify.text.toString()) &&
                    phoneNumber.text.toString().length == 10 && phoneVerify.text.toString().length == 6) {
                progress.visibility = View.VISIBLE
                if (downloadUrl != null) {
                    val random = Random()
                    val inRan = random.nextInt(52121) + 1023
                    val users = Users(inRan, downloadUrl.toString(),
                            firstName.text.toString(), lastName.text.toString(), phoneNumber.text.toString())
                    val profileUpload = apiService.createUser(firstName.text.toString(), users)
                    profileUpload.enqueue(object : retrofit2.Callback<Users> {
                        override fun onFailure(call: Call<Users>?, t: Throwable?) {
                            progress.visibility = View.GONE
                            Log.e(TAG, "There was some issue uploading the data. Please try again later....")
                            Toast.makeText(rootView.context, "Something went wrong. Please try again later..",
                                    Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<Users>?, response: Response<Users>?) {
                            progress.visibility = View.GONE
                            prefManager.setFirstTimeLogin(false)
                            (activity as HomeActivity).replaceFrags()
                        }
                    })
                } else {
                    Toast.makeText(rootView.context, "There was some problem and image is not saved. Please upload again...",
                            Toast.LENGTH_SHORT).show()
                }
            } else if (TextUtils.isEmpty(firstName.text.toString()) || TextUtils.isEmpty(lastName.text.toString()) ||
                    TextUtils.isEmpty(phoneNumber.text.toString()) || TextUtils.isEmpty(phoneVerify.text.toString()) ||
                    phoneNumber.text.toString().length < 10 || phoneVerify.text.toString().length < 6) {
                if (TextUtils.isEmpty(firstName.text.toString())) {
                    Toast.makeText(rootView.context, "Please enter a valid first name", Toast.LENGTH_SHORT).show()
                }
                if (TextUtils.isEmpty(lastName.text.toString())) {
                    Toast.makeText(rootView.context, "Please enter a valid last name", Toast.LENGTH_SHORT).show()
                }
                if (TextUtils.isEmpty(phoneNumber.text.toString())) {
                    Toast.makeText(rootView.context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                }
                if (TextUtils.isEmpty(phoneVerify.text.toString())) {
                    Toast.makeText(rootView.context, "Please enter a verification code", Toast.LENGTH_SHORT).show()
                }
                if (phoneNumber.text.toString().length < 10) {
                    Toast.makeText(rootView.context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                }
                if (phoneVerify.text.toString().length < 6) {
                    Toast.makeText(rootView.context, "Please enter a valid verification code", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return rootView
    }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(Intent.createChooser(intent,
                    "Select a file to upload"), FILE_SELECT_CODE)
        } catch (ex: Exception) {
            Toast.makeText(rootView.context, "Please install a file manager at least...", Toast.LENGTH_SHORT).show()
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT_CODE -> {
                if (resultCode == RESULT_OK) {
                    val uri = data!!.data
                    Glide.with(rootView.context).load(uri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(profileImage)
                    Log.d(TAG, "File Uri: " + uri.toString())
                    val path = FileUtils.getPath(rootView.context, uri)
                    Log.d(TAG, "File path: " + path)
                    val profile: StorageReference = if (auth.currentUser != null) {
                        storageRef.child("images/profile_"+auth.currentUser)
                    } else {
                        storageRef.child("images/profile_1234")
                    }
                    profile.putFile(uri)
                            .addOnSuccessListener { taskSnapshot ->
                                val downloadUri = taskSnapshot.downloadUrl
                                downloadUrl = downloadUri
                            }.addOnFailureListener {
                        Log.d(TAG, "Some error occurred....")
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
