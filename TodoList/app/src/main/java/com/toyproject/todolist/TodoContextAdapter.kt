package com.toyproject.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class TodoContextAdapter (val context: Context, val ContentList : ArrayList<TodoContext>) :
    BaseAdapter() {
    override fun getCount(): Int {return ContentList.size}

    override fun getItem(position: Int): Any {return ContentList[position]}

    override fun getItemId(position: Int): Long {return 0}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.listview_form, null)

        val title = view.findViewById<TextView>(R.id.title)
        val context = view.findViewById<TextView>(R.id.context)

        val content = ContentList[position]

        title.text = content.title
        context.text = content.context

        return view
    }
}