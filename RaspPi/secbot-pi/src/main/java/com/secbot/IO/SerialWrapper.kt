package com.secbot.IO

import com.pi4j.io.serial.*
import java.io.InputStream
import java.io.OutputStream
import java.io.Writer
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset

class SerialWrapper : Serial {

    private val listeners : MutableList<SerialDataEventListener> = ArrayList()

    override fun available(): Int {
        TODO("Not yet implemented")
    }

    override fun discardData() {
        TODO("Not yet implemented")
    }

    override fun read(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun read(length: Int): ByteArray {
        TODO("Not yet implemented")
    }

    override fun read(buffer: ByteBuffer?) {
        TODO("Not yet implemented")
    }

    override fun read(length: Int, buffer: ByteBuffer?) {
        TODO("Not yet implemented")
    }

    override fun read(stream: OutputStream?) {
        TODO("Not yet implemented")
    }

    override fun read(length: Int, stream: OutputStream?) {
        TODO("Not yet implemented")
    }

    override fun read(collection: MutableCollection<ByteBuffer>?) {
        TODO("Not yet implemented")
    }

    override fun read(length: Int, collection: MutableCollection<ByteBuffer>?) {
        TODO("Not yet implemented")
    }

    override fun read(charset: Charset?): CharBuffer {
        TODO("Not yet implemented")
    }

    override fun read(length: Int, charset: Charset?): CharBuffer {
        TODO("Not yet implemented")
    }

    override fun read(charset: Charset?, writer: Writer?) {
        TODO("Not yet implemented")
    }

    override fun read(length: Int, charset: Charset?, writer: Writer?) {
        TODO("Not yet implemented")
    }

    override fun write(data: ByteArray?, offset: Int, length: Int) {
        TODO("Not yet implemented")
    }

    override fun write(vararg data: Byte) {
        TODO("Not yet implemented")
    }

    override fun write(vararg data: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun write(vararg data: ByteBuffer?) {
        TODO("Not yet implemented")
    }

    override fun write(input: InputStream?) {
        TODO("Not yet implemented")
    }

    override fun write(charset: Charset?, data: CharArray?, offset: Int, length: Int) {
        TODO("Not yet implemented")
    }

    override fun write(charset: Charset?, vararg data: Char) {
        TODO("Not yet implemented")
    }

    override fun write(vararg data: Char) {
        TODO("Not yet implemented")
    }

    override fun write(charset: Charset?, vararg data: CharBuffer?) {
        TODO("Not yet implemented")
    }

    override fun write(vararg data: CharBuffer?) {
        TODO("Not yet implemented")
    }

    override fun write(charset: Charset?, vararg data: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun write(vararg data: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun write(charset: Charset?, data: MutableCollection<out CharSequence>?) {
        TODO("Not yet implemented")
    }

    override fun write(data: MutableCollection<out CharSequence>?) {
        TODO("Not yet implemented")
    }

    override fun writeln(charset: Charset?, vararg data: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun writeln(vararg data: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun writeln(charset: Charset?, data: MutableCollection<out CharSequence>?) {
        TODO("Not yet implemented")
    }

    override fun writeln(data: MutableCollection<out CharSequence>?) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun open(device: String?, baud: Int, dataBits: Int, parity: Int, stopBits: Int, flowControl: Int) {
        TODO("Not yet implemented")
    }

    override fun open(device: String?, baud: Int) {
        TODO("Not yet implemented")
    }

    override fun open(
        device: String?,
        baud: Baud?,
        dataBits: DataBits?,
        parity: Parity?,
        stopBits: StopBits?,
        flowControl: FlowControl?
    ) {
        TODO("Not yet implemented")
    }

    override fun open(serialConfig: SerialConfig?) {
        TODO("Not yet implemented")
    }

    override fun isOpen(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isClosed(): Boolean {
        TODO("Not yet implemented")
    }

    override fun flush() {
        TODO("Not yet implemented")
    }

    override fun discardInput() {
        TODO("Not yet implemented")
    }

    override fun discardOutput() {
        TODO("Not yet implemented")
    }

    override fun discardAll() {
        TODO("Not yet implemented")
    }

    override fun sendBreak(duration: Int) {
        TODO("Not yet implemented")
    }

    override fun sendBreak() {
        TODO("Not yet implemented")
    }

    override fun setBreak(enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setRTS(enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setDTR(enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getRTS(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDTR(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCTS(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDSR(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getRI(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCD(): Boolean {
        TODO("Not yet implemented")
    }

    override fun addListener(vararg listener: SerialDataEventListener) {
        listeners.addAll(listener)
    }

    override fun removeListener(vararg listener: SerialDataEventListener) {
        TODO("Not yet implemented")
    }

    override fun getFileDescriptor(): Int {
        TODO("Not yet implemented")
    }

    override fun getInputStream(): InputStream {
        TODO("Not yet implemented")
    }

    override fun getOutputStream(): OutputStream {
        TODO("Not yet implemented")
    }

    override fun isBufferingDataReceived(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setBufferingDataReceived(enabled: Boolean) {
        TODO("Not yet implemented")
    }
}