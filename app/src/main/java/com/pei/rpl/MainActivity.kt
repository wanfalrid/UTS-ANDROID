package com.pei.rpl

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem // Penting untuk OnItemSelectedListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.navigation.NavigationBarView // PERBAIKAN: Import yang benar
import com.google.android.material.tabs.TabLayoutMediator
import com.pei.rpl.databinding.ActivityMainBinding
import com.pei.rpl.databinding.ItemCareerProspectBinding
import com.pei.rpl.databinding.ItemFacilityImageBinding
import com.pei.rpl.databinding.ItemFavoriteCourseBinding
import com.pei.rpl.databinding.ItemLecturerProfileBinding
import com.pei.rpl.databinding.ItemTestimonialCardBinding
import java.lang.ref.WeakReference
import kotlin.math.abs

// --- Data Classes (Idealnya di file terpisah) ---
data class Course(val id: String, val name: String, val iconResId: Int, val description: String)
data class Lecturer(val id: String, val name: String, val expertise: String, val photoResId: Int, val quote: String, val coursesTaught: String)
data class Facility(val id: String, val caption: String, val imageResId: Int)
data class Testimonial(val id: String, val studentName: String, val studentDetails: String, val photoResId: Int, val testimonialText: String)
data class CareerProspect(val id: String, val jobTitle: String, val salaryEstimate: String, val relevance: String, val iconResId: Int)
data class CompetencyTab(val title: String, val description: String, val iconResId: Int)


