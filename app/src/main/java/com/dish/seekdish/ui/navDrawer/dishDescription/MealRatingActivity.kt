package com.dish.seekdish.ui.navDrawer.dishDescription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dish.seekdish.R
import com.dish.seekdish.walkthrough.WalkThroughActivity
import kotlinx.android.synthetic.main.activity_meal_rating.*

class MealRatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_rating)

        tvBack.setOnClickListener()
        {
            finish()
        }


        tvNext.setOnClickListener()
        {
            val intent = Intent(this@MealRatingActivity, RestoRatingActivity::class.java)
            startActivity(intent)
        }

    }
}
