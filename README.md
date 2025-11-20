# SIKAS

**SIKAS (Sistem Informasi Stok dan Kasir)** adalah aplikasi berbasis web yang dirancang untuk membantu UMKM dalam mengelola stok barang serta proses penjualan secara efisien. Aplikasi ini menyediakan fitur manajemen inventaris, pencatatan transaksi penjualan, laporan keuangan,
dan analisis data penjualan.

## Cara Menjalankan Aplikasi (Development)

### 1. Persiapan Lingkungan

Pastikan Anda telah menginstal: 
- **JDK 21** atau versi yang lebih baru
- **Apache Maven 3.8.0** atau versi yang lebih baru

### 2. Clone Repository

``` bash
    git clone git@github.com:budpoetra/sikas-be.git
```

### 3. Buka Proyek

Buka proyek menggunakan IDE pilihan Anda seperti IntelliJ IDEA atau
Eclipse.

### 4. Buat File Konfigurasi Database

Buat salah satu file berikut di directory:

-   `src/main/resources/application.yaml`
-   `src/main/resources/application.properties`

Salin template dari: 
- `src/main/resources/application-example.yaml`
- `src/main/resources/application-example.properties`

### 5. Generate Kunci Enkripsi AES

Jalankan file:

`src/main/java/com/juaracoding/sikas/security/AESGeneratedKey.java`

Salin hasil kunci AES pada file `src/main/java/com/juaracoding/sikas/security/Crypto.java` ke variabel berikut:

``` java
private static final String defaultKey = "salin_dan_tempel_kunci_anda_di_sini";
```

### 6. Enkripsi URL Database

Ubah nilai `strToEncrypt` pada file ``src/main/java/com/juaracoding/sikas/security/Crypto.java``:

``` java
public static void main(String[] args) {
    String strToEncrypt = "jdbc:mysql://localhost:3306/sikas_db"; // Ganti dengan URL database Anda

    System.out.println("Encryption Result : " + performEncrypt(strToEncrypt));
}
```

Masukkan hasil enkripsi ke file konfigurasi:

``` yaml
spring:
  datasource:
    url: "HASIL_ENKRIPSI_URL"
```

### 7. Enkripsi Username & Password

Lakukan langkah yang sama untuk: - `spring.datasource.username` -
`spring.datasource.password`

### 8. Build dan Jalankan Aplikasi

Build proyek:

``` bash
    mvn install
```

Jalankan aplikasi:

``` bash
    mvn spring-boot:run
```

## Developers

-   Kristo --- github.com/\
-   [Budi Sahputra](https://github.com/budpoetra)
-   M. Fadli --- github.com/\
-   Daffa --- github.com/\
-   Fauzi --- github.com/

**Juara Coding 2025**