// PERBAIKAN: MainActivity sekarang mengimplementasikan NavigationBarView.OnItemSelectedListener
class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val sectionViews = mutableMapOf<String, WeakReference<View>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSectionViews()
        setupToolbar()
        setupThemeToggle()
        setupCTAClickListeners()

        binding.bottomNavigationView.setOnItemSelectedListener(this)
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.nav_item_home
        }

        setupHeroSectionListeners()
        setupCurriculumCompetenciesTabs()
        setupFavoriteCourses()
        setupLecturerProfiles()
        setupFacilitiesCarousel()
        setupTestimonialsSlider()
        setupPromoVideo()
        setupCareerProspects()
        setupFooterLinks()
        setupScrollRevealAnimations()
    }

    private fun initSectionViews() {
        sectionViews[resources.getResourceEntryName(R.id.nav_item_home)] = WeakReference(binding.sectionHero.root)
        sectionViews[resources.getResourceEntryName(R.id.nav_item_kurikulum)] = WeakReference(binding.sectionCurriculumCompetencies.root)
        sectionViews[resources.getResourceEntryName(R.id.nav_item_dosen)] = WeakReference(binding.sectionLecturers.root)
        sectionViews[resources.getResourceEntryName(R.id.nav_item_fasilitas)] = WeakReference(binding.sectionFacilities.root)
        sectionViews[resources.getResourceEntryName(R.id.nav_item_testimoni)] = WeakReference(binding.sectionTestimonials.root)
    }

    @SuppressLint("SetTextI18n")
    private fun setupToolbar() {
        binding.toolbarTitleMain.text = "Profil Jurusan RPL"
    }

    private fun setupThemeToggle() {
        binding.themeToggle.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.themeToggle.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun scrollToSection(sectionMenuId: Int) {
        val sectionKey = resources.getResourceEntryName(sectionMenuId)
        sectionViews[sectionKey]?.get()?.let { view ->
            val appBarHeight = binding.appBarLayout.height.takeIf { it > 0 } ?: resources.getDimensionPixelSize(R.dimen.toolbar_height)
            val scrollTo = view.top - appBarHeight
            binding.nestedScrollView.smoothScrollTo(0, scrollTo.coerceAtLeast(0), 700)
        } ?: Toast.makeText(this, "Target section untuk '$sectionKey' tidak ditemukan.", Toast.LENGTH_SHORT).show()
    }

    // PERBAIKAN: Signature metode ini harus cocok dengan NavigationBarView.OnItemSelectedListener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        scrollToSection(item.itemId)
        // Update judul toolbar sesuai item yang dipilih (opsional)
        binding.toolbarTitleMain.text = item.title
        return true
    }

    private fun setupCTAClickListeners() {
        binding.sectionCtaFooter.btnCtaGabungSekarang.setOnClickListener {
            Toast.makeText(this, "Tombol Gabung Sekarang (Footer) diklik!", Toast.LENGTH_SHORT).show()
        }
        binding.sectionCtaFooter.btnCtaKonsultasiGratis.setOnClickListener {
            Toast.makeText(this, "Tombol Konsultasi Gratis diklik!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupHeroSectionListeners() {
        binding.sectionHero.btnLihatKurikulum.setOnClickListener {
            binding.bottomNavigationView.selectedItemId = R.id.nav_item_kurikulum
        }
        binding.sectionHero.btnVideoProfil.setOnClickListener {
            val targetView = binding.sectionPromoVideo.root
            val appBarHeight = binding.appBarLayout.height.takeIf { it > 0 } ?: resources.getDimensionPixelSize(R.dimen.toolbar_height)
            binding.nestedScrollView.smoothScrollTo(0, targetView.top - appBarHeight, 700)
        }
    }

    private fun setupCurriculumCompetenciesTabs() {
        val tabLayout = binding.sectionCurriculumCompetencies.tabLayoutCompetencies
        val viewPager = binding.sectionCurriculumCompetencies.viewPagerCompetencies
        val competencyTabsData = listOf(
            CompetencyTab("UI/UX Design", "Mempelajari prinsip desain antarmuka (UI) dan pengalaman pengguna (UX) untuk menciptakan produk digital yang intuitif dan menyenangkan.", R.drawable.ic_course_uiux_placeholder),
            CompetencyTab("Web Development", "Menguasai teknologi pengembangan web modern, termasuk HTML, CSS, JavaScript, serta framework front-end dan back-end.", R.drawable.ic_course_webdev_placeholder),
            CompetencyTab("Mobile Apps", "Fokus pada pengembangan aplikasi mobile native dan cross-platform untuk Android dan iOS.", R.drawable.ic_course_mobile_placeholder),
            CompetencyTab("Cyber Security", "Memahami konsep keamanan siber, analisis kerentanan, dan teknik pertahanan.", R.drawable.ic_course_cybersec_placeholder),
            CompetencyTab("AI & Data", "Pengantar Kecerdasan Buatan, Machine Learning, dan analisis data.", R.drawable.ic_ai_data_placeholder) // Pastikan drawable ini ada
        )
        val adapter = CompetencyPagerAdapter(this, competencyTabsData)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = competencyTabsData[position].title
            // tab.setIcon(competencyTabsData[position].iconResId)
        }.attach()
    }

    private fun setupFavoriteCourses() {
        val rvCourses = binding.sectionFavoriteCourses.rvFavoriteCourses
        rvCourses.layoutManager = GridLayoutManager(this, 2)
        val courses = listOf(
            Course("C01", "Pemrograman Mobile Lanjut", R.drawable.ic_course_mobile_placeholder, "Deep dive into advanced Android/iOS development."),
            Course("C02", "Keamanan Jaringan", R.drawable.ic_course_cybersec_placeholder, "Protecting networks and systems."),
            Course("C03", "Desain Interaksi H&K", R.drawable.ic_course_uiux_placeholder, "Creating engaging user experiences."),
            Course("C04", "Web Full-Stack", R.drawable.ic_course_webdev_placeholder, "End-to-end web application development.")
        )
        rvCourses.adapter = FavoriteCourseAdapter(courses) { course ->
            Toast.makeText(this, "Mata kuliah '${course.name}' dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupLecturerProfiles() {
        val rvLecturers = binding.sectionLecturers.rvLecturers
        rvLecturers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val lecturers = listOf(
            Lecturer("L01", "Halimil Fathi, S.Kom, M.Kom", "AI & Machine Learning", R.drawable.ic_lecturer_placeholder_1, "\"Belajar tanpa henti adalah kunci inovasi.\"", "Kecerdasan Artifisial, Pembelajaran Mesin"), // Pastikan drawable ini ada
            Lecturer("L02", "Musawarman, S.Kom, M.M.S.I.", "Software Architecture", R.drawable.ic_lecturer_placeholder_2, "\"Arsitektur yang baik adalah fondasi sistem yang kokoh.\"", "Rekayasa Perangkat Lunak Lanjut, Pola Desain"), // Pastikan drawable ini ada
            Lecturer("L03", "Ricak Agus Setiawan, S.T., M.SI.", "Mobile & Web Technology", R.drawable.ic_lecturer_placeholder_3, "\"Ciptakan solusi digital yang berdampak.\"", "Pengembangan Aplikasi Mobile, Teknologi Web Terkini"), // Pastikan drawable ini ada
            Lecturer("L04", "Widya Andayani Rahayu, S.Pd., M.Pd.", "English for IT", R.drawable.ic_lecturer_placeholder_4, "Penguasaan bahasa Inggris membuka peluang lebih luas dalam dunia teknologi.", "Bahasa Inggris untuk Teknologi Informasi, Komunikasi Profesional dalam IT"),
            Lecturer("L05", "Heti Mulyani, S.T., M.Kom", "Algorithms & Programming", R.drawable.ic_lecturer_placeholder_5, "Pemahaman algoritma yang kuat menjadi dasar untuk membangun solusi yang efisien.", "Algoritma Dasar, Pemrograman Terstruktur"),
            Lecturer("L06", "Muhammad Nugraha, S.T., M.Eng", "Web Development", R.drawable.ic_lecturer_placeholder_6, "Kemampuan membangun web yang dinamis dan responsif menjadi kunci di era digital.", "Pengembangan Frontend & Backend, Framework Modern"),
            Lecturer("L07", "Sukrina Herman, S.Kom, M.Kom", "Database Systems", R.drawable.ic_lecturer_placeholder_7, "Pengelolaan data yang baik adalah fondasi dari sistem informasi yang handal.", "Basis Data Relasional, Manajemen dan Perancangan Database")
        )
        rvLecturers.adapter = LecturerAdapter(lecturers)
    }

    private fun setupFacilitiesCarousel() {
        val viewPager = binding.sectionFacilities.viewPagerFacilities
        val dotsIndicator = binding.sectionFacilities.dotsIndicatorFacilities
        val facilities = listOf(
            Facility("F01", "Lab Inovasi RPL", R.drawable.placeholder_lab_rpl),
            Facility("F02", "Studio Multimedia Kreatif", R.drawable.placeholder_multimedia_room),
            Facility("F03", "Perpustakaan Digital & Fisik", R.drawable.placeholder_library)
        )
        viewPager.adapter = FacilityAdapter(facilities)

        viewPager.offscreenPageLimit = 3
        (viewPager.getChildAt(0) as? RecyclerView)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER // Safe cast

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(resources.getDimensionPixelOffset(R.dimen.spacing_medium)))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
            page.alpha = 0.6f + r * 0.4f
        }
        viewPager.setPageTransformer(compositePageTransformer)
        dotsIndicator.attachTo(viewPager)
    }

    private fun setupTestimonialsSlider() {
        val viewPager = binding.sectionTestimonials.viewPagerTestimonials
        val dotsIndicator = binding.sectionTestimonials.dotsIndicatorTestimonials
        val testimonials = listOf(
            Testimonial("T01", "M Najwan Naufal", "Mahasiswi RPL, Angkatan 2023", R.drawable.ic_student_placeholder_1, "\"Dosennya ahli di bidangnya dan sangat membantu. Fasilitasnya juga oke banget!\""), // Pastikan drawable ini ada
            Testimonial("T02", "Leksa Septia Ramadhani", "Alumni RPL 2021, Sekarang di Tech Startup", R.drawable.ic_student_placeholder_2, "\"Ilmu dari RPL PEI sangat terpakai di dunia kerja. Bangga jadi alumni!\""), // Pastikan drawable ini ada
            Testimonial("T03", "Wina Ambar Yustika", "Alumni RPL 2021, Sekarang di Tech Startup", R.drawable.ic_student_placeholder_3, "\"Ilmu dari RPL PEI sangat terpakai di dunia kerja. Bangga jadi alumni!\"") // Pastikan drawable ini ada
        )
        viewPager.adapter = TestimonialAdapter(testimonials)
        dotsIndicator.attachTo(viewPager)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setupPromoVideo() {
        binding.sectionPromoVideo.videoThumbnailContainerCard.setOnClickListener {
            val videoId = "YOUR_YOUTUBE_VIDEO_ID_HERE" // GANTI DENGAN ID VIDEO YOUTUBE ANDA
            if (videoId.isBlank() || videoId.equals("YOUR_YOUTUBE_VIDEO_ID_HERE", ignoreCase = true)) {
                Toast.makeText(this, "ID Video YouTube belum diatur dengan benar.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"))
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                try {
                    if (webIntent.resolveActivity(packageManager) != null) {
                        startActivity(webIntent)
                    } else {
                        Toast.makeText(this, "Tidak ada aplikasi browser untuk membuka video.", Toast.LENGTH_SHORT).show()
                    }
                } catch (ex2: Exception) {
                    Toast.makeText(this, "Gagal membuka video: ${ex2.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupCareerProspects() {
        val rvCareer = binding.sectionCareerProspects.recyclerViewCareerProspects
        rvCareer.layoutManager = GridLayoutManager(this, 2)
        val careers = listOf(
            CareerProspect("CP01", "Full-Stack Developer", "Rp 10jt - 30jt", "Sangat Tinggi", R.drawable.ic_career_software_engineer),
            CareerProspect("CP02", "Mobile App Developer", "Rp 9jt - 28jt", "Sangat Tinggi", R.drawable.ic_career_mobile_dev_placeholder), // Pastikan drawable ini ada
            CareerProspect("CP03", "UI/UX Specialist", "Rp 8jt - 25jt", "Tinggi", R.drawable.ic_career_ui_ux),
            CareerProspect("CP04", "Data Analyst / Engineer", "Rp 10jt - 30jt", "Tinggi", R.drawable.ic_career_data_scientist)
        )
        rvCareer.adapter = CareerAdapter(careers)
    }

    private fun setupFooterLinks() {
        // Asumsi ID footerSocialYoutube sudah benar di footer_main.xml dan diakses via binding.footerMain
        binding.footerMain.footerLinkPendaftaran.setOnClickListener {
            openLink("https://pmb.pei.ac.id/pendaftaran") // Ganti dengan URL PMB Anda
        }
        binding.footerMain.footerLinkKurikulum.setOnClickListener {
            binding.bottomNavigationView.selectedItemId = R.id.nav_item_kurikulum
        }
        binding.footerMain.footerLinkBlog.setOnClickListener { openLink("https://blog.pei.ac.id/rpl") } // Ganti dengan URL Blog Anda
        binding.footerMain.footerLinkFaq.setOnClickListener { Toast.makeText(this, "Buka halaman FAQ", Toast.LENGTH_SHORT).show() }

        binding.footerMain.footerSocialIg.setOnClickListener { openLink("https://instagram.com/rpl.pei") }
        binding.footerMain.footerSocialTiktok.setOnClickListener { openLink("https://tiktok.com/@rpl.pei") }
        // PERBAIKAN: Pastikan ID footerSocialYoutube sudah benar di XML dan di-generate oleh ViewBinding
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Tidak ada aplikasi untuk membuka link ini.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Tidak dapat membuka link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupScrollRevealAnimations() {
        val viewsToAnimate = listOfNotNull(
            binding.sectionAbout.root,
            binding.sectionCurriculumCompetencies.root,
            binding.sectionFavoriteCourses.root,
            binding.sectionLecturers.root,
            binding.sectionFacilities.root,
            binding.sectionTestimonials.root,
            binding.sectionPromoVideo.root,
            binding.sectionCareerProspects.root,
            binding.sectionIndustryPartners.root, // Pastikan ID include sectionIndustryPartners ada
            binding.sectionCtaFooter.root,
            binding.footerMain.root
        )

        viewsToAnimate.forEach { view ->
            view.alpha = 0f
            view.translationY = 150f
        }

        binding.nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                viewsToAnimate.forEach { view ->
                    if (view.alpha == 0f) {
                        revealViewOnScroll(view)
                    }
                }
            }
        )
        binding.nestedScrollView.post {
            viewsToAnimate.forEach { view -> revealViewOnScroll(view) }
        }
    }

    private fun revealViewOnScroll(view: View) {
        if (!view.isAttachedToWindow) return

        val screenHeight = resources.displayMetrics.heightPixels
        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)
        val viewTopOnScreen = viewLocation[1]

        if (view.height > 0 && viewTopOnScreen < screenHeight * 0.90 && (viewTopOnScreen + view.height * 0.2) > 0) {
            if (view.alpha == 0f) {
                view.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(800)
                    .setInterpolator(android.view.animation.DecelerateInterpolator(1.5f))
                    .setStartDelay(150)
                    .start()
            }
        }
    }

    // --- Adapter Classes (Sebaiknya di file terpisah) ---
    inner class CompetencyPagerAdapter(
        activity: FragmentActivity,
        private val tabsData: List<CompetencyTab>
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = tabsData.size
        override fun createFragment(position: Int): Fragment {
            val competencyTab = tabsData[position]
            // Pastikan CompetencyDetailFragment.newInstance() menerima semua argumen
            return CompetencyDetailFragment.newInstance(competencyTab.title, competencyTab.description, competencyTab.iconResId)
        }
    }

    inner class FavoriteCourseAdapter(private val courses: List<Course>, private val onItemClick: (Course) -> Unit) :
        RecyclerView.Adapter<FavoriteCourseAdapter.ViewHolder>() {
        inner class ViewHolder(private val binding: ItemFavoriteCourseBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(course: Course) {
                binding.courseName.text = course.name
                binding.courseIcon.setImageResource(course.iconResId) // TODO: Implement Glide/Coil
                binding.courseDescriptionShort.text = course.description
                binding.courseDescriptionShort.visibility = View.GONE
                itemView.setOnClickListener {
                    onItemClick(course)
                    binding.courseDescriptionShort.visibility = if (binding.courseDescriptionShort.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemFavoriteCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(courses[position])
        override fun getItemCount(): Int = courses.size
    }

    inner class LecturerAdapter(private val lecturers: List<Lecturer>) :
        RecyclerView.Adapter<LecturerAdapter.ViewHolder>() {
        inner class ViewHolder(private val binding: ItemLecturerProfileBinding) : RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(lecturer: Lecturer) {
                binding.lecturerName.text = lecturer.name
                binding.lecturerExpertise.text = lecturer.expertise
                binding.lecturerPhoto.setImageResource(lecturer.photoResId) // TODO: Implement Glide/Coil
                binding.lecturerQuote.text = lecturer.quote
                binding.lecturerCoursesTaught.text = "Mengampu: ${lecturer.coursesTaught}"
                binding.lecturerCoursesTaught.visibility = View.GONE
                itemView.setOnClickListener {
                    binding.lecturerCoursesTaught.visibility = if (binding.lecturerCoursesTaught.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemLecturerProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(lecturers[position])
        override fun getItemCount(): Int = lecturers.size
    }

    inner class FacilityAdapter(private val facilities: List<Facility>) :
        RecyclerView.Adapter<FacilityAdapter.ViewHolder>() {
        inner class ViewHolder(private val binding: ItemFacilityImageBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(facility: Facility) {
                binding.facilityCaption.text = facility.caption
                binding.facilityImage.setImageResource(facility.imageResId) // TODO: Implement Glide/Coil
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemFacilityImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(facilities[position])
        override fun getItemCount(): Int = facilities.size
    }

    inner class TestimonialAdapter(private val testimonials: List<Testimonial>) :
        RecyclerView.Adapter<TestimonialAdapter.ViewHolder>() {
        inner class ViewHolder(private val binding: ItemTestimonialCardBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(testimonial: Testimonial) {
                binding.testimonialStudentName.text = testimonial.studentName
                binding.testimonialStudentDetails.text = testimonial.studentDetails
                binding.testimonialStudentPhoto.setImageResource(testimonial.photoResId) // TODO: Implement Glide/Coil
                binding.testimonialText.text = testimonial.testimonialText
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemTestimonialCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(testimonials[position])
        override fun getItemCount(): Int = testimonials.size
    }

    inner class CareerAdapter(private val careers: List<CareerProspect>) :
        RecyclerView.Adapter<CareerAdapter.ViewHolder>() {
        inner class ViewHolder(private val binding: ItemCareerProspectBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(career: CareerProspect) {
                binding.careerJobTitle.text = career.jobTitle
                binding.careerSalaryEstimate.text = career.salaryEstimate
                binding.careerRelevance.text = career.relevance
                binding.careerIcon.setImageResource(career.iconResId) // TODO: Implement Glide/Coil
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            ItemCareerProspectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(careers[position])
        override fun getItemCount(): Int = careers.size
    }
}