package com.dongsun.cardgame

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dongsun.cardgame.adapters.CardViewAdapter
import com.dongsun.cardgame.models.Card
import com.dongsun.cardgame.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CardViewAdapter.ListItemClickListener {
    private lateinit var mAdapter: CardViewAdapter
    private lateinit var mMainActivityViewModel: MainActivityViewModel
    private var cardItems: ArrayList<Card> = ArrayList()
    private var mFirstSelected: Int = -1
    private var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        startNewGame()

        btnreset.setOnClickListener {
            startNewGame()
            counter = 0
            txtcounter.text = counter.toString()
        }
    }

    private fun startNewGame() {
        mAdapter.stopAnimation()
        mMainActivityViewModel.init()
        mMainActivityViewModel.cardLiveData.observe(this, Observer { items ->
            cardItems = items as ArrayList<Card>
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun initRecyclerView() {
        mAdapter = CardViewAdapter(this, this)
        grid_list.adapter = mAdapter
    }

    override fun getItems(): ArrayList<Card> = cardItems

    override fun itemClickedPosition(position: Int, number: Card) {
        counter++
        txtcounter.text = counter.toString()
        if (mFirstSelected == -1)
            mFirstSelected = position
        else {
            if (cardItems[mFirstSelected].number == number.number) {
                cardItems[mFirstSelected].isSelected = true
                cardItems[position].isSelected = true
                mAdapter.stopAnimation()
                mAdapter.notifyDataSetChanged()
                mFirstSelected = -1
            } else {
                mFirstSelected = position
            }

            if (verifyAllCorrect()) {
                showDialog("Congratulation!")
            }
        }
    }

    private fun verifyAllCorrect():Boolean {
        val filter = cardItems.filter {
            it.isSelected
        }
        return filter.size == cardItems.size
    }

    private fun showDialog(message:String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton("OK") {
            dialogInterface, i ->
            dialogInterface.dismiss()
            startNewGame()
        }
        builder.show()
    }

    override fun itemUnClickedPosition(position: Int, number: Card) {
        mFirstSelected = -1
    }
}