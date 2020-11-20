package ky.korins.blake3

import java.io.InputStream

trait Hasher {
  /**
   * Updates a hasher by provided bytes, returns the same hasher
   */
  def update(input: Array[Byte]): Hasher

  /**
   * Updates a hasher by specified part of provided bytes, returns the same hasher
   */
  def update(input: Array[Byte], offset: Int, len: Int): Hasher

  /**
   * Updates a hasher by specified byte, returns the same hasher
   */
  def update(input: Byte): Hasher

  /**
   * Updates a hasher by specified string, returns the same hasher
   */
  def update(input: String): Hasher

  /**
   * Updates a hasher from specified InputStream, returns the same hasher
   *
   * It reads `input` until it returns `-1`
   */
  def update(input: InputStream): Hasher

  /**
   * Calculate a hash into specified byte array
   */
  def done(out: Array[Byte]): Unit

  /**
   * Calculate a hash into specified part of array
   */
  def done(out: Array[Byte], offset: Int, len: Int): Unit

  /**
   * Create a new byte array of specified length and calculate a hash into this array
   */
  def done(len: Int): Array[Byte] = {
    val bytes = new Array[Byte](len)
    done(bytes)
    bytes
  }

  /**
   * Calculate a hash as single byte
   */
  def done(): Byte

  /**
   * Calculate a hash and return it as positive BigInt with specified length in bits
   */
  @throws(classOf[IllegalArgumentException])
  def doneBigInt(bitLength: Int): BigInt = {
    if (bitLength % 8 != 0) {
      throw new IllegalArgumentException(s"bitLength: $bitLength should be a multiple of 8")
    }
    BigInt(1, done(bitLength / 8))
  }

  /**
   * Calculate a hash and return as hex encoded string with specified output length in characters
   */
  @throws(classOf[IllegalArgumentException])
  def doneHex(resultLength: Int): String = {
    if (resultLength % 2 != 0) {
      throw new IllegalArgumentException(s"resultLength: $resultLength should be even")
    }
    RFC4648.base16(done(resultLength / 2)).toLowerCase
  }

  /**
   * Create a base16 representative of calculated hash for specified length
   */
  def doneBase16(len: Int): String =
    RFC4648.base16(done(len))

  /**
   * Create a base32 representative of calculated hash for specified length
   */
  def doneBase32(len: Int): String =
    RFC4648.base32(done(len))

  /**
   * Create a base32 hex-compatibly representative of calculated hash for specified length
   */
  def doneBase32Hex(len: Int): String =
    RFC4648.base32_hex(done(len))

  /**
   * Create a base64 representative of calculated hash for specified length
   */
  def doneBase64(len: Int): String =
    RFC4648.base64(done(len))

  /**
   * Create a base64 URL-safe representative of calculated hash for specified length
   */
  def doneBase64Url(len: Int): String =
    RFC4648.base64_url(done(len))
}
