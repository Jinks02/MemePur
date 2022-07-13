package com.example.memeshareapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var CurrentImageURL: String?=null  //url string which will be null initially

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private fun loadMeme(){
        // Instantiate the RequestQueue.
        // volley library is used to transmit data over the network. It actually makes networking faster and easier for Apps.
        // it is also used to make api calls
        ProgressBar.visibility=View.VISIBLE // making the progressbar visible
//        val queue = Volley.newRequestQueue(this)   // acc to google use singleton for Volley
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
//        val stringRequest = StringRequest(
//            Request.Method.GET, url,
//            { response ->                   // request succeeded
//
//
//            },
//            Response.ErrorListener {
//                                            // error in request eg. internet prob or api prob
//            })




       // use logd to log in logcat
       // now creating json obj request as the api is in that form only
        val JSONobjRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->                   // request succeeded


                CurrentImageURL= response.getString("url") // extracting url from api link
                // to put this url into image of android, we use glide library
                Glide.with(this).load(CurrentImageURL).listener(object:RequestListener<Drawable>{  // listener for progressbar

                    override fun onLoadFailed(           // if loading failed then this is called
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ProgressBar.visibility=View.GONE  // removing progressbar
                        return false
                    }

                    override fun onResourceReady(      // if load successful
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ProgressBar.visibility=View.GONE
                        return false
                    }


                }).into(MemeImageView)

            },
            Response.ErrorListener {
                                            // error in request eg. internet prob or api prob
                Toast.makeText(this,"Something went wrong!",Toast.LENGTH_LONG).show()
            })




        // Add the request to the RequestQueue.
//        queue.add(JSONobjRequest)
        MySingletonForVolley.getInstance(this).addToRequestQueue(JSONobjRequest) //using singleton pattern
    }

    fun shareMeme(view: View) {
        //intent is also used for interprocess communication
        val intent=Intent(Intent.ACTION_SEND)  // this intent will help us send
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! have a look at this amazing meme from Reddit, here's the link $CurrentImageURL")
        // now making the chooser to choose to share from different options:

        val chooser =Intent.createChooser(intent,"Choose any option to share...")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {

        loadMeme()
    }
}