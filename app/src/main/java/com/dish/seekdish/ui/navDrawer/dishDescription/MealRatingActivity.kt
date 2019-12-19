package com.dish.seekdish.ui.navDrawer.dishDescription

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.walkthrough.WalkThroughActivity
import kotlinx.android.synthetic.main.activity_meal_rating.*


class MealRatingActivity : BaseActivity() {

    var mealId: String = ""
    var restId: String = ""
    var imageUrl:String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_rating)


        getIntents()


        tvBack.setOnClickListener()
        {
            finish()
        }


        tvNext.setOnClickListener()
        {
            val intent = Intent(this@MealRatingActivity, RestoRatingActivity::class.java)
            intent.putExtra("TASTE", tasteRating.rating)
            intent.putExtra("PRESENTATION", presentationRating.rating)
            intent.putExtra("TEXTURE", textureRating.rating)
            intent.putExtra("ODOR", odorRating.rating)
            intent.putExtra("MEAL_ID", mealId)
            intent.putExtra("RESTAURANT_ID", restId)
            intent.putExtra("IMAGE_Bitmap", imageUrl)

            startActivity(intent)
        }

    }

    private fun getIntents() {
        mealId = intent.getStringExtra("MEALID")
        restId = intent.getStringExtra("RESTROID")
         imageUrl = intent.getStringExtra("IMAGE")


        GlideApp.with(this)
            .load(imageUrl)
            .into(img_meal_full)

        GlideApp.with(this)
            .load(imageUrl)
            .into(img_meal_cir)

    }

}
