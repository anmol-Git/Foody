package com.example.foody.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.data.DataStoreRepository
import com.example.foody.util.Constant.Companion.API_KEY
import com.example.foody.util.Constant.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constant.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constant.Companion.DEFAULT_RECIPE_NUMBER
import com.example.foody.util.Constant.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foody.util.Constant.Companion.QUERY_API_KEY
import com.example.foody.util.Constant.Companion.QUERY_DIET
import com.example.foody.util.Constant.Companion.QUERY_FILL_INGREDIENT
import com.example.foody.util.Constant.Companion.QUERY_NUMBER
import com.example.foody.util.Constant.Companion.QUERY_SEARCH
import com.example.foody.util.Constant.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RecipesViewModel @ViewModelInject constructor(application: Application, private val dataStoreRepository: DataStoreRepository) : AndroidViewModel(application) {


    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE


    var netwokStatus = false
     var backOnline =false


    val readMealAndDietType =dataStoreRepository.readMealAndDataType
     val readBackOnline =dataStoreRepository.readBackOnline.asLiveData()


    fun saveMealAndDietType(mealType :String,MealId :Int, dietType :String , DietId :Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType,MealId,dietType,DietId)
        }
    }


    fun saveBackOnline(backOnline : Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }


     fun applyQuery() :HashMap<String, String>{
        val queries :HashMap<String, String> =HashMap()

         viewModelScope.launch {
             readMealAndDietType.collect { value ->
                 mealType=value.selectedMealType
                 dietType=value.selectedDietType
             }
         }

         Log.d("in query ", "meal type $mealType and diet type $dietType")
        queries[QUERY_NUMBER]= DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY]= API_KEY
        queries[QUERY_TYPE]= mealType
        queries[QUERY_DIET]= dietType
        queries[QUERY_ADD_RECIPE_INFORMATION]="true"
        queries[QUERY_FILL_INGREDIENT]="true"
        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENT] = "true"

        return queries
    }



    fun showNetworkStatus(){
        if (!netwokStatus){
            Toast.makeText(getApplication(),"No Internet Connection.",Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }else if(netwokStatus){
            if (backOnline){
                Toast.makeText(getApplication(),"We're back online",Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}