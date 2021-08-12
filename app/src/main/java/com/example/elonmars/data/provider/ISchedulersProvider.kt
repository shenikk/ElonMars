package com.example.elonmars.data.provider

import io.reactivex.Scheduler

interface ISchedulersProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}