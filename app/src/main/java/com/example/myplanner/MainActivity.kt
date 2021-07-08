package com.example.myplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplanner.db.DbManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var job: Job?=null
    val dbManager = DbManager(this)
    val cardAdapter = CardAdapter(ArrayList(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        dbManager.openDb()
        allDbAdapter()
    }

    fun init(){
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = cardAdapter
        val swapHepler = getSwapMg()
        swapHepler.attachToRecyclerView(rcView)
    }

    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                cardAdapter.removeItem(viewHolder.adapterPosition, dbManager)
            }
        })
    }

    fun onClickAdd(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    private fun allDbAdapter(){
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = dbManager.readDb()
            cardAdapter.updateAdapter(list)
            if (list.size > 0) {
                txtEmpty.visibility = View.GONE
            } else {
                txtEmpty.visibility = View.VISIBLE
            }
        }
    }
}