package com.example.elonmars.domain.provider

import io.reactivex.Scheduler

/** Интерфейс провайдер для работы на разных потоках. */
interface ISchedulersProvider {

    /**
     * Возвращает экземпляр общего планировщика по умолчанию,
     * предназначенный для работы, связанной с вводом-выводом.
     */
    fun io(): Scheduler

    /**
     * Возвращает планировщика, который выполняет действия в главном потоке Android.
     */
    fun ui(): Scheduler
}
