package com.pei.rpl // Pastikan package name ini sesuai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pei.rpl.databinding.FragmentCompetencyDetailBinding

class CompetencyDetailFragment : Fragment() {

    private var _binding: FragmentCompetencyDetailBinding? = null
    private val binding get() = _binding!!

    private var competencyTitle: String? = null
    private var competencyDescription: String? = null
    private var competencyIconResId: Int = 0 // Default jika tidak ada ikon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            competencyTitle = it.getString(ARG_TITLE)
            competencyDescription = it.getString(ARG_DESCRIPTION)
            competencyIconResId = it.getInt(ARG_ICON_RES_ID, R.drawable.ic_placeholder_generic) // Ambil iconResId, dengan default
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompetencyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.competencyDetailTitle.text = competencyTitle ?: "Judul Kompetensi"
        binding.competencyDetailDescription.text = competencyDescription ?: "Deskripsi detail akan ditampilkan di sini."

        if (competencyIconResId != 0 && competencyIconResId != R.drawable.ic_placeholder_generic) {
            binding.competencyIcon.setImageResource(competencyIconResId)
        } else {
            // Fallback jika iconResId tidak valid atau default, bisa juga sembunyikan ImageView
            // atau atur ikon default berdasarkan title jika ada logika tambahan
            binding.competencyIcon.setImageResource(R.drawable.ic_placeholder_generic) // Pastikan drawable ini ada
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_DESCRIPTION = "arg_description"
        private const val ARG_ICON_RES_ID = "arg_icon_res_id" // Konstanta untuk key iconResId

        /**
         * Factory method untuk membuat instance baru dari fragment ini
         * dengan argumen yang diperlukan.
         *
         * @param title Judul kompetensi.
         * @param description Deskripsi kompetensi.
         * @param iconResId Resource ID untuk ikon kompetensi.
         * @return Sebuah instance baru dari CompetencyDetailFragment.
         */
        @JvmStatic
        fun newInstance(title: String, description: String, iconResId: Int): CompetencyDetailFragment { // SEKARANG MENERIMA 3 ARGUMEN
            return CompetencyDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DESCRIPTION, description)
                    putInt(ARG_ICON_RES_ID, iconResId) // Simpan iconResId ke arguments
                }
            }
        }
    }
}