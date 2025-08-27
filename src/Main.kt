import java.io.File
import kotlin.text.trim

const val RESET = "\u001B[0m"
const val RED = "\u001B[31m"
const val GREEN = "\u001B[32m"
const val BLUE = "\u001B[34m"

fun normalizeFileName(name: String): String {
    return name
        .trim()
        .replace(" ", "_")
        .replace(Regex("[^A-Za-z0-9_\\-]"), "")
        .trim('_')
}

fun askNoteName(action: String): String {
    print("${BLUE}Note name to $action: $RESET")
    val input = readln()
    return normalizeFileName(input)
}

fun notePad() {

    val folder = File("notes")
    if (!folder.exists()) folder.mkdir()

    while (true) {
        println("===== NotePad =====")
        println("1. Create a note")
        println("2. Modify a note")
        println("3. Read a note")
        println("4. Delete a note")
        println("5. Show all notes")
        println("6. Exit")

        print("\n${BLUE}Choice: $RESET")
        when (readln().trim()) {
            "1" -> createNote(folder)
            "2" -> modifyNote(folder)
            "3" -> readNote(folder)
            "4" -> deleteNote(folder)
            "5" -> showNotes(folder)
            "6" -> {
                println("${GREEN}See you soon!$RESET")
                return
            }
            else -> println("${RED}Invalid choice$RESET")
        }
    }
}

fun createNote(folder: File) {
    println()
    var noteCreated = false

    while (!noteCreated) {
        val fileName = askNoteName("create")
        val files = folder.listFiles() ?: arrayOf()
        val existingFile = files.find { it.nameWithoutExtension.equals(fileName, ignoreCase = true) }

        if (existingFile != null) {
            println("${RED}A note with this name already exists!$RESET")
            println("1. Overwrite existing note")
            println("2. Choose a new name")
            print("${BLUE}Choice: $RESET")
            when (readln().trim()) {
                "1" -> {
                    print("${BLUE}Content to add to $fileName: $RESET")
                    val content = readln()
                    existingFile.writeText(content + "\n")
                    println("${GREEN}The note \"$fileName\" has been overwritten.$RESET\n")
                    noteCreated = true
                }
                "2" -> continue
                else -> println("${RED}Invalid choice$RESET\n")
            }
        } else {
            print("${BLUE}Content to add to $fileName: $RESET")
            val content = readln()
            val file = File(folder, "$fileName.txt")
            file.writeText(content + "\n")
            println("${GREEN}The note \"$fileName\" has been created.$RESET\n")
            noteCreated = true
        }
    }
}

fun modifyNote(folder: File) {
    showNotes(folder)

    val fileName = askNoteName("modify")
    val files = folder.listFiles() ?: arrayOf()
    val file = files.find { it.nameWithoutExtension.equals(fileName, ignoreCase = true) }

    if (file != null) {
        println("1. Append content to \"$fileName\"")
        println("2. Replace current content of \"$fileName\"")
        print("${BLUE}Choice: $RESET")
        when (readln().trim()) {
            "1" -> {
                print("${BLUE}Content to append: $RESET")
                val content = readln()
                file.appendText(content + "\n")
                println("${GREEN}The note \"$fileName\" has been updated.$RESET\n")
            }
            "2" -> {
                print("${BLUE}New content: $RESET")
                val content = readln()
                file.writeText(content + "\n")
                println("${GREEN}The note \"$fileName\" has been updated.$RESET\n")
            }
            else -> println("${RED}Invalid choice$RESET\n")
        }
    } else {
        println("${RED}This note does not exist.$RESET\n")
    }
}

fun readNote(folder: File) {
    showNotes(folder)

    val fileName = askNoteName("read")
    val files = folder.listFiles() ?: arrayOf()
    val file = files.find { it.nameWithoutExtension.equals(fileName, ignoreCase = true) }

    if (file != null) {
        println("\n--- Content ---")
        println(file.readText())
    } else {
        println("${RED}This note does not exist.$RESET\n")
    }
}

fun deleteNote(folder: File) {
    showNotes(folder)

    val fileName = askNoteName("delete")
    val files = folder.listFiles() ?: arrayOf()
    val file = files.find { it.nameWithoutExtension.equals(fileName, ignoreCase = true) }

    if (file != null) {
        file.delete()
        println("${GREEN}Note \"$fileName\" has been deleted.$RESET\n")
    } else {
        println("${RED}This note does not exist.$RESET\n")
    }
}

fun showNotes(folder: File) {
    println()
    val files = folder.listFiles()
    if (files.isNullOrEmpty()) {
        println("${RED}No notes available$RESET")
    } else {
        println("Available notes:")
        files.forEach { println("- ${it.nameWithoutExtension}") }
    }
    println()
}

fun main() {
    println()
    notePad()
}