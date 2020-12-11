package com.example.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.model.Result
import com.example.foody.util.Constant.Companion.FAVOURITE_RECIPES_TABLE


@Entity(tableName = FAVOURITE_RECIPES_TABLE)
class FavouriteEntity(@PrimaryKey(autoGenerate = true) val id :Int, var result: Result) {
}