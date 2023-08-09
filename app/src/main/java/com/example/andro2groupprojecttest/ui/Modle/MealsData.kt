package com.example.andro2groupprojecttest.ui.Modle

import android.net.Uri
import com.google.firebase.firestore.DocumentId

data class MealsData (
    @DocumentId
    var m_id:String,
    var name:String, var price:String, var rate:Double, var description:String, var imageUrl: String, var restid:String ,var isSold:String)
{
    constructor() : this ("","","",0.0,"", "","","")
}
