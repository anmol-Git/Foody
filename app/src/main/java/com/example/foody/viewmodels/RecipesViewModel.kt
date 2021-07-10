package com.example.foody.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.data.DataStoreRepository
import com.example.foody.data.MealAndDietType
import com.example.foody.util.Constant.Companion.API_KEY
import com.example.foody.util.Constant.Companion.DEFAULT_RECIPE_NUMBER
import com.example.foody.util.Constant.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foody.util.Constant.Companion.QUERY_API_KEY
import com.example.foody.util.Constant.Companion.QUERY_DIET
import com.example.foody.util.Constant.Companion.QUERY_FILL_INGREDIENT
import com.example.foody.util.Constant.Companion.QUERY_NUMBER
import com.example.foody.util.Constant.Companion.QUERY_SEARCH
import com.example.foody.util.Constant.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var netwokStatus = false
    var backOnline = false
    private lateinit var mealAndDietType: MealAndDietType

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()


    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(
                mealAndDietType.selectedMealType,
                mealAndDietType.selectedMealTypeId,
                mealAndDietType.selectedDietType,
                mealAndDietType.selectedDietTypeId
            )
        }

    fun saveMealAndDietTypeTemp(mealType: String, MealId: Int, dietType: String, DietId: Int) {
        mealAndDietType = MealAndDietType(mealType, MealId, dietType, DietId)
    }


    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }


    fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealAndDietType.selectedMealType
        queries[QUERY_DIET] = mealAndDietType.selectedDietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENT] = "true"
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