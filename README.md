# PEI TRPL Android Mobile Application - UI/UX Design Project

## Project Overview

This project is a UI/UX design and implementation поклонfor an Android mobile application for the **Politeknik Enjinering Indorama (PEI) Program Studi Teknologi Rekayasa Perangkat Lunak (TRPL)**. The primary goal was to transform the existing web-based platform of PEI TRPL, which was primarily designed for desktop use, into a mobile-friendly, responsive, and intuitive Android application[cite: 12, 14, 89].

The focus of this project, as part of the "Workshop Pemrograman Android 1" coursework, was on the visual design and user experience (UX) aspects of the mobile application, without involving backend systems or database management[cite: 1, 15]. The aim was to create an interface that is clean, intuitive, responsive, and enhances on-the-go access to academic and informational content for students and faculty[cite: 11, 16, 88, 89, 90].

## 🌟 Key Features

The application showcases a comprehensive profile for the TRPL department, featuring:

* **Dynamic Homepage:** A scrollable main page with a hero section, introduction to the department, reasons to choose PEI TRPL, and quick access to other key information[cite: 91].
* **Detailed Sections:**
    * **Curriculum & Competencies:** Information on featured curriculum and core competencies (e.g., UI/UX Design, Web Development, Mobile Apps, Cyber Security, AI & Data) presented with tabs and expandable details[cite: 91, 53].
    * **Lecturer Profiles:** Displays profiles of teaching staff with photos, expertise, and quotes in a horizontal scrolling list[cite: 91, 57].
    * **Facilities:** A carousel gallery showcasing modern learning facilities like labs and multimedia rooms[cite: 91, 61].
    * **Student Testimonials:** A slider displaying testimonials from students[cite: 91, 65].
    * **Promotional Video:** Embedded section for a promotional video[cite: 65].
    * **Career Prospects:** Information on potential career paths for graduates, presented in a grid[cite: 69].
    * **Industry Partners:** Display of logos from industry partners[cite: 44].
    * **Call to Action & Footer:** Clear CTAs and a comprehensive footer with contact information, quick links, and social media[cite: 73].
* **Bottom Navigation:** Easy-to-use bottom navigation bar for quick scrolling to different sections of the application[cite: 92].
* **Dark/Light Theme Support:** A modern dark theme with blue/green accent colors, and a corresponding light theme for user preference, with a toggle switch[cite: 92, 32, 39].
* **Interactive Elements:** Carousels, expandable cards, and scroll-reveal animations for an engaging user experience.
* **Responsive Design:** Layouts and visuals adapted for mobile responsiveness across various screen sizes[cite: 47, 89, 109].

## 🎨 Design Philosophy & UI/UX Focus

The design was driven by the need for a clean, intuitive, and modern interface[cite: 89, 92]:

* **Visual Style:** Primarily a dark theme complemented by blue and green accent colors, along with white and cream, to create a modern and elegant appeal[cite: 92, 98]. Light mode is also fully supported.
* **Modularity:** Information is presented using modular card layouts and feature blocks for easy scanning and digestion[cite: 92, 101].
* **Navigation:** A clear content hierarchy and bottom navigation are implemented to guide the user, minimize effort, and maximize clarity[cite: 92, 97, 104].
* **Accessibility:** Focus on providing easy and logical user movement and helping users focus on important information[cite: 90, 97, 104].

## 🛠️ Tech Stack & Implementation Details

* **Platform:** Android Native
* **Language:** Kotlin
* **Design System:** Material 3 (M3)
* **UI Components:**
    * `CoordinatorLayout`, `AppBarLayout`, `MaterialToolbar`
    * `NestedScrollView` for the main scrollable content
    * `BottomNavigationView` for primary scroll navigation
    * `RecyclerView` with `GridLayoutManager` and `LinearLayoutManager` (for courses, lecturers, career prospects)
    * `ViewPager2` with `TabLayout` (for competencies) and for carousels (facilities, testimonials)
    * `MaterialCardView` (Elevated, Filled, Outlined M3 styles)
    * `MaterialButton`, `MaterialSwitch`
    * Included layouts (`section_*.xml`, `item_*.xml`) for modularity.
* **Architecture/Patterns:**
    * Single Activity holding multiple themed sections.
    * ViewBinding for safe and efficient view access.
    * Adapters for `RecyclerView` and `ViewPager2`.
    * Custom styles and themes following Material 3 guidelines.

## 📸 Screenshots

*(It's highly recommended to add screenshots of your application here. You can take them from your presentation or the implemented app images in your report .)*

**Example Markdown for an image:**
`![App Screenshot Alt Text](path/to/your/screenshot.png)`

## 📜 Project Conclusion

This project successfully transformed the PEI TRPL website's concept into an Android-native interface, with layouts and visuals adapted for mobile responsiveness[cite: 79, 109]. The resulting visual identity and navigation are designed to be intuitive and engaging[cite: 110]. The mockup process provided a clear visual guide, facilitating the implementation process[cite: 80]. The implemented application demonstrates good adaptability across various screen sizes, enhancing comfort and access to information for students and lecturers[cite: 81].

## 🚀 Next Steps

Further development for this application could include:

* Connecting to a backend system for live data management.
* Adding user authentication features (login/register).
* Conducting thorough user testing with the target audience to gather feedback on the designed interface's usability and effectiveness[cite: 83].
* Publishing the application on the Google Play Store.

## 👥 Team (Group 5)

* Muhammad Najwan Naufal Alfarid ([202304020])
* Wina Ambar Yustika ([202304012])
* Leksa Septia Ramadhani ([202304009]) (Note: Name in report is Leksa Septia Ramdani)

## 🙏 Acknowledgements

* This project was undertaken to fulfill the requirements of the "Workshop Pemrograman Android 1" course.
* Supervising Lecturer: Musawarman, S.kom., M.M. Si.

---
