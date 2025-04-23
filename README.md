
#  Transactional App

## ✒️ Description

### Transactional-App is a web-based personal finance management application. It allows users to create, view and analyze their transactions, receive notifications about fund movements and build reports on finances.

## 🗃️ Architecture

<img width="2560" alt="Transactional app(2)" src="https://github.com/user-attachments/assets/9d03567c-e6b6-479d-a801-4854764f5d07" />

## 📋 Endpoints

###  Signing in to your account and generate access and refresh tokens

#### Request

`POST /api/auth/sign-in/`

```json
{
    "email": "johndoe@example.com",
    "password": "secret"
}
```

#### Response

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpvaG4iOnsic3RhY2siOiJI",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpvaG4iOnsic3RhY2siOiJI"
}

```

### Signing up to your account and generate access and refresh tokens

#### Request

`POST /api/auth/sign-up/`

```json
{
    "username": "johndoe",
    "email": "johndoe@example.com",
    "password": "secret"
}
```

#### Response

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpvaG4iOnsic3RhY2siOiJI",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpvaG4iOnsic3RhY2siOiJI"
}

```
### Signing out to your account and remove access token

#### Request

`POST /api/auth/sign-out/`

#### Response

```json
{
  "message": "Logged out successfully"
}

```

### Show all accounts

#### Request

`GET /api/:username/accounts`

#### Response

```json

{
  "page": 1,
  "limit": 10,
  "accounts": [
    {
      "user": {
        "username": "alex-gray",
        "email": "alex-gray@example.com"
      },
      "amount": 123.23,
      "updated_at": "30.07.2024"
    }
  ]
}

```


### Creating an account

#### Request

`POST /api/:username/accounts`

```json
{
    "user_id": 123
}
```

#### Response

```json
{
  "status": 201,
  "message": "Account created successfully!"
}

```

