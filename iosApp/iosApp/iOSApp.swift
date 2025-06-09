import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        // Initialize Koin or any other dependency injection framework here
        KoinModuleKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}