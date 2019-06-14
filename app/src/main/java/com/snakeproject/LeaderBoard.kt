package com.snakeproject

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_leader_board.*

class LeaderBoard : AppCompatActivity() {
    lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        val kitNumber = intent.getIntExtra("kit", 0)

        main_menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }
        listView = findViewById(R.id.main_listview)


        val l = scoreManager.getTopScores(10).map { it.toString() }.toTypedArray()
        listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l)

//        val listView = findViewById<ListView>(R.id.main_listview)
//        val color = Color.parseColor("FF0000")
//        listView.setBackgroundColor(color)
//        listView.adapter = MyCustomAdapter(this)

    }

//    private class MyCustomAdapter(context: Context): BaseAdapter(){
//
//        private val mContext : Context
//
//        init{
//            mContext = context
//        }
//
//
//        override fun getCount(): Int {
//            return 3 //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun getItem(position: Int): Any {
//            return "Name"//To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun getItemId(position: Int): Long {
//            return position.toLong()//To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val textView = TextView(mContext)
//            textView.text = "BAD"
//            return textView//To change body of created functions use File | Settings | File Templates.
//        }
//    }

}



