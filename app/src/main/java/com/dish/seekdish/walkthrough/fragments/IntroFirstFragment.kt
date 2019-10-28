package com.dish.seekdish.walkthrough.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dish.seekdish.R
import com.dish.seekdish.util.UiHelper


class IntroFirstFragment : Fragment() {

    private val uiHelper = UiHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_walk_first, container, false)
        view.findViewById<TextView>(R.id.notificationAlertsTextView)
        view.findViewById<TextView>(R.id.notificationAlertsSubTitleTextView)
        view.findViewById<Button>(R.id.btn_register)

        return view
    }

}