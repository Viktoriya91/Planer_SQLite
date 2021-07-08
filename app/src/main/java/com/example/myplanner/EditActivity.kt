package com.example.myplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myplanner.db.DbManager
import com.example.myplanner.db.IntentConstants
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    var isEditState = false
    var isButtonState = false
    var id = 0
    var oldTitle = "empty"
    var oldPoint = "empty"
    val dbManager = DbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        getIntentView()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        dbManager.openDb()
    }

    fun onClickSave (view: View){
        val newTitle = edTextTitle.text.toString()
        val newItem = edTextPoint.text.toString()
        val newDateCreate = getDateCreatePoint()
        val isCompareText = compareText(newTitle,newItem)
        if (newItem != "" && newTitle != ""){
            if (isEditState){
                if (isButtonState && isCompareText){
                    dbManager.updateItem(id, newTitle, newItem, newDateCreate)
                    onClickBack(view)
                }else {
                    onClickBack(view)
                }
            }else{
                dbManager.insertDb(newTitle, newItem, newDateCreate)
                onClickBack(view)
            }
        }
    }

    fun onClickBack (view: View){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun onClickEdit (view: View){
        edTextPoint.isEnabled = true
        edTextTitle.isEnabled = true
        editButton.visibility = View.GONE
        isButtonState = true
    }

    fun getIntentView(){
        editButton.visibility = View.GONE
        val i = intent
        if(i != null){
            if(i.getStringExtra(IntentConstants.TITLE_KEY) != null && i.getStringExtra(IntentConstants.POINT_KEY) != null){
                isEditState = true
                editButton.visibility = View.VISIBLE
                id = i.getIntExtra(IntentConstants.ID_KEY,0)
                oldTitle = i.getStringExtra(IntentConstants.TITLE_KEY).toString()
                oldPoint = i.getStringExtra(IntentConstants.POINT_KEY).toString()
                edTextTitle.setText(i.getStringExtra(IntentConstants.TITLE_KEY))
                edTextPoint.setText(i.getStringExtra(IntentConstants.POINT_KEY))
                edTextTitle.isEnabled = false
                edTextPoint.isEnabled = false
            }
        }
    }

    private fun getDateCreatePoint():String{
        val dateNew = Calendar.getInstance().time
        val formatDateNew = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        val fDateNew = formatDateNew.format(dateNew)
        return fDateNew
    }

    fun compareText(title: String, point: String):Boolean{
       if(title == oldTitle && point == oldPoint){
           return false
       }else{
           return true
       }
    }
}