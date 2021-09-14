package com.example.elonmars.domain.provider

import io.reactivex.Scheduler

interface ISchedulersProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}
