package com.example.my_calendar2

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diary.InputDiary
import com.example.diary.MainActivity
import com.example.diary.R
import kotlinx.android.synthetic.main.item.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by KimYounSug750 on 25/09/2019.
 */
class RecyclerViewAdapter(val mainActivity: MainActivity) : RecyclerView.Adapter<ModuleHelper>() {

    val baseCalendar =ModuleBase()
    init {
        baseCalendar.initBaseCalendar {
            refreshView(it)
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////item<textView>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleHelper {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ModuleHelper(view)/////item의 진정한 adapter(가상 공간에 start지점과 start주체 박아넣는역할)
}
//////////////////////////////////////////item의 갯수 결정///////////////////////////////////////////////
    override fun getItemCount(): Int {
        return ModuleBase.LOW_OF_CALENDAR * ModuleBase.DAYS_OF_WEEK
    }

    override fun onBindViewHolder(holder: ModuleHelper, position: Int)  {

        val number = baseCalendar.data[position]





        holder.containerView.setOnClickListener {
            val nextIntent=Intent(mainActivity, InputDiary::class.java)

            /*val YM = baseCalendar.calendar.time

            nextIntent.putExtra("YM",YM)*/
            val Y=baseCalendar.calendar.get(Calendar.YEAR)
            val M=baseCalendar.calendar.get(Calendar.MONTH)
            nextIntent.putExtra("picked",number)
            nextIntent.putExtra("Y",Y)
            nextIntent.putExtra("M",M)
            mainActivity.startActivity(nextIntent)
        }

        if (position % ModuleBase.DAYS_OF_WEEK == 0) holder.containerView.item_date.setTextColor(Color.parseColor("#ff1200"))
        else holder.containerView.item_date.setTextColor(Color.parseColor("#676d6e"))

        if (position < baseCalendar.prevMonthTailOffset || position >= baseCalendar.prevMonthTailOffset + baseCalendar.currentMonthMaxDate) {
            holder.containerView.item_date.alpha=0.3f
        } else {
            holder.containerView.item_date.alpha = 1f
        }
        holder.containerView.item_date.text = baseCalendar.data[position].toString()/////oncreateViewholder에서 item을 (가상)view?에 박아넣었는데 그 아이템들을 위에 if구문에서 글자체,갯수등 설정해놓고
        //여기서 이제 item글자에 들어가는애들 뭐나오는지 설정해주는거임.
        //진짜 Layout(Prev,next,날짜 구현되어있는 layout)에 박아넣는 작업은 Main에 구현되어있음. 이전까지는 걍 가상의 공간에서 item 어떻게 될지 작업한거

    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun changeToPrevMonth() {
        baseCalendar.changeToPrevMonth {
            refreshView(it)
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            refreshView(it)
        }
    }

    private fun refreshView(calendar: Calendar) {
        notifyDataSetChanged()
        mainActivity.refreshCurrentMonth(calendar)
    }
}