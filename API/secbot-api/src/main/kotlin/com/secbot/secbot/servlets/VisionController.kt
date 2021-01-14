package com.secbot.secbot.servlets

import com.google.gson.GsonBuilder
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

    val gson = GsonBuilder().create()

    @PostMapping("/vision")
    fun handleFileUpload(
        @RequestParam("file") file: MultipartFile,
        redirectAttributes: RedirectAttributes
    ): String {

        val name = writeByte(file.bytes)

        redirectAttributes.addFlashAttribute(
            "message",
            "You successfully uploaded " + file.originalFilename + "!"
        )
        println("Photo recieved: ${file.originalFilename}")
        return gson.toJson(name)
    }

    @GetMapping(value = ["/vision"], produces = ["text/plain"])
    fun index(): String {

        return  gson.toJson("Hello Vision")
    }

    fun writeByte(bytes: ByteArray) : String{

            val file = File("/tmp/${UUID.randomUUID()}.jpg")
            println( file.exists() )
            val os: OutputStream = FileOutputStream(file)

            // Starts writing the bytes in it
            os.write(bytes)

            println( "saved file ${file.absolutePath}" )
            // Close the file
            os.close()
            return file.name

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