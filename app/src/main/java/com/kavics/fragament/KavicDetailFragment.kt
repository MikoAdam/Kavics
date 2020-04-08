package com.kavics.fragament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kavics.R
import com.kavics.model.Kavic
import kotlinx.android.synthetic.main.kavic_detail.*

class KavicDetailFragment : Fragment() {


    private var selectedKavic: Kavic? = null

    companion object {

        const val ARG_ITEM_ID = "item_id"
        private const val KEY_KAVIC_DESCRIPTION = "KEY_KAVIC_DESCRIPTION"

        fun newInstance(kavicDesc: String): KavicDetailFragment {
            val args = Bundle()
            args.putString(KEY_KAVIC_DESCRIPTION, kavicDesc)

            val result = KavicDetailFragment()
            result.arguments = args
            return result
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            selectedKavic = Kavic(
                title = "cim",
                dueDate = "1987.23.12",
                description = args.getString(KEY_KAVIC_DESCRIPTION) ?: ""
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.kavic_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvKavicDetail.text = selectedKavic?.description
    }

}