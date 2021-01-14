package com.secbot.secbot.servlets

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

@RestController
class VisionController {

    @PostMapping("/vision")
    fun handleFileUpload(
        @RequestParam("file") file: MultipartFile,
        redirectAttributes: RedirectAttributes
    ): String? {

        writeByte(file.bytes)

        redirectAttributes.addFlashAttribute(
            "message",
            "You successfully uploaded " + file.originalFilename + "!"
        )
        return "redirect:/"
    }

    @GetMapping(value = ["/vision"], produces = ["text/plain"])
    fun index(): String {
        return "Hello Vision"
    }

    fun writeByte(bytes: ByteArray) {
        try {

            // Initialize a pointer
            // in file using OutputStream
            val file = File("/tmp/${UUID.randomUUID()}.jpg")
            println( file.exists() )
            val os: OutputStream = FileOutputStream(file)

            // Starts writing the bytes in it
            os.write(bytes)

            println( "saved file ${file.absolutePath}" )
            // Close the file
            os.close()
        } catch (e: Exception) {
            println("Exception: $e")
        }
    }

    @GetMapping("/vision/{filename}")
    @ResponseBody
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource?>? {

        println("getting $filename")
        val file: Resource = FileSystemResource("/tmp/$filename.jpg")
        println(file.exists())
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename.toString() + "\"")
            .body<Resource?>(file)
    }

}