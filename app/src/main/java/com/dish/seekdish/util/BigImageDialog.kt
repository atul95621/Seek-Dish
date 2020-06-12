package com.example.firestorecrud.chat.imageOpen

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.dish.seekdish.R

import kotlinx.android.synthetic.main.fragment_big_image_dialog.view.*

class BigImageDialog(): DialogFragment() {
    private var imageUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = arguments!!.getString("url").toString()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater!!.inflate(R.layout.fragment_big_image_dialog, container, false)
        this.dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        Glide.with(this).load(imageUrl)
            .into(v.bigImageView);
        return v
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(imageUrl: String) =
            BigImageDialog().apply {
                arguments = Bundle().apply {
                    putString("url", imageUrl)
                }
            }
    }
}