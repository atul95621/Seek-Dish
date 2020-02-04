package com.dish.seekdish.ui.navDrawer.dishDescription

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dish.seekdish.util.BaseActivity
import com.dish.seekdish.R
import com.dish.seekdish.custom.GlideApp
import com.dish.seekdish.custom.PagerContainer
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Ingredients
import com.dish.seekdish.ui.navDrawer.dishDescription.model.Meals
import kotlinx.android.synthetic.main.activity_dish_detail.*
import kotlinx.android.synthetic.main.activity_opinion_details.*
import kotlinx.android.synthetic.main.activity_opinion_details.tvBack
import java.util.HashSet
import android.text.TextUtils.join as join1


class DishDetailActivity : BaseActivity() {


    // for images
    internal lateinit var mContainer: PagerContainer
    internal lateinit var pager: ViewPager
    internal lateinit var adapterPager: PagerAdapter

    var seasonStr: String = ""
    var status: String = ""
    var tagsStr: String = ""
    var intolerStr: String = ""
    var seasonArr = HashSet<String>()
    var statusArr = HashSet<String>()
    var intolerArr = HashSet<String>()
    var tagsArr = HashSet<String>()


    val mResources: ArrayList<String> = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_detail)

        getBundleDataAndSet();

        //for swipe images on top
        initializeviews()

        tvBack.setOnClickListener()
        {
            finish()
        }

    }

    private fun getBundleDataAndSet() {
        mResources.clear()

        val dishMealModel = intent.getSerializableExtra("MEAL_SEARIALIZE") as Meals
        val dishIngredientModel = intent.getSerializableExtra("INGREDIENT_SEARIALIZE") as Ingredients

        Log.e("searilBudget", ":   " + dishMealModel.budget)

        // feeding the image to the list
        var imageMeal = dishMealModel.meal_image
        mResources.add(imageMeal)

        tvRestroName.setText(dishMealModel.restro_name)
        tvBudget.setText(dishMealModel.budget)
        tvCalories.setText(dishMealModel.calories.toString())
        tvPerson.setText(dishMealModel.meal_people)
        tvSpeed.setText(dishMealModel.preperation_time)
        tvPrepTime.setText(dishMealModel.preperation_time)
        tvTypeDish.setText(dishMealModel.meal_type)

        for (item in dishMealModel.seasons) {
            seasonArr.add(item)
        }
        seasonStr = join1(" | ", seasonArr)
        Log.e("seasonArr", "" + seasonArr)
        tvSeasonlity.setText(seasonStr)

        for (items in dishMealModel.meal_status) {
            statusArr.add(items)
        }
        status = join1(" | ", statusArr)
        tvStatus.setText(status)

        for (i in dishIngredientModel.intolerance_compatibilities) {
            intolerArr.add(i)
        }
        intolerStr = join1(" | ", intolerArr)
        tvIntolrence.setText(intolerStr)

        for (j in dishIngredientModel.meal_tags) {
            tagsArr.add(j)
        }
        tagsStr = join1(" | ", tagsArr)
        tvIntolrence.setText(tagsStr)

        tvDishName.setText(dishMealModel.meal_name)
        starRating.rating = dishMealModel.meal_avg_rating.toFloat()
        euroRating.rating = dishMealModel.budget.toFloat()

    }

    private fun initializeviews() {

        mContainer = findViewById(R.id.pager_container) as PagerContainer
        pager = mContainer.viewPager
        adapterPager = MyPageradapterPager(this)
        pager.adapter = adapterPager
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.offscreenPageLimit = adapterPager.count
        //A little space between pages
        pager.pageMargin = 15
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.clipChildren = false
    }

//Nothing special about this adapterPager, just throwing up colored views for demo

    private inner class MyPageradapterPager(internal var mContext: Context) : PagerAdapter() {
        override fun getCount(): Int {
            return mResources.size
        }

        internal var mLayoutInflater: LayoutInflater

        init {
            mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as LinearLayout
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = mLayoutInflater.inflate(R.layout.pager_detail_item, container, false)

            val imageView = itemView.findViewById(R.id.imageView) as ImageView

//            imageView.setOnClickListener()
//            {
//                val intent = Intent(this@DishDetailActivity, DishDetailActivity::class.java)
//                startActivity(intent)
//            }


            GlideApp.with(this@DishDetailActivity)
                .load(mResources[position])
                .placeholder(R.drawable.app_logo)
                .into(imageView)


//            imageView.setImageResource(mResources[position])
            container.addView(itemView)


            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as LinearLayout)
        }
    }
}
