package com.dongsun.cardgame.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.dongsun.cardgame.R
import com.dongsun.cardgame.models.Card
import java.util.*
import kotlin.collections.ArrayList


class CardViewAdapter(activity: Activity, private val clickItemListener: ListItemClickListener) : BaseAdapter() {
    private var mItems: ArrayList<Card>
        get() = clickItemListener.getItems()

    var mItemAnimation: Hashtable<Int, Runnable>
    private var inflter: LayoutInflater

    // Constructor
    init {
        mItems = ArrayList()
        inflter = LayoutInflater.from(activity)
        mItemAnimation = Hashtable()
    }

    override fun getItem(position: Int): Any {
        return mItems.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mItems.size
    }

    // create a new ImageView for each item referenced by the Adapter
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val gridView: View
        gridView = if (convertView == null) {
            // get layout from mobile.xml
            inflter.inflate(R.layout.layout_listitem, null)
        } else {
            convertView
        }

        val textView = gridView.findViewById<TextView>(R.id.txt_number)
        textView.layoutParams.height = parent?.height!!/6

        val itemCard = mItems[position]
        if (itemCard.isSelected) {
            gridView.isEnabled = false
            gridView.setBackgroundColor(parent.context?.getColor(R.color.white)!!)
            textView.text = itemCard.number.toString()
        } else {
            gridView.setBackgroundColor(parent.context?.getColor(R.color.teal_700)!!)
            textView.text = "?"
        }

        gridView.setOnClickListener {
            if (mItemAnimation.size<2 && !mItemAnimation.containsKey(position)) {
                val runnable = Runnable {
                    startFirstAnimations(gridView, textView, position, itemCard, 2)
                }
                handler.postDelayed(runnable, 3000)
                mItemAnimation[position] = runnable
                clickItemListener.itemClickedPosition(position, itemCard)
                startFirstAnimations(gridView, textView, position, itemCard, 1)
            }
        }
        return gridView
    }

    private fun startFirstAnimations(gridView: View, textView:TextView, position:Int, card: Card, tag:Int) {
        val oa1 = ObjectAnimator.ofFloat(gridView, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(gridView, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            @SuppressLint("NewApi")
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                when(tag) {
                    1 -> {
                        gridView.setBackgroundColor(gridView.context.getColor(R.color.white))
                        textView.text = card.number.toString()
                    }
                    2 -> {
                        gridView.setBackgroundColor(gridView.context.getColor(R.color.teal_700))
                        textView.text = "?"
                        mItemAnimation.remove(position)
                        if (mItemAnimation.size == 0)
                            clickItemListener.itemUnClickedPosition(position, card)
                    }
                }
                oa2.start()
            }
        })
        oa1.start()
    }

    private val handler = Handler()
    fun stopAnimation() {
        val integerIterator: Iterator<Int?> = mItemAnimation.keys.iterator()
        integerIterator.forEach {
            mItemAnimation[it]?.let { it1 ->
                handler.removeCallbacks(it1)
            }
        }
        mItemAnimation.clear()
    }

    interface ListItemClickListener {
        fun getItems(): ArrayList<Card>
        fun itemClickedPosition(position: Int, item: Card)
        fun itemUnClickedPosition(position: Int, item: Card)
    }
}