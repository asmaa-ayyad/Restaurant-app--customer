package com.example.andro2groupprojecttest.ui.Modle

class CartMeal(  var m_id:String, var name:String, var price:String, var rate:Double, var description:String, var imageUrl: String, var restid:String ,var isSold:String ,var quantity:Int)
{
    constructor() : this ("","","",0.0,"", "","","",0)
}