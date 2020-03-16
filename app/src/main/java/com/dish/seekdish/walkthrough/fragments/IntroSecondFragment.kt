package com.dish.seekdish.walkthrough.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dish.seekdish.R
import com.dish.seekdish.ui.signup.SignupActivity
import com.dish.seekdish.util.UiHelper
import kotlinx.android.synthetic.main.fragment_walk_second.view.*

class IntroSecondFragment : Fragment() {

    private val uiHelper = UiHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_walk_second, container, false)
        view.findViewById<TextView>(R.id.easyPaymentTextView)
        view.findViewById<TextView>(R.id.easyPaymentSubTitleTextView)
        view.findViewById<Button>(R.id.btn_register)

        view.btn_register.setOnClickListener()
        {
            val intent = Intent(activity, SignupActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}