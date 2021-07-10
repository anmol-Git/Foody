package com.example.foody.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foody.databinding.RecipeBottomSheetBinding
import com.example.foody.util.Constant.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constant.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class RecipeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipeViewModel: RecipesViewModel
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0


    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = RecipeBottomSheetBinding.inflate(inflater, container, false)

        recipeViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        })

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }


        //Apply button click listener
        binding.button.setOnClickListener {
            recipeViewModel.saveMealAndDietTypeTemp(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action = RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipeFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
               if (chipId!=0){
                   try {
                       val targetView = chipGroup.findViewById<Chip>(chipId)
                       targetView.isChecked = true;
                       chipGroup.requestChildFocus(targetView, targetView)
                   } catch (e: Exception) {
                       Log.d("RecipeBottomSheet", e.message.toString())
                   }
               }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}