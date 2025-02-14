dependencies {
    implementation(projects.shared)
    implementation(libs.bstats.bukkit)
    implementation(libs.multilib)
    compileOnly(libs.bukkit)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.vault)
    compileOnly(libs.via)
    compileOnly(libs.authlib)
    compileOnly(libs.essentials) {
        exclude("org.spigotmc", "spigot-api")
    }
}
