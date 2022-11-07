package com.example.numberstesttask.main.presentaion

import com.example.numberstesttask.numbers.presentaion.Communication

interface NavigationCommunication {

    interface Observe : Communication.Observe<NavigationStrategy>
    interface Mutate: Communication.Mutate<NavigationStrategy>
    interface Mutable: Observe, Mutate

    class Base : Communication.SingleUi<NavigationStrategy>(), Mutable
}