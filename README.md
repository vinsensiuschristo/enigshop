# 📘 POS 2024 - Dokumentasi REST API

Dokumentasi ini menjelaskan secara lengkap semua endpoint API dari aplikasi POS (Point of Sale), termasuk method, path, deskripsi, parameter, contoh request dan response.

## 🌐 Base URL

```
http://localhost:8080/api
```

---

## 🔐 Authentication

### 🔑 POST `/api/auth/signin`
**Deskripsi:** Login ke sistem dan mendapatkan token JWT.

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "roles": ["ROLE_ADMIN"]
}
```

---

### 🆕 POST `/api/auth/signup`
**Deskripsi:** Registrasi user baru ke dalam sistem.

**Request:**
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "pass123",
  "role": ["user"]
}
```

**Response:**
```json
{
  "message": "User registered successfully!"
}
```

---

## 👥 User Management

### 🔍 GET `/api/user`
**Deskripsi:** Mengambil semua user yang terdaftar. (Admin Only)

**Response:**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "roles": ["ROLE_ADMIN"]
  }
]
```

---

## 📦 Product Management

### 🔍 GET `/api/product`
**Deskripsi:** Mendapatkan daftar semua produk.

**Response:**
```json
[
  {
    "id": 1,
    "name": "Product A",
    "description": "Deskripsi produk A",
    "price": 10000,
    "stock": 5
  }
]
```

---

### 🔍 GET `/api/product/{id}`
**Deskripsi:** Mendapatkan detail produk berdasarkan ID.

**Response:**
```json
{
  "id": 1,
  "name": "Product A",
  "description": "Deskripsi produk A",
  "price": 10000,
  "stock": 5
}
```

---

### ➕ POST `/api/product`
**Deskripsi:** Menambahkan produk baru.

**Request:**
```json
{
  "name": "Product B",
  "description": "Deskripsi produk B",
  "price": 15000,
  "stock": 10
}
```

**Response:**
```json
{
  "message": "Product was created successfully."
}
```

---

### ✏️ PUT `/api/product/{id}`
**Deskripsi:** Memperbarui informasi produk berdasarkan ID.

**Request:**
```json
{
  "name": "Product A Updated",
  "description": "Deskripsi baru",
  "price": 20000,
  "stock": 8
}
```

**Response:**
```json
{
  "message": "Product was updated successfully."
}
```

---

### ❌ DELETE `/api/product/{id}`
**Deskripsi:** Menghapus produk berdasarkan ID.

**Response:**
```json
{
  "message": "Product was deleted successfully."
}
```

---

## 💰 Transaction Management

### 🔍 GET `/api/transaction`
**Deskripsi:** Mendapatkan semua transaksi yang tercatat.

**Response:**
```json
[
  {
    "id": 1,
    "productId": 1,
    "quantity": 2,
    "totalPrice": 20000,
    "timestamp": "2024-05-04T12:34:56"
  }
]
```

---

### ➕ POST `/api/transaction`
**Deskripsi:** Membuat transaksi pembelian baru.

**Request:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

**Response:**
```json
{
  "message": "Transaction completed successfully."
}
```

---

## 🚫 Error Handling

### ❗ Contoh Error Validasi
```json
{
  "message": "Error: Username is already taken!"
}
```

### ❗ Error Autentikasi
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

---

## 🧾 Format Umum Response

```json
{
  "message": "string",
  "data": {},
  "timestamp": "2024-05-04T12:34:56"
}
```

---

## 🧪 Tools Pengujian

Direkomendasikan untuk menggunakan **Postman** atau **cURL** untuk menguji endpoint.
