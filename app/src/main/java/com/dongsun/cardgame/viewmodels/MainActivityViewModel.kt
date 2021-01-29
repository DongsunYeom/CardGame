package com.dongsun.cardgame.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongsun.cardgame.models.Card
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel : ViewModel() {
    var cardList: MutableLiveData<ArrayList<Card>> = MutableLiveData()
    fun init() {
        createCardGameAsync()
    }

    private fun createCardGameAsync() {
        object : AsyncTask<Void?, Void?, ArrayList<Card>>() {
            override fun doInBackground(vararg params: Void?): ArrayList<Card> {
                return createCardGame()
            }
            override fun onPostExecute(array: ArrayList<Card>) {
                super.onPostExecute(array)
                cardList.postValue(array)
            }

        }.execute()
    }

    fun createCardGame(): ArrayList<Card> {
        val tempList: HashMap<Int, Int> = HashMap()
        while (tempList.size < CARD_TOTAL_SIZE / 2) {
            val random = Random().nextInt(CARD_PAIRS_VALUE) + 1
            if (!tempList.containsKey(random)) {
                tempList[random] = 2
            }
        }
        val tempArray = IntArray(12)
        val integerIterator: Iterator<Int?> = tempList.keys.iterator()
        integerIterator.forEach {
            var value = tempList[it]!!
            while (value > 0) {
                val random = Random().nextInt(CARD_TOTAL_SIZE)
                if (tempArray[random] == 0) {
                    tempArray[random] = it!!
                    tempList[it] = --value
                }
            }
        }

        val list: ArrayList<Card> = ArrayList()
        tempArray.forEach {
            list.add(Card(it, false))
        }
        return list
    }

    val cardLiveData: LiveData<ArrayList<Card>>
        get() = cardList

    companion object {
        var CARD_PAIRS_VALUE = 99
        var CARD_TOTAL_SIZE = 12
    }
}