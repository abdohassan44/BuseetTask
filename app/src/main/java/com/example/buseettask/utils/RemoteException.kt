package com.example.buseettask.utils

class RemoteException(val errorMessage: String? = null) : Throwable(errorMessage)

class UnauthorizedException : Throwable("User Unauthorized")