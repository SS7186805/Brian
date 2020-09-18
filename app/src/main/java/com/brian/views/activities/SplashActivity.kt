package com.brian.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedActivity

class SplashActivity : ScopedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            if(Prefs.init().isLogIn.equals("true")){
                startActivity(Intent(this,HomeActivity::class.java))

            }else{
                startActivity(Intent(this,AccountHandlerActivity::class.java))

            }
            finish()
        }, 2000)

    }
}
