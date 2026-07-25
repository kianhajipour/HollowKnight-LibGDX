
<div align="center">
  <img width="250" alt="Hollow Knight Demake Gameplay" src="https://github.com/user-attachments/assets/060b3b1d-e9be-4506-94a9-cae2baa6306c" />
</div>

# Hollow Knight 2D Demake - LibGDX (Java)

A feature-rich 2D platformer game inspired by **Hollow Knight**, developed as an advanced computer graphics academic project using the **LibGDX** framework and **Java**. The project implements a clean **Model-View-Controller (MVC)** architectural pattern, custom physics, state management, and asset loading.

---

## 🎮 Features & Gameplay

*   **Fluid Movement Mechanics:** Smooth running, jumping, double jumping, airborne control, dashing, and the iconic *Focus* healing mechanic.
*   **Combat & Spells:** Precise melee slash attacks with combo variations, projectile spells (*Soul Ball*, *Soul Scream*), and dynamic hit reactions.
*   **Boss & Enemy AI:** Fully scripted AI states for multiple enemies (Crawlid, Crystallized, Husk, Mosquito, Mosscreep, Mossfly) and a challenging boss encounter with the **False Knight**.
*   **Interactive UI & Menus:** Main menu, pause menu, inventory, achievements tracker, cheat menu, and settings panel.
*   **Save System:** JSON-based save/load slot management for game progression and player configurations.
*   **Audio & Visuals:** Immersive sound effects, background tracks, and custom particle/slash visual effects using LibGDX scene graph and asset manager.

---

## 🏗️ Project Architecture (MVC)

The project is structured following strict software engineering principles, separating game logic from rendering and state representation:

```text
core/src/main/java/io/github/some_example_name/
├── controller/       # Game loops, audio systems, collision resolution, and menu controllers
├── model/            # Game entities, player stats, enemy states, and save data structures
└── view/             # Screen renderers, map loaders, UI components, and animations
```

---

## 🚀 Getting Started

### Prerequisites
*   Java Development Kit (JDK 17 or higher)
*   Gradle (Wrapper is included in the repository)

### Running the Game locally

Clone the repository and run the desktop launcher via Gradle:

```bash
git clone https://github.com/kianhajipour/HollowKnight-LibGDX.git
cd HollowKnight-LibGDX
```

*   **On Windows:**
    ```powershell
    ./gradlew lwjgl3:run
    ```
*   **On Linux/macOS:**
    ```bash
    ./gradlew lwjgl3:run
    ```

---

## 📂 Project Structure

*   `core/` - Shared game logic, MVC architecture, and core systems.
*   `lwjgl3/` - Desktop-specific launcher (LWJGL3 backend) and window configurations.
*   `assets/` - Spritesheets, textures, audio tracks, fonts, and Tiled maps (`.tmx`).
*   `database/` - JSON configurations and save slot files.

---

## 🛡️ License

This project is developed for educational and portfolio purposes as part of computer graphics coursework.
