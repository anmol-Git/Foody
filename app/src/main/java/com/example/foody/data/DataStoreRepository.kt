package com.example.foody.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.foody.util.Constant.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constant.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constant.Companion.DIET_TYPE
import com.example.foody.util.Constant.Companion.DIET_TYPE_ID
import com.example.foody.util.Constant.Companion.MEAL_TYPE
import com.example.foody.util.Constant.Companion.MEAL_TYPE_ID
import com.example.foody.util.Constant.Companion.PREFRENCES_BACK_ONLINE
import com.example.foody.util.Constant.Companion.PREFRENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(PREFRENCES_NAME)
//It uses datastore to save some prefrences in key value pairs
@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PrefrenceKeys {
        val selectedMealType = stringPreferencesKey(MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFRENCES_BACK_ONLINE)
    }

    //initializing data store
    private val dataStore: DataStore<Preferences> = context.dataStore


   suspend fun saveMealAndDietType(mealType :String,mealId :Int, dietType : String, dietId : Int){
        dataStore.edit {prefrences ->
            prefrences[PrefrenceKeys.selectedMealType]=mealType
            prefrences[PrefrenceKeys.selectedMealTypeId]=mealId
            prefrences[PrefrenceKeys.selectedDietType]=dietType
            prefrences[PrefrenceKeys.selectedDietTypeId]=dietId

        }
    }


    suspend fun saveBackOnline(backOnline :Boolean){
        dataStore.edit {prefrences ->
            prefrences[PrefrenceKeys.backOnline] =backOnline

        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { perferences ->
            //  "x ?: y"  is Elivs operator and it work as if x is null then y is returned else x is returned
            val selectedMealType = perferences[PrefrenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = perferences[PrefrenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = perferences[PrefrenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val  selectedDietTypeId = perferences[PrefrenceKeys.selectedDietTypeId] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    //variable for reading the prefrence data type notice the type
    val readBackOnline :Flow<Boolean> =dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else {
                throw exception
            }
        }.map {prefrences ->
            val backOnline =prefrences[PrefrenceKeys.backOnline] ?: false
            backOnline
        }
}

//data class for the meal and diet type
data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)