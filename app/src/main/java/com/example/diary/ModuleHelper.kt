package com.example.my_calendar2



import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.GridView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/*
 * Created by KimYounSug750 on 25/09/2019.
 */
open class ModuleHelper(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer