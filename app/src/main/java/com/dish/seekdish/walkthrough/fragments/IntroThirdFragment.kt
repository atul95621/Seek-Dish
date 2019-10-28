package com.dish.seekdish.walkthrough.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dish.seekdish.R
import com.dish.seekdish.util.UiHelper

import kotlinx.android.synthetic.main.fragment_walk_third.view.*

class IntroThirdFragment : Fragment() {

    private val uiHelper = UiHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_walk_third, container, false)
//        view.findViewById<TextView>(R.id.moneyRecordTextView).typeface = uiHelper.getTypeFace(TypeFaceEnum.HEADING_TYPEFACE, activity!!)
//        view.findViewById<TextView>(R.id.moneyRecordSubTitleTextView).typeface = uiHelper.getTypeFace(TypeFaceEnum.SEMI_TITLE_TYPEFACE, activity!!)

        view.findViewById<TextView>(R.id.moneyRecordTextView)
        view.findViewById<TextView>(R.id.moneyRecordSubTitleTextView)
        view.findViewById<Button>(R.id.btn_register)


        view.moneyRecordTextView.setOnClickListener()
        {
            Log.e("status", "thrird one preessed")
        }
        return view
    }
}