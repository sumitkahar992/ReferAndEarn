package com.example.referandearn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.DynamicLink.IosParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}


@Composable
fun MyApp() {
    var longRefer: Uri
    var shortRefer: Uri
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        val intent1 = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
        val shortLink: String = ""
        val context = LocalContext.current


        Button(onClick = {

            createLink(Link = shortLink)


            val intent = Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT,
                    Uri.parse(shortLink).toString())
                .setType("text/plain")

            startActivity(context, intent, null)


        }
        )
        {
            Text(text = "Create Link")
        }




        Button(onClick = {
//            startActivity(context, intent1, null)

        }) {
            Text(text = "Share Link")
        }

        Text(text = "")

    }
}


private fun createLink(Link: String) {

    Log.d("main", "createLink")

    val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()

        .setLink(Uri.parse("https://www.referandearnmoney.com/"))

        .setDomainUriPrefix("https://referandearnmoney.page.link")

        // Open links with this app on Android
        .setAndroidParameters(
            AndroidParameters.Builder().build()
        )

        // Open links with com.example.ios on iOS
        .setIosParameters(IosParameters.Builder("com.example.ios").build())
        .buildDynamicLink()

    val dynamicLinkUri = dynamicLink.uri

    Log.d("main", "Long refer: ${dynamicLink.uri}")



    // Manually
    val sharelinktext = "https://referandearnmoney.page.link/?" +
            "link=http://www.google.com/" +
            "&apn=" + "com.example.referandearn" +
            "&st=" + "My Refer Link" +
            "&sd=" + "Reward Coins 20" +
            "&si=" + "https://www.blueappsoftware.com/logo-1.png"

    //Shorten Link
    val shortLinkTask: Task<ShortDynamicLink> =
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLongLink(sharelinktext.toUri())
            .buildShortDynamicLink()
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Short link created
                        val Link = task.result.shortLink
                        val flowchartLink: Uri? = task.result.previewLink
                        Log.d("main", "short Link: $Link ")
                    } else {
                        // Error
                        // ...
                        Log.d("main", "error...: ${task.exception}")





                    }
                },
            )
}